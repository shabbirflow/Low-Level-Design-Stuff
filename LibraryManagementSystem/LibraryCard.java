import java.util.Date;

public class LibraryCard {
    String cardNumber;
    Date issued;
    boolean active = true;

    public LibraryCard(String cardNumber, Date issued) {
        this.cardNumber = cardNumber;
        this.issued = issued;
    }

    public boolean isActive() {
        return active;
    }
}
