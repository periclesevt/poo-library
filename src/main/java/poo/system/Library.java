package poo.system;

import poo.item.*; // Import all classes from the 'item' package (Book, Dvd, Magazine, LibraryItem)
import poo.entity.*; // Import all classes from the 'entity' package (Librarian, Person, User)
import java.time.LocalDate;
import java.time.Period; // Still unused, but might be used by a new calculatePenalty logic in Library
import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;
import java.util.stream.Collectors;

public class Library {

    // Collections to store all entities - now declared as final
    private final Map<String, User> users; // Key: User's CPF
    private final Map<String, LibraryItem> libraryItems; // Key: Item's unique ID (e.g., ISBN, or generated ID)
    private final List<Borrow> activeBorrows; // Borrows that are currently active (item not returned)
    private final List<Borrow> borrowHistory; // All borrows, including returned ones, for reports
    private final List<Penalty> pendingPenalties; // Penalties not yet paid
    private final List<Penalty> paidPenalties; // Paid penalties for revenue reports

    // Constructor
    public Library() {
        this.users = new HashMap<>();
        this.libraryItems = new HashMap<>();
        this.activeBorrows = new ArrayList<>();
        this.borrowHistory = new ArrayList<>();
        this.pendingPenalties = new ArrayList<>();
        this.paidPenalties = new ArrayList<>();
    }

    // --- Item Management Methods ---

    public boolean addItem(LibraryItem item) {
        if (item == null) {
            System.err.println("Cannot add a null item.");
            return false;
        }
        String itemId = getItemIdentifier(item);
        if (libraryItems.containsKey(itemId)) {
            System.err.println("Item with ID " + itemId + " already exists.");
            return false;
        }
        libraryItems.put(itemId, item);
        System.out.println("Item '" + item.getTitle() + "' added successfully.");
        return true;
    }

    public LibraryItem getItem(String itemId) {
        return libraryItems.get(itemId);
    }

    public boolean updateItem(String itemId, String newTitle, String newAuthor, int newPublicationYear) {
        LibraryItem item = libraryItems.get(itemId);
        if (item == null) {
            System.err.println("Item with ID " + itemId + " not found for update.");
            return false;
        }
        // Update common properties
        item.setTitle(newTitle);
        item.setAuthor(newAuthor);
        item.setPublicationYear(newPublicationYear);

        // Specific updates for subclasses (e.g., ISBN for Book, ISSN for Magazine, Director for DVD)
        // This would typically involve casting and specific setters, or a more generic update map
        // For simplicity in this method, we'll keep it general.
        // In a real Swing app, specific update forms would handle this.

        System.out.println("Item '" + item.getTitle() + "' updated successfully.");
        return true;
    }

    public List<LibraryItem> listAllItems() {
        return new ArrayList<>(libraryItems.values());
    }

    public List<LibraryItem> listItemsByCategory(String category) {
        return libraryItems.values().stream()
                .filter(item -> {
                    if ("Book".equalsIgnoreCase(category)) return item instanceof Book;
                    if ("Magazine".equalsIgnoreCase(category)) return item instanceof Magazine;
                    if ("Dvd".equalsIgnoreCase(category)) return item instanceof Dvd;
                    return true; // If category is "all" or invalid, return all
                })
                .collect(Collectors.toList());
    }

    // Helper to get a unique ID for library items
    private String getItemIdentifier(LibraryItem item) {
        if (item instanceof Book book) { // Using pattern variable
            return book.getISBN();
        } else if (item instanceof Magazine magazine) { // Using pattern variable
            return magazine.getISSN();
        } else if (item instanceof Dvd dvd) { // Using pattern variable
            // For DVD, we might need a unique ID generation logic if no natural unique key exists
            // For now, let's use a combination of title and director/year as a basic identifier
            return dvd.getTitle() + "-" + dvd.getAuthor() + "-" + dvd.getPublicationYear();
        }
        return item.getTitle(); // Fallback for generic LibraryItem or if no specific ID applies
    }

    // --- User Management Methods ---

    public boolean registerUser(User user) {
        if (user == null) {
            System.err.println("Cannot register a null user.");
            return false;
        }
        if (users.containsKey(user.getCPF())) {
            System.err.println("User with CPF " + user.getCPF() + " already exists.");
            return false;
        }
        users.put(user.getCPF(), user);
        System.out.println("User '" + user.getName() + "' registered successfully.");
        return true;
    }

