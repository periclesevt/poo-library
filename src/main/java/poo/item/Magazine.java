package poo.item;

public class Magazine extends  LibraryItem implements Borrowable {

    private int edicao;
    private int volume;

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
