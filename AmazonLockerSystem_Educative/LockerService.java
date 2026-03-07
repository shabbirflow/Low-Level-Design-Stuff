import java.util.*;

public class LockerService {
    private List<LockerLocation> locations;
    private static LockerService lockerService = null;

    private LockerService() {
        this.locations = new ArrayList<>();
    }

    public static LockerService getInstance() {
        if (lockerService == null) {
            lockerService = new LockerService();
        }
        return lockerService;
    }

    public List<LockerLocation> getLocations() {
        return locations;
    }

    public void addLocation(LockerLocation loc) {
        locations.add(loc);
    }

    public Locker findLockerById(String lockerId) {
        for (LockerLocation loc : locations) {
            for (Locker l : loc.getLockers()) {
                if (l.getLockerId().equals(lockerId)) {
                    return l;
                }
            }
        }
        return null;
    }

    public boolean requestReturn(Order order) {
        System.out.println("Return request received for order: " + order.getOrderId());
        return true;
    }

    public Locker requestLocker(LockerSize size) {
        for (LockerLocation loc : locations) {
            for (Locker l : loc.getLockers()) {
                if (l.getLockerState() == LockerState.AVAILABLE && l.getLockerSize() == size) {
                    System.out.println("Found available locker: " + l.getLockerId());
                    return l;
                }
            }
        }
        System.out.println("No available lockers at the moment.");
        return null;
    }

    public boolean verifyOTP(LockerPackage lpkg, String code) {
        return lpkg.verifyCode(code);
    }
}