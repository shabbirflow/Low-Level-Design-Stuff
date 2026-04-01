public class EmailNotification extends Notification {
    String email;

    public EmailNotification(String notificationId, String content, String email) {
        super(notificationId, content);
        this.email = email;
    }

    public boolean sendNotification() {
        System.out.println("Email notification to " + email + ": " + content);
        return true;
    }
}
