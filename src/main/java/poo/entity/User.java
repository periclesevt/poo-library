package poo.entity;

import poo.system.Borrow;
import poo.system.Penalty;
import java.time.LocalDate;
import java.time.Period;
import java.util.ArrayList;
import java.util.List;

public class User extends Person {

    // Enum for different user types
    public enum UserType {
        STUDENT(5, 15, 0.50),   // Max borrows, default period days, daily penalty rate
        PROFESSOR(10, 30, 0.25), // Max borrows, default period days, daily penalty rate
        EMPLOYEE(7, 20, 0.30);  // Max borrows, default period days, daily penalty rate

        private final int maxBorrows;
        private final int defaultBorrowPeriodDays;
        private final double dailyPenaltyRate;

        UserType(int maxBorrows, int defaultBorrowPeriodDays, double dailyPenaltyRate) {
            this.maxBorrows = maxBorrows;
            this.defaultBorrowPeriodDays = defaultBorrowPeriodDays;
            this.dailyPenaltyRate = dailyPenaltyRate;
        }

        public int getMaxBorrows() {
            return maxBorrows;
        }

        public int getDefaultBorrowPeriodDays() {
            return defaultBorrowPeriodDays;
        }

        public double getDailyPenaltyRate() {
            return dailyPenaltyRate;
        }
    }

    private UserType userType;
    private List<Borrow> borrowHistory; // List of all past and current borrows
    private boolean isBlocked; // True if the user has pending penalties

    // Constructor
    public User(String name, String cpf, String email, UserType userType) {
        super(name, cpf, email);
        setUserType(userType);
        this.borrowHistory = new ArrayList<>();
        this.isBlocked = false; // Not blocked by default
    }

    // Getter for UserType
    public UserType getUserType() {
        return userType;
    }

    // Setter for UserType (Encapsulation)
    public void setUserType(UserType userType) {
        if (userType == null) {
            throw new IllegalArgumentException("User type cannot be null.");
        }
        this.userType = userType;
    }

    // Getter for Borrow History
    public List<Borrow> getBorrowHistory() {
        return borrowHistory;
    }

    // Method to add a borrow to history
    public void addBorrowToHistory(Borrow borrow) {
        if (borrow == null) {
            throw new IllegalArgumentException("Borrow object cannot be null.");
        }
        this.borrowHistory.add(borrow);
    }

    // Getter for isBlocked
    public boolean isBlocked() {
        return isBlocked;
    }

    // Setter for isBlocked
    public void setBlocked(boolean blocked) {
        isBlocked = blocked;
    }

    // --- Implementation of Abstract Methods from Person ---

    @Override
    public double calculateDiscount() {
        // Example: Students get a 10% discount on something (e.g., membership fees, if applicable)
        // For a library system, discounts might not apply directly to items or borrows.
        // This is primarily for demonstrating polymorphism as per project requirements.
        switch (userType) {
            case STUDENT:
                return 0.10; // 10% discount
            case PROFESSOR:
                return 0.05; // 5% discount
            case EMPLOYEE:
                return 0.00; // No discount
            default:
                return 0.00;
        }
    }

    @Override
    public double calculatePenalty(Borrow borrow) {
        if (borrow == null || borrow.getReturnDate() == null || borrow.getReturnDate().isBefore(borrow.getDueDate())) {
            return 0.0; // No penalty if no borrow, not yet returned, or returned on time
        }

        long overdueDays = Period.between(borrow.getDueDate(), borrow.getReturnDate()).getDays();

        if (overdueDays <= 0) {
            return 0.0; // No penalty if returned on or before due date
        }

        // Penalty calculation based on user type's daily rate
        return overdueDays * userType.getDailyPenaltyRate();
    }

    // Overriding toString() method (Polymorphism)
    @Override
    public String toString() {
        return "User{" +
                "name='" + getName() + '\'' +
                ", cpf='" + getCPF() + '\'' +
                ", email='" + getEmail() + '\'' +
                ", userType=" + userType +
                ", isBlocked=" + isBlocked +
                ", borrowHistorySize=" + borrowHistory.size() + // Showing size for brevity
                '}';
    }
}
