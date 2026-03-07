import java.util.*;

public class Notification {
    private String customerId;
    private String orderId;
    private String lockerId;
    private String code;

    public Notification(String customerId, String orderId, String lockerId, String code) {
        this.customerId = customerId;
        this.orderId = orderId;
        this.lockerId = lockerId;
        this.code = code;
    }

    public String getCustomerId() {
        return customerId;
    }

    public String getOrderId() {
        return orderId;
    }

    public String getLockerId() {
        return lockerId;
    }

    public String getCode() {
        return code;
    }

    public void send() {
        System.out.println("Notification sent to customer " + customerId +
                ": Your order " + orderId +
                " has been placed in locker " + lockerId +
                ". Pickup code: " + code);
    }
}