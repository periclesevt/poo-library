package poo.system;

import java.util.UUID; // For generating unique IDs

public class Penalty {
    private final String penaltyId;
    private final Borrow borrow;
    private final double amount;
    private boolean isPaid;

    public Penalty(Borrow borrow, double amount) {
        if (borrow == null) {
            throw new IllegalArgumentException("Multa deve estar relacionada com um empréstimo válido.");
        }
        if (amount < 0) {
            throw new IllegalArgumentException("Multa não pode ser de valor negativo.");
        }

        this.penaltyId = UUID.randomUUID().toString(); // Gerando ID único
        this.borrow = borrow;
        this.amount = amount;
        this.isPaid = false; // Não paga por padrão
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

    public void setPaid(boolean paid) {
        isPaid = paid;
    }

    @Override
    public String toString() {
        return "Penalty{" +
                "penaltyId='" + penaltyId + '\'' +
                ", borrowId='" + borrow.getBorrowId() + '\'' +
                ", user='" + borrow.getUser().getName() + '\'' +
                ", item='" + borrow.getItem().getTitle() + '\'' +
                ", amount=" + String.format("%.2f", amount) +
                ", isPaid=" + isPaid +
                '}';
    }
}