package org.example;

import java.util.ArrayList;
import java.util.List;
import java.util.Map;

public class Group {
    private final String id;
    private final List<User> members = new ArrayList<>();
    private final BalanceSheet balanceSheet = new BalanceSheet();
    private final List<Expense> expenses = new ArrayList<>();
    private final ExpenseValidator validator = new ExpenseValidator();

    public Group(String id) {
        this.id = id;
    }

    public void addMember(User user) {
        members.add(user);
    }

    public boolean hasMember(User user) {
        return members.contains(user);
    }

    public void addExpense(Expense expense){
        validator.validate(expense, this);
        Map<User, Double> splits = expense.getStrategy()
                .computeSplits(expense.getAmount(), expense.getParticipants());

        User payer = expense.getPaidBy();

        for(Map.Entry<User, Double> entry : splits.entrySet()){
            double owed = entry.getValue();
            User user = entry.getKey();

            if(!user.equals(payer)){
                balanceSheet.addDebt(user, payer, owed);
            }

            expenses.add(expense);
        }
    }

    public BalanceSheet getBalanceSheet() {
        return balanceSheet;
    }

}
