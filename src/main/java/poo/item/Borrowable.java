package poo.item;

import poo.entity.User;

public interface Borrowable {
    /**
     * Marks the item as borrowed by a specific user.
     *
     * @param user The user who is borrowing the item.
     * @return true if the item was successfully borrowed, false otherwise (e.g., if not available).
     */
    boolean borrowItem(User user);

    /**
     * Marks the item as returned.
     *
     * @return true if the item was successfully returned, false otherwise (e.g., if it was already available).
     */
    boolean returnItem();

    /**
     * Returns the default borrowing period in days for this type of item.
     *
     * @return The number of days an item can be borrowed.
     */
    int getBorrowPeriodDays();
}