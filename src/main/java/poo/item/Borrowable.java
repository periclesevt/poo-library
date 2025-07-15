package poo.item;

import poo.entity.User;

public interface Borrowable {

    boolean borrowItem(User user);

    boolean returnItem();

    int getBorrowPeriodDays();
}