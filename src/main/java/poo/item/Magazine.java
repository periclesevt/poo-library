package poo.item;

import poo.entity.User;

import java.time.LocalDate;

public class Magazine extends  LibraryItem implements Borrowable {
    private String issn;
    private int editionNumber;

    // Constructors
    public Magazine(String title, String editor, int publicationYear, String issn, int editionNumber) {
        super(title, editor, publicationYear); // Using author field for editor
        setISSN(issn); // Use setter for validation
        setEditionNumber(editionNumber); // Use setter for validation
    }

    // Overloaded constructor (example for polymorphism)
    public Magazine(String title, String editor, String issn, int editionNumber) {
        this(title, editor, LocalDate.now().getYear(), issn, editionNumber); // Default year to current
    }

    // Getter for ISSN
    public String getISSN() {
        return issn;
    }

    // Setter for ISSN with validation (Encapsulation)
    public void setISSN(String issn) {
        if (issn == null || issn.trim().isEmpty()) {
            throw new IllegalArgumentException("ISSN cannot be null or empty.");
        }
        // Basic ISSN format validation (e.g., 8 digits, optional hyphen)
        if (!issn.matches("^\\d{4}-\\d{3}[\\dX]$")) { // Example: XXXX-XXXX or XXXX-XXXD, where D is a digit
            System.err.println("Warning: The ISSN format '" + issn + "' might be invalid. Example: 1234-5678");
            // You might choose to throw an IllegalArgumentException here instead of just printing
            // throw new IllegalArgumentException("Invalid ISSN format.");
        }
        this.issn = issn;
    }

    // Getter for Edition Number
    public int getEditionNumber() {
        return editionNumber;
    }

    // Setter for Edition Number with validation (Encapsulation)
    public void setEditionNumber(int editionNumber) {
        if (editionNumber <= 0) {
            throw new IllegalArgumentException("Edition number must be a positive integer.");
        }
        this.editionNumber = editionNumber;
    }

    // Implementation of Borrowable interface methods
    @Override
    public boolean borrowItem(User user) {
        if (isAvailable()) {
            // Here, you'd ideally also check user's loan limit and if they are blocked
            // This method in Magazine only handles its own availability status
            setAvailable(false);
            incrementBorrowCount(); // Increment the loan counter inherited from LibraryItem
            System.out.println("Magazine '" + getTitle() + "' successfully borrowed by " + user.getName() + ".");
            return true;
        } else {
            System.out.println("Magazine '" + getTitle() + "' is not available for borrowing.");
            return false;
        }
    }

    @Override
    public boolean returnItem() {
        if (!isAvailable()) {
            setAvailable(true);
            System.out.println("Magazine '" + getTitle() + "' successfully returned.");
            return true;
        } else {
            System.out.println("Magazine '" + getTitle() + "' is already available (was not borrowed).");
            return false;
        }
    }

    @Override
    public int getBorrowPeriodDays() {
        // Default loan period for a magazine (e.g., 7 days)
        return 7;
    }

    // Overriding toString() method (Polymorphism)
    @Override
    public String toString() {
        return "Magazine{" +
                "title='" + getTitle() + '\'' +
                ", editor='" + getAuthor() + '\'' + // Using getAuthor() as editor
                ", publicationYear=" + getPublicationYear() +
                ", issn='" + issn + '\'' +
                ", editionNumber=" + editionNumber +
                ", available=" + isAvailable() +
                ", borrowCount=" + getBorrowCount() +
                '}';
    }
}
