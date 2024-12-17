package com.hatsukha.nikolai.client.operationForms;

import com.hatsukha.nikolai.client.ClientConnection;
import com.hatsukha.nikolai.client.clientForms.AdminMainForm;
import com.hatsukha.nikolai.client.utils.StyleUtils;

import javax.swing.*;
import javax.swing.table.DefaultTableModel;
import javax.swing.table.TableRowSorter;
import java.awt.*;
import java.util.ArrayList;
import java.util.List;

public class OperationCrudForm extends JFrame {
    private final ClientConnection clientConnection;
    private final JTable operationsTable;
    private final int userId;

    public OperationCrudForm(ClientConnection clientConnection, int userId) {
        this.clientConnection = clientConnection;
        this.userId = userId;

        setTitle("Управление операциями");
        setSize(1000, 600);
        setDefaultCloseOperation(DISPOSE_ON_CLOSE);
        setLocationRelativeTo(null);

        JPanel mainPanel = new JPanel(new BorderLayout());

        // Используем стилизованную таблицу
        operationsTable = StyleUtils.createStyledTable();
        JScrollPane scrollPane = new JScrollPane(operationsTable);
        scrollPane.setBorder(BorderFactory.createTitledBorder(
                StyleUtils.createStyledLineBorder(),
                "Список операций",
                0,
                0,
                new Font("Arial", Font.BOLD, 16),
                StyleUtils.TITLE_COLOR
        ));

        JPanel buttonPanel = createButtonPanel();

        loadOperations();

        mainPanel.add(scrollPane, BorderLayout.CENTER);
        mainPanel.add(buttonPanel, BorderLayout.SOUTH);

        add(mainPanel);
        setVisible(true);
    }

    private JPanel createButtonPanel() {
        JPanel buttonPanel = new JPanel(new GridLayout(1, 4, 10, 10));
        buttonPanel.setBorder(BorderFactory.createEmptyBorder(10, 10, 10, 10));

        // Создаём стилизованные кнопки
        JButton addButton = StyleUtils.createStyledButton("Добавить операцию");
        JButton updateButton = StyleUtils.createStyledButton("Редактировать операцию");
        JButton deleteButton = StyleUtils.createStyledButton("Удалить операцию");
        JButton backButton = StyleUtils.createStyledButton("Назад");

        addButton.addActionListener(e -> openAddOperationForm());
        updateButton.addActionListener(e -> openUpdateOperationForm());
        deleteButton.addActionListener(e -> deleteSelectedOperation());

        backButton.addActionListener(e -> {
            dispose();
            new AdminMainForm(clientConnection, userId).show();
        });

        buttonPanel.add(addButton);
        buttonPanel.add(updateButton);
        buttonPanel.add(deleteButton);
        buttonPanel.add(backButton);

        return buttonPanel;
    }

    public void loadOperations() {
        clientConnection.send("VIEW_OPERATION");

        try {
            int operationCount = Integer.parseInt(clientConnection.receive());
            List<Object[]> rowData = new ArrayList<>();
            for (int i = 0; i < operationCount; i++) {
                String[] data = clientConnection.receive().split(",");
                rowData.add(new Object[]{data[0], data[1], data[2], data[3], data[4], data[5]});
            }

            String[] columnNames = {"ID", "Продукт ID", "Склад ID", "User ID", "Тип операции", "Количество", "Дата операции"};
            DefaultTableModel model = new DefaultTableModel(rowData.toArray(new Object[0][]), columnNames);
            operationsTable.setModel(model);

            TableRowSorter<DefaultTableModel> sorter = new TableRowSorter<>(model);
            operationsTable.setRowSorter(sorter);
        } catch (NumberFormatException e) {
            JOptionPane.showMessageDialog(this, "Ошибка загрузки операций. Неверный формат ответа сервера!",
                    "Ошибка", JOptionPane.ERROR_MESSAGE);
        }
    }

    private void openAddOperationForm() {
        new AddOperationForm(clientConnection, this).setVisible(true);
    }

    private void openUpdateOperationForm() {
        int selectedRow = operationsTable.getSelectedRow();
        if (selectedRow == -1) {
            JOptionPane.showMessageDialog(this, "Выберите операцию для редактирования!", "Ошибка", JOptionPane.ERROR_MESSAGE);
            return;
        }

        int operationId = Integer.parseInt(operationsTable.getValueAt(selectedRow, 0).toString());
        String productId = operationsTable.getValueAt(selectedRow, 1).toString();
        String warehouseId = operationsTable.getValueAt(selectedRow, 2).toString();
        String operationType = operationsTable.getValueAt(selectedRow, 4).toString();
        String quantity = operationsTable.getValueAt(selectedRow, 5).toString();

        new UpdateOperationForm(clientConnection, operationId, userId, this, productId, warehouseId, operationType, quantity).setVisible(true);
    }

    private void deleteSelectedOperation() {
        int selectedRow = operationsTable.getSelectedRow();
        if (selectedRow == -1) {
            JOptionPane.showMessageDialog(this, "Выберите операцию для удаления!", "Ошибка", JOptionPane.ERROR_MESSAGE);
            return;
        }

        int operationId = Integer.parseInt(operationsTable.getValueAt(selectedRow, 0).toString());

        int confirmation = JOptionPane.showConfirmDialog(this, "Вы уверены, что хотите удалить эту операцию?",
                "Подтверждение", JOptionPane.YES_NO_OPTION);
        if (confirmation == JOptionPane.YES_OPTION) {
            clientConnection.send("DELETE_OPERATION");
            clientConnection.send(String.valueOf(operationId));

            String response = clientConnection.receive();
            if ("SUCCESS".equals(response)) {
                JOptionPane.showMessageDialog(this, "Операция успешно удалена!");
                loadOperations();
            } else {
                JOptionPane.showMessageDialog(this, "Ошибка удаления операции!", "Ошибка", JOptionPane.ERROR_MESSAGE);
            }
        }
    }
}