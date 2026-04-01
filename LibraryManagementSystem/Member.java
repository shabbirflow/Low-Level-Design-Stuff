import java.util.*;

public class Member extends User {
    Date dateOfMembership;
    int totalBooksCheckedOut = 0;
    List<BookItem> booksBorrowed = new ArrayList<>();
    double finesDue = 0.0;

    public Member(String id, String password, Person person, LibraryCard card) {
        super(id, password, person, card);
        this.dateOfMembership = new Date();
    }

    public boolean reserveBookItem(BookItem bookItem) {
        if (bookItem.reserve()) {
            System.out.println("BookItem " + bookItem.id + " reserved by member " + id);
            return true;
        }
        System.out.println("Cannot reserve book; it is not available.");
        return false;
    }

    public boolean checkoutBookItem(BookItem bookItem) {
        if (totalBooksCheckedOut >= 10) {
            System.out.println("Book limit reached.");
            return false;
        }
        if (bookItem.checkout(id)) {
            booksBorrowed.add(bookItem);
            totalBooksCheckedOut++;
            System.out.println("Member " + id + " checked out BookItem " + bookItem.id);
            return true;
        }
        return false;
    }

    public boolean returnBookItem(BookItem bookItem) {
        if (!booksBorrowed.contains(bookItem)) {
            System.out.println("Book not checked out by member.");
            return false;
        }
        int lateDays = 0;
        if (bookItem.dueDate != null) {
            long diffMs = new Date().getTime() - bookItem.dueDate.getTime();
            lateDays = (int) (diffMs / (1000 * 60 * 60 * 24));
        }
        if (lateDays > 0) {
            double fine = Fine.collectFine(id, lateDays);
            finesDue += fine;
            System.out.printf("Fine of $%.2f applied for %d late days.\n", fine, lateDays);
        }
        bookItem.returnBook();
        booksBorrowed.remove(bookItem);
        totalBooksCheckedOut--;
        return true;
    }

    public boolean renewBookItem(BookItem bookItem) {
        if (booksBorrowed.contains(bookItem)) {
            return bookItem.renew();
        }
        System.out.println("This member has not checked out the book.");
        return false;
    }
}
