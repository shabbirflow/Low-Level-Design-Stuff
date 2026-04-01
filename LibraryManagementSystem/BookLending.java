import java.util.*;

public class BookLending {
    static Map<String, BookLending> lendings = new HashMap<>();
    String itemId;
    Date creationDate, dueDate, returnDate;
    String memberId;

    public BookLending(String itemId, String memberId) {
        this.itemId = itemId;
        this.memberId = memberId;
        creationDate = new Date();
        Calendar c = Calendar.getInstance();
        c.setTime(creationDate);
        c.add(Calendar.DATE, 15);
        dueDate = c.getTime();
        lendings.put(itemId, this);
    }

    public static BookLending lendBook(String bookItemId, String memberId) {
        BookLending lending = new BookLending(bookItemId, memberId);
        System.out.println("BookItem " + bookItemId + " lent to member " + memberId);
        return lending;
    }

    public static BookLending fetchLendingDetails(String bookItemId) {
        return lendings.get(bookItemId);
    }

    public Date getReturnDate() {
        return returnDate;
    }
}
