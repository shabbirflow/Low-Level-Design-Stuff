public class Librarian extends User {
    public Librarian(String id, String password, Person person, LibraryCard card) {
        super(id, password, person, card);
    }

    public boolean addBookItem(Catalog catalog, BookItem bookItem) {
        catalog.addBookItem(bookItem);
        System.out.println("BookItem " + bookItem.id + " added to catalog by librarian " + id);
        return true;
    }

    public boolean blockMember(Member member) {
        member.status = AccountStatus.BLACKLISTED;
        System.out.println("Member " + member.id + " is now blacklisted.");
        return true;
    }

    public boolean unBlockMember(Member member) {
        member.status = AccountStatus.ACTIVE;
        System.out.println("Member " + member.id + " is now active.");
        return true;
    }
}
