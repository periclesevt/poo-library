package poo.item;

import poo.entity.User;

import java.time.LocalDate;

public class Dvd extends LibraryItem implements Borrowable {
    private String director;

    // Constructors
    public Dvd(String title, String director, int publicationYear) {
        super(title, director, publicationYear); // Using author field for director
        setDirector(director); // Use setter for validation
    }

    // Overloaded constructor (example for polymorphism)
    public Dvd(String title, String director) {
        this(title, director, LocalDate.now().getYear()); // Default year to current
    }

    // Getter for Director
    public String getDirector() {
        return director;
    }

    // Setter for Director with validation (Encapsulation)
    public void setDirector(String director) {
        if (director == null || director.trim().isEmpty()) {
            throw new IllegalArgumentException("Director cannot be null or empty.");
        }
        this.director = director;
    }

    // Implementation of Borrowable interface methods
    @Override
    public boolean borrowItem(User user) {
        if (isAvailable()) {
            // Here, you'd ideally also check user's loan limit and if they are blocked
            // This method in Dvd only handles its own availability status
            setAvailable(false);
            incrementBorrowCount(); // Increment the loan counter inherited from LibraryItem
            System.out.println("DVD '" + getTitle() + "' successfully borrowed by " + user.getName() + ".");
            return true;
        } else {
            System.out.println("DVD '" + getTitle() + "' is not available for borrowing.");
            return false;
        }
    }

    @Override
    public boolean returnItem() {
        if (!isAvailable()) {
            setAvailable(true);
            System.out.println("DVD '" + getTitle() + "' successfully returned.");
            return true;
        } else {
            System.out.println("DVD '" + getTitle() + "' is already available (was not borrowed).");
            return false;
        }
    }

    @Override
    public int getBorrowPeriodDays() {
        // Default loan period for a DVD (e.g., 7 days)
        return 7;
    }

    // Overriding toString() method (Polymorphism)
    @Override
    public String toString() {
        return "Dvd{" +
                "title='" + getTitle() + '\'' +
                ", director='" + director + '\'' +
                ", publicationYear=" + getPublicationYear() +
                ", available=" + isAvailable() +
                ", borrowCount=" + getBorrowCount() +
                '}';
    }
}
