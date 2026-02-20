package org.example;

public class BeverageValidator {

    public void validateAddOn(Beverage beverage, AddOnItem newItem){
        // Example rule: max 3 of same add-on

        int existingQuantity = beverage.getAddOnQuantity(newItem.getAddOn().getName());

        if (existingQuantity + newItem.getQuantity() > 3) {
            throw new IllegalArgumentException("Exceeded max allowed quantity for add-on");
        }

        // Additional rules can be added here
    }
}
