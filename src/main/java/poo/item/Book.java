package poo.item;

public class Book extends LibraryItem implements Borrowable {

    private String isbn;
    private String autor;

    @Override
    public boolean borrow(Usuario usuario) {
        return false;
    }

    @Override
    public boolean toReturn() {
        return false;
    }

    @Override
    public int getBorrowingPeriod() {
        return 0;
    }
}
