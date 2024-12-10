package com.hatsukha.nikolai.client.warehouseForms;

import com.hatsukha.nikolai.client.ClientConnection;
import com.hatsukha.nikolai.client.utils.StyleUtils;

import javax.swing.*;
import java.awt.*;

public class WarehouseForm extends JFrame {
    private final ClientConnection clientConnection;
    private final WarehouseCrudForm parentForm;

    public WarehouseForm(ClientConnection clientConnection, String mode, WarehouseCrudForm parentForm,
                         String warehouseId, String name, String address) {
        this.clientConnection = clientConnection;
        this.parentForm = parentForm;

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
        JLabel nameLabel = StyleUtils.createStyledLabel("Название:");
        JTextField nameField = StyleUtils.createStyledTextField(name);

        JLabel addressLabel = StyleUtils.createStyledLabel("Адрес:");
        JTextField addressField = StyleUtils.createStyledTextField(address);

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
        if (!isUniqueLocally(id, name, address)) {
            JOptionPane.showMessageDialog(this, "Склад с таким названием или адресом уже существует!", "Ошибка", JOptionPane.ERROR_MESSAGE);
            return;
        }
        clientConnection.send("UPDATE_WAREHOUSE");
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

    private boolean isUniqueLocally(String id, String name, String address) {
        JTable warehouseTable = parentForm.getWarehouseTable();
        for (int i = 0; i < warehouseTable.getRowCount(); i++) {
            String existingId = warehouseTable.getValueAt(i, 0).toString();
            String existingName = warehouseTable.getValueAt(i, 1).toString();
            String existingAddress = warehouseTable.getValueAt(i, 2).toString();

            if (!existingId.equals(id) && (existingName.equalsIgnoreCase(name) || existingAddress.equalsIgnoreCase(address))) {
                return false;
            }
        }
        return true;
    }
}
