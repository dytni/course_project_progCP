package com.hatsukha.nikolai.client.operationForms;

import com.hatsukha.nikolai.client.ClientConnection;
import com.hatsukha.nikolai.client.utils.StyleUtils;

import javax.swing.*;
import java.awt.*;

public class AddOperationForm extends JFrame {
    private final ClientConnection clientConnection;

    public AddOperationForm(ClientConnection clientConnection, OperationCrudForm parentForm) {
        this.clientConnection = clientConnection;

        setTitle("Добавить операцию");
        setSize(500, 400);
        setDefaultCloseOperation(DISPOSE_ON_CLOSE);
        setLocationRelativeTo(parentForm);

        JPanel panel = new JPanel(new GridLayout(6, 2, 10, 10));
        panel.setBorder(BorderFactory.createEmptyBorder(20, 20, 20, 20));

        JLabel productLabel = StyleUtils.createStyledLabel("Выберите продукт:");
        JComboBox<String> productComboBox = new JComboBox<>();

        JLabel warehouseLabel = StyleUtils.createStyledLabel("Выберите склад:");
        JComboBox<String> warehouseComboBox = new JComboBox<>();

        JLabel operationTypeLabel = StyleUtils.createStyledLabel("Тип операции:");
        JComboBox<String> operationTypeComboBox = new JComboBox<>(new String[]{"приём", "перемещение", "списание"});

        JLabel quantityLabel = StyleUtils.createStyledLabel("Количество:");
        JTextField quantityField = new JTextField();

        JButton addButton = StyleUtils.createStyledButton("Добавить");
        JButton cancelButton = StyleUtils.createStyledButton("Отмена");

        loadProducts(productComboBox);

        loadWarehouses(warehouseComboBox);

        addButton.addActionListener(e -> {
            try {
                String selectedProduct = (String) productComboBox.getSelectedItem();
                String selectedWarehouse = (String) warehouseComboBox.getSelectedItem();

                if (selectedProduct == null || selectedWarehouse == null) {
                    JOptionPane.showMessageDialog(this, "Выберите продукт и склад!", "Ошибка", JOptionPane.ERROR_MESSAGE);
                    return;
                }

                int productId = Integer.parseInt(selectedProduct.split(" ")[1]);
                int warehouseId = Integer.parseInt(selectedWarehouse.split(" ")[1]);
                int quantity = Integer.parseInt(quantityField.getText().trim());
                String operationType = (String) operationTypeComboBox.getSelectedItem();

                clientConnection.send("ADD_OPERATION");
                clientConnection.send(String.valueOf(productId));
                clientConnection.send(String.valueOf(warehouseId));
                clientConnection.send(operationType);
                clientConnection.send(String.valueOf(quantity));

                String response = clientConnection.receive();
                if ("SUCCESS".equals(response)) {
                    JOptionPane.showMessageDialog(this, "Операция успешно добавлена!");
                    parentForm.loadOperations(); // Обновить таблицу операций
                    dispose();
                } else {
                    JOptionPane.showMessageDialog(this, "Ошибка добавления операции!", "Ошибка", JOptionPane.ERROR_MESSAGE);
                }
            } catch (NumberFormatException ex) {
                JOptionPane.showMessageDialog(this, "Введите корректное количество!", "Ошибка", JOptionPane.ERROR_MESSAGE);
            }
        });

        cancelButton.addActionListener(e -> dispose());

        panel.add(productLabel);
        panel.add(productComboBox);
        panel.add(warehouseLabel);
        panel.add(warehouseComboBox);
        panel.add(operationTypeLabel);
        panel.add(operationTypeComboBox);
        panel.add(quantityLabel);
        panel.add(quantityField);
        panel.add(addButton);
        panel.add(cancelButton);

        add(panel);
        setVisible(true);
    }

    private void loadProducts(JComboBox<String> productComboBox) {
        clientConnection.send("VIEW_PRODUCTS");
        try {
            int productCount = Integer.parseInt(clientConnection.receive());
            for (int i = 0; i < productCount; i++) {
                String[] productData = clientConnection.receive().split(",");
                String productItem = "ID: " + productData[0] + " - " + productData[1];
                productComboBox.addItem(productItem);
            }
        } catch (NumberFormatException e) {
            JOptionPane.showMessageDialog(this, "Ошибка загрузки продуктов!", "Ошибка", JOptionPane.ERROR_MESSAGE);
        }
    }

    private void loadWarehouses(JComboBox<String> warehouseComboBox) {
        clientConnection.send("VIEW_WAREHOUSES");
        try {
            int warehouseCount = Integer.parseInt(clientConnection.receive());
            for (int i = 0; i < warehouseCount; i++) {
                String[] warehouseData = clientConnection.receive().split(",");
                String warehouseItem = "ID: " + warehouseData[0] + " - " + warehouseData[1];
                warehouseComboBox.addItem(warehouseItem);
            }
        } catch (NumberFormatException e) {
            JOptionPane.showMessageDialog(this, "Ошибка загрузки складов!", "Ошибка", JOptionPane.ERROR_MESSAGE);
        }
    }
}
