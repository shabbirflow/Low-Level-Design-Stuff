import java.util.*;

public abstract class Notification {
    String notificationId, content;
    Date created;

    public Notification(String notificationId, String content) {
        this.notificationId = notificationId;
        this.content = content;
        this.created = new Date();
    }

    public abstract boolean sendNotification();
}
