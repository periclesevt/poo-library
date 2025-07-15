package poo.item;

import poo.entity.User;
import poo.item.Borrowable;
import poo.item.LibraryItem;
import java.time.LocalDate;

public class Book extends LibraryItem implements Borrowable {
    private String isbn;

    // Construtor
    public Book(String title, String author, int publicationYear, String isbn) {
        super(title, author, publicationYear);
        setISBN(isbn);
    }

    public Book(String title, String author, String isbn) {
        this(title, author, LocalDate.now().getYear(), isbn); // Ano default para atual
    }

    public String getISBN() {
        return isbn;
    }

    public void setISBN(String isbn) {
        if (isbn == null || isbn.trim().isEmpty()) {
            throw new IllegalArgumentException("ISBN não pode ser nulo ou vazio");
        }
        if (!isbn.matches("^(?:978|979)(?:-?\\d){9}(\\d|X)$")) {
            System.err.println("AVISO: o formato ISBN '" + isbn + "' parece ser inválido. É esperado 13 digitos. Ex: 978-85-7836-070-8");
        }
        this.isbn = isbn;
    }

    // Borrowable métodos de interface
    @Override
    public boolean borrowItem(User user) {
        if (isAvailable()) {
            setAvailable(false);
            incrementBorrowCount();
            System.out.println("Livro '" + getTitle() + "' foi alugado com sucesso pelo " + user.getName() + ".");
            return true;
        } else {
            System.out.println("Livro '" + getTitle() + "' não está disponível para empréstimo.");
            return false;
        }
    }

    @Override
    public boolean returnItem() {
        if (!isAvailable()) {
            setAvailable(true);
            System.out.println("Livro '" + getTitle() + "' devolvido com sucesso.");
            return true;
        } else {
            System.out.println("Livro '" + getTitle() + "' já está disponível (não foi alugado).");
            return false;
        }
    }

    @Override
    public int getBorrowPeriodDays() {
        // Tempo de empréstimo de livro padrão
        return 15;
    }

    @Override
    public String toString() {
        return "Book{" +
                "title='" + getTitle() + '\'' +
                ", author='" + getAuthor() + '\'' +
                ", publicationYear=" + getPublicationYear() +
                ", isbn='" + isbn + '\'' +
                ", available=" + isAvailable() +
                ", borrowCount=" + getBorrowCount() +
                '}';
    }
}