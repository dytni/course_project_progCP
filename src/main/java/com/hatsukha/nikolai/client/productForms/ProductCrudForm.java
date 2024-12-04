package com.hatsukha.nikolai.client.productForms;

import com.hatsukha.nikolai.client.ClientConnection;
import com.hatsukha.nikolai.client.clientForms.AdminMainForm;
import com.hatsukha.nikolai.client.clientForms.UserMainForm;
import com.hatsukha.nikolai.client.utils.StyleUtils;

import javax.swing.*;
import javax.swing.table.DefaultTableModel;
import javax.swing.table.TableRowSorter;
import java.awt.*;
import java.util.ArrayList;

public class ProductCrudForm extends JFrame {
    private final ClientConnection clientConnection;
    private final ProductTablePanel productTablePanel;
    private final String role;
    private final int userId;
    private TableRowSorter<DefaultTableModel> tableRowSorter;

    public ProductCrudForm(ClientConnection clientConnection, String role, int userId) {
        this.userId = userId;
        this.role = role;
        this.clientConnection = clientConnection;

        setTitle("Управление продуктами");
        setSize(1000, 700);
        setDefaultCloseOperation(EXIT_ON_CLOSE);
        setLocationRelativeTo(null);

        productTablePanel = new ProductTablePanel(clientConnection);
        JPanel buttonPanel = createButtonPanel();

        add(productTablePanel, BorderLayout.CENTER);
        add(buttonPanel, BorderLayout.SOUTH);

        setVisible(true);
    }


    private JPanel createButtonPanel() {
        JPanel buttonPanel = new JPanel(new GridLayout(1, 5, 10, 10));
        buttonPanel.setBorder(BorderFactory.createEmptyBorder(10, 20, 10, 20));

        JButton addButton = StyleUtils.createStyledButton("Добавить продукт");
        JButton editButton = StyleUtils.createStyledButton("Редактировать продукт");
        JButton deleteButton = StyleUtils.createStyledButton("Удалить продукт");
        JButton setLocate = StyleUtils.createStyledButton("Установить локацию");
        JButton refreshButton = StyleUtils.createStyledButton("Обновить");
        JButton backButton = StyleUtils.createStyledButton("Назад");

        addButton.addActionListener(e -> showAddProductForm());
        editButton.addActionListener(e -> showEditProductForm());
        deleteButton.addActionListener(e -> deleteProduct());
        setLocate.addActionListener(e -> changeLocate());
        refreshButton.addActionListener(e -> productTablePanel.loadProducts());
        backButton.addActionListener(e -> {
            if (role.equals("ADMIN")) {
                dispose();
                new AdminMainForm(clientConnection, userId).show();
            } else {
                dispose();
                new UserMainForm(clientConnection, userId).show();
            }
        });

        buttonPanel.add(addButton);
        buttonPanel.add(editButton);
        buttonPanel.add(deleteButton);
        buttonPanel.add(setLocate);
        buttonPanel.add(refreshButton);
        buttonPanel.add(backButton);
        return buttonPanel;
    }

    public void loadProducts() {
        productTablePanel.loadProducts();
    }

    private void changeLocate() {
        int selectedRow = productTablePanel.getProductTable().getSelectedRow();
        if (selectedRow == -1) {
            JOptionPane.showMessageDialog(this, "Пожалуйста, выберите продукт для изменения локации!", "Ошибка", JOptionPane.ERROR_MESSAGE);
            return;
        }

        String productId = productTablePanel.getProductTable().getValueAt(selectedRow, 0).toString();
        String productName = productTablePanel.getProductTable().getValueAt(selectedRow, 1).toString();

        // Вызов формы для выбора склада
        new SelectLocationForm(clientConnection, productId, productName, productTablePanel).setVisible(true);
    }

    private void showAddProductForm() {
        new ProductForm(clientConnection, "ADD", this, null, null, null, null, null, null, "Не указанно").show();
    }

    private void showEditProductForm() {
        JTable productTable = productTablePanel.getProductTable();
        int selectedRow = productTable.getSelectedRow();
        if (selectedRow == -1) {
            JOptionPane.showMessageDialog(this, "Пожалуйста, выберите продукт для редактирования!");
            return;
        }

        String productId = productTable.getValueAt(selectedRow, 0).toString();
        String name = productTable.getValueAt(selectedRow, 1).toString();
        String description = productTable.getValueAt(selectedRow, 2).toString();
        String category = productTable.getValueAt(selectedRow, 3).toString();
        String weight = productTable.getValueAt(selectedRow, 4).toString();
        String volume = productTable.getValueAt(selectedRow, 5).toString();
        String location = productTable.getValueAt(selectedRow, 6).toString();

        new ProductForm(clientConnection, "EDIT", this, productId, name, description, category, weight, volume, location).show();
    }

    private void deleteProduct() {
        JTable productTable = productTablePanel.getProductTable();
        int selectedRow = productTable.getSelectedRow();
        if (selectedRow == -1) {
            JOptionPane.showMessageDialog(this, "Пожалуйста, выберите продукт для удаления!");
            return;
        }

        String productId = productTable.getValueAt(selectedRow, 0).toString();

        int confirmation = JOptionPane.showConfirmDialog(this, "Вы уверены, что хотите удалить продукт?", "Подтверждение", JOptionPane.YES_NO_OPTION);
        if (confirmation == JOptionPane.YES_OPTION) {
            clientConnection.send("DELETE_PRODUCT");
            clientConnection.send(productId);

            String response = clientConnection.receive();
            if ("SUCCESS".equals(response)) {
                JOptionPane.showMessageDialog(this, "Продукт успешно удален!");
                productTablePanel.loadProducts();
            } else {
                JOptionPane.showMessageDialog(this, "Ошибка удаления продукта!");
            }
        }
    }
}
