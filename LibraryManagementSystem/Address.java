public class Address {
    String street, city, state, country;
    int zipcode;

    public Address(String street, String city, String state, int zipcode, String country) {
        this.street = street;
        this.city = city;
        this.state = state;
        this.zipcode = zipcode;
        this.country = country;
    }

    public String toString() {
        return street + ", " + city + ", " + state + ", " + zipcode + ", " + country;
    }
}
