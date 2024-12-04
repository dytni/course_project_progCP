package com.hatsukha.nikolai.client.productForms;

import com.hatsukha.nikolai.client.ClientConnection;

import javax.swing.*;
import java.awt.*;

public class ProductForm extends JFrame {
    private final ClientConnection clientConnection;
    private final String mode; // "ADD" или "EDIT"
    private final ProductCrudForm parentForm;
    private final String productId;

    public ProductForm(ClientConnection clientConnection, String mode, ProductCrudForm parentForm,
                       String productId, String name, String description, String category, String weight, String volume, String location) {
        this.clientConnection = clientConnection;
        this.mode = mode;
        this.parentForm = parentForm;
        this.productId = productId;

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
        JLabel nameLabel = createStyledLabel("Название:");
        JTextField nameField = createStyledTextField(name);

        JLabel descriptionLabel = createStyledLabel("Описание:");
        JTextField descriptionField = createStyledTextField(description);

        JLabel categoryLabel = createStyledLabel("Категория:");
        JTextField categoryField = createStyledTextField(category);

        JLabel weightLabel = createStyledLabel("Вес:");
        JTextField weightField = createStyledTextField(weight);

        JLabel volumeLabel = createStyledLabel("Объём:");
        JTextField volumeField = createStyledTextField(volume);

        // Кнопки
        JButton saveButton = createStyledButton(mode.equals("ADD") ? "Добавить" : "Сохранить");
        JButton cancelButton = createStyledButton("Отмена");

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

        // Добавление панели и отображение
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

    // Создание стилизованной метки
    private JLabel createStyledLabel(String text) {
        JLabel label = new JLabel(text);
        label.setFont(new Font("Arial", Font.BOLD, 16));
        label.setForeground(new Color(135, 206, 250)); // Голубой цвет текста
        return label;
    }

    // Создание стилизованного текстового поля
    private JTextField createStyledTextField(String text) {
        JTextField textField = new JTextField(text, 15);
        textField.setFont(new Font("Arial", Font.PLAIN, 16));
        textField.setBackground(Color.WHITE); // Белый фон
        textField.setForeground(Color.BLACK); // Чёрный текст
        textField.setCaretColor(Color.BLACK); // Чёрный курсор
        textField.setBorder(BorderFactory.createLineBorder(new Color(135, 206, 250), 2)); // Голубая рамка
        return textField;
    }

    // Создание стилизованной кнопки
    private JButton createStyledButton(String text) {
        JButton button = new JButton(text);
        button.setFont(new Font("Arial", Font.BOLD, 18));
        button.setForeground(Color.BLACK);
        button.setBackground(new Color(135, 206, 250)); // Голубой фон
        button.setFocusPainted(false);
        button.setCursor(new Cursor(Cursor.HAND_CURSOR));
        button.setBorder(BorderFactory.createEmptyBorder(10, 20, 10, 20));

        button.addMouseListener(new java.awt.event.MouseAdapter() {
            public void mouseEntered(java.awt.event.MouseEvent evt) {
                button.setBackground(new Color(173, 216, 230)); // Светло-голубой при наведении
            }

            public void mouseExited(java.awt.event.MouseEvent evt) {
                button.setBackground(new Color(135, 206, 250)); // Голубой фон
            }
        });

        return button;
    }
}
