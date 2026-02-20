package org.example;

import java.util.List;

public class Expense {
    private final User paidBy;
    private final SplitStrategy strategy;
    private final List<User> participants;
    private final double amount;

    public Expense(User paidBy, SplitStrategy strategy, List<User> participants, int amount) {
        this.paidBy = paidBy;
        this.strategy = strategy;
        this.participants = participants;
        this.amount = amount;
    }

    public User getPaidBy() {
        return paidBy;
    }

    public List<User> getParticipants() {
        return participants;
    }

    public double getAmount() {
        return amount;
    }

    public SplitStrategy getStrategy() {
        return strategy;
    }
}
