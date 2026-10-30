package GUI;

import models.*;
import javax.swing.*;
import javax.swing.table.DefaultTableModel;
import java.awt.*;
import java.awt.event.ActionEvent;
import java.sql.*;
import java.util.Vector;

public class BookstoreAdminApp extends JFrame {

    // --- DATABASE CONFIG --- //
    private static final String DB_URL = "jdbc:mysql://localhost:3306/java";
    private static final String DB_USER = "java";
    private static final String DB_PASS = "java";

    private String userRole;

    // --- BOOK TABLE COMPONENTS --- //
    private JTable bookTable;
    private DefaultTableModel bookModel;

    // --- STAFF TABLE COMPONENTS --- //
    private JTable staffTable;
    private DefaultTableModel staffModel;

    private Person user;

public BookstoreAdminApp(Person user) {
    this.user = user;

    setTitle("Bookstore Admin Panel - Logged in as: " + user.getRole());

    JTabbedPane tabs = new JTabbedPane();

    tabs.add("Books", createBookPanel());

    if (user.canManageStaff()) {
        tabs.add("Staff", createStaffPanel());
    }

    add(tabs);

    loadBooksFromDB();
    if (user.canManageStaff()) loadStaffFromDB();

    setDefaultCloseOperation(JFrame.EXIT_ON_CLOSE);
    setSize(900, 600);
    setLocationRelativeTo(null);
    setVisible(true);
}


    // ---------------------------
    //   BOOK MANAGEMENT PANEL
    // ---------------------------
    private JPanel createBookPanel() {
    JPanel panel = new JPanel(new BorderLayout());

    bookModel = new DefaultTableModel(new String[]{"ID", "Title", "Author", "Price", "Quantity"}, 0);
    bookTable = new JTable(bookModel);

    JPanel buttonPanel = new JPanel();
    JButton addBtn = new JButton("Add Book");
    JButton updateBtn = new JButton("Update Book");
    JButton deleteBtn = new JButton("Delete Book");

    // Attach actions
    addBtn.addActionListener(this::addBook);
    updateBtn.addActionListener(this::updateBook);
    deleteBtn.addActionListener(this::deleteBook);

    // --- ROLE RESTRICTION ---
    if (!user.canManageBooks()) {
    addBtn.setEnabled(false);
    updateBtn.setEnabled(false);
    deleteBtn.setEnabled(false);
    }


    buttonPanel.add(addBtn);
    buttonPanel.add(updateBtn);
    buttonPanel.add(deleteBtn);

    panel.add(new JScrollPane(bookTable), BorderLayout.CENTER);
    panel.add(buttonPanel, BorderLayout.SOUTH);
    return panel;
}

    // STAFF MANAGEMENT PANEL
    private JPanel createStaffPanel() {
        JPanel panel = new JPanel(new BorderLayout());

        staffModel = new DefaultTableModel(new String[]{"ID", "Name", "Role"}, 0);
        staffTable = new JTable(staffModel);

        JPanel buttonPanel = new JPanel();
        JButton addBtn = new JButton("Add Staff");
        JButton updateBtn = new JButton("Update Staff");
        JButton deleteBtn = new JButton("Delete Staff");

        addBtn.addActionListener(this::addStaff);
        updateBtn.addActionListener(this::updateStaff);
        deleteBtn.addActionListener(this::deleteStaff);

        buttonPanel.add(addBtn);
        buttonPanel.add(updateBtn);
        buttonPanel.add(deleteBtn);

        panel.add(new JScrollPane(staffTable), BorderLayout.CENTER);
        panel.add(buttonPanel, BorderLayout.SOUTH);

        return panel;
    }


    // BOOK DATABASE 
    private void loadBooksFromDB() {
        bookModel.setRowCount(0);

        try (Connection conn = DriverManager.getConnection(DB_URL, DB_USER, DB_PASS);
             Statement stmt = conn.createStatement();
             ResultSet rs = stmt.executeQuery("SELECT * FROM books")) {

            while (rs.next()) {
                Vector<Object> row = new Vector<>();
                row.add(rs.getInt("id"));
                row.add(rs.getString("title"));
                row.add(rs.getString("author"));
                row.add(rs.getDouble("price"));
                row.add(rs.getInt("quantity"));
                bookModel.addRow(row);
            }
            conn.close();
        } catch (Exception e) {
            JOptionPane.showMessageDialog(this, "Error loading books: " + e.getMessage());
        }
    }

    private void addBook(ActionEvent e) {
        String title = JOptionPane.showInputDialog("Enter title:");
        if (title == null) return;
        String author = JOptionPane.showInputDialog("Enter author:");
        if (author == null) return;
        double price = Double.parseDouble(JOptionPane.showInputDialog("Enter price:"));
        int quantity = Integer.parseInt(JOptionPane.showInputDialog("Enter quantity:"));

        String sql = "INSERT INTO books (title, author, price, quantity) VALUES (?, ?, ?, ?)";

        try (Connection conn = DriverManager.getConnection(DB_URL, DB_USER, DB_PASS);
             PreparedStatement ps = conn.prepareStatement(sql)) {

            ps.setString(1, title);
            ps.setString(2, author);
            ps.setDouble(3, price);
            ps.setInt(4, quantity);
            ps.executeUpdate();

            loadBooksFromDB();
            conn.close();
        } catch (Exception ex) {
            JOptionPane.showMessageDialog(this, "Error adding book: " + ex.getMessage());
        }
    }

