package models;

// Class LibraryItem
abstract class LibraryItem {
    protected String title;
    protected String author;
    protected int numAvailable;
    protected double price;

    LibraryItem(String title, String author, int numAvailable, double price) {
        this.title = title;
        this.author = author;
        this.numAvailable = numAvailable;
        this.price = price;
    }

    // Get functions
    public abstract String getTitle();
    public abstract String getAuthor();
    public abstract int getNumAvailable();
    public abstract double getPrice();

    // Set functions
    abstract void setTitle(String title);
    abstract void setAuthor(String author);
    abstract void setNumAvailable(int numAvailable);
    abstract void setPrice(double price);

    public abstract void displayInfo();
}