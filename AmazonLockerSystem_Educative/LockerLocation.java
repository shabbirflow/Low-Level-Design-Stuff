import java.util.*;

public class LockerLocation {
    private String name;
    private List<Locker> lockers;
    private double longitude;
    private double latitude;
    private Date openTime;
    private Date closeTime;

    public LockerLocation(String name, double longitude, double latitude, Date openTime, Date closeTime) {
        this.name = name;
        this.longitude = longitude;
        this.latitude = latitude;
        this.openTime = openTime;
        this.closeTime = closeTime;
        this.lockers = new ArrayList<>();
    }

    public String getName() {
        return name;
    }

    public List<Locker> getLockers() {
        return lockers;
    }

    public double getLongitude() {
        return longitude;
    }

    public double getLatitude() {
        return latitude;
    }

    public Date getOpenTime() {
        return openTime;
    }

    public Date getCloseTime() {
        return closeTime;
    }

    public void addLocker(Locker locker) {
        lockers.add(locker);
    }
}