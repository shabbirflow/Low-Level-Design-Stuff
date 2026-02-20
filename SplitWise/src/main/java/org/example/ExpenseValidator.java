package org.example;

class ExpenseValidator {

    public void validate(Expense expense, Group group) {
        if (!group.hasMember(expense.getPaidBy())) {
            throw new IllegalArgumentException("Payer not in group");
        }

        for (User u : expense.getParticipants()) {
            if (!group.hasMember(u)) {
                throw new IllegalArgumentException("Participant not in group");
            }
        }

        if (expense.getAmount() <= 0) {
            throw new IllegalArgumentException("Invalid amount");
        }
    }
}

