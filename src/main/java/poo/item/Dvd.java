package poo.item;

import poo.entity.User;

import java.time.LocalDate;

public class Dvd extends LibraryItem implements Borrowable {
    private String director;

    public Dvd(String title, String director, int publicationYear) {
        super(title, director, publicationYear); // Usando campo do Autor para Diretor
        setDirector(director);
    }

    public Dvd(String title, String director) {
        this(title, director, LocalDate.now().getYear()); // Ano atual como padrão
    }

    public String getDirector() {
        return director;
    }

    public void setDirector(String director) {
        if (director == null || director.trim().isEmpty()) {
            throw new IllegalArgumentException("Diretor não pode ser nulo ou vazio");
        }
        this.director = director;
    }

    @Override
    public boolean borrowItem(User user) {
        if (isAvailable()) {
            setAvailable(false);
            incrementBorrowCount();
            System.out.println("DVD '" + getTitle() + "'foi alugado com sucesso por " + user.getName() + ".");
            return true;
        } else {
            System.out.println("DVD '" + getTitle() + "' não está disponível para empréstimo.");
            return false;
        }
    }

    @Override
    public boolean returnItem() {
        if (!isAvailable()) {
            setAvailable(true);
            System.out.println("DVD '" + getTitle() + "' devolvido com sucesso.");
            return true;
        } else {
            System.out.println("DVD '" + getTitle() + "' já está disponível (não foi alugado).");
            return false;
        }
    }

    @Override
    public int getBorrowPeriodDays() {
        // Tempo de devolução padrão DVD
        return 7;
    }

    @Override
    public String toString() {
        return "Dvd{" +
                "title='" + getTitle() + '\'' +
                ", director='" + director + '\'' +
                ", publicationYear=" + getPublicationYear() +
                ", available=" + isAvailable() +
                ", borrowCount=" + getBorrowCount() +
                '}';
    }
}
