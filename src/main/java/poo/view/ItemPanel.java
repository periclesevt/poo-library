package poo.view;

import poo.system.Library;
import poo.item.*; // Importa Book, Dvd, Magazine, LibraryItem
import javax.swing.*;
import javax.swing.table.DefaultTableModel;
import java.awt.*;
import java.awt.event.ActionEvent;
import java.util.List;

public class ItemPanel extends JPanel {

    private Library library;
    private JTextField titleField, authorDirectorEditorField, publicationYearField;
    private JTextField specificIdField; // Para ISBN ou ISSN
    private JTextField editionNumberField; // Para revista apenas
    private JComboBox<String> itemTypeComboBox;
    private JLabel specificIdLabel, editionNumberLabel;
    private JButton addItemButton, refreshListButton;
    private JTable itemTable;
    private DefaultTableModel tableModel;

    public ItemPanel(Library library) {
        this.library = library;
        setLayout(new BorderLayout(10, 10)); // Layout principal

        // --- Painel de Entrada de Dados ---
        JPanel inputPanel = new JPanel(new GridBagLayout());
        inputPanel.setBorder(BorderFactory.createTitledBorder("Adicionar Novo Item"));
        GridBagConstraints gbc = new GridBagConstraints();
        gbc.insets = new Insets(5, 5, 5, 5); // Espaçamento entre componentes
        gbc.fill = GridBagConstraints.HORIZONTAL;

        // Linha 1: Tipo de Item
        gbc.gridx = 0; gbc.gridy = 0; inputPanel.add(new JLabel("Tipo de Item:"), gbc);
        gbc.gridx = 1; gbc.gridy = 0; gbc.gridwidth = 2;
        itemTypeComboBox = new JComboBox<>(new String[]{"Livro", "Revista", "DVD"});
        inputPanel.add(itemTypeComboBox, gbc);
        gbc.gridwidth = 1; // Reset gridwidth

        // Linha 2: Título
        gbc.gridx = 0; gbc.gridy = 1; inputPanel.add(new JLabel("Título:"), gbc);
        gbc.gridx = 1; gbc.gridy = 1; gbc.gridwidth = 2;
        titleField = new JTextField(20);
        inputPanel.add(titleField, gbc);
        gbc.gridwidth = 1;

        // Linha 3: Autor/Diretor/Editor
        gbc.gridx = 0; gbc.gridy = 2; inputPanel.add(new JLabel("Autor/Diretor/Editor:"), gbc);
        gbc.gridx = 1; gbc.gridy = 2; gbc.gridwidth = 2;
        authorDirectorEditorField = new JTextField(20);
        inputPanel.add(authorDirectorEditorField, gbc);
        gbc.gridwidth = 1;

        // Linha 4: Ano de Publicação
        gbc.gridx = 0; gbc.gridy = 3; inputPanel.add(new JLabel("Ano de Publicação:"), gbc);
        gbc.gridx = 1; gbc.gridy = 3; gbc.gridwidth = 2;
        publicationYearField = new JTextField(20);
        inputPanel.add(publicationYearField, gbc);
        gbc.gridwidth = 1;

        // Linha 5: Campo Específico (ISBN/ISSN)
        gbc.gridx = 0; gbc.gridy = 4;
        specificIdLabel = new JLabel("ISBN/ISSN:");
        inputPanel.add(specificIdLabel, gbc);
        gbc.gridx = 1; gbc.gridy = 4; gbc.gridwidth = 2;
        specificIdField = new JTextField(20);
        inputPanel.add(specificIdField, gbc);
        gbc.gridwidth = 1;

        // Linha 6: Número da Edição (Apenas para Revista)
        gbc.gridx = 0; gbc.gridy = 5;
        editionNumberLabel = new JLabel("Número da Edição:");
        inputPanel.add(editionNumberLabel, gbc);
        gbc.gridx = 1; gbc.gridy = 5; gbc.gridwidth = 2;
        editionNumberField = new JTextField(20);
        inputPanel.add(editionNumberField, gbc);
        gbc.gridwidth = 1;

        // Linha 7: Botão Adicionar
        gbc.gridx = 0; gbc.gridy = 6; gbc.gridwidth = 3;
        addItemButton = new JButton("Adicionar Item");
        inputPanel.add(addItemButton, gbc);
        gbc.gridwidth = 1; // Reset gridwidth

        add(inputPanel, BorderLayout.NORTH);

        // --- Tabela de Exibição de Itens (Centro) ---
        String[] columnNames = {"Título", "Autor/Diretor/Editor", "Ano", "ID Específico", "Tipo", "Disponível", "Empréstimos"};
        tableModel = new DefaultTableModel(columnNames, 0) {
            @Override
            public boolean isCellEditable(int row, int column) {
                return false; // Torna as células não editáveis
            }
        };
        itemTable = new JTable(tableModel);
        JScrollPane scrollPane = new JScrollPane(itemTable);
        add(scrollPane, BorderLayout.CENTER);

        // --- Painel de Botões da Tabela (Sul) ---
        JPanel tableButtonPanel = new JPanel(new FlowLayout(FlowLayout.CENTER));
        refreshListButton = new JButton("Atualizar Lista");
        tableButtonPanel.add(refreshListButton);
        add(tableButtonPanel, BorderLayout.SOUTH);


        // --- Adicionar Listeners ---
        addItemButton.addActionListener(this::addItemAction);
        refreshListButton.addActionListener(e -> updateItemList());
        itemTypeComboBox.addActionListener(this::itemTypeChanged);

        // Inicializar visibilidade dos campos específicos
        itemTypeChanged(null); // Chama para configurar a visibilidade inicial
        updateItemList(); // Carrega a lista de itens ao iniciar
    }

