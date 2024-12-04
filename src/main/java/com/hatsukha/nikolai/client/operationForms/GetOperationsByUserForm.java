package com.hatsukha.nikolai.client.operationForms;

import com.hatsukha.nikolai.client.ClientConnection;
import com.hatsukha.nikolai.client.clientForms.UserMainForm;

import javax.swing.*;
import javax.swing.table.DefaultTableModel;
import javax.swing.table.TableRowSorter;
import java.awt.*;
import java.util.ArrayList;
import java.util.List;
import java.util.Timer;
import java.util.TimerTask;

public class GetOperationsByUserForm extends JFrame {
    private final ClientConnection clientConnection;
    private final int userId;
    private final JTable operationsTable;

    public GetOperationsByUserForm(ClientConnection clientConnection, int userId) {
        this.clientConnection = clientConnection;
        this.userId = userId;

        setTitle("Операции пользователя");
        setSize(800, 600);
        setDefaultCloseOperation(DISPOSE_ON_CLOSE);
        setLocationRelativeTo(null);

        JPanel mainPanel = new JPanel(new BorderLayout());
        operationsTable = createStyledTable();
        JScrollPane scrollPane = new JScrollPane(operationsTable);
        scrollPane.setBorder(BorderFactory.createTitledBorder(
                BorderFactory.createLineBorder(new Color(173, 216, 230), 2),
                "Операции пользователя",
                0,
                0,
                new Font("Arial", Font.BOLD, 16),
                new Color(173, 216, 230)
        ));

        JButton orderButton = createStyledButton("Провести операцию");
        JButton updateButton = createStyledButton("Обновить операцию");
        JButton backButton = createStyledButton("Назад");

        JPanel buttonPanel = new JPanel(new GridLayout(1, 2, 10, 10));
        buttonPanel.setBorder(BorderFactory.createEmptyBorder(10, 10, 10, 10));
        buttonPanel.add(orderButton);
        buttonPanel.add(updateButton);
        buttonPanel.add(backButton);

        orderButton.addActionListener(e -> {
                makeOrder();
        });
        backButton.addActionListener(e -> {
            dispose();
            new UserMainForm(clientConnection, userId).show();
        });

        updateButton.addActionListener(e -> {
            int selectedRow = operationsTable.getSelectedRow();
            if (selectedRow == -1) {
                JOptionPane.showMessageDialog(this, "Выберите операцию для обновления!", "Ошибка", JOptionPane.ERROR_MESSAGE);
                return;
            }

            int operationId = Integer.parseInt(operationsTable.getValueAt(selectedRow, 0).toString());
            String productId = operationsTable.getValueAt(selectedRow, 1).toString();
            String warehouseId = operationsTable.getValueAt(selectedRow, 2).toString();
            String operationType = operationsTable.getValueAt(selectedRow, 3).toString();
            String quantity = operationsTable.getValueAt(selectedRow, 4).toString();

            new UpdateOperationForm(clientConnection, operationId, userId, this, productId, warehouseId, operationType, quantity);
        });


        mainPanel.add(scrollPane, BorderLayout.CENTER);
        mainPanel.add(buttonPanel, BorderLayout.SOUTH);

        add(mainPanel);
        setVisible(true);

        // Автоматическое обновление таблицы
        loadOperationsByUser();

    }

    private JTable createStyledTable() {
        JTable table = new JTable();
        table.setFont(new Font("Arial", Font.PLAIN, 16));
        table.setRowHeight(30);
        table.getTableHeader().setFont(new Font("Arial", Font.BOLD, 18));
        table.getTableHeader().setBackground(new Color(173, 216, 230));
        table.getTableHeader().setForeground(Color.BLACK);
        table.setSelectionBackground(new Color(135, 206, 235));
        table.setSelectionForeground(Color.BLACK);
        table.setGridColor(new Color(173, 216, 230));
        table.setBackground(Color.WHITE);
        table.setForeground(Color.BLACK);
        table.setShowGrid(true);
        table.setAutoCreateRowSorter(true);
        return table;
    }

    private JButton createStyledButton(String text) {
        JButton button = new JButton(text);
        button.setFont(new Font("Arial", Font.BOLD, 16));
        button.setForeground(Color.BLACK);
        button.setBackground(new Color(173, 216, 230));
        button.setFocusPainted(false);
        button.setCursor(new Cursor(Cursor.HAND_CURSOR));
        button.setBorder(BorderFactory.createEmptyBorder(10, 20, 10, 20));

        button.addMouseListener(new java.awt.event.MouseAdapter() {
            public void mouseEntered(java.awt.event.MouseEvent evt) {
                button.setBackground(new Color(135, 206, 235));
            }

            public void mouseExited(java.awt.event.MouseEvent evt) {
                button.setBackground(new Color(173, 216, 230));
            }
        });

        return button;
    }

