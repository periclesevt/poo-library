package poo.system;

import java.util.UUID; // For generating unique IDs

public class Penalty {
    private final String penaltyId; // Made final as it's set once in constructor
    private final Borrow borrow;    // Made final as it's set once in constructor
    private final double amount;    // Made final as it's set once in constructor
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

    public boolean isPaid() { // This getter is fine, even if not used internally
        return isPaid;
    }

    // Setter for isPaid (with validation where appropriate)
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