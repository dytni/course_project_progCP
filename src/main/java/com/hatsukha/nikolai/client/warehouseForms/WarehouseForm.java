package com.hatsukha.nikolai.client.warehouseForms;

import com.hatsukha.nikolai.client.ClientConnection;

import javax.swing.*;
import javax.swing.table.DefaultTableModel;
import javax.swing.table.TableRowSorter;
import java.awt.*;

public class WarehouseForm extends JFrame {
    private final ClientConnection clientConnection;
    private final String mode; // "ADD" или "EDIT"
    private final WarehouseCrudForm parentForm;
    private final String warehouseId;

    public WarehouseForm(ClientConnection clientConnection, String mode, WarehouseCrudForm parentForm,
                         String warehouseId, String name, String address) {
        this.clientConnection = clientConnection;
        this.mode = mode;
        this.parentForm = parentForm;
        this.warehouseId = warehouseId;

        setTitle(mode.equals("ADD") ? "Добавить склад" : "Редактировать склад");
        setSize(400, 300);
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

        JLabel addressLabel = createStyledLabel("Адрес:");
        JTextField addressField = createStyledTextField(address);

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
        panel.add(addressLabel, gbc);
        gbc.gridx = 1;
        panel.add(addressField, gbc);

        gbc.gridx = 0;
        gbc.gridy = 2;
        gbc.gridwidth = 2;
        panel.add(saveButton, gbc);

        gbc.gridy = 3;
        panel.add(cancelButton, gbc);

        // Обработка событий кнопок
        saveButton.addActionListener(e -> {
            String warehouseName = nameField.getText().trim();
            String warehouseAddress = addressField.getText().trim();

            // Проверка ввода
            if (warehouseName.isEmpty() || warehouseAddress.isEmpty()) {
                JOptionPane.showMessageDialog(this, "Все поля должны быть заполнены!", "Ошибка", JOptionPane.ERROR_MESSAGE);
                return;
            }

            if (mode.equals("ADD")) {
                addWarehouse(warehouseName, warehouseAddress);
            } else {
                editWarehouse(warehouseId, warehouseName, warehouseAddress);
            }
        });

        cancelButton.addActionListener(e -> dispose());

        // Добавление панели и отображение
        add(panel);
        setVisible(true);
    }

    private void addWarehouse(String name, String address) {
        clientConnection.send("ADD_WAREHOUSE");
        clientConnection.send(name);
        clientConnection.send(address);

        String response = clientConnection.receive();
        if ("SUCCESS".equals(response)) {
            JOptionPane.showMessageDialog(this, "Склад успешно добавлен!", "Успех", JOptionPane.INFORMATION_MESSAGE);
            parentForm.loadWarehouses();
            dispose();
        } else {
            JOptionPane.showMessageDialog(this, "Ошибка при добавлении склада!", "Ошибка", JOptionPane.ERROR_MESSAGE);
        }
    }

    private void editWarehouse(String id, String name, String address) {
        clientConnection.send("EDIT_WAREHOUSE");
        clientConnection.send(id);
        clientConnection.send(name);
        clientConnection.send(address);

        String response = clientConnection.receive();
        if ("SUCCESS".equals(response)) {
            JOptionPane.showMessageDialog(this, "Склад успешно обновлен!", "Успех", JOptionPane.INFORMATION_MESSAGE);
            parentForm.loadWarehouses();
            dispose();
        } else {
            JOptionPane.showMessageDialog(this, "Ошибка при обновлении склада!", "Ошибка", JOptionPane.ERROR_MESSAGE);
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
