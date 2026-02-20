package org.example;

//TIP To <b>Run</b> code, press <shortcut actionId="Run"/> or
// click the <icon src="AllIcons.Actions.Execute"/> icon in the gutter.
import java.util.List;
import java.util.Map;

import java.util.*;

public class Main {

    public static void main(String[] args) {

        /*
         * STORY:
         * Alice, Bob, Charlie, and Dave visit a milk factory.
         * They buy milk, cheese, and ice cream at different times.
         * Everyone pays sometimes. Chaos ensues.
         * Splitwise must restore peace.
         */

        // ---- Users ----
        User alice = new User("1", "Alice");
        User bob = new User("2", "Bob");
        User charlie = new User("3", "Charlie");
        User dave = new User("4", "Dave");

        // ---- Group ----
        Group milkTrip = new Group("milk-factory-trip");
        milkTrip.addMember(alice);
        milkTrip.addMember(bob);
        milkTrip.addMember(charlie);
        milkTrip.addMember(dave);

        /*
         * Expense 1:
         * Alice buys milk for everyone.
         * Total = 400
         * Split equally.
         */
        Expense milk = new Expense(
                alice, new EqualSplitStrategy(),
                List.of(alice, bob, charlie, dave),
                400
        );
        milkTrip.addExpense(milk);

        /*
         * Expense 2:
         * Bob buys cheese for Bob and Charlie only.
         * Total = 300
         * Exact split:
         *   Bob -> 100
         *   Charlie -> 200
         */
        Map<User, Double> cheeseSplit = new HashMap<>();
        cheeseSplit.put(bob, 100.0);
        cheeseSplit.put(charlie, 200.0);

        Expense cheese = new Expense(
                bob, new ExactSplitStrategy(cheeseSplit),
                List.of(bob, charlie), 300

        );
        milkTrip.addExpense(cheese);

        /*
         * Expense 3:
         * Dave buys ice cream for Alice, Charlie, and Dave.
         * Total = 500
         * Percentage split:
         *   Alice -> 20%
         *   Charlie -> 50%
         *   Dave -> 30%
         */
        Map<User, Double> iceCreamPercent = new HashMap<>();
        iceCreamPercent.put(alice, 20.0);
        iceCreamPercent.put(charlie, 50.0);
        iceCreamPercent.put(dave, 30.0);

        Expense iceCream = new Expense(
                dave, new PercentageSplitStrategy(iceCreamPercent),
                List.of(alice, charlie, dave),
                500

        );
        milkTrip.addExpense(iceCream);

        // ---- Raw balances ----
        System.out.println("\n=== RAW BALANCES (before settle-up) ===");
        printBalances(milkTrip.getBalanceSheet());

        // ---- Settle Up ----
        SettlementService settleUpService = new SettlementService();
        List<Settlement> settlements =
                settleUpService.settleUp(milkTrip.getBalanceSheet());

        // ---- Final settlement plan ----
        System.out.println("\n=== SETTLE UP PLAN ===");
        for (Settlement s : settlements) {
            System.out.println(
                    s.getFrom().getName() + " pays " +
                            s.getTo().getName() + " ₹" +
                            String.format("%.2f", s.getAmount())
            );
        }
    }

    private static void printBalances(BalanceSheet balanceSheet) {
        for (Map.Entry<User, Map<User, Double>> entry
                : balanceSheet.getBalances().entrySet()) {

            User from = entry.getKey();
            for (Map.Entry<User, Double> inner : entry.getValue().entrySet()) {
                System.out.println(
                        from.getName() + " owes " +
                                inner.getKey().getName() + " ₹" +
                                String.format("%.2f", inner.getValue())
                );
            }
        }
    }
}
