package poo.entity;

import poo.system.Borrow;

public class Librarian extends Person {

    // Construtor
    public Librarian(String name, String cpf, String email) {
        super(name, cpf, email);
    }

    // Métodos de Person

    @Override
    public double calculateDiscount() {
        // Geralmente funcionários não recebem desconto
        return 0.0;
    }

    @Override
    public double calculatePenalty(Borrow borrow) {
        // Geralmente funcionários não recebem multas
        return 0.0;
    }

    @Override
    public String toString() {
        return "Librarian{" +
                "name='" + getName() + '\'' +
                ", cpf='" + getCPF() + '\'' +
                ", email='" + getEmail() + '\'' +
                '}';
    }
}
