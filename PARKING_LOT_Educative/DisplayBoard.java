import java.util.*;

public class DisplayBoard {
    private int id;
    private Map<String, Integer> freeCount = new HashMap<>();

    public DisplayBoard(int id) {
        this.id = id;
    }

    public void update(Collection<ParkingSpot> spots) {
        freeCount.clear();
        for (ParkingSpot s : spots) {
            if (s.isFree()) {
                String type = s.getClass().getSimpleName();
                freeCount.put(type, freeCount.getOrDefault(type, 0) + 1);
            }
        }
    }

    public void showFreeSlot() {
        System.out.println("\nFree slots by type:");
        System.out.printf("%-15s %s%n", "Type", "Count");
        for (String type : freeCount.keySet())
            System.out.printf("%-15s %d%n", type, freeCount.get(type));
    }
}
/*
 * The DisplayBoard class tracks and displays the number of available parking
 * spots by type.
 * 
 * - update(Collection<ParkingSpot> spots): This method clears the current
 * counts,
 * iterates through all provided spots, and for each free spot, increments a
 * counter in the 'freeCount' map based on the spot's specific class type
 * (e.g., "HandicapSpot", "CompactSpot").
 * 
 * - showFreeSlot(): This method prints a formatted table showing the total
 * number
 * of free slots for each category currently stored in the 'freeCount' map.
 */
