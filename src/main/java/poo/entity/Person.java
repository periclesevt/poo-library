package poo.entity;

import poo.system.Borrow;

public abstract class Person {

    private final String name; // Made final
    private final String cpf;  // Made final
    private final String email; // Made final

    // Constructor
    public Person(String name, String cpf, String email) {
        // Direct assignment with validation for final fields
        if (name == null || name.trim().isEmpty()) {
            throw new IllegalArgumentException("Name cannot be null or empty.");
        }
        this.name = name;

        if (cpf == null || cpf.trim().isEmpty()) {
            throw new IllegalArgumentException("CPF cannot be null or empty.");
        }
        // Basic CPF format validation (e.g., 11 digits without dots or hyphens)
        if (!cpf.matches("^\\d{11}$")) {
            System.err.println("Warning: The CPF format '" + cpf + "' might be invalid. Please use 11 digits.");
            // You might choose to throw an IllegalArgumentException here instead of just printing
            // throw new IllegalArgumentException("Invalid CPF format.");
        }
        this.cpf = cpf;

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

    // Setters are removed for final fields, as they are set only once in the constructor.
    // If you need to modify these after creation, they cannot be final.
    // For this project's requirements, making them final is appropriate as people's basic info typically doesn't change post-registration.

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