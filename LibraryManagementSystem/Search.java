import java.util.*;

public interface Search {
    List<BookItem> searchByTitle(String title);
    List<BookItem> searchByAuthor(String author);
    List<BookItem> searchBySubject(String subject);
    List<BookItem> searchByPublicationDate(Date pubDate);
}