    public User getUser(String cpf) {
        return users.get(cpf);
    }

    // --- Borrow Management Methods ---

    public Borrow performBorrow(String userCpf, String itemId) {
        User user = users.get(userCpf);
        LibraryItem item = libraryItems.get(itemId);

        if (user == null) {
            System.err.println("Borrow failed: User with CPF " + userCpf + " not found.");
            return null;
        }
        if (item == null) {
            System.err.println("Borrow failed: Item with ID " + itemId + " not found.");
            return null;
        }
        if (!item.isAvailable()) {
            System.err.println("Borrow failed: Item '" + item.getTitle() + "' is not available.");
            return null;
        }
        if (user.isBlocked()) {
            System.err.println("Borrow failed: User '" + user.getName() + "' is blocked due to pending penalties.");
            return null;
        }

        // Check user's borrow limit
        long currentUserBorrows = activeBorrows.stream()
                .filter(b -> b.getUser().getCPF().equals(userCpf) && b.getReturnDate() == null)
                .count();
        if (currentUserBorrows >= user.getUserType().getMaxBorrows()) {
            System.err.println("Borrow failed: User '" + user.getName() + "' has reached their maximum borrow limit (" + user.getUserType().getMaxBorrows() + ").");
            return null;
        }

        // Ensure item is Borrowable (though all LibraryItem subclasses should implement it)
        if (!(item instanceof Borrowable borrowableItem)) { // Using pattern variable
            System.err.println("Borrow failed: Item '" + item.getTitle() + "' is not borrowable.");
            return null;
        }

        if (borrowableItem.borrowItem(user)) { // Updates item's availability and borrow count
            LocalDate borrowDate = LocalDate.now();
            LocalDate dueDate = borrowDate.plusDays(user.getUserType().getDefaultBorrowPeriodDays()); // User type specific period

            Borrow newBorrow = new Borrow(user, item, borrowDate, dueDate);
            activeBorrows.add(newBorrow);
            borrowHistory.add(newBorrow); // Add to overall history
            user.addBorrowToHistory(newBorrow); // Add to user's history
            System.out.println("Borrow successful: '" + item.getTitle() + "' to '" + user.getName() + "'. Due: " + dueDate);
            return newBorrow;
        }
        return null;
    }

    public boolean returnBorrow(String borrowId) {
        Borrow borrowToReturn = activeBorrows.stream()
                .filter(b -> b.getBorrowId().equals(borrowId))
                .findFirst()
                .orElse(null);

        if (borrowToReturn == null) {
            System.err.println("Return failed: Active borrow with ID " + borrowId + " not found.");
            return false;
        }

        // Check if already returned (safety check)
        if (borrowToReturn.getReturnDate() != null) {
            System.err.println("Return failed: Borrow ID " + borrowId + " has already been returned.");
            return false;
        }

        // Update item availability
        if (!(borrowToReturn.getItem() instanceof Borrowable borrowableItem)) { // Using pattern variable
            System.err.println("Internal error: Borrowed item is not Borrowable.");
            return false;
        }

        if (borrowableItem.returnItem()) { // Updates item's availability
            borrowToReturn.setReturnDate(LocalDate.now());

            // Check for penalty
            double penaltyAmount = borrowToReturn.getUser().calculatePenalty(borrowToReturn);
            if (penaltyAmount > 0) {
                Penalty newPenalty = new Penalty(borrowToReturn, penaltyAmount);
                pendingPenalties.add(newPenalty);
                borrowToReturn.getUser().setBlocked(true); // Block user if penalty incurred
                System.out.println("Penalty incurred for '" + borrowToReturn.getItem().getTitle() + "': $" + String.format("%.2f", penaltyAmount) + ". User " + borrowToReturn.getUser().getName() + " is now blocked.");
            }

            activeBorrows.remove(borrowToReturn); // Remove from active list

            System.out.println("Borrow ID " + borrowId + " successfully returned and processed.");
            return true;
        }
        return false;
    }

