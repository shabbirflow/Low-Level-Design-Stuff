public abstract class User {
    String id, password;
    AccountStatus status;
    Person person;
    LibraryCard card;

    public User(String id, String password, Person person, LibraryCard card) {
        this.id = id;
        this.password = password;
        this.person = person;
        this.card = card;
        this.status = AccountStatus.ACTIVE;
    }

    public boolean resetPassword() {
        System.out.println("Reset password for user " + id);
        return true;
    }
}
