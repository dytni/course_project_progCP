package com.hatsukha.nikolai.client.productForms.old;

import com.hatsukha.nikolai.client.ClientConnection;
import com.hatsukha.nikolai.client.clientForms.AdminMainForm;
import com.hatsukha.nikolai.client.clientForms.UserMainForm;
import com.hatsukha.nikolai.client.utils.StyleUtils;

import javax.swing.*;
import javax.swing.table.DefaultTableModel;
import javax.swing.table.TableRowSorter;
import java.awt.*;
import java.util.ArrayList;
import java.util.List;

public class ViewProductsForm extends JFrame {
    private final ClientConnection clientConnection;
    private final String role;
    private final int userId;
    private final JTable productTable;
    private final JTextField searchField;
    private final JComboBox<String> categoryFilter;

    public ViewProductsForm(ClientConnection clientConnection, String role, int userId) {
        this.clientConnection = clientConnection;
        this.role = role;
        this.userId = userId;

        setTitle("Список продуктов");
        setSize(1000, 600);
        setDefaultCloseOperation(DISPOSE_ON_CLOSE);
        setLocationRelativeTo(null);

        productTable = StyleUtils.createStyledTable();
        searchField = new JTextField(15);
        categoryFilter = new JComboBox<>(new String[]{"Все категории"});

        initializeUI();
        loadProducts();

        setVisible(true);
    }

    private void initializeUI() {
        JPanel mainPanel = new JPanel(new BorderLayout());
        JScrollPane scrollPane = new JScrollPane(productTable);
        scrollPane.setBorder(BorderFactory.createTitledBorder(
                BorderFactory.createLineBorder(new Color(173, 216, 230), 2),
                "Список продуктов",
                0,
                0,
                new Font("Arial", Font.BOLD, 16),
                new Color(173, 216, 230)
        ));

        JPanel controlsPanel = new JPanel(new FlowLayout(FlowLayout.LEFT));
        controlsPanel.add(new JLabel("Поиск:"));
        controlsPanel.add(searchField);
        controlsPanel.add(new JLabel("Категория:"));
        controlsPanel.add(categoryFilter);

        JButton backButton = StyleUtils.createStyledButton("Назад");
        backButton.addActionListener(e -> {
            dispose();
            if ("ADMIN".equalsIgnoreCase(role)) {
                new AdminMainForm(clientConnection, userId).show();
            } else {
                new UserMainForm(clientConnection, userId).show();
            }
        });

        searchField.getDocument().addDocumentListener(new javax.swing.event.DocumentListener() {
            @Override
            public void insertUpdate(javax.swing.event.DocumentEvent e) {
                applyFilters();
            }

            @Override
            public void removeUpdate(javax.swing.event.DocumentEvent e) {
                applyFilters();
            }

            @Override
            public void changedUpdate(javax.swing.event.DocumentEvent e) {
                applyFilters();
            }
        });

        categoryFilter.addActionListener(e -> applyFilters());

        mainPanel.add(controlsPanel, BorderLayout.NORTH);
        mainPanel.add(scrollPane, BorderLayout.CENTER);
        mainPanel.add(backButton, BorderLayout.SOUTH);
        add(mainPanel);
    }

    private void loadProducts() {
        clientConnection.send("VIEW_PRODUCTS");

        try {
            int productCount = Integer.parseInt(clientConnection.receive());
            List<Object[]> rowData = new ArrayList<>();
            List<String> categories = new ArrayList<>();
            categories.add("Все категории");

            for (int i = 0; i < productCount; i++) {
                String[] data = clientConnection.receive().split(",");
                rowData.add(new Object[]{data[0], data[1], data[2], data[3], data[4], data[5], data[6]});
                if (!categories.contains(data[3])) {
                    categories.add(data[3]);
                }
            }

            String[] columnNames = {"ID", "Название", "Описание", "Категория", "Вес", "Объём", "Локация"};
            DefaultTableModel model = new DefaultTableModel(rowData.toArray(new Object[0][]), columnNames);
            productTable.setModel(model);

            TableRowSorter<DefaultTableModel> sorter = new TableRowSorter<>(model);
            productTable.setRowSorter(sorter);

            categoryFilter.setModel(new DefaultComboBoxModel<>(categories.toArray(new String[0])));

        } catch (NumberFormatException e) {
            JOptionPane.showMessageDialog(this, "Ошибка загрузки продуктов. Неверный формат ответа сервера!",
                    "Ошибка", JOptionPane.ERROR_MESSAGE);
        }
    }

    private void applyFilters() {
        DefaultTableModel model = (DefaultTableModel) productTable.getModel();
        TableRowSorter<DefaultTableModel> sorter = new TableRowSorter<>(model);

        String searchText = searchField.getText().toLowerCase();
        String selectedCategory = (String) categoryFilter.getSelectedItem();

        sorter.setRowFilter(new javax.swing.RowFilter<>() {
            @Override
            public boolean include(Entry<? extends DefaultTableModel, ? extends Integer> entry) {
                String name = entry.getStringValue(1).toLowerCase();
                String category = entry.getStringValue(3);

                boolean matchesSearch = name.contains(searchText);
                boolean matchesCategory = "Все категории".equals(selectedCategory) || category.equals(selectedCategory);

                return matchesSearch && matchesCategory;
            }
        });

        productTable.setRowSorter(sorter);
    }
}