    public boolean renewBorrow(String borrowId) {
        Borrow borrowToRenew = activeBorrows.stream()
                .filter(b -> b.getBorrowId().equals(borrowId))
                .findFirst()
                .orElse(null);

        if (borrowToRenew == null) {
            System.err.println("Renew failed: Active borrow with ID " + borrowId + " not found.");
            return false;
        }
        if (borrowToRenew.getReturnDate() != null) {
            System.err.println("Renew failed: Borrow ID " + borrowId + " has already been returned.");
            return false;
        }
        if (borrowToRenew.isRenewed()) { // Optional: limit renewals
            System.err.println("Renew failed: Borrow ID " + borrowId + " has already been renewed.");
            return false;
        }
        if (LocalDate.now().isAfter(borrowToRenew.getDueDate())) {
            System.err.println("Renew failed: Borrow ID " + borrowId + " is already overdue.");
            return false;
        }
        // Additional checks like "is another user waiting for this item" could be added here.

        // Extend due date based on item's default borrow period or user's type's default period
        // Ensure item is Borrowable before casting
        if (!(borrowToRenew.getItem() instanceof Borrowable borrowableItem)) {
            System.err.println("Internal error: Item in borrow is not Borrowable.");
            return false;
        }
        int extensionDays = borrowableItem.getBorrowPeriodDays();
        // Or based on user type: int extensionDays = borrowToRenew.getUser().getUserType().getDefaultBorrowPeriodDays();
        borrowToRenew.setDueDate(borrowToRenew.getDueDate().plusDays(extensionDays));
        borrowToRenew.setRenewed(true); // Mark as renewed
        System.out.println("Borrow ID " + borrowId + " renewed successfully. New due date: " + borrowToRenew.getDueDate());
        return true;
    }

    // --- Penalty Management Methods ---

    public List<Penalty> getPendingPenalties() {
        return new ArrayList<>(pendingPenalties);
    }

    public boolean payPenalty(String penaltyId) {
        Penalty penaltyToPay = pendingPenalties.stream()
                .filter(p -> p.getPenaltyId().equals(penaltyId))
                .findFirst()
                .orElse(null);

        if (penaltyToPay == null) {
            System.err.println("Penalty with ID " + penaltyId + " not found in pending penalties.");
            return false;
        }

        penaltyToPay.setPaid(true);
        pendingPenalties.remove(penaltyToPay);
        paidPenalties.add(penaltyToPay);

        // Check if user has any other pending penalties. If not, unblock.
        boolean userStillHasPendingPenalties = pendingPenalties.stream()
                .anyMatch(p -> p.getBorrow().getUser().getCPF().equals(penaltyToPay.getBorrow().getUser().getCPF()));

        if (!userStillHasPendingPenalties) {
            penaltyToPay.getBorrow().getUser().setBlocked(false);
            System.out.println("User " + penaltyToPay.getBorrow().getUser().getName() + " unblocked.");
        }
        System.out.println("Penalty ID " + penaltyId + " paid successfully. Amount: $" + String.format("%.2f", penaltyToPay.getAmount()));
        return true;
    }

    // --- Reporting Methods ---

    public List<LibraryItem> getMostBorrowedItems(int limit) {
        return libraryItems.values().stream()
                .sorted((item1, item2) -> Integer.compare(item2.getBorrowCount(), item1.getBorrowCount()))
                .limit(limit)
                .collect(Collectors.toList());
    }

    public List<User> getUsersWithMostBorrows(int limit) {
        // This report needs to calculate total borrows for each user from history
        Map<User, Long> userBorrowCounts = borrowHistory.stream()
                .collect(Collectors.groupingBy(Borrow::getUser, Collectors.counting()));

        return userBorrowCounts.entrySet().stream()
                .sorted(Map.Entry.<User, Long>comparingByValue().reversed())
                .map(Map.Entry::getKey)
                .limit(limit)
                .collect(Collectors.toList());
    }

    public List<Borrow> getOverdueItems() {
        LocalDate today = LocalDate.now();
        return activeBorrows.stream()
                .filter(borrow -> today.isAfter(borrow.getDueDate()))
                .collect(Collectors.toList());
    }

    public double getTotalPenaltyRevenue() {
        return paidPenalties.stream()
                .mapToDouble(Penalty::getAmount)
                .sum();
    }
}