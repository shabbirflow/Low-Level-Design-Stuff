package org.example;

class Settlement {
    User from;
    User to;
    double amount;

    public Settlement(User to, double amount, User from) {
        this.to = to;
        this.amount = amount;
        this.from = from;
    }

    public User getFrom() {
        return from;
    }

    public double getAmount() {
        return amount;
    }

    public User getTo() {
        return to;
    }
}
