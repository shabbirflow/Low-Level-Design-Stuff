package org.example;

import java.util.HashMap;
import java.util.Map;

class BalanceSheet {

    // balances[A][B] = A owes B
    private final Map<User, Map<User, Double>> balances = new HashMap<>();

    public void addDebt(User from, User to, double amount) {
        if (from.equals(to)) return;

        double reverse = getBalance(to, from);

        if (reverse >= amount) {
            setBalance(to, from, reverse - amount);
        } else {
            removeBalance(to, from);
            setBalance(from, to, amount - reverse);
        }
    }

    private double getBalance(User from, User to) {
        return balances
                .getOrDefault(from, Map.of())
                .getOrDefault(to, 0.0);
    }

    private void setBalance(User from, User to, double amount) {
        if (amount == 0) {
            removeBalance(from, to);
            return;
        }
        balances
                .computeIfAbsent(from, k -> new HashMap<>())
                .put(to, amount);
    }

    private void removeBalance(User from, User to) {
        Map<User, Double> inner = balances.get(from);
        if (inner != null) {
            inner.remove(to);
            if (inner.isEmpty()) {
                balances.remove(from);
            }
        }
    }

    public Map<User, Map<User, Double>> getBalances() {
        return balances;
    }
}
