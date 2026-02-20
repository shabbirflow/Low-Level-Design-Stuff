package org.example;

import java.util.Map;

public class AddOnItem {
    private final AddOn addOn;
    private final int quantity;

    public AddOnItem(AddOn addOn, int quantity) {
        this.addOn = addOn;
        this.quantity = quantity;
    }

    public AddOn getAddOn() {
        return addOn;
    }

    public int getQuantity() {
        return quantity;
    }
}
