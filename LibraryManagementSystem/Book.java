import java.util.*;

public class Book {
    String isbn, title, subject, publisher, language;
    Date publicationDate;
    int numberOfPages;
    BookFormat format;
    List<Author> authors;

    public Book(String isbn, String title, String subject, String publisher, Date publicationDate,
            String language, int numberOfPages, BookFormat format, List<Author> authors) {
        this.isbn = isbn;
        this.title = title;
        this.subject = subject;
        this.publisher = publisher;
        this.publicationDate = publicationDate;
        this.language = language;
        this.numberOfPages = numberOfPages;
        this.format = format;
        this.authors = authors;
    }
}
