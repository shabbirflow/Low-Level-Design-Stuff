import java.util.*;
import java.text.SimpleDateFormat;

public class Catalog implements Search {
    Map<String, List<BookItem>> bookTitles = new HashMap<>();
    Map<String, List<BookItem>> bookAuthors = new HashMap<>();
    Map<String, List<BookItem>> bookSubjects = new HashMap<>();
    Map<String, List<BookItem>> bookPublicationDates = new HashMap<>();
    Map<String, BookItem> allBookItems = new HashMap<>();

    public void addBookItem(BookItem bookItem) {
        allBookItems.put(bookItem.id, bookItem);
        // By title
        bookTitles.computeIfAbsent(bookItem.book.title, k -> new ArrayList<>()).add(bookItem);
        // By author
        for (Author author : bookItem.book.authors)
            bookAuthors.computeIfAbsent(author.name, k -> new ArrayList<>()).add(bookItem);
        // By subject
        bookSubjects.computeIfAbsent(bookItem.book.subject, k -> new ArrayList<>()).add(bookItem);
        // By publication date
        String pubDateStr = new SimpleDateFormat("yyyy-MM-dd").format(bookItem.book.publicationDate);
        bookPublicationDates.computeIfAbsent(pubDateStr, k -> new ArrayList<>()).add(bookItem);
    }

    public List<BookItem> searchByTitle(String title) {
        return bookTitles.getOrDefault(title, new ArrayList<>());
    }
    public List<BookItem> searchByAuthor(String author) {
        return bookAuthors.getOrDefault(author, new ArrayList<>());
    }
    public List<BookItem> searchBySubject(String subject) {
        return bookSubjects.getOrDefault(subject, new ArrayList<>());
    }
    public List<BookItem> searchByPublicationDate(Date pubDate) {
        String dateStr = new SimpleDateFormat("yyyy-MM-dd").format(pubDate);
        return bookPublicationDates.getOrDefault(dateStr, new ArrayList<>());
    }
}
