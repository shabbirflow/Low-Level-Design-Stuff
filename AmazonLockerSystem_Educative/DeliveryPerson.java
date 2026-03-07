import java.util.*;

public class DeliveryPerson {
    private String deliveryPersonId;

    public DeliveryPerson(String deliveryPersonId) {
        this.deliveryPersonId = deliveryPersonId;
    }

    public String getDeliveryPersonId() {
        return deliveryPersonId;
    }

    public void deliverPackage(LockerPackage pkg, Locker locker) {
        if (locker.addPackage(pkg)) {
            System.out.println("DeliveryPerson " + deliveryPersonId + " delivered package " + pkg.getPackageId()
                    + " to locker " + locker.getLockerId());
        }
    }

    public void pickupReturn(LockerPackage pkg, Locker locker) {
        if (locker.removePackage(pkg.getCode())) {
            System.out.println("DeliveryPerson " + deliveryPersonId + " picked up returned package "
                    + pkg.getPackageId() + " from locker " + locker.getLockerId());
        }
    }

    public void receiveReturnNotification(Notification notification) {
        System.out.println("DeliveryPerson " + deliveryPersonId + " received return notification for locker "
                + notification.getLockerId());
    }
}