package models;

public class Member extends Person {
    public Member(String username, String password) {
        super(username, password, "MEMBER");
    }

    @Override
    public boolean canManageBooks() { return false; }

    @Override
    public boolean canViewStaff() { return false; }

    public void paid() {
        System.out.println(getRole() + " " + getUsername() + " is paying to the book.");
    }

    public void borrow() {
        System.out.println(getRole() + " " + getUsername() + " is borrowing the book.");
    }
}

