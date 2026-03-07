import java.util.*;

public class Package {
    private String packageId;
    private double packageSize;
    private Order order;

    public Package(String packageId, double packageSize, Order order) {
        this.packageId = packageId;
        this.packageSize = packageSize;
        this.order = order;
    }

    public String getPackageId() {
        return packageId;
    }

    public double getPackageSize() {
        return packageSize;
    }

    public Order getOrder() {
        return order;
    }

    public void pack() {
        System.out.println("Packing package " + packageId + " for order " + order.getOrderId());
    }
}