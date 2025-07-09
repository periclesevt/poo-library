package poo.item;

import poo.entity.User;
import poo.item.Borrowable;
import poo.item.LibraryItem;
import java.time.LocalDate;

public class Book extends LibraryItem implements Borrowable {
    private String isbn;

    // Constructors
    public Book(String title, String author, int publicationYear, String isbn) {
        super(title, author, publicationYear);
        setISBN(isbn); // Use setter for validation during construction
    }

    // Overloaded constructor (example for polymorphism)
    public Book(String title, String author, String isbn) {
        this(title, author, LocalDate.now().getYear(), isbn); // Default year to current
    }

    // Getter for ISBN
    public String getISBN() {
        return isbn;
    }

    // Setter for ISBN with validation (Encapsulation)
    public void setISBN(String isbn) {
        if (isbn == null || isbn.trim().isEmpty()) {
            throw new IllegalArgumentException("ISBN cannot be null or empty.");
        }
        // Updated Regex: Validates 13 digits, optionally with hyphens in typical ISBN-13 format.
        // It no longer tries to match "ISBN:" prefix, which was causing the 'unnecessary non-capturing group' warning.
        // Example valid formats: "9781234567890", "978-1-234-56789-0"
        if (!isbn.matches("^(?:978|979)(?:-?\\d){9}(\\d|X)$")) {
            System.err.println("Warning: The ISBN format '" + isbn + "' might be invalid. Expected 13 digits (optionally with hyphens). Ex: 978-85-7836-070-8");
            // You might choose to throw an IllegalArgumentException here instead of just printing
            // throw new IllegalArgumentException("Invalid ISBN format.");
        }
        this.isbn = isbn;
    }

    // Implementation of Borrowable interface methods
    @Override
    public boolean borrowItem(User user) {
        if (isAvailable()) {
            // Here, you'd ideally also check user's loan limit and if they are blocked
            // This method in Book only handles its own availability status
            setAvailable(false);
            incrementBorrowCount(); // Increment the loan counter inherited from LibraryItem
            System.out.println("Book '" + getTitle() + "' successfully borrowed by " + user.getName() + ".");
            return true;
        } else {
            System.out.println("Book '" + getTitle() + "' is not available for borrowing.");
            return false;
        }
    }

    @Override
    public boolean returnItem() {
        if (!isAvailable()) {
            setAvailable(true);
            System.out.println("Book '" + getTitle() + "' successfully returned.");
            return true;
        } else {
            System.out.println("Book '" + getTitle() + "' is already available (was not borrowed).");
            return false;
        }
    }

    @Override
    public int getBorrowPeriodDays() {
        // Default loan period for a book (e.g., 15 days)
        return 15;
    }

    // Overriding toString() method (Polymorphism)
    @Override
    public String toString() {
        return "Book{" +
                "title='" + getTitle() + '\'' +
                ", author='" + getAuthor() + '\'' +
                ", publicationYear=" + getPublicationYear() +
                ", isbn='" + isbn + '\'' +
                ", available=" + isAvailable() +
                ", borrowCount=" + getBorrowCount() +
                '}';
    }
}