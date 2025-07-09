package poo.entity;

import poo.system.Borrow;

public class Librarian extends Person {

    // Optional: You could add specific attributes for a Librarian here,
    // like employeeId, hireDate, etc., if the project scope required.
    // For now, it primarily extends Person's functionality.

    // Constructor
    public Librarian(String name, String cpf, String email) {
        super(name, cpf, email);
    }

    // --- Implementation of Abstract Methods from Person ---

    @Override
    public double calculateDiscount() {
        // Librarians typically don't receive discounts in the context of library services
        return 0.0;
    }

    @Override
    public double calculatePenalty(Borrow borrow) {
        // Librarians typically do not incur penalties for overdue items in this system context
        return 0.0;
    }

    // Overriding toString() method (Polymorphism)
    @Override
    public String toString() {
        return "Librarian{" +
                "name='" + getName() + '\'' +
                ", cpf='" + getCPF() + '\'' +
                ", email='" + getEmail() + '\'' +
                '}';
    }
}
