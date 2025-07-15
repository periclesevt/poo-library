package poo.system;

import poo.item.Book; // Correct import
import poo.item.Magazine; // Correct import
import poo.entity.User;
import poo.item.LibraryItem;
import java.time.LocalDate;
import java.util.UUID; // For generating unique IDs

public class Borrow {
    private final String borrowId;
    private final User user;
    private final LibraryItem item;
    private final LocalDate borrowDate;
    private LocalDate dueDate;
    private LocalDate returnDate; // Nulo se não devolvido
    private boolean isRenewed; // Restrear se o empréstimo foi renovado

    public Borrow(User user, LibraryItem item, LocalDate borrowDate, LocalDate dueDate) {
        if (user == null || item == null || borrowDate == null || dueDate == null) {
            throw new IllegalArgumentException("Parâmetros do empréstimo não podem ser nulos.");
        }
        if (borrowDate.isAfter(dueDate)) {
            throw new IllegalArgumentException("A data do empréstimo não pode ser posterior a data de vencimento.");
        }

        this.borrowId = UUID.randomUUID().toString(); // Gera ID único para cada empréstimo.
        this.user = user;
        this.item = item;
        this.borrowDate = borrowDate;
        this.dueDate = dueDate;
        this.returnDate = null; // Não retornado
        this.isRenewed = false; // Não renovado por padrão
    }

    public String getBorrowId() {
        return borrowId;
    }

    public User getUser() {
        return user;
    }

    public LibraryItem getItem() {
        return item;
    }

    public LocalDate getBorrowDate() { // This getter is fine, even if not used internally
        return borrowDate;
    }

    public LocalDate getDueDate() {
        return dueDate;
    }

    public LocalDate getReturnDate() {
        return returnDate;
    }

    public boolean isRenewed() {
        return isRenewed;
    }

    public void setDueDate(LocalDate dueDate) {
        if (dueDate == null) {
            throw new IllegalArgumentException("Data de vencimento não pode ser nula.");
        }
        // Garante que a nova data de vencimento não é anterior a data de empréstimo.
        if (dueDate.isBefore(this.borrowDate)) {
            throw new IllegalArgumentException("Data de vencimento não pode ser anterior a data de empréstimo.");
        }
        this.dueDate = dueDate;
    }

    public void setReturnDate(LocalDate returnDate) {
        this.returnDate = returnDate;
    }

    public void setRenewed(boolean renewed) {
        isRenewed = renewed;
    }

    @Override
    public String toString() {
        String itemIdDisplay;
        if (item instanceof Book) {
            itemIdDisplay = "ISBN: " + ((Book) item).getISBN();
        } else if (item instanceof Magazine) {
            itemIdDisplay = "ISSN: " + ((Magazine) item).getISSN();
        } else {
            itemIdDisplay = "ID: " + item.getTitle() + "-" + item.getAuthor() + "-" + item.getPublicationYear();
        }

        return "Borrow{" +
                "borrowId='" + borrowId + '\'' +
                ", user=" + user.getName() + " (CPF: " + user.getCPF() + ')' +
                ", item=" + item.getTitle() + " (" + itemIdDisplay + ')' + // Atualiza ID do item
                ", borrowDate=" + borrowDate +
                ", dueDate=" + dueDate +
                ", returnDate=" + (returnDate != null ? returnDate : "N/A") +
                ", isRenewed=" + isRenewed +
                '}';
    }
}