public class Library {
    private static Library instance = null;
    String name;
    Address address;
    Catalog catalog;

    private Library(String name, Address address) {
        this.name = name;
        this.address = address;
        this.catalog = new Catalog();
    }

    public static Library getInstance(String name, Address address) {
        if (instance == null)
            instance = new Library(name, address);
        return instance;
    }

    public String getAddress() {
        return address.toString();
    }
}
