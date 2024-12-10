package com.hatsukha.nikolai.client.warehouseForms;

import com.hatsukha.nikolai.client.ClientConnection;
import com.hatsukha.nikolai.client.clientForms.AdminMainForm;
import com.hatsukha.nikolai.client.utils.StyleUtils;

import javax.swing.*;
import javax.swing.table.DefaultTableModel;
import javax.swing.table.TableRowSorter;
import java.awt.*;
import java.util.ArrayList;
import java.util.List;

public class WarehouseCrudForm extends JFrame {
    private final ClientConnection clientConnection;
    private final JTable warehouseTable;

    public WarehouseCrudForm(ClientConnection clientConnection) {
        this.clientConnection = clientConnection;
        this.warehouseTable = createStyledTable();

        setTitle("Управление складами");
        setSize(800, 600);
        setDefaultCloseOperation(DISPOSE_ON_CLOSE);
        setLocationRelativeTo(null);

        JPanel mainPanel = new JPanel(new BorderLayout());
        JScrollPane tableScrollPane = new JScrollPane(warehouseTable);
        tableScrollPane.setBorder(BorderFactory.createTitledBorder(
                StyleUtils.createStyledLineBorder(),
                "Список складов",
                0,
                0,
                new Font("Arial", Font.BOLD, 16),
                StyleUtils.TITLE_COLOR
        ));

        JPanel buttonPanel = createButtonPanel();

        mainPanel.add(tableScrollPane, BorderLayout.CENTER);
        mainPanel.add(buttonPanel, BorderLayout.SOUTH);

        add(mainPanel);

        loadWarehouses();
        setVisible(true);
    }

    private JPanel createButtonPanel() {
        JPanel buttonPanel = new JPanel(new GridLayout(1, 5, 10, 10));
        buttonPanel.setBorder(BorderFactory.createEmptyBorder(10, 20, 10, 20));

        JButton addButton = StyleUtils.createStyledButton("Добавить склад");
        JButton editButton = StyleUtils.createStyledButton("Редактировать склад");
        JButton deleteButton = StyleUtils.createStyledButton("Удалить склад");
        JButton refreshButton = StyleUtils.createStyledButton("Обновить");
        JButton backButton = StyleUtils.createStyledButton("Назад");

        addButton.addActionListener(e -> showAddWarehouseForm());
        editButton.addActionListener(e -> showEditWarehouseForm());
        deleteButton.addActionListener(e -> deleteWarehouse());
        refreshButton.addActionListener(e -> loadWarehouses());
        backButton.addActionListener(e -> {
            dispose();
            new AdminMainForm(clientConnection, 0).show();
        });

        buttonPanel.add(addButton);
        buttonPanel.add(editButton);
        buttonPanel.add(deleteButton);
        buttonPanel.add(refreshButton);
        buttonPanel.add(backButton);

        return buttonPanel;
    }

    private JTable createStyledTable() {
        JTable table = new JTable();
        table.setFont(new Font("Arial", Font.PLAIN, 16));
        table.setRowHeight(30);
        table.getTableHeader().setFont(new Font("Arial", Font.BOLD, 18));
        table.getTableHeader().setBackground(StyleUtils.HEADER_BG_COLOR);
        table.getTableHeader().setForeground(StyleUtils.HEADER_TEXT_COLOR);
        table.setSelectionBackground(StyleUtils.SELECTION_BG_COLOR);
        table.setSelectionForeground(StyleUtils.SELECTION_TEXT_COLOR);
        table.setGridColor(StyleUtils.GRID_COLOR);
        table.setBackground(Color.WHITE);
        table.setForeground(Color.BLACK);
        table.setShowGrid(true);
        table.setAutoCreateRowSorter(true);
        return table;
    }

    public void loadWarehouses() {
        clientConnection.send("VIEW_WAREHOUSES");

        try {
            int warehouseCount = Integer.parseInt(clientConnection.receive());
            List<Object[]> rowData = new ArrayList<>();
            for (int i = 0; i < warehouseCount; i++) {
                String[] data = clientConnection.receive().split(",");
                rowData.add(new Object[]{data[0], data[1], data[2]});
            }

            String[] columnNames = {"ID", "Название", "Адрес"};
            DefaultTableModel model = new DefaultTableModel(rowData.toArray(new Object[0][]), columnNames);
            warehouseTable.setModel(model);

            TableRowSorter<DefaultTableModel> sorter = new TableRowSorter<>(model);
            warehouseTable.setRowSorter(sorter);

        } catch (NumberFormatException e) {
            JOptionPane.showMessageDialog(this, "Ошибка загрузки складов. Неверный формат ответа сервера!",
                    "Ошибка", JOptionPane.ERROR_MESSAGE);
        }
    }

    private void showAddWarehouseForm() {
        new WarehouseForm(clientConnection, "ADD", this, null, null, null).setVisible(true);
    }
    public JTable getWarehouseTable() {
        return warehouseTable;
    }


    private void showEditWarehouseForm() {
        int selectedRow = warehouseTable.getSelectedRow();
        if (selectedRow == -1) {
            JOptionPane.showMessageDialog(this, "Пожалуйста, выберите склад для редактирования!");
            return;
        }

        String warehouseId = warehouseTable.getValueAt(selectedRow, 0).toString();
        String name = warehouseTable.getValueAt(selectedRow, 1).toString();
        String address = warehouseTable.getValueAt(selectedRow, 2).toString();

        new WarehouseForm(clientConnection, "EDIT", this, warehouseId, name, address).setVisible(true);
    }

    private void deleteWarehouse() {
        int selectedRow = warehouseTable.getSelectedRow();
        if (selectedRow == -1) {
            JOptionPane.showMessageDialog(this, "Пожалуйста, выберите склад для удаления!");
            return;
        }

        String warehouseId = warehouseTable.getValueAt(selectedRow, 0).toString();

        int confirmation = JOptionPane.showConfirmDialog(this, "Вы уверены, что хотите удалить склад?",
                "Подтверждение", JOptionPane.YES_NO_OPTION);
        if (confirmation == JOptionPane.YES_OPTION) {
            clientConnection.send("DELETE_WAREHOUSE");
            clientConnection.send(warehouseId);

            String response = clientConnection.receive();
            if ("SUCCESS".equals(response)) {
                JOptionPane.showMessageDialog(this, "Склад успешно удалён!");
                loadWarehouses();
            } else {
                JOptionPane.showMessageDialog(this, "Ошибка удаления склада!");
            }
        }
    }
}
