package org.example;

import java.util.HashMap;
import java.util.List;
import java.util.Map;

interface SplitStrategy {
    Map<User, Double> computeSplits(
            double totalAmount,
            List<User> participants
    );
}

class EqualSplitStrategy implements SplitStrategy{

    @Override
    public Map<User, Double> computeSplits(double totalAmount, List<User> participants) {
        double share = totalAmount / participants.size();
        Map<User, Double> result = new HashMap<>();

        for (User user : participants) {
            result.put(user, share);
        }
        return result;
    }
}

class ExactSplitStrategy implements SplitStrategy {

    private final Map<User, Double> exactAmounts;

    public ExactSplitStrategy(Map<User, Double> exactAmounts) {
        this.exactAmounts = exactAmounts;
    }

    @Override
    public Map<User, Double> computeSplits(
            double totalAmount,
            List<User> participants
    ) {
        // assume validation already done
        return new HashMap<>(exactAmounts);
    }
}

class PercentageSplitStrategy implements SplitStrategy {

    private final Map<User, Double> percentages;

    public PercentageSplitStrategy(Map<User, Double> percentages) {
        this.percentages = percentages;
    }

    @Override
    public Map<User, Double> computeSplits(
            double totalAmount,
            List<User> participants
    ) {
        Map<User, Double> result = new HashMap<>();
        for (Map.Entry<User, Double> entry : percentages.entrySet()) {
            double amount = totalAmount * entry.getValue() / 100.0;
            result.put(entry.getKey(), amount);
        }
        return result;
    }
}




