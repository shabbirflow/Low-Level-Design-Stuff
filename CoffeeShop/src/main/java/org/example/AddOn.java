package org.example;

public class AddOn {
    private final double pricePerUnit;
    private final String name;

    public AddOn(double pricePerUnit, String name) {
        this.pricePerUnit = pricePerUnit;
        this.name = name;
    }

    public double getPricePerUnit() {
        return pricePerUnit;
    }

    public String getName() {
        return name;
    }
}
