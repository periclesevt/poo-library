package poo.system;

import poo.item.Book; // Correct import
import poo.item.Magazine; // Correct import
import poo.entity.User;
import poo.item.LibraryItem;
import java.time.LocalDate;
import java.util.UUID; // For generating unique IDs

public class Borrow {
    private final String borrowId; // Made final as it's set once in constructor
    private final User user;       // Made final as it's set once in constructor
    private final LibraryItem item; // Made final as it's set once in constructor
    private final LocalDate borrowDate; // Made final as it's set once in constructor
    private LocalDate dueDate;
    private LocalDate returnDate; // Null if not yet returned
    private boolean isRenewed; // To track if the borrow has been renewed

    // Constructor
    public Borrow(User user, LibraryItem item, LocalDate borrowDate, LocalDate dueDate) {
        if (user == null || item == null || borrowDate == null || dueDate == null) {
            throw new IllegalArgumentException("Borrow constructor parameters cannot be null.");
        }
        if (borrowDate.isAfter(dueDate)) {
            throw new IllegalArgumentException("Borrow date cannot be after due date.");
        }

        this.borrowId = UUID.randomUUID().toString(); // Generate a unique ID for each borrow
        this.user = user;
        this.item = item;
        this.borrowDate = borrowDate;
        this.dueDate = dueDate;
        this.returnDate = null; // Initially not returned
        this.isRenewed = false; // Not renewed by default
    }

    // Getters
    public String getBorrowId() {
        return borrowId;
    }

    public User getUser() {
        return user;
    }

    public LibraryItem getItem() {
        return item;
    }

    public LocalDate getBorrowDate() { // This getter is fine, even if not used internally
        return borrowDate;
    }

    public LocalDate getDueDate() {
        return dueDate;
    }

    public LocalDate getReturnDate() {
        return returnDate;
    }

    public boolean isRenewed() {
        return isRenewed;
    }

    // Setters (with validation where appropriate)
    public void setDueDate(LocalDate dueDate) {
        if (dueDate == null) {
            throw new IllegalArgumentException("Due date cannot be null.");
        }
        // Ensure new due date is not before the original borrow date
        if (dueDate.isBefore(this.borrowDate)) {
            throw new IllegalArgumentException("Due date cannot be before borrow date.");
        }
        this.dueDate = dueDate;
    }

    public void setReturnDate(LocalDate returnDate) {
        // returnDate can be null if not yet returned, so no null check for null assignment
        this.returnDate = returnDate;
    }

    public void setRenewed(boolean renewed) {
        isRenewed = renewed;
    }

    // Overriding toString() method
    @Override
    public String toString() {
        // Safely determine item ID display based on type
        String itemIdDisplay;
        if (item instanceof Book) {
            itemIdDisplay = "ISBN: " + ((Book) item).getISBN();
        } else if (item instanceof Magazine) {
            itemIdDisplay = "ISSN: " + ((Magazine) item).getISSN();
        } else {
            // For DVD or generic LibraryItem, use a combination of title, author, year
            itemIdDisplay = "ID: " + item.getTitle() + "-" + item.getAuthor() + "-" + item.getPublicationYear();
        }

        return "Borrow{" +
                "borrowId='" + borrowId + '\'' +
                ", user=" + user.getName() + " (CPF: " + user.getCPF() + ')' +
                ", item=" + item.getTitle() + " (" + itemIdDisplay + ')' + // Updated item ID display
                ", borrowDate=" + borrowDate +
                ", dueDate=" + dueDate +
                ", returnDate=" + (returnDate != null ? returnDate : "N/A") +
                ", isRenewed=" + isRenewed +
                '}';
    }
}