    private void updateBook(ActionEvent e) {
        int row = bookTable.getSelectedRow();
        if (row == -1) { JOptionPane.showMessageDialog(this, "Select a book first."); return; }

        int id = (int) bookModel.getValueAt(row, 0);

        String newTitle = JOptionPane.showInputDialog("New title:", bookModel.getValueAt(row, 1));
        if (newTitle == null) return;

        String newAuthor = JOptionPane.showInputDialog("New author:", bookModel.getValueAt(row, 2));
        if (newAuthor == null) return;

        double newPrice = Double.parseDouble(JOptionPane.showInputDialog("New price:", bookModel.getValueAt(row, 3)));
        int newQuantity = Integer.parseInt(JOptionPane.showInputDialog("New quantity:", bookModel.getValueAt(row, 4)));

        String sql = "UPDATE books SET title=?, author=?, price=?, quantity=? WHERE id=?";

        try (Connection conn = DriverManager.getConnection(DB_URL, DB_USER, DB_PASS);
             PreparedStatement ps = conn.prepareStatement(sql)) {

            ps.setString(1, newTitle);
            ps.setString(2, newAuthor);
            ps.setDouble(3, newPrice);
            ps.setInt(4, newQuantity);
            ps.setInt(5, id);
            ps.executeUpdate();

            loadBooksFromDB();
            conn.close();
        } catch (Exception ex) {
            JOptionPane.showMessageDialog(this, "Error updating book: " + ex.getMessage());
        }
    }

    private void deleteBook(ActionEvent e) {
        int row = bookTable.getSelectedRow();
        if (row == -1) { JOptionPane.showMessageDialog(this, "Select a book first."); return; }

        int id = (int) bookModel.getValueAt(row, 0);

        String sql = "DELETE FROM books WHERE id=?";

        try (Connection conn = DriverManager.getConnection(DB_URL, DB_USER, DB_PASS);
             PreparedStatement ps = conn.prepareStatement(sql)) {

            ps.setInt(1, id);
            ps.executeUpdate();
            loadBooksFromDB();
            conn.close();
        } catch (Exception ex) {
            JOptionPane.showMessageDialog(this, "Error deleting book: " + ex.getMessage());
        }
    }

    // ==================================
    //         STAFF DATABASE METHODS
    // ==================================

    private void loadStaffFromDB() {
        staffModel.setRowCount(0);

        try (Connection conn = DriverManager.getConnection(DB_URL, DB_USER, DB_PASS);
             Statement stmt = conn.createStatement();
             ResultSet rs = stmt.executeQuery("SELECT * FROM staff")) {

            while (rs.next()) {
                Vector<Object> row = new Vector<>();
                row.add(rs.getInt("id"));
                row.add(rs.getString("name"));
                row.add(rs.getString("role"));
                staffModel.addRow(row);
            }

        } catch (Exception e) {
            JOptionPane.showMessageDialog(this, "Error loading staff: " + e.getMessage());
        }
    }

    private void addStaff(ActionEvent e) {
        String name = JOptionPane.showInputDialog("Enter name:");
        if (name == null) return;
        String role = JOptionPane.showInputDialog("Enter role (ADMIN/LIBRARIAN):");
        if (role == null) return;

        String sql = "INSERT INTO staff (name, role) VALUES (?, ?)";

        try (Connection conn = DriverManager.getConnection(DB_URL, DB_USER, DB_PASS);
             PreparedStatement ps = conn.prepareStatement(sql)) {

            ps.setString(1, name);
            ps.setString(2, role);
            ps.executeUpdate();

            loadStaffFromDB();
            conn.close();
        } catch (Exception ex) {
            JOptionPane.showMessageDialog(this, "Error adding staff: " + ex.getMessage());
        }
    }

    private void updateStaff(ActionEvent e) {
        int row = staffTable.getSelectedRow();
        if (row == -1) { JOptionPane.showMessageDialog(this, "Select a staff first."); return; }

        int id = (int) staffModel.getValueAt(row, 0);

        String newName = JOptionPane.showInputDialog("New name:", staffModel.getValueAt(row, 1));
        if (newName == null) return;

        String newRole = JOptionPane.showInputDialog("New role (ADMIN/LIBRARIAN):", staffModel.getValueAt(row, 2));
        if (newRole == null) return;

        String sql = "UPDATE staff SET name=?, role=? WHERE id=?";

        try (Connection conn = DriverManager.getConnection(DB_URL, DB_USER, DB_PASS);
             PreparedStatement ps = conn.prepareStatement(sql)) {

            ps.setString(1, newName);
            ps.setString(2, newRole);
            ps.setInt(3, id);
            ps.executeUpdate();

            loadStaffFromDB();
            conn.close();
        } catch (Exception ex) {
            JOptionPane.showMessageDialog(this, "Error updating staff: " + ex.getMessage());
        }
    }

    private void deleteStaff(ActionEvent e) {
        int row = staffTable.getSelectedRow();
        if (row == -1) { JOptionPane.showMessageDialog(this, "Select a staff first."); return; }

        int id = (int) staffModel.getValueAt(row, 0);

        String sql = "DELETE FROM staff WHERE id=?";

        try (Connection conn = DriverManager.getConnection(DB_URL, DB_USER, DB_PASS);
             PreparedStatement ps = conn.prepareStatement(sql)) {

            ps.setInt(1, id);
            ps.executeUpdate();
            loadStaffFromDB();
            conn.close();
        } catch (Exception ex) {
            JOptionPane.showMessageDialog(this, "Error deleting staff: " + ex.getMessage());
        }
    }
}
