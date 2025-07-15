package poo.entity;

import poo.system.Borrow;

public abstract class Person {

    private final String name;
    private final String cpf;
    private final String email;

    // Construtor
    public Person(String name, String cpf, String email) {
        if (name == null || name.trim().isEmpty()) {
            throw new IllegalArgumentException("Nome não pode ser nulo ou vazio");
        }
        this.name = name;

        if (cpf == null || cpf.trim().isEmpty()) {
            throw new IllegalArgumentException("CPF não pode ser nulo ou vazio");
        }
        // Validação CPF
        if (!cpf.matches("^\\d{11}$")) {
            System.err.println("AVISO: O formato do CPF '" + cpf + "' parece ser inválido. Por favor use 11 digitos");
        }
        this.cpf = cpf;

        if (email == null || email.trim().isEmpty()) {
            throw new IllegalArgumentException("Email não pode ser nulo ou vazio");
        }
        // Validação email
        if (!email.matches("^[a-zA-Z0-9._%+-]+@[a-zA-Z0-9.-]+\\.[a-zA-Z]{2,6}$")) {
            System.err.println("AVISO: O formato do email '" + email + "' parece ser inválido.");
        }
        this.email = email;
    }

    public String getName() {
        return name;
    }

    public String getCPF() {
        return cpf;
    }

    public String getEmail() {
        return email;
    }

    public abstract double calculateDiscount();

    public abstract double calculatePenalty(Borrow borrow);

    @Override
    public String toString() {
        return "Person{" +
                "name='" + name + '\'' +
                ", cpf='" + cpf + '\'' +
                ", email='" + email + '\'' +
                '}';
    }
}