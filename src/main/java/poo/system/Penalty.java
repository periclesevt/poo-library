package poo.system;

import java.util.UUID; // For generating unique IDs

public class Penalty {
    private String penaltyId;
    private Borrow borrow; // Reference to the borrow that incurred this penalty
    private double amount;
    private boolean isPaid;

    // Constructor
    public Penalty(Borrow borrow, double amount) {
        if (borrow == null) {
            throw new IllegalArgumentException("Penalty must be associated with a valid Borrow object.");
        }
        if (amount < 0) {
            throw new IllegalArgumentException("Penalty amount cannot be negative.");
        }

        this.penaltyId = UUID.randomUUID().toString(); // Generate a unique ID
        this.borrow = borrow;
        this.amount = amount;
        this.isPaid = false; // Not paid by default
    }

    // Getters
    public String getPenaltyId() {
        return penaltyId;
    }

    public Borrow getBorrow() {
        return borrow;
    }

    public double getAmount() {
        return amount;
    }

    public boolean isPaid() {
        return isPaid;
    }

    // Setters (with validation where appropriate)
    public void setPaid(boolean paid) {
        isPaid = paid;
    }

    // Overriding toString() method
    @Override
    public String toString() {
        return "Penalty{" +
                "penaltyId='" + penaltyId + '\'' +
                ", borrowId='" + borrow.getBorrowId() + '\'' + // Reference to the borrow ID
                ", user='" + borrow.getUser().getName() + '\'' + // User who incurred penalty
                ", item='" + borrow.getItem().getTitle() + '\'' + // Item associated with penalty
                ", amount=" + String.format("%.2f", amount) +
                ", isPaid=" + isPaid +
                '}';
    }
}