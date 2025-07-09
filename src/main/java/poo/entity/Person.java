package poo.entity;

import poo.system.Borrow;

public abstract class Person {

    private String name;
    private String cpf; // CPF will be the unique identifier
    private String email;

    // Constructor
    public Person(String name, String cpf, String email) {
        setName(name);
        setCPF(cpf);
        setEmail(email);
    }

    // Getters
    public String getName() {
        return name;
    }

    public String getCPF() {
        return cpf;
    }

    public String getEmail() {
        return email;
    }

    // Setters with basic validation (Encapsulation)
    public void setName(String name) {
        if (name == null || name.trim().isEmpty()) {
            throw new IllegalArgumentException("Name cannot be null or empty.");
        }
        this.name = name;
    }

    public void setCPF(String cpf) {
        if (cpf == null || cpf.trim().isEmpty()) {
            throw new IllegalArgumentException("CPF cannot be null or empty.");
        }
        // Basic CPF format validation (e.g., length, numeric characters only, or a more complex regex for real CPF validation)
        // For a college project, a simple non-empty and length check might suffice.
        if (!cpf.matches("^\\d{11}$")) { // Assumes 11 digits without dots or hyphens
            System.err.println("Warning: The CPF format '" + cpf + "' might be invalid. Please use 11 digits.");
            // You might choose to throw an IllegalArgumentException here instead of just printing
            // throw new IllegalArgumentException("Invalid CPF format.");
        }
        this.cpf = cpf;
    }

    public void setEmail(String email) {
        if (email == null || email.trim().isEmpty()) {
            throw new IllegalArgumentException("Email cannot be null or empty.");
        }
        // Basic email format validation (more robust regex for real-world scenarios)
        if (!email.matches("^[a-zA-Z0-9._%+-]+@[a-zA-Z0-9.-]+\\.[a-zA-Z]{2,6}$")) {
            System.err.println("Warning: The email format '" + email + "' might be invalid.");
            // throw new IllegalArgumentException("Invalid email format.");
        }
        this.email = email;
    }

    // Abstract method for polymorphism (discount calculation)
    public abstract double calculateDiscount();

    // Abstract method for polymorphism (penalty calculation)
    public abstract double calculatePenalty(Borrow borrow);

    // Overriding toString() method (Polymorphism)
    @Override
    public String toString() {
        return "Person{" +
                "name='" + name + '\'' +
                ", cpf='" + cpf + '\'' +
                ", email='" + email + '\'' +
                '}';
    }
}
}
