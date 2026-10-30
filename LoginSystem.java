package GUI;

import models.*;
import javax.swing.*;
import java.awt.*;

public class LoginSystem extends JFrame {
    // private static final String ADMIN_USERNAME = "admin";
    // private static final String ADMIN_PASSWORD = "admin123";
    // private static final String ADMIN_ROLE = "ADMIN";
    Admin A00 = new Admin("admin",  "admin123");
    
    // private static final String LIBRARIAN_USERNAME = "library";
    // private static final String LIBRARIAN_PASSWORD = "library123";
    // private static final String LIBRARIAN_ROLE = "LIBRARIAN";
    Librarian L00 = new Librarian("library", "library123");

    // private static final String MEMBER_USERNAME = "member";
    // private static final String MEMBER_PASSWORD = "member123";
    // private static final String MEMBER_ROLE = "MEMBER";
    Member M00 = new Member("member", "member123");

    private JTextField usernameField;
    private JPasswordField passwordField;

    public LoginSystem() {
        setTitle("Bookstore Login");
        setSize(400, 200);
        setDefaultCloseOperation(JFrame.EXIT_ON_CLOSE);
        setLocationRelativeTo(null);

        // Layout
        JPanel panel = new JPanel(new GridLayout(3, 2, 10, 10));

        panel.add(new JLabel("Username:"));
        usernameField = new JTextField();
        panel.add(usernameField);

        panel.add(new JLabel("Password:"));
        passwordField = new JPasswordField();
        panel.add(passwordField);

        JButton loginButton = new JButton("Login");
        loginButton.addActionListener(e -> handleLogin());
        panel.add(loginButton);

        add(panel);
        setVisible(true);
    }

    private void handleLogin() {
        String username = usernameField.getText().trim();
        String password = new String(passwordField.getPassword()).trim();

        Person person = validateUser(username, password);

        if (person == null) {
            JOptionPane.showMessageDialog(this, "Invalid credentials!");
            return;
        }

        this.dispose();
        new BookstoreAdminApp(person);   // pass the Person object
    }

    private Person validateUser(String user, String pass) {

        if (user.equals("admin") && pass.equals("admin123"))
            return A00;

        if (user.equals("library") && pass.equals("library123"))
            return L00;

        if (user.equals("member") && pass.equals("member123"))
            return M00;

        return null;
    }


    public static void main(String[] args) {
        new LoginSystem();
    }
}
