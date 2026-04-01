import java.util.*;

public class BookReservation {
    static Map<String, BookReservation> reservations = new HashMap<>();
    String itemId;
    Date creationDate;
    ReservationStatus status;
    String memberId;

    public BookReservation(String itemId, String memberId) {
        this.itemId = itemId;
        this.creationDate = new Date();
        this.status = ReservationStatus.PENDING;
        this.memberId = memberId;
        reservations.put(itemId, this);
    }

    public static BookReservation fetchReservationDetails(String bookItemId) {
        return reservations.get(bookItemId);
    }
}
