package org.example;

public class OrderItem {
    private final Beverage beverage;
    private final int quantity;

    public OrderItem(Beverage beverage, int quantity) {
        if (quantity <= 0) {
            throw new IllegalArgumentException("Quantity must be positive");
        }
        this.beverage = beverage;
        this.quantity = quantity;
    }

    public int getQuantity() {
        return quantity;
    }

    public Beverage getBeverage() {
        return beverage;
    }
}
