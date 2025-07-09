package poo.main;

import poo.entity.*;
import poo.item.*;
import poo.system.*;

import java.time.LocalDate;
import java.util.List;

public class LibraryApp {

    public static void main(String[] args) {
        System.out.println("--- Starting Library System Test ---");

        Library library = new Library();
        System.out.println("\n--- Library Initialized ---");

        // 1. Register Users
        System.out.println("\n--- Registering Users ---");
        User student = new User("Alice Student", "11122233344", "alice@email.com", User.UserType.STUDENT);
        User professor = new User("Bob Professor", "55566677788", "bob@email.com", User.UserType.PROFESSOR);
        User employee = new User("Charlie Employee", "99988877766", "charlie@email.com", User.UserType.EMPLOYEE);
        Librarian librarian = new Librarian("Dana Librarian", "10120230340", "dana@email.com"); // Instantiated but not registered/used in library map for simplicity of this test.

        library.registerUser(student);
        library.registerUser(professor);
        library.registerUser(employee);
        // Test invalid user registration (duplicate CPF)
        library.registerUser(new User("Alice Clone", "11122233344", "alice.clone@email.com", User.UserType.STUDENT));


        // 2. Add Library Items
        System.out.println("\n--- Adding Library Items ---");
        Book book1 = new Book("The Great Java", "J.Coder", 2020, "978-0-13-468599-1");
        Book book2 = new Book("Design Patterns", "E. Gamma", 1994, "978-0-201-63361-0");
        Magazine mag1 = new Magazine("Tech Today", "Editor A", 2024, "1234-5678", 101);
        Dvd dvd1 = new Dvd("Coding Documentary", "D. Vision", 2023);
        Dvd dvd2 = new Dvd("Java Basics Vol.1", "J. Films", 2022);

        library.addItem(book1);
        library.addItem(book2);
        library.addItem(mag1);
        library.addItem(dvd1);
        library.addItem(dvd2);

        // Test invalid item add (duplicate ISBN/ID)
        library.addItem(new Book("The Great Java", "J.Coder", 2020, "978-0-13-468599-1"));


        // 3. Perform Borrows
        System.out.println("\n--- Performing Borrows ---");
        Borrow borrow1 = library.performBorrow(student.getCPF(), book1.getISBN()); // Student borrows Book 1
        Borrow borrow2 = library.performBorrow(professor.getCPF(), mag1.getISSN()); // Professor borrows Magazine 1
        Borrow borrow3 = library.performBorrow(employee.getCPF(), dvd1.getTitle() + "-" + dvd1.getAuthor() + "-" + dvd1.getPublicationYear()); // Employee borrows DVD 1

        // Test invalid borrows
        System.out.println("\n--- Testing Invalid Borrows ---");
        library.performBorrow(student.getCPF(), book1.getISBN()); // Book 1 already borrowed
        library.performBorrow("99999999999", book2.getISBN()); // Non-existent user
        library.performBorrow(student.getCPF(), "NonExistentItem"); // Non-existent item


        // 4. Return Borrows
        System.out.println("\n--- Returning Borrows ---");
        // Simulate an overdue return for student (5 days overdue) by passing a past date
        if (borrow1 != null) {
            System.out.println("Simulating overdue return for Book 1 by Alice Student...");
            library.returnBorrow(borrow1.getBorrowId(), borrow1.getDueDate().plusDays(5)); // Pass actual return date
            System.out.println("Student's current block status: " + student.isBlocked()); // Should be true now
        }

        // Return on time
        if (borrow2 != null) {
            library.returnBorrow(borrow2.getBorrowId(), LocalDate.now()); // Pass current date
        }

        // 5. Test Penalties
        System.out.println("\n--- Testing Penalties ---");
        List<Penalty> pendingPenalties = library.getPendingPenalties();
        System.out.println("Pending Penalties: " + pendingPenalties.size());
        if (!pendingPenalties.isEmpty()) {
            System.out.println("First pending penalty: " + pendingPenalties.get(0));
            library.payPenalty(pendingPenalties.get(0).getPenaltyId()); // Pay the penalty
            System.out.println("Student's block status after paying penalty: " + student.isBlocked()); // Should be false
        }

        // Test borrow when user was blocked and then unblocked
        System.out.println("\n--- Testing Borrow After Unblock ---");
        library.performBorrow(student.getCPF(), book2.getISBN()); // Student should be able to borrow now


        // 6. Renew Borrows
        System.out.println("\n--- Renewing Borrows ---");
        Borrow borrow4 = library.performBorrow(professor.getCPF(), dvd2.getTitle() + "-" + dvd2.getAuthor() + "-" + dvd2.getPublicationYear());
        if (borrow4 != null) {
            System.out.println("Borrow 4 initial due date: " + borrow4.getDueDate());
            library.renewBorrow(borrow4.getBorrowId()); // Renew
            System.out.println("Borrow 4 new due date: " + borrow4.getDueDate());
            library.renewBorrow(borrow4.getBorrowId()); // Try to renew again (should fail based on current logic)
        }


        // 7. Generate Reports
        System.out.println("\n--- Generating Reports ---");
        System.out.println("\n--- All Items ---");
        library.listAllItems().forEach(System.out::println);

        System.out.println("\n--- Items by Category (Books) ---");
        library.listItemsByCategory("Book").forEach(System.out::println);

        System.out.println("\n--- Most Borrowed Items (Top 2) ---");
        library.getMostBorrowedItems(2).forEach(System.out::println);

        System.out.println("\n--- Users with Most Borrows (Top 2) ---");
        library.getUsersWithMostBorrows(2).forEach(System.out::println);

        System.out.println("\n--- Overdue Items ---");
        // To show an overdue item, we need one that is NOT returned yet but is past its due date.
        // We cannot directly manipulate borrowDate for items created via performBorrow using LocalDate.now().
        // So, we will simply list any existing active borrows whose due dates have passed *relative to now*.
        // The previous attempt to manually set overdueBorrow.setDueDate(LocalDate.now().minusDays(3)) caused the error
        // because its borrowDate was LocalDate.now() and you cannot set dueDate before borrowDate.
        // We remove the problematic manual dueDate setting for the overdue test here.
        Book overdueTestBook = new Book("Temp Overdue Test Book", "Test Author", 2024, "978-9-999-99999-9");
        library.addItem(overdueTestBook);
        Borrow tempOverdueBorrow = library.performBorrow(employee.getCPF(), overdueTestBook.getISBN());
        if (tempOverdueBorrow != null) {
            // For testing purposes, manually set its due date to be in the past IF this were a real scenario
            // where time had passed. For immediate test, this will not show unless you adjust system clock
            // or modify performBorrow to allow past borrowDates.
            // We won't set it to a past date to avoid the IllegalArgumentException.
            System.out.println("Temp overdue borrow due date is: " + tempOverdueBorrow.getDueDate() + " (will only appear in report if system date passes it).");
        }
        library.getOverdueItems().forEach(System.out::println);


        System.out.println("\n--- Total Penalty Revenue ---");
        System.out.println("Total Revenue: $" + String.format("%.2f", library.getTotalPenaltyRevenue()));

        System.out.println("\n--- End of Library System Test ---");
    }
}