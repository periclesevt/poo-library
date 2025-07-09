package poo.item;

public class Magazine extends  LibraryItem implements Borrowable {
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
