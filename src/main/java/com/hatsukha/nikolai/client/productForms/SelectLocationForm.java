package com.hatsukha.nikolai.client.productForms;

import com.hatsukha.nikolai.client.ClientConnection;

import javax.swing.*;
import java.awt.*;
import java.util.ArrayList;
import java.util.List;

public class SelectLocationForm extends JFrame {
    private final ClientConnection clientConnection;
    private final String productId;
    private final String productName;
    private final ProductTablePanel productTablePanel;

    public SelectLocationForm(ClientConnection clientConnection, String productId, String productName, ProductTablePanel productTablePanel) {
        this.clientConnection = clientConnection;
        this.productId = productId;
        this.productName = productName;
        this.productTablePanel = productTablePanel;

        setTitle("Установить локацию для продукта: " + productName);
        setSize(400, 300);
        setDefaultCloseOperation(DISPOSE_ON_CLOSE);
        setLocationRelativeTo(null);

        JPanel panel = new JPanel(new BorderLayout());
        panel.setBorder(BorderFactory.createEmptyBorder(20, 20, 20, 20));

        // Список складов
        JComboBox<String> warehouseComboBox = new JComboBox<>(loadWarehouses());
        warehouseComboBox.setFont(new Font("Arial", Font.PLAIN, 16));

        JLabel warehouseLabel = new JLabel("Выберите склад:");
        warehouseLabel.setFont(new Font("Arial", Font.BOLD, 16));

        JButton saveButton = new JButton("Сохранить");
        saveButton.setFont(new Font("Arial", Font.BOLD, 16));
        saveButton.addActionListener(e -> {
            String selectedWarehouse = (String) warehouseComboBox.getSelectedItem();
            if (selectedWarehouse != null) {
                setLocationForProduct(selectedWarehouse.split(" - ")[1]); // Извлечение ID склада
            } else {
                JOptionPane.showMessageDialog(this, "Пожалуйста, выберите склад!", "Ошибка", JOptionPane.ERROR_MESSAGE);
            }
        });

        JButton cancelButton = new JButton("Отмена");
        cancelButton.setFont(new Font("Arial", Font.BOLD, 16));
        cancelButton.addActionListener(e -> dispose());

        JPanel formPanel = new JPanel(new GridLayout(2, 1, 10, 10));
        formPanel.add(warehouseLabel);
        formPanel.add(warehouseComboBox);

        JPanel buttonPanel = new JPanel(new GridLayout(1, 2, 10, 10));
        buttonPanel.add(saveButton);
        buttonPanel.add(cancelButton);

        panel.add(formPanel, BorderLayout.CENTER);
        panel.add(buttonPanel, BorderLayout.SOUTH);

        add(panel);
    }

    private String[] loadWarehouses() {
        clientConnection.send("VIEW_WAREHOUSES");
        int warehouseCount = Integer.parseInt(clientConnection.receive());
        List<String> warehouses = new ArrayList<>();
        for (int i = 0; i < warehouseCount; i++) {
            String[] data = clientConnection.receive().split(",");
            warehouses.add(data[0] + " - " + data[1]); // Формат: "ID - Location"
        }
        return warehouses.toArray(new String[0]);
    }

    private void setLocationForProduct(String warehouseId) {
        clientConnection.send("SET_LOCATION");
        clientConnection.send(productId);
        clientConnection.send(warehouseId);

        String response = clientConnection.receive();
        if ("SUCCESS".equals(response)) {
            JOptionPane.showMessageDialog(this, "Локация успешно установлена!", "Успех", JOptionPane.INFORMATION_MESSAGE);
            productTablePanel.loadProducts();
            dispose();
        } else {
            JOptionPane.showMessageDialog(this, "Ошибка при установке локации!", "Ошибка", JOptionPane.ERROR_MESSAGE);
        }
    }
}
