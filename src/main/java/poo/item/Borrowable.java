package poo.item;

public interface Borrowable {

    boolean borrow (Usuario usuario);

    boolean toReturn ();

    int getBorrowingPeriod();
}
