import java.util.*;

public class Author extends Person {
    String description;

    public Author(String name, Address address, String email, String phone, String description) {
        super(name, address, email, phone);
        this.description = description;
    }
}
