package poo.item;

import java.time.LocalDate;

public class LibraryItem {

    private String title;
    private String author; // Can also represent director for DVD, or editor for Magazine
    private int publicationYear;
    private boolean available; // Indicates if the item is currently available for borrowing
    private int borrowCount; // To track how many times the item has been borrowed for reports

    // Constructor
    public LibraryItem(String title, String author, int publicationYear) {
        setTitle(title);
        setAuthor(author);
        setPublicationYear(publicationYear);
        this.available = true; // New items are available by default
        this.borrowCount = 0; // Initialize borrow count
    }

    // Getters
    public String getTitle() {
        return title;
    }

    public String getAuthor() {
        return author;
    }

    public int getPublicationYear() {
        return publicationYear;
    }

    public boolean isAvailable() {
        return available;
    }

    public int getBorrowCount() {
        return borrowCount;
    }

    // Setters with basic validation (Encapsulation)
    public void setTitle(String title) {
        if (title == null || title.trim().isEmpty()) {
            throw new IllegalArgumentException("Title cannot be null or empty.");
        }
        this.title = title;
    }

    public void setAuthor(String author) {
        if (author == null || author.trim().isEmpty()) {
            throw new IllegalArgumentException("Author/Director/Editor cannot be null or empty.");
        }
        this.author = author;
    }

    public void setPublicationYear(int publicationYear) {
        // Basic validation: publication year should not be in the future
        if (publicationYear <= 0 || publicationYear > LocalDate.now().getYear()) {
            throw new IllegalArgumentException("Publication year is invalid.");
        }
        this.publicationYear = publicationYear;
    }

    public void setAvailable(boolean available) {
        this.available = available;
    }

    // Method to increment borrow count
    protected void incrementBorrowCount() {
        this.borrowCount++;
    }

    // Overriding toString() method (Polymorphism)
    @Override
    public String toString() {
        return "LibraryItem{" +
                "title='" + title + '\'' +
                ", author='" + author + '\'' +
                ", publicationYear=" + publicationYear +
                ", available=" + available +
                ", borrowCount=" + borrowCount +
                '}';
    }
}
