import java.util.Calendar;
import java.util.Date;
import java.util.List;

public class Driver {
    public static void main(String[] args) {
        System.out.println(new String(new char[100]).replace('\0', '═'));
        System.out.println("\t\t\t\t\tAMAZON LOCKER SERVICE SYSTEM");
        System.out.println(new String(new char[100]).replace('\0', '═'));

        // --- System Initialization ---
        System.out.println("🛠️  [SETUP] Initializing Locker Service, Locations, and Lockers...\n");
        LockerService lockerService = LockerService.getInstance();
        LockerLocation loc = new LockerLocation("Downtown", 10.5, 20.8, new Date(), new Date());
        Locker locker1 = new Locker("L1", LockerSize.MEDIUM, loc.getName());
        Locker locker2 = new Locker("L2", LockerSize.LARGE, loc.getName());
        loc.addLocker(locker1);
        loc.addLocker(locker2);
        lockerService.addLocation(loc);
        System.out.println("    → Added LockerLocation: Downtown with Lockers: [L1, L2]\n");

        // --- Customer and DeliveryPerson Setup ---
        Customer customer = new Customer("CUST1", "Alice", "alice@example.com", "1234567890");
        DeliveryPerson deliveryGuy = new DeliveryPerson("DEL1");

        // === Scenario 1: Customer Places an Order ===
        System.out.println("1️⃣  SCENARIO 1: Customer Places an Order");
        System.out.println(new String(new char[100]).replace('\0', '-'));
        Order order = new Order("ORD1", loc.getName(), customer.getCustomerId());
        order.addItem(new Item("ITM1", 2));
        System.out.println("  → [Customer] Placing order ORD1 for 2x ITM1.");
        customer.placeOrder(order);

        Package pkg = new Package("PKG1", 2.5, order);
        System.out.println("  → [System] Packing the order...");
        pkg.pack();

        System.out.println("  → [System] Assigning a locker for package delivery...");
        Locker assignedLocker = lockerService.requestLocker(LockerSize.MEDIUM);
        String otp = "123456";
        LockerPackage lpkg = new LockerPackage("PKG1", 2.5, order, 3, assignedLocker.getLockerId(), otp, new Date(),
                deliveryGuy.getDeliveryPersonId());

        System.out.println("  → [DeliveryPerson] Delivering package PKG1 to Locker L1...");
        deliveryGuy.deliverPackage(lpkg, assignedLocker);

        Notification notification = new Notification(customer.getCustomerId(), order.getOrderId(),
                assignedLocker.getLockerId(), otp);
        System.out.println("  → [System] Sending pickup notification to customer...");
        notification.send();
        customer.receiveNotification(notification);

        // === Scenario 2: Customer Picks Up the Package ===
        System.out.println("\n2️⃣  SCENARIO 2: Customer Picks Up the Package");
        System.out.println(new String(new char[100]).replace('\0', '-'));
        System.out.println("  → [Customer] Arriving at Locker L1 to pick up package.");
        boolean pickupSuccess = assignedLocker.removePackage(otp);
        if (pickupSuccess) {
            System.out.println("    ✔️  [Customer] Pickup successful! Package retrieved from locker L1.");
        } else {
            System.out.println("    ❌ [Customer] Pickup failed.");
        }

        // === Scenario 3: Customer Initiates a Return ===
        System.out.println("\n3️⃣  SCENARIO 3: Customer Initiates a Return");
        System.out.println(new String(new char[100]).replace('\0', '-'));
        customer.requestReturn(order);
        boolean returnApproved = lockerService.requestReturn(order);

        if (returnApproved) {
            System.out.println("  → [System] Return approved! Assigning locker for return...");
            Locker returnLocker = lockerService.requestLocker(LockerSize.MEDIUM);
            String returnOtp = "654321";
            LockerPackage returnPkg = new LockerPackage("PKG1-R", 2.5, order, 3, returnLocker.getLockerId(), returnOtp,
                    new Date(), deliveryGuy.getDeliveryPersonId());

            Notification returnNotif = new Notification(customer.getCustomerId(), order.getOrderId(),
                    returnLocker.getLockerId(), returnOtp);
            System.out.println("  → [System] Sending return locker details to customer...");
            returnNotif.send();
            customer.receiveNotification(returnNotif);

            System.out.println("  → [System] Notifying DeliveryPerson about expected return...");
            deliveryGuy.receiveReturnNotification(returnNotif);

            System.out.println(
                    "  → [Customer] Placing return package PKG1-R in Locker " + returnLocker.getLockerId() + "...");
            returnLocker.addPackage(returnPkg);
            System.out
                    .println("      ↳ [Customer] Return package placed in locker " + returnLocker.getLockerId() + ".");

            System.out.println("  → [DeliveryPerson] Arriving to pick up returned package...");
            deliveryGuy.pickupReturn(returnPkg, returnLocker);
        }

        System.out.println("\n🏁\t\t\t\t\tSYSTEM DEMO COMPLETE");
        System.out.println(new String(new char[100]).replace('\0', '═'));
    }
}
