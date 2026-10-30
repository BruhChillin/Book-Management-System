package models;

public abstract class Person {

    private String username;
    private String password;
    private String role;

    public Person(String username, String password, String role) {
        this.username = username;
        this.password = password;
        this.role = role;
    }

    public String getUsername() { return username;}
    public String getPassword() { return password; }
    public String getRole() { return role; }

    public abstract boolean canManageBooks();
    public abstract boolean canViewStaff();

    public void viewBooks() {
        System.out.println(role + " " + username + " is viewing the book list.");
    }
}

