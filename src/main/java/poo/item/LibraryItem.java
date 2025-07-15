package poo.item;

import java.time.LocalDate;

// A classe é abstrata, como planejado.
public abstract class LibraryItem {

    private final String title;
    private final String author;
    private final int publicationYear;

    // Campos mutáveis, pois seus valores mudam ao longo do tempo
    private boolean available;
    private int borrowCount;

    public LibraryItem(String title, String author, int publicationYear) {
        if (title == null || title.trim().isEmpty()) {
            throw new IllegalArgumentException("Título não pode ser vazio ou nulo.");
        }
        this.title = title;

        // Validação e atribuição para autor
        if (author == null || author.trim().isEmpty()) {
            throw new IllegalArgumentException("Autor/Diretor/Editor não pode ser nulo ou vazio.");
        }
        this.author = author;

        // Validação básica: ano de publicação não deve ser no futuro ou zero/negativo
        if (publicationYear <= 0 || publicationYear > LocalDate.now().getYear()) {
            throw new IllegalArgumentException("Ano de publicação é inválido.");
        }
        this.publicationYear = publicationYear;

        this.available = true; // Novos itens estão disponíveis por padrão
        this.borrowCount = 0;  // Contagem de empréstimos inicia em zero
    }

    // Getters para todos os campos
    public String getTitle() {
        return title;
    }

    public String getAuthor() {
        return author;
    }

    public int getPublicationYear() {
        return publicationYear;
    }

    public boolean isAvailable() {
        return available;
    }

    public int getBorrowCount() {
        return borrowCount;
    }

    // Setter apenas para 'available', pois é um campo final
    public void setAvailable(boolean available) {
        this.available = available;
    }

    // Método para incrementar a contagem de empréstimos
    protected void incrementBorrowCount() {
        this.borrowCount++;
    }

    @Override
    public String toString() {
        return "LibraryItem{" +
                "title='" + title + '\'' +
                ", author='" + author + '\'' +
                ", publicationYear=" + publicationYear +
                ", available=" + available +
                ", borrowCount=" + borrowCount +
                '}';
    }
}