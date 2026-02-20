package org.example;

import java.util.ArrayList;
import java.util.Collections;
import java.util.List;

public class Order {
    private final List<OrderItem> orderItemList;

    public Order(){
        this.orderItemList = new ArrayList<>();
    }

    public void addItem(OrderItem item){
        orderItemList.add(item);
    }

    public double calculateSubtotal() {
        double total = 0;
        for (OrderItem item : orderItemList) {
            total += item.getBeverage().calculatePrice() * item.getQuantity();
        }
        return total;
    }

    public List<OrderItem> getItems() {
        return Collections.unmodifiableList(orderItemList);
    }
}
