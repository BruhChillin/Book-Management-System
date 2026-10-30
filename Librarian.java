package models;

public class Librarian extends Person {

    public Librarian(String username, String password) {
        super(username, password, "LIBRARIAN");
    }

    @Override
    public boolean canManageBooks() { return true; }

    @Override
    public boolean canViewStaff() { return false; }
}
