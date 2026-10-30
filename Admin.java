package models;

public class Admin extends Person {

    public Admin(String username, String password) {
        super(username, password, "ADMIN");
    }

    @Override
    public boolean canManageBooks() { return true; }

    @Override
    public boolean canViewStaff() { return true; }
}