package models;

// Subclass Book
public class Magazine extends LibraryItem {
   Magazine(String title, String author, int numAvailable, double price) {
        super(title, author, numAvailable, price);
    }
    
    public boolean canBorrow() {
        return getNumAvailable() > 0;
    }

    // Get functions
    public String getTitle(){
            return this.title;
    }

    public String getAuthor() {
        return this.author;
    }

    public double getPrice() {
        return this.price;
    }
    public int getNumAvailable(){
        return this.numAvailable;
    }
    // Set functions
    void setTitle(String title){
        this.title = title;
    }

    void setAuthor(String author) {
        this.author = author;
    }

    void setPrice(double price) {
        this.price = price;
    }

    void setNumAvailable(int numAvailable){
        this.numAvailable = numAvailable;
    }

    public void displayInfo() {
        System.out.println("Magazine Info:");
        System.out.println("Title: " + getTitle());
        System.out.println("Author: " + getAuthor());
        System.out.println("Price: $" + getPrice());
        System.out.println("Number Available: " + getNumAvailable());
    }
}