    private void itemTypeChanged(ActionEvent e) {
        String selectedType = (String) itemTypeComboBox.getSelectedItem();
        if ("Livro".equals(selectedType)) {
            specificIdLabel.setText("ISBN:");
            specificIdField.setVisible(true);
            editionNumberLabel.setVisible(false);
            editionNumberField.setVisible(false);
        } else if ("Revista".equals(selectedType)) {
            specificIdLabel.setText("ISSN:");
            specificIdField.setVisible(true);
            editionNumberLabel.setVisible(true);
            editionNumberField.setVisible(true);
        } else if ("DVD".equals(selectedType)) {
            specificIdLabel.setText("ID (opcional):"); // DVD pode não ter ID externo padrão
            specificIdField.setVisible(false); // ID é gerado pelo getItemIdentifier se não houver ISBN/ISSN
            editionNumberLabel.setVisible(false);
            editionNumberField.setVisible(false);
        }
        revalidate(); // Revalida o layout para ajustar visibilidade
        repaint();    // Repinta o componente
    }


    private void addItemAction(ActionEvent e) {
        String title = titleField.getText();
        String authorDirectorEditor = authorDirectorEditorField.getText();
        String publicationYearText = publicationYearField.getText();
        String specificId = specificIdField.getText(); // ISBN or ISSN
        String editionNumberText = editionNumberField.getText(); // For Magazine

        String selectedType = (String) itemTypeComboBox.getSelectedItem();

        try {
            int publicationYear = Integer.parseInt(publicationYearText);
            LibraryItem newItem = null;

            switch (selectedType) {
                case "Livro":
                    newItem = new Book(title, authorDirectorEditor, publicationYear, specificId);
                    break;
                case "Revista":
                    int editionNumber = Integer.parseInt(editionNumberText);
                    newItem = new Magazine(title, authorDirectorEditor, publicationYear, specificId, editionNumber);
                    break;
                case "DVD":
                    newItem = new Dvd(title, authorDirectorEditor, publicationYear);
                    // For DVD, if specificIdField was shown/used, you might want to integrate it here.
                    // Currently, Dvd doesn't take an ID in its constructor directly.
                    // The getItemIdentifier handles its unique ID for internal map storage.
                    break;
                default:
                    JOptionPane.showMessageDialog(this, "Tipo de item desconhecido.", "Erro", JOptionPane.ERROR_MESSAGE);
                    return;
            }

            if (newItem != null && library.addItem(newItem)) {
                JOptionPane.showMessageDialog(this, "Item '" + title + "' adicionado com sucesso!", "Sucesso", JOptionPane.INFORMATION_MESSAGE);
                clearFields(); // Limpa os campos após o sucesso
                updateItemList(); // Atualiza a tabela
            } else {
                // Mensagem de erro já é impressa pela classe Library
                // Apenas exibe um diálogo genérico aqui se Library.addItem retornar false
                JOptionPane.showMessageDialog(this, "Falha ao adicionar item. Verifique os dados e se o item já existe.", "Erro", JOptionPane.ERROR_MESSAGE);
            }

        } catch (NumberFormatException ex) {
            JOptionPane.showMessageDialog(this, "Ano de Publicação e/ou Número da Edição devem ser números válidos.", "Erro de Formato", JOptionPane.ERROR_MESSAGE);
        } catch (IllegalArgumentException ex) {
            JOptionPane.showMessageDialog(this, "Erro de Validação: " + ex.getMessage(), "Erro de Validação", JOptionPane.ERROR_MESSAGE);
        } catch (Exception ex) {
            JOptionPane.showMessageDialog(this, "Ocorreu um erro inesperado: " + ex.getMessage(), "Erro", JOptionPane.ERROR_MESSAGE);
            ex.printStackTrace();
        }
    }

    private void clearFields() {
        titleField.setText("");
        authorDirectorEditorField.setText("");
        publicationYearField.setText("");
        specificIdField.setText("");
        editionNumberField.setText("");
        itemTypeComboBox.setSelectedIndex(0); // Volta para "Livro"
    }

    private void updateItemList() {
        tableModel.setRowCount(0); // Limpa a tabela
        List<LibraryItem> items = library.listAllItems();
        for (LibraryItem item : items) {
            String specificId = "";
            if (item instanceof Book book) {
                specificId = book.getISBN();
            } else if (item instanceof Magazine magazine) {
                specificId = magazine.getISSN() + " (Ed. " + magazine.getEditionNumber() + ")";
            } else if (item instanceof Dvd dvd) {
                specificId = dvd.getTitle() + "-" + dvd.getAuthor() + "-" + dvd.getPublicationYear(); // Usa o ID interno para DVD
            }

            // Garante que a coluna 'Tipo' exiba o tipo correto
            String itemType = "";
            if (item instanceof Book) itemType = "Livro";
            else if (item instanceof Magazine) itemType = "Revista";
            else if (item instanceof Dvd) itemType = "DVD";

            tableModel.addRow(new Object[]{
                    item.getTitle(),
                    item.getAuthor(),
                    item.getPublicationYear(),
                    specificId,
                    itemType,
                    item.isAvailable() ? "Sim" : "Não",
                    item.getBorrowCount()
            });
        }
    }
}
