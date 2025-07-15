package poo.entity;

import poo.system.Borrow;
// import poo.system.Penalty; // This import is not directly used in User class, remove it.
import java.time.LocalDate;
import java.time.Period;
import java.util.ArrayList;
import java.util.List;

public class User extends Person {

    // ENUM para diferentes tipos de usuários
    public enum UserType {
        STUDENT(5, 15, 0.50),   // Empréstimos máximos, Tempo de devolução e multa por dia
        PROFESSOR(10, 30, 0.25), // Empréstimos máximos, Tempo de devolução e multa por dia
        EMPLOYEE(7, 20, 0.30);  // Empréstimos máximos, Tempo de devolução e multa por dia

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
    private final List<Borrow> borrowHistory;
    private boolean isBlocked; // Verdadeiro se o usuário tem multas a pagar

    // Construtor
    public User(String name, String cpf, String email, UserType userType) {
        super(name, cpf, email);
        setUserType(userType);
        this.borrowHistory = new ArrayList<>();
        this.isBlocked = false; // Não bloqueado por padrão
    }

    public UserType getUserType() {
        return userType;
    }

    // Setter (Encapsulamento)
    public void setUserType(UserType userType) {
        if (userType == null) {
            throw new IllegalArgumentException("Tipo de usuário não pode ser nulo.");
        }
        this.userType = userType;
    }

    // Getter para histórico
    public List<Borrow> getBorrowHistory() {
        return borrowHistory;
    }

    // Método para adicionar um empréstimo ao histórico
    public void addBorrowToHistory(Borrow borrow) {
        if (borrow == null) {
            throw new IllegalArgumentException("Objeto alugado não pode ser nulo.");
        }
        this.borrowHistory.add(borrow);
    }

    // Getter pra isBlocked
    public boolean isBlocked() {
        return isBlocked;
    }

    // Setter pra isBlocked
    public void setBlocked(boolean blocked) {
        isBlocked = blocked;
    }

    // Métodos abstratos de Person

    @Override
    public double calculateDiscount() {
        return switch (userType) {
            case STUDENT -> 0.10; // 10%
            case PROFESSOR -> 0.05; // 5%
            case EMPLOYEE -> 0.00; // Sem desconto
            default -> 0.00;
        };
    }

    @Override
    public double calculatePenalty(Borrow borrow) {
        // Mesma lógica de cálculo de multa
        if (borrow == null || borrow.getReturnDate() == null || borrow.getReturnDate().isBefore(borrow.getDueDate())) {
            return 0.0; // Sem pena se não alugou, ainda não devolveu ou devolveu no tempo correto.
        }

        long overdueDays = Period.between(borrow.getDueDate(), borrow.getReturnDate()).getDays();

        if (overdueDays <= 0) {
            return 0.0; // Sem pena se retornou no dia de vencimento ou antes
        }

        // Multa calculada baseada no DailyPenaltyRate
        return overdueDays * userType.getDailyPenaltyRate();
    }

    @Override
    public String toString() {
        return "User{" +
                "name='" + getName() + '\'' +
                ", cpf='" + getCPF() + '\'' +
                ", email='" + getEmail() + '\'' +
                ", userType=" + userType +
                ", isBlocked=" + isBlocked +
                ", borrowHistorySize=" + borrowHistory.size() +
                '}';
    }
}