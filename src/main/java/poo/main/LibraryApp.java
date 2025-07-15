package poo.main;

import poo.entity.*;
import poo.item.*;
import poo.system.*;

import java.time.LocalDate;
import java.util.List;

public class LibraryApp {

    public static void main(String[] args) {
        System.out.println("--- Iniciando Sistema ---");

        Library library = new Library();
        System.out.println("\n--- Biblioteca Inicializada ---");

        // 1. Registrar Usuários
        System.out.println("\n--- Registrando Usuários ---");
        User student = new User("Estudante Fulano", "11122233344", "fulano@email.com", User.UserType.STUDENT);
        User professor = new User("Professor Roberto", "55566677788", "roberto@email.com", User.UserType.PROFESSOR);
        User employee = new User("Funcionário Beltrano", "99988877766", "beltrano@email.com", User.UserType.EMPLOYEE);
        Librarian librarian = new Librarian("Bibliotecaria Raquel", "10120230340", "raquel@email.com"); // Instanciada mas não utilizada no map library para simplificar o teste.

        library.registerUser(student);
        library.registerUser(professor);
        library.registerUser(employee);
        // Teste inválido de registro (Cpf duplicado)
        library.registerUser(new User("Alice Clone", "11122233344", "alice.clone@email.com", User.UserType.STUDENT));


        // 2. Adicionar itens da biblioteca
        System.out.println("\n--- Adding Library Items ---");
        Book book1 = new Book("Pena Capital", "A.Klavan", 1995, "978-0-13-468599-1");
        Book book2 = new Book("A morte de Ivan Ilitch", "Tolstoi", 2023, "978-0-201-63361-0");
        Magazine mag1 = new Magazine("Revista Tecnologia", "Editor A", 2024, "1234-5678", 101);
        Dvd dvd1 = new Dvd("Documentando Código", "D. V", 2023);
        Dvd dvd2 = new Dvd("Volume Básico Java", "J. Filmes", 2022);

        library.addItem(book1);
        library.addItem(book2);
        library.addItem(mag1);
        library.addItem(dvd1);
        library.addItem(dvd2);

        // Teste inválido de registro (isbn duplicado)
        library.addItem(new Book("Pena Capital", "A.Klavan", 2020, "978-0-13-468599-1"));


        // 3. Realizar Empréstimos
        System.out.println("\n--- Realizando Empréstimos ---");
        Borrow borrow1 = library.performBorrow(student.getCPF(), book1.getISBN()); // Estudante aluga Book 1
        Borrow borrow2 = library.performBorrow(professor.getCPF(), mag1.getISSN()); // Professor aluga Magazine 1
        Borrow borrow3 = library.performBorrow(employee.getCPF(), dvd1.getTitle() + "-" + dvd1.getAuthor() + "-" + dvd1.getPublicationYear()); // Funcionário aluga DVD 1

        // Empréstimos inválidos
        System.out.println("\n--- Testando Empréstimos Inválidos ---");
        library.performBorrow(student.getCPF(), book1.getISBN()); // Book 1 já alugado
        library.performBorrow("99999999999", book2.getISBN()); // Usuário não existe
        library.performBorrow(student.getCPF(), "NonExistentItem"); // Item não existe


        // 4. Devoluções
        System.out.println("\n--- Testando Devoluções ---");
        // Simular a devolução de um estudante depois de 5 dias de atraso
        if (borrow1 != null) {
            System.out.println("Simulando devolução atrasada do Book 1...");
            library.returnBorrow(borrow1.getBorrowId(), borrow1.getDueDate().plusDays(5)); // Passou do prazo
            System.out.println("Status Estudante: " + student.isBlocked()); // Deve ser verdadeiro
        }

        // Devolução correta
        if (borrow2 != null) {
            library.returnBorrow(borrow2.getBorrowId(), LocalDate.now());
        }

        // 5. Testando multas
        System.out.println("\n--- Testando Multas ---");
        List<Penalty> pendingPenalties = library.getPendingPenalties();
        System.out.println("Multa: " + pendingPenalties.size());
        if (!pendingPenalties.isEmpty()) {
            System.out.println("Primeira multa: " + pendingPenalties.get(0));
            library.payPenalty(pendingPenalties.get(0).getPenaltyId()); // Pague a multa
            System.out.println("Status Estudante depois de pagar a multa: " + student.isBlocked()); // Deve ser falso
        }

        // Testando estudante bloqueado e depois desbloqueado
        System.out.println("\n--- Testando Empréstimo depois de ser desbloqueado ---");
        library.performBorrow(student.getCPF(), book2.getISBN()); // Estudante deve conseguir alugar


        // 6. Renovar empréstimos
        System.out.println("\n--- Renovar empréstimos ---");
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