    public void loadOperationsByUser() {
        clientConnection.send("VIEW_OPERATION_BY_ID");
        clientConnection.send(String.valueOf(userId));

        try {
            int operationCount = Integer.parseInt(clientConnection.receive());
            List<Object[]> rowData = new ArrayList<>();
            for (int i = 0; i < operationCount; i++) {
                String[] data = clientConnection.receive().split(",");
                rowData.add(new Object[]{data[0], data[1], data[2], data[4], data[5], data[6]});
            }

            String[] columnNames = {"ID", "Продукт ID", "Склад ID", "Тип операции", "Количество", "Дата операции"};
            DefaultTableModel model = new DefaultTableModel(rowData.toArray(new Object[0][]), columnNames);
            operationsTable.setModel(model);

            TableRowSorter<DefaultTableModel> sorter = new TableRowSorter<>(model);
            operationsTable.setRowSorter(sorter);
        } catch (NumberFormatException e) {
            JOptionPane.showMessageDialog(this, "Ошибка загрузки операций. Неверный формат ответа сервера!",
                    "Ошибка", JOptionPane.ERROR_MESSAGE);
            e.printStackTrace();
        }
    }

    private void makeOrder() {
        JFrame frame = new JFrame("Сделать заказ");
        frame.setSize(500, 400);
        frame.setDefaultCloseOperation(DISPOSE_ON_CLOSE);
        frame.setLocationRelativeTo(this);

        JPanel panel = new JPanel(new GridLayout(6, 2, 10, 10));
        panel.setBorder(BorderFactory.createEmptyBorder(20, 20, 20, 20));

        JLabel productLabel = new JLabel("Выберите продукт:");
        JComboBox<String> productComboBox = new JComboBox<>();

        JLabel warehouseLabel = new JLabel("Выберите склад:");
        JComboBox<String> warehouseComboBox = new JComboBox<>();

        JLabel operationTypeLabel = new JLabel("Тип операции:");
        JComboBox<String> operationTypeComboBox = new JComboBox<>(new String[]{"приём", "перемещение", "списание"});

        JLabel quantityLabel = new JLabel("Количество:");
        JTextField quantityField = new JTextField();

        JButton orderButton = createStyledButton("Оформить");
        JButton cancelButton = createStyledButton("Отмена");

        // Загрузка доступных продуктов
        clientConnection.send("VIEW_PRODUCTS");
        try {
            int productCount = Integer.parseInt(clientConnection.receive());
            for (int i = 0; i < productCount; i++) {
                String[] productData = clientConnection.receive().split(",");
                String productItem = "ID: " + productData[0] + " - " + productData[1];
                productComboBox.addItem(productItem);
            }
        } catch (NumberFormatException e) {
            JOptionPane.showMessageDialog(frame, "Ошибка загрузки продуктов!", "Ошибка", JOptionPane.ERROR_MESSAGE);
            e.printStackTrace();
            return;
        }

        // Загрузка доступных складов
        clientConnection.send("VIEW_WAREHOUSES");
        try {
            int warehouseCount = Integer.parseInt(clientConnection.receive());
            for (int i = 0; i < warehouseCount; i++) {
                String[] warehouseData = clientConnection.receive().split(",");
                String warehouseItem = "ID: " + warehouseData[0] + " - " + warehouseData[1];
                warehouseComboBox.addItem(warehouseItem);
            }
        } catch (NumberFormatException e) {
            JOptionPane.showMessageDialog(frame, "Ошибка загрузки складов!", "Ошибка", JOptionPane.ERROR_MESSAGE);
            e.printStackTrace();
            return;
        }

        orderButton.addActionListener(e -> {
            try {
                String selectedProduct = (String) productComboBox.getSelectedItem();
                String selectedWarehouse = (String) warehouseComboBox.getSelectedItem();

                if (selectedProduct == null || selectedWarehouse == null) {
                    JOptionPane.showMessageDialog(frame, "Выберите продукт и склад!", "Ошибка", JOptionPane.ERROR_MESSAGE);
                    return;
                }

                int productId = Integer.parseInt(selectedProduct.split(" ")[1]);
                int warehouseId = Integer.parseInt(selectedWarehouse.split(" ")[1]);
                int quantity = Integer.parseInt(quantityField.getText().trim());
                String operationType = (String) operationTypeComboBox.getSelectedItem();

                clientConnection.send("ADD_OPERATION");
                clientConnection.send(String.valueOf(productId));
                clientConnection.send(String.valueOf(warehouseId));
                clientConnection.send(String.valueOf(userId));
                clientConnection.send(operationType);
                clientConnection.send(String.valueOf(quantity));

                String response = clientConnection.receive();
                if ("SUCCESS".equals(response)) {
                    JOptionPane.showMessageDialog(frame, "Операция успешно добавлена!");
                    loadOperationsByUser();
                    frame.dispose();
                } else {
                    JOptionPane.showMessageDialog(frame, "Ошибка добавления операции!", "Ошибка", JOptionPane.ERROR_MESSAGE);
                }
            } catch (NumberFormatException ex) {
                JOptionPane.showMessageDialog(frame, "Введите корректное количество!", "Ошибка", JOptionPane.ERROR_MESSAGE);
            }
        });

        cancelButton.addActionListener(e -> frame.dispose());

        panel.add(productLabel);
        panel.add(productComboBox);
        panel.add(warehouseLabel);
        panel.add(warehouseComboBox);
        panel.add(operationTypeLabel);
        panel.add(operationTypeComboBox);
        panel.add(quantityLabel);
        panel.add(quantityField);
        panel.add(orderButton);
        panel.add(cancelButton);

        frame.add(panel);
        frame.setVisible(true);
    }

}