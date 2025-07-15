package poo.view;

import poo.system.Library;
import javax.swing.*;
import java.awt.*;

public class LibraryGUI extends JFrame {

    private Library library; // Instância da nossa classe de lógica de negócio
    private JTabbedPane tabbedPane;

    public LibraryGUI(Library library) {
        this.library = library; // Recebe a instância da Library
        setTitle("Sistema de Gerenciamento de Biblioteca");
        setSize(800, 600); // Tamanho inicial da janela
        setDefaultCloseOperation(JFrame.EXIT_ON_CLOSE); // Fecha a aplicação ao fechar a janela
        setLocationRelativeTo(null); // Centraliza a janela na tela

        initComponents(); // Método para inicializar os componentes da GUI
    }

    private void initComponents() {
        tabbedPane = new JTabbedPane();

        // --- Painel de Gerenciamento de Itens ---
        ItemPanel itemPanel = new ItemPanel(library);
        tabbedPane.addTab("Gerenciar Itens", itemPanel);

        // --- Adicione outros painéis aqui conforme for desenvolvendo ---
        // tabbedPane.addTab("Gerenciar Usuários", new UserPanel(library));
        // tabbedPane.addTab("Empréstimos", new BorrowReturnPanel(library));
        // tabbedPane.addTab("Multas", new PenaltyPanel(library));
        // tabbedPane.addTab("Relatórios", new ReportPanel(library));

        add(tabbedPane, BorderLayout.CENTER); // Adiciona o painel de abas à janela
    }

    public static void main(String[] args) {
        // Ponto de entrada da aplicação Swing
        // Cria uma nova instância da Library (lógica de negócio)
        Library library = new Library();

        // Garante que a GUI seja construída e exibida na Event Dispatch Thread (EDT)
        // ESSENCIAL para aplicações Swing.
        SwingUtilities.invokeLater(() -> {
            new LibraryGUI(library).setVisible(true);
        });
    }
}