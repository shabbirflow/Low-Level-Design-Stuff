public class PostalNotification extends Notification {
    Address address;

    public PostalNotification(String notificationId, String content, Address address) {
        super(notificationId, content);
        this.address = address;
    }

    public boolean sendNotification() {
        System.out.println("Postal notification to " + address + ": " + content);
        return true;
    }
}
