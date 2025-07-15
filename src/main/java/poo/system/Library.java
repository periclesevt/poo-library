package poo.system;

import poo.item.*;
import poo.entity.*;
import java.time.LocalDate;
import java.time.Period;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;
import java.util.stream.Collectors;

public class Library {

    private final Map<String, User> users;
    private final Map<String, LibraryItem> libraryItems;
    private final List<Borrow> activeBorrows;
    private final List<Borrow> borrowHistory;
    private final List<Penalty> pendingPenalties;
    private final List<Penalty> paidPenalties;

    public Library() {
        this.users = new HashMap<>();
        this.libraryItems = new HashMap<>();
        this.activeBorrows = new ArrayList<>();
        this.borrowHistory = new ArrayList<>();
        this.pendingPenalties = new ArrayList<>();
        this.paidPenalties = new ArrayList<>();
    }

    public boolean addItem(LibraryItem item) {
        if (item == null) {
            System.err.println("Não pode adicionar um item nulo.");
            return false;
        }
        String itemId = getItemIdentifier(item);
        if (libraryItems.containsKey(itemId)) {
            System.err.println("Item com ID " + itemId + " já existente.");
            return false;
        }
        libraryItems.put(itemId, item);
        System.out.println("Item '" + item.getTitle() + "' adicionado com sucesso.");
        return true;
    }

    public LibraryItem getItem(String itemId) {
        return libraryItems.get(itemId);
    }

    public boolean updateItem(String oldItemId, LibraryItem updatedItem) {
        if (oldItemId == null || oldItemId.trim().isEmpty()) {
            System.err.println("Antigo ID do item não pode ser nulo ou vazio para atualização");
            return false;
        }
        if (updatedItem == null) {
            System.err.println("Item atualizado não pode ser nulo.");
            return false;
        }

        if (!libraryItems.containsKey(oldItemId)) {
            System.err.println("Item com ID " + oldItemId + " não encontrado para atualização.");
            return false;
        }

        String updatedItemId = getItemIdentifier(updatedItem);
        if (!oldItemId.equals(updatedItemId)) {
            System.err.println("Atualização falhou: novo ID (" + updatedItemId + ") não bate com o ID anterior (" + oldItemId + ").");
            return false;
        }

        libraryItems.put(oldItemId, updatedItem);
        System.out.println("Item com ID " + oldItemId + " atualizado com sucesso para '" + updatedItem.getTitle() + "'.");
        return true;
    }

    public List<LibraryItem> listAllItems() {
        return new ArrayList<>(libraryItems.values());
    }

    public List<LibraryItem> listItemsByCategory(String category) {
        return libraryItems.values().stream()
                .filter(item -> {
                    if ("Livro".equalsIgnoreCase(category)) return item instanceof Book;
                    if ("Revista".equalsIgnoreCase(category)) return item instanceof Magazine;
                    if ("Dvd".equalsIgnoreCase(category)) return item instanceof Dvd;
                    return true;
                })
                .collect(Collectors.toList());
    }

    private String getItemIdentifier(LibraryItem item) {
        if (item instanceof Book book) {
            return book.getISBN();
        } else if (item instanceof Magazine magazine) {
            return magazine.getISSN();
        } else if (item instanceof Dvd dvd) {
            return dvd.getTitle() + "-" + dvd.getAuthor() + "-" + dvd.getPublicationYear();
        }
        return item.getTitle();
    }

    public boolean registerUser(User user) {
        if (user == null) {
            System.err.println("Não pode registrar um Usuário nulo.");
            return false;
        }
        if (users.containsKey(user.getCPF())) {
            System.err.println("Usuário com CPF " + user.getCPF() + " já existente.");
            return false;
        }
        users.put(user.getCPF(), user);
        System.out.println("Usuário '" + user.getName() + "' registrado com sucesso.");
        return true;
    }

    public User getUser(String cpf) {
        return users.get(cpf);
    }

    public Borrow performBorrow(String userCpf, String itemId) {
        User user = users.get(userCpf);
        LibraryItem item = libraryItems.get(itemId);

        if (user == null) {
            System.err.println("Empréstimo falhou: usuário com CPF " + userCpf + " não encontrado.");
            return null;
        }
        if (item == null) {
            System.err.println("Empréstimo falhou: item com ID " + itemId + " não encontrado.");
            return null;
        }
        if (!item.isAvailable()) {
            System.err.println("Empréstimo falhou: item '" + item.getTitle() + "' não disponível.");
            return null;
        }
        if (user.isBlocked()) {
            System.err.println("Empréstimo falhou: usuário '" + user.getName() + "' está bloqueado devido multas.");
            return null;
        }

        long currentUserBorrows = activeBorrows.stream()
                .filter(b -> b.getUser().getCPF().equals(userCpf) && b.getReturnDate() == null)
                .count();
        if (currentUserBorrows >= user.getUserType().getMaxBorrows()) {
            System.err.println("Empréstimo falhou: usuário '" + user.getName() + "' já chegou no máximo de empréstimos (" + user.getUserType().getMaxBorrows() + ").");
            return null;
        }

        if (!(item instanceof Borrowable borrowableItem)) {
            System.err.println("Empréstimo falhou: Item '" + item.getTitle() + "' não é alugável.");
            return null;
        }

        if (borrowableItem.borrowItem(user)) {
            LocalDate borrowDate = LocalDate.now();
            LocalDate dueDate = borrowDate.plusDays(user.getUserType().getDefaultBorrowPeriodDays());

            Borrow newBorrow = new Borrow(user, item, borrowDate, dueDate);
            activeBorrows.add(newBorrow);
            borrowHistory.add(newBorrow);
            user.addBorrowToHistory(newBorrow);
            System.out.println("Empréstimo falhou: '" + item.getTitle() + "' para '" + user.getName() + "'. Vencido: " + dueDate);
            return newBorrow;
        }
        return null;
    }

