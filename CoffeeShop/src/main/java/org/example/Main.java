package org.example;
import java.util.HashMap;
import java.util.Map;

public class Main {
    public static void main(String[] args) {

        System.out.println("☕ Welcome to Shabbir's Ultra Premium Overpriced Coffee Shop ☕");
        System.out.println("--------------------------------------------------------------");

        BeverageValidator validator = new BeverageValidator();

        // Define base prices for Latte
        Map<Size, Double> lattePrices = new HashMap<>();
        lattePrices.put(Size.SMALL, 10.0);
        lattePrices.put(Size.MEDIUM, 12.0);
        lattePrices.put(Size.LARGE, 15.0);

        // Define base prices for Espresso
        Map<Size, Double> espressoPrices = new HashMap<>();
        espressoPrices.put(Size.SMALL, 8.0);
        espressoPrices.put(Size.MEDIUM, 10.0);
        espressoPrices.put(Size.LARGE, 12.0);

        // Create beverages
        Beverage largeLatte = new Beverage("Latte", Size.LARGE, lattePrices, validator);
        Beverage smallEspresso = new Beverage("Espresso", Size.SMALL, espressoPrices, validator);

        // Define add-ons
        AddOn extraShot = new AddOn(3.0, "Extra Shot");
        AddOn almondMilk = new AddOn(2.5, "Almond Milk");
        AddOn whippedCream = new AddOn(1.5, "Whipped Cream");

        // Add add-ons to Latte
        System.out.println("\nCustomer: I want a LARGE Latte with almond milk and 2 extra shots.");
        largeLatte.addAddOn(new AddOnItem(almondMilk, 1));
        largeLatte.addAddOn(new AddOnItem(extraShot, 2));

        // Add add-ons to Espresso
        System.out.println("Customer: Also give me a SMALL Espresso with whipped cream.");
        smallEspresso.addAddOn(new AddOnItem(whippedCream, 1));

        // Try violating rule
        System.out.println("\nCustomer: Actually... add 5 more extra shots 😈");
        try {
            largeLatte.addAddOn(new AddOnItem(extraShot, 5));
        } catch (Exception e) {
            System.out.println("Barista: Sir, that would legally classify as rocket fuel.");
            System.out.println("System says: " + e.getMessage());
        }

        // Create order
        Order order = new Order();

        order.addItem(new OrderItem(largeLatte, 1));
        order.addItem(new OrderItem(smallEspresso, 2));

        // Calculate subtotal
        double subtotal = order.calculateSubtotal();

        System.out.println("\n📋 Order Summary:");
        for (OrderItem item : order.getItems()) {
            System.out.println("- " + item.getQuantity() + " x "
                    + item.getBeverage().calculatePrice() + " each");
        }

        System.out.println("\n💰 Subtotal: $" + subtotal);

        System.out.println("\nBarista: That'll be $" + subtotal +
                ". We accept cash, card, and emotional damage.");

        System.out.println("\n☕ Thank you for funding our third espresso machine!");
    }
}
