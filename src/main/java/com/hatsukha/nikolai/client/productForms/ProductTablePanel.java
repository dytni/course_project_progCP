package com.hatsukha.nikolai.client.productForms;

import com.hatsukha.nikolai.client.ClientConnection;

import javax.swing.*;
import javax.swing.table.DefaultTableModel;
import javax.swing.table.TableRowSorter;
import java.awt.*;
import java.util.ArrayList;
import java.util.List;

public class ProductTablePanel extends JPanel {
    private final ClientConnection clientConnection;
    private final JTable productTable;
    private final JComboBox<String> categoryFilter;
    private final JTextField searchField;

    public ProductTablePanel(ClientConnection clientConnection) {
        this.clientConnection = clientConnection;
        this.productTable = createStyledTable();
        this.categoryFilter = new JComboBox<>(new String[]{"Все категории"});
        this.searchField = new JTextField(20);

        setLayout(new BorderLayout());


        JPanel filterPanel = new JPanel(new FlowLayout(FlowLayout.LEFT));
        filterPanel.add(new JLabel("Поиск:"));
        filterPanel.add(searchField);
        filterPanel.add(new JLabel("Категория:"));
        filterPanel.add(categoryFilter);

        JScrollPane scrollPane = new JScrollPane(productTable);
        scrollPane.setBorder(BorderFactory.createTitledBorder(
                BorderFactory.createLineBorder(new Color(173, 216, 230), 2), // Голубая рамка
                "Список продуктов",
                0,
                0,
                new Font("Arial", Font.BOLD, 16),
                new Color(173, 216, 230) // Голубой текст заголовка
        ));

        add(filterPanel, BorderLayout.NORTH);
        add(scrollPane, BorderLayout.CENTER);

        categoryFilter.addActionListener(e -> applyFilters());
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

        loadProducts();
    }

    public JTable getProductTable() {
        return productTable;
    }

    public void loadProducts() {
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
                    categories.add(data[3]); // Добавляем уникальные категории
                }
            }

            String[] columnNames = {"ID", "Название", "Описание", "Категория", "Вес", "Объём", "Локация"};
            DefaultTableModel model = new DefaultTableModel(
                    rowData.toArray(new Object[0][]), columnNames
            );
            productTable.setModel(model);

            TableRowSorter<DefaultTableModel> sorter = new TableRowSorter<>(model);
            productTable.setRowSorter(sorter);

            // Обновляем категории в фильтре
            categoryFilter.setModel(new DefaultComboBoxModel<>(categories.toArray(new String[0])));

        } catch (NumberFormatException e) {
            JOptionPane.showMessageDialog(this, "Ошибка загрузки продуктов. Неверный формат ответа сервера!",
                    "Ошибка", JOptionPane.ERROR_MESSAGE);
            e.printStackTrace();
        }
    }

    private void applyFilters() {
        DefaultTableModel model = (DefaultTableModel) productTable.getModel();
        TableRowSorter<DefaultTableModel> sorter = new TableRowSorter<>(model);

        String selectedCategory = (String) categoryFilter.getSelectedItem();
        String searchText = searchField.getText().toLowerCase();

        sorter.setRowFilter(new javax.swing.RowFilter<>() {
            @Override
            public boolean include(Entry<? extends DefaultTableModel, ? extends Integer> entry) {
                String category = entry.getStringValue(3);
                String name = entry.getStringValue(1).toLowerCase();

                boolean matchesCategory = "Все категории".equals(selectedCategory) || category.equals(selectedCategory);
                boolean matchesSearch = name.contains(searchText);

                return matchesCategory && matchesSearch;
            }
        });

        productTable.setRowSorter(sorter);
    }

    private JTable createStyledTable() {
        JTable table = new JTable();
        table.setFont(new Font("Arial", Font.PLAIN, 16));
        table.setRowHeight(30);
        table.getTableHeader().setFont(new Font("Arial", Font.BOLD, 18));
        table.getTableHeader().setBackground(new Color(173, 216, 230)); // Голубая шапка
        table.getTableHeader().setForeground(Color.BLACK); // Чёрный текст заголовка
        table.setSelectionBackground(new Color(135, 206, 235)); // Голубая подсветка строки
        table.setSelectionForeground(Color.BLACK); // Чёрный текст при выделении
        table.setGridColor(new Color(173, 216, 230)); // Голубые линии сетки
        table.setBackground(Color.WHITE); // Белый фон таблицы
        table.setForeground(Color.BLACK); // Чёрный текст ячеек
        table.setShowGrid(true);

        // Включение сортировки
        table.setAutoCreateRowSorter(true);

        return table;
    }
}
