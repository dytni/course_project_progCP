package com.hatsukha.nikolai.client.productForms;

import com.hatsukha.nikolai.client.ClientConnection;
import com.hatsukha.nikolai.client.utils.StyleUtils;

import javax.swing.*;
import java.awt.*;

public class ProductForm extends JFrame {
    private final ClientConnection clientConnection;
    private final ProductCrudForm parentForm;

    public ProductForm(ClientConnection clientConnection, String mode, ProductCrudForm parentForm,
                       String productId, String name, String description, String category, String weight, String volume, String location) {
        this.clientConnection = clientConnection;
        // "ADD" или "EDIT"
        this.parentForm = parentForm;

        setTitle(mode.equals("ADD") ? "Добавить продукт" : "Редактировать продукт");
        setSize(500, 500);
        setDefaultCloseOperation(DISPOSE_ON_CLOSE);
        setLocationRelativeTo(parentForm);

        JPanel panel = new JPanel(new GridBagLayout());
        panel.setBorder(BorderFactory.createEmptyBorder(20, 20, 20, 20));

        GridBagConstraints gbc = new GridBagConstraints();
        gbc.insets = new Insets(10, 10, 10, 10);
        gbc.fill = GridBagConstraints.HORIZONTAL;

        // Поля ввода
        JLabel nameLabel = StyleUtils.createStyledLabel("Название:");
        JTextField nameField = StyleUtils.createStyledTextField(name);

        JLabel descriptionLabel = StyleUtils.createStyledLabel("Описание:");
        JTextField descriptionField = StyleUtils.createStyledTextField(description);

        JLabel categoryLabel = StyleUtils.createStyledLabel("Категория:");
        JTextField categoryField = StyleUtils.createStyledTextField(category);

        JLabel weightLabel = StyleUtils.createStyledLabel("Вес:");
        JTextField weightField = StyleUtils.createStyledTextField(weight);

        JLabel volumeLabel = StyleUtils.createStyledLabel("Объём:");
        JTextField volumeField = StyleUtils.createStyledTextField(volume);

        // Кнопки
        JButton saveButton = StyleUtils.createStyledButton(mode.equals("ADD") ? "Добавить" : "Сохранить");
        JButton cancelButton = StyleUtils.createStyledButton("Отмена");

        // Расположение элементов
        gbc.gridx = 0;
        gbc.gridy = 0;
        panel.add(nameLabel, gbc);
        gbc.gridx = 1;
        panel.add(nameField, gbc);

        gbc.gridx = 0;
        gbc.gridy = 1;
        panel.add(descriptionLabel, gbc);
        gbc.gridx = 1;
        panel.add(descriptionField, gbc);

        gbc.gridx = 0;
        gbc.gridy = 2;
        panel.add(categoryLabel, gbc);
        gbc.gridx = 1;
        panel.add(categoryField, gbc);

        gbc.gridx = 0;
        gbc.gridy = 3;
        panel.add(weightLabel, gbc);
        gbc.gridx = 1;
        panel.add(weightField, gbc);

        gbc.gridx = 0;
        gbc.gridy = 4;
        panel.add(volumeLabel, gbc);
        gbc.gridx = 1;
        panel.add(volumeField, gbc);

        gbc.gridx = 0;
        gbc.gridy = 5;
        gbc.gridwidth = 2;
        panel.add(saveButton, gbc);

        gbc.gridy = 6;
        panel.add(cancelButton, gbc);

        // Обработка событий кнопок
        saveButton.addActionListener(e -> {
            String productName = nameField.getText().trim();
            String productDescription = descriptionField.getText().trim();
            String productCategory = categoryField.getText().trim();
            double productWeight;
            double productVolume;

            // Проверка ввода
            if (productName.isEmpty() || productDescription.isEmpty() || productCategory.isEmpty()) {
                JOptionPane.showMessageDialog(this, "Все поля должны быть заполнены!", "Ошибка", JOptionPane.ERROR_MESSAGE);
                return;
            }

            try {
                productWeight = Double.parseDouble(weightField.getText().trim());
                productVolume = Double.parseDouble(volumeField.getText().trim());
            } catch (NumberFormatException ex) {
                JOptionPane.showMessageDialog(this, "Вес и объём должны быть числами!", "Ошибка", JOptionPane.ERROR_MESSAGE);
                return;
            }

            if (mode.equals("ADD")) {
                addProduct(productName, productDescription, productCategory, productWeight, productVolume);
            } else {
                editProduct(productId, productName, productDescription, productCategory, productWeight, productVolume, location);
            }
        });
        cancelButton.addActionListener(e -> dispose());
        add(panel);
        setVisible(true);
    }

    private void addProduct(String name, String description, String category, double weight, double volume) {
        clientConnection.send("ADD_PRODUCT");
        clientConnection.send(name);
        clientConnection.send(description);
        clientConnection.send(category);
        clientConnection.send(String.valueOf(weight));
        clientConnection.send(String.valueOf(volume));

        String response = clientConnection.receive();
        if ("SUCCESS".equals(response)) {
            JOptionPane.showMessageDialog(this, "Продукт успешно добавлен!", "Успех", JOptionPane.INFORMATION_MESSAGE);
            parentForm.loadProducts();
            dispose();
        } else {
            JOptionPane.showMessageDialog(this, "Ошибка при добавлении продукта!", "Ошибка", JOptionPane.ERROR_MESSAGE);
        }
    }

    private void editProduct(String id, String name, String description, String category, double weight, double volume, String location) {
        clientConnection.send("UPDATE_PRODUCT");
        clientConnection.send(id);
        clientConnection.send(name);
        clientConnection.send(description);
        clientConnection.send(category);
        clientConnection.send(String.valueOf(weight));
        clientConnection.send(String.valueOf(volume));
        clientConnection.send(location);

        String response = clientConnection.receive();
        if ("SUCCESS".equals(response)) {
            JOptionPane.showMessageDialog(this, "Продукт успешно обновлен!", "Успех", JOptionPane.INFORMATION_MESSAGE);
            parentForm.loadProducts();
            dispose();
        } else {
            JOptionPane.showMessageDialog(this, "Ошибка при обновлении продукта!", "Ошибка", JOptionPane.ERROR_MESSAGE);
        }
    }
}
