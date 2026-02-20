package org.example;

import java.util.ArrayList;
import java.util.List;
import java.util.Map;

public class Beverage {

    private final String name;
    private final List<AddOnItem> addOnItems;
    private final Size size;
    private final Map<Size, Double> basePriceBySize;
    private final BeverageValidator validator;

    public Beverage(String name, Size size, Map<Size, Double> priceBySize, BeverageValidator validator) {
        this.name = name;
        this.addOnItems = new ArrayList<>();
        this.size = size;
        this.basePriceBySize = priceBySize;
        this.validator = validator;
    }

    public void addAddOn(AddOnItem item){
        validator.validateAddOn(this, item);
        addOnItems.add(item);
    }

    public double calculatePrice(){
        double basePrice = basePriceBySize.get(size);

        for (AddOnItem item : addOnItems) {
            basePrice += item.getAddOn().getPricePerUnit() * item.getQuantity();
        }

        return basePrice;
    }

    public int getAddOnQuantity(String addOnName) {
        int total = 0;
        for (AddOnItem item : addOnItems) {
            if (item.getAddOn().getName().equals(addOnName)) {
                total += item.getQuantity();
            }
        }
        return total;
    }
}
