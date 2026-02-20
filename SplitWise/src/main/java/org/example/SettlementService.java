package org.example;

import java.util.*;

/**
 * SettlementService is responsible for simplifying group balances.
 *
 * IMPORTANT DESIGN CHOICE:
 * ------------------------
 * - This class does NOT mutate the BalanceSheet.
 * - It computes a *settlement plan* (who should pay whom and how much)
 *   that would clear all balances if executed.
 *
 * Why?
 * - BalanceSheet is the source of truth.
 * - Settle-up is a derived, algorithmic view.
 *
 * SRP:
 * ----
 * - BalanceSheet -> maintains invariants & state
 * - SettlementService -> computes minimal transactions
 */
public class SettlementService {

    /**
     * Computes a minimal list of settlements that would clear all balances.
     *
     * @param balanceSheet the current group balance sheet
     * @return list of settlements (transactions)
     */
    public List<Settlement> settleUp(BalanceSheet balanceSheet) {

        // Step 1: Convert pairwise balances into net balance per user.
        // Positive  -> user owes money
        // Negative  -> user is owed money
        Map<User, Double> netBalance = computeNetBalances(balanceSheet);

        // Step 2: Separate users into debtors and creditors.
        // We use queues to greedily match them.
        Queue<Map.Entry<User, Double>> debtors = new LinkedList<>();
        Queue<Map.Entry<User, Double>> creditors = new LinkedList<>();

        for (Map.Entry<User, Double> entry : netBalance.entrySet()) {
            if (entry.getValue() > 0) {
                // User owes money
                debtors.add(entry);
            } else if (entry.getValue() < 0) {
                // User is owed money
                creditors.add(entry);
            }
        }

        List<Settlement> settlements = new ArrayList<>();

        // Step 3: Greedily settle debts.
        // Always match one debtor with one creditor.
        while (!debtors.isEmpty() && !creditors.isEmpty()) {

            Map.Entry<User, Double> debtor = debtors.poll();
            Map.Entry<User, Double> creditor = creditors.poll();

            // debtor.getValue()  > 0  -> amount debtor owes
            // creditor.getValue() < 0 -> amount creditor should receive
            double owes = debtor.getValue();
            double isOwed = -creditor.getValue();

            // We can only settle the minimum of the two.
            double settledAmount = Math.min(owes, isOwed);

            // Create a settlement transaction:
            // debtor pays creditor
            settlements.add(
                    new Settlement(
                            creditor.getKey(),   // receiver
                            settledAmount,
                            debtor.getKey()      // payer
                    )
            );

            // Reduce remaining balances after settlement
            owes -= settledAmount;
            isOwed -= settledAmount;

            // If debtor still owes money, put them back in the queue
            if (owes > 0) {
                debtors.add(Map.entry(debtor.getKey(), owes));
            }

            // If creditor is still owed money, put them back in the queue
            if (isOwed > 0) {
                creditors.add(Map.entry(creditor.getKey(), -isOwed));
            }
        }

        return settlements;
    }

/**
 * Computes net balance per user from pairwise balances.
 *
 * Given:
 *   balances[A][B] = X  (A owes B X)
 *
 * We update:
 *   net[A] += X
 *   net[B] -= X
 *
 * Final meaning:
 * - net[user] > 0  -> user owes money
 * - net[user] < 0  -> user is owed money
 *
 * @param balanceSheet the balance sheet to compute net balances from
 * @return map of users to their net balances
 */
    private Map<User, Double> computeNetBalances(BalanceSheet balanceSheet) {
        Map<User, Double> net = new HashMap<>();

        // Iterate through all users who owe money to others
        for (Map.Entry<User, Map<User, Double>> entry : balanceSheet.getBalances().entrySet()) {
            User from = entry.getKey();  // User who owes money

            // For each debtor, iterate through all their creditors
            for (Map.Entry<User, Double> inner : entry.getValue().entrySet()) {
                User to = inner.getKey();      // User who is owed money
                double amount = inner.getValue(); // Amount owed

                // Update net balances:
                // Debtor's balance increases (they owe more)
                // Creditor's balance decreases (they are owed more)
                net.put(from, net.getOrDefault(from, 0.0) + amount);
                net.put(to, net.getOrDefault(to, 0.0) - amount);
            }
        }

        return net;
    }
}
