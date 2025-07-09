package poo.item;

public class Dvd extends LibraryItem implements Borrowable {

    private String diretor;
    private double duracao;

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
