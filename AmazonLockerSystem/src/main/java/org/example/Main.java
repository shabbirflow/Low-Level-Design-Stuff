package org.example;

import java.util.List;

//TIP To <b>Run</b> code, press <shortcut actionId="Run"/> or
// click the <icon src="AllIcons.Actions.Execute"/> icon in the gutter.
public class Main {

    public static void main(String[] args) throws InterruptedException {

        System.out.println("=======================================");
        System.out.println("🚚 AMAZON LOCKER SYSTEM CHAOS SIMULATION");
        System.out.println("=======================================\n");

        // ------------------------------
        // 1️⃣ Setup lockers
        // ------------------------------

        System.out.println("🧱 Setting up lockers...");
        System.out.println("Expected: 2 SMALL, 1 MEDIUM, 1 LARGE locker available.\n");

        List<Locker> lockers = List.of(
                new Locker("L1", LockerSize.SMALL),
                new Locker("L2", LockerSize.SMALL),
                new Locker("L3", LockerSize.MEDIUM),
                new Locker("L4", LockerSize.LARGE)
        );

        LockerManager manager = new LockerManager(lockers);

        System.out.println("Lockers initialized.\n");


        // ------------------------------
        // 2️⃣ Deliver parcels
        // ------------------------------

        System.out.println("📦 Delivering Parcel P1 (SMALL)");
        System.out.println("Expected: Should go to SMALL locker.\n");

        Parcel p1 = new Parcel("P1", LockerSize.SMALL);
        String code1 = manager.deliverParcel(p1);

        System.out.println("Returned pickup code: " + code1 + "\n");


        System.out.println("📦 Delivering Parcel P2 (MEDIUM)");
        System.out.println("Expected: Should go to MEDIUM locker.\n");

        Parcel p2 = new Parcel("P2", LockerSize.MEDIUM);
        String code2 = manager.deliverParcel(p2);

        System.out.println("Returned pickup code: " + code2 + "\n");


        System.out.println("📦 Delivering Parcel P3 (SMALL)");
        System.out.println("Expected: Should use second SMALL locker.\n");

        Parcel p3 = new Parcel("P3", LockerSize.SMALL);
        String code3 = manager.deliverParcel(p3);

        System.out.println("Returned pickup code: " + code3 + "\n");


        System.out.println("📦 Delivering Parcel P4 (SMALL)");
        System.out.println("Expected: No SMALL lockers left.");
        System.out.println("Expected: Should upgrade to MEDIUM or LARGE.\n");

        Parcel p4 = new Parcel("P4", LockerSize.SMALL);

        try {
            String code4 = manager.deliverParcel(p4);
            System.out.println("Returned pickup code: " + code4 + "\n");
        } catch (Exception e) {
            System.out.println("💥 Exception: " + e.getMessage());
        }


        // ------------------------------
        // 3️⃣ Pickup flow
        // ------------------------------

        System.out.println("🎟️ Picking up Parcel P1");
        System.out.println("Expected: Locker becomes available again.\n");

        Parcel picked = manager.pickupParcel(code1);
        System.out.println("Picked up parcel: " + picked.getId() + "\n");


        // ------------------------------
        // 4️⃣ Invalid pickup attempt
        // ------------------------------

        System.out.println("🕵️ Attempting pickup with fake code");
        System.out.println("Expected: Should throw error.\n");

        try {
            manager.pickupParcel("FAKE-CODE-123");
        } catch (Exception e) {
            System.out.println("🚫 Correctly failed: " + e.getMessage() + "\n");
        }


        // ------------------------------
        // 5️⃣ Expiration simulation
        // ------------------------------

        System.out.println("⏳ Simulating expiration...");
        System.out.println("Expected: Expired lockers should be freed.\n");

        // Wait if your expiration uses System.currentTimeMillis
        Thread.sleep(2000);

        manager.cleanupExpiredLockers();

        System.out.println("Cleanup completed.\n");


        // ------------------------------
        // 6️⃣ Deliver after cleanup
        // ------------------------------

        System.out.println("📦 Delivering Parcel P5 (LARGE)");
        System.out.println("Expected: Should use available LARGE locker.\n");

        Parcel p5 = new Parcel("P5", LockerSize.LARGE);
        String code5 = manager.deliverParcel(p5);

        System.out.println("Returned pickup code: " + code5 + "\n");


        // ------------------------------
        // 7️⃣ Chaos test
        // ------------------------------

        System.out.println("🔥 CHAOS MODE: Deliver 10 SMALL parcels");
        System.out.println("Expected: Eventually throw 'NO LOCKER' error.\n");

        for (int i = 0; i < 10; i++) {
            try {
                Parcel px = new Parcel("PX-" + i, LockerSize.SMALL);
                String code = manager.deliverParcel(px);
                System.out.println("Delivered PX-" + i + " Code: " + code);
            } catch (Exception e) {
                System.out.println("💀 System says: " + e.getMessage());
                break;
            }
        }

        System.out.println("\n=======================================");
        System.out.println("🏁 Simulation Complete");
        System.out.println("=======================================");
    }
}