    public boolean returnBorrow(String borrowId, LocalDate actualReturnDate) {
        Borrow borrowToReturn = activeBorrows.stream()
                .filter(b -> b.getBorrowId().equals(borrowId))
                .findFirst()
                .orElse(null);

        if (borrowToReturn == null) {
            System.err.println("Empréstimo falhou: Empréstimo ativo com ID " + borrowId + " não encontrado");
            return false;
        }

        if (borrowToReturn.getReturnDate() != null) {
            System.err.println("Empréstimo falhou: empréstimo ID " + borrowId + " já foi devolvido.");
            return false;
        }

        if (!(borrowToReturn.getItem() instanceof Borrowable borrowableItem)) {
            System.err.println("Erro interno: item alugável não é alugável (verificar).");
            return false;
        }

        if (borrowableItem.returnItem()) {
            borrowToReturn.setReturnDate(actualReturnDate);

            double penaltyAmount = borrowToReturn.getUser().calculatePenalty(borrowToReturn);
            if (penaltyAmount > 0) {
                Penalty newPenalty = new Penalty(borrowToReturn, penaltyAmount);
                pendingPenalties.add(newPenalty);
                borrowToReturn.getUser().setBlocked(true);
                System.out.println("Multa incorrida por '" + borrowToReturn.getItem().getTitle() + "': $" + String.format("%.2f", penaltyAmount) + ". Usuário " + borrowToReturn.getUser().getName() + " foi bloqueado.");
            }

            activeBorrows.remove(borrowToReturn);

            System.out.println("Empréstimo ID " + borrowId + " retornado e processado com sucesso.");
            return true;
        }
        return false;
    }

    public boolean renewBorrow(String borrowId) {
        Borrow borrowToRenew = activeBorrows.stream()
                .filter(b -> b.getBorrowId().equals(borrowId))
                .findFirst()
                .orElse(null);

        if (borrowToRenew == null) {
            System.err.println("Renovação falhou: Empréstimo ativo com ID " + borrowId + " não encontrado.");
            return false;
        }
        if (borrowToRenew.getReturnDate() != null) {
            System.err.println("Renovação falhou: Empréstimo com ID " + borrowId + " já foi retornado.");
            return false;
        }
        if (borrowToRenew.isRenewed()) {
            System.err.println("Renovação falhou: Empréstimo com ID " + borrowId + " já foi renovado.");
            return false;
        }
        if (LocalDate.now().isAfter(borrowToRenew.getDueDate())) {
            System.err.println("Renovação falhou: Empréstimo com ID " + borrowId + " já está atrasado.");
            return false;
        }

        if (!(borrowToRenew.getItem() instanceof Borrowable borrowableItem)) {
            System.err.println("Erro interno: Item em empréstimo não é alugável.");
            return false;
        }
        int extensionDays = borrowableItem.getBorrowPeriodDays();
        borrowToRenew.setDueDate(borrowToRenew.getDueDate().plusDays(extensionDays));
        borrowToRenew.setRenewed(true);
        System.out.println("Empréstimo ID " + borrowId + " renovado com sucesso. Nova data de vencimento: " + borrowToRenew.getDueDate());
        return true;
    }

    public List<Penalty> getPendingPenalties() {
        return new ArrayList<>(pendingPenalties);
    }

    public boolean payPenalty(String penaltyId) {
        Penalty penaltyToPay = pendingPenalties.stream()
                .filter(p -> p.getPenaltyId().equals(penaltyId))
                .findFirst()
                .orElse(null);

        if (penaltyToPay == null) {
            System.err.println("Multa com ID " + penaltyId + " não identificada em multas pendentes.");
            return false;
        }

        penaltyToPay.setPaid(true);
        pendingPenalties.remove(penaltyToPay);
        paidPenalties.add(penaltyToPay);

        boolean userStillHasPendingPenalties = pendingPenalties.stream()
                .anyMatch(p -> p.getBorrow().getUser().getCPF().equals(penaltyToPay.getBorrow().getUser().getCPF()));

        if (!userStillHasPendingPenalties) {
            penaltyToPay.getBorrow().getUser().setBlocked(false);
            System.out.println("Usuário " + penaltyToPay.getBorrow().getUser().getName() + " desbloqueado.");
        }
        System.out.println("Multa ID " + penaltyId + " paga com sucesso. Valor: R$" + String.format("%.2f", penaltyToPay.getAmount()));
        return true;
    }

    public List<LibraryItem> getMostBorrowedItems(int limit) {
        return libraryItems.values().stream()
                .sorted((item1, item2) -> Integer.compare(item2.getBorrowCount(), item1.getBorrowCount()))
                .limit(limit)
                .collect(Collectors.toList());
    }

    public List<User> getUsersWithMostBorrows(int limit) {
        Map<User, Long> userBorrowCounts = borrowHistory.stream()
                .collect(Collectors.groupingBy(Borrow::getUser, Collectors.counting()));

        return userBorrowCounts.entrySet().stream()
                .sorted(Map.Entry.<User, Long>comparingByValue().reversed())
                .map(Map.Entry::getKey)
                .limit(limit)
                .collect(Collectors.toList());
    }

    public List<Borrow> getOverdueItems() {
        LocalDate today = LocalDate.now();
        return activeBorrows.stream()
                .filter(borrow -> today.isAfter(borrow.getDueDate()))
                .collect(Collectors.toList());
    }

    public double getTotalPenaltyRevenue() {
        return paidPenalties.stream()
                .mapToDouble(Penalty::getAmount)
                .sum();
    }
}