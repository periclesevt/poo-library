package poo.item;

import java.time.LocalDate;

// A classe é abstrata, como planejado.
public abstract class LibraryItem {

    // Campos final, definidos apenas no construtor
    private final String title;
    private final String author;
    private final int publicationYear;
    // Campos mutáveis, pois seus valores mudam ao longo do tempo
    private boolean available;
    private int borrowCount;

    // Construtor: Responsável por inicializar os campos finais e os mutáveis.
    // As validações para os campos finais são feitas diretamente aqui.
    public LibraryItem(String title, String author, int publicationYear) {
        // Validação e atribuição para 'title'
        if (title == null || title.trim().isEmpty()) {
            throw new IllegalArgumentException("Title cannot be null or empty.");
        }
        this.title = title; // Atribuição direta ao campo final

        // Validação e atribuição para 'author'
        if (author == null || author.trim().isEmpty()) {
            throw new IllegalArgumentException("Author/Director/Editor cannot be null or empty.");
        }
        this.author = author; // Atribuição direta ao campo final

        // Validação e atribuição para 'publicationYear'
        // Validação básica: ano de publicação não deve ser no futuro ou zero/negativo
        if (publicationYear <= 0 || publicationYear > LocalDate.now().getYear()) {
            throw new IllegalArgumentException("Publication year is invalid.");
        }
        this.publicationYear = publicationYear; // Atribuição direta ao campo final

        // Inicialização de campos mutáveis
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

    // Setter apenas para 'available', pois é um campo mutável
    public void setAvailable(boolean available) {
        this.available = available;
    }

    // Método para incrementar a contagem de empréstimos (protected para uso por subclasses)
    protected void incrementBorrowCount() {
        this.borrowCount++;
    }

    // Sobrescrita do método toString() para uma representação legível do objeto
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