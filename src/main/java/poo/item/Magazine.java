package poo.item;

import poo.entity.User;

import java.time.LocalDate;

public class Magazine extends  LibraryItem implements Borrowable {
    private String issn;
    private int editionNumber;

    // Constructors
    public Magazine(String title, String editor, int publicationYear, String issn, int editionNumber) {
        super(title, editor, publicationYear); // Usando campo do Autor para Editor
        setISSN(issn);
        setEditionNumber(editionNumber);
    }

    public Magazine(String title, String editor, String issn, int editionNumber) {
        this(title, editor, LocalDate.now().getYear(), issn, editionNumber); // Ano atual por padrão
    }

    public String getISSN() {
        return issn;
    }

    public void setISSN(String issn) {
        if (issn == null || issn.trim().isEmpty()) {
            throw new IllegalArgumentException("ISSN não pode ser nulo ou vazio");
        }
        // formato basico ISSN
        if (!issn.matches("^\\d{4}-\\d{3}[\\dX]$")) { // Exemplo: XXXX-XXXX ou XXXX-XXXD, onde D é um digito
            System.err.println("AVISO: o ISSN formato '" + issn + "' parece ser inválido. Exemplo: 1234-5678");
        }
        this.issn = issn;
    }

    public int getEditionNumber() {
        return editionNumber;
    }

    public void setEditionNumber(int editionNumber) {
        if (editionNumber <= 0) {
            throw new IllegalArgumentException("Número de edição deve ser um inteiro positivo.");
        }
        this.editionNumber = editionNumber;
    }

    @Override
    public boolean borrowItem(User user) {
        if (isAvailable()) {
            setAvailable(false);
            incrementBorrowCount();
            System.out.println("Revista '" + getTitle() + "' alugada com sucesso por " + user.getName() + ".");
            return true;
        } else {
            System.out.println("Revista '" + getTitle() + "' não está válida para empréstimo.");
            return false;
        }
    }

    @Override
    public boolean returnItem() {
        if (!isAvailable()) {
            setAvailable(true);
            System.out.println("Revista '" + getTitle() + "' devolvida com sucesso.");
            return true;
        } else {
            System.out.println("Revista '" + getTitle() + "' já está disponível (foi devolvida). ");
            return false;
        }
    }

    @Override
    public int getBorrowPeriodDays() {
        // Tempo padrão para devolução revista.
        return 7;
    }

    @Override
    public String toString() {
        return "Magazine{" +
                "title='" + getTitle() + '\'' +
                ", editor='" + getAuthor() + '\'' +
                ", publicationYear=" + getPublicationYear() +
                ", issn='" + issn + '\'' +
                ", editionNumber=" + editionNumber +
                ", available=" + isAvailable() +
                ", borrowCount=" + getBorrowCount() +
                '}';
    }
}
