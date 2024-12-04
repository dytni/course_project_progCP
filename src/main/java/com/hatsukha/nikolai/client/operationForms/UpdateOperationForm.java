package com.hatsukha.nikolai.client.operationForms;

import com.hatsukha.nikolai.client.ClientConnection;

import javax.swing.*;
import java.awt.*;
import java.awt.event.MouseAdapter;
import java.awt.event.MouseEvent;

public class UpdateOperationForm extends JFrame {
    private final ClientConnection clientConnection;
    private final int operationId;
    private final GetOperationsByUserForm parentForm;
    private final OperationCrudForm parentCrudForm;
    private final int userId;

    public UpdateOperationForm(ClientConnection clientConnection, int operationId, int userId, GetOperationsByUserForm parentForm,
                               String productId, String warehouseId, String operationType, String quantity) {
        this.clientConnection = clientConnection;
        this.operationId = operationId;
        this.parentForm = parentForm;
        this.userId = userId;

        setTitle("Редактировать операцию");
        setSize(500, 400);
        setDefaultCloseOperation(DISPOSE_ON_CLOSE);
        setLocationRelativeTo(parentForm);

        JPanel panel = new JPanel(new GridLayout(6, 2, 10, 10));
        panel.setBorder(BorderFactory.createEmptyBorder(20, 20, 20, 20));

        JLabel productLabel = new JLabel("ID продукта:");
        JTextField productField = new JTextField(productId);

        JLabel warehouseLabel = new JLabel("ID склада:");
        JTextField warehouseField = new JTextField(warehouseId);

        JLabel operationTypeLabel = new JLabel("Тип операции:");
        JComboBox<String> operationTypeComboBox = new JComboBox<>(new String[]{"приём", "перемещение", "списание"});
     //   operationTypeComboBox.setSelectedItem(operationType);

        JLabel quantityLabel = new JLabel("Количество:");
        JTextField quantityField = new JTextField(quantity);

        JButton updateButton = createStyledButton("Обновить");
        JButton cancelButton = createStyledButton("Отмена");

        updateButton.addActionListener(e -> {
            try {
                int updatedProductId = Integer.parseInt(productField.getText().trim());
                int updatedWarehouseId = Integer.parseInt(warehouseField.getText().trim());
                int updatedQuantity = Integer.parseInt(quantityField.getText().trim());
                String updatedOperationType = (String) operationTypeComboBox.getSelectedItem();

                clientConnection.send("UPDATE_OPERATION");
                clientConnection.send(String.valueOf(operationId));
                clientConnection.send(String.valueOf(updatedProductId));
                clientConnection.send(String.valueOf(userId));
                clientConnection.send(String.valueOf(updatedWarehouseId));
                clientConnection.send(updatedOperationType);
                clientConnection.send(String.valueOf(updatedQuantity));

                String response = clientConnection.receive();
                if ("SUCCESS".equals(response)) {
                    JOptionPane.showMessageDialog(this, "Операция успешно обновлена!");
                    parentForm.loadOperationsByUser();
                    dispose();
                } else {
                    JOptionPane.showMessageDialog(this, "Ошибка при обновлении операции!", "Ошибка", JOptionPane.ERROR_MESSAGE);
                }
            } catch (NumberFormatException ex) {
                JOptionPane.showMessageDialog(this, "Введите корректные данные!", "Ошибка", JOptionPane.ERROR_MESSAGE);
            }
        });

        cancelButton.addActionListener(e -> dispose());

        panel.add(productLabel);
        panel.add(productField);
        panel.add(warehouseLabel);
        panel.add(warehouseField);
        panel.add(operationTypeLabel);
        panel.add(operationTypeComboBox);
        panel.add(quantityLabel);
        panel.add(quantityField);
        panel.add(updateButton);
        panel.add(cancelButton);

        add(panel);
        setVisible(true);
        parentCrudForm = null;
    }
    public UpdateOperationForm(ClientConnection clientConnection, int operationId, int userId,
                               OperationCrudForm parentForm, String productId,
                               String warehouseId, String operationType, String quantity) {
        this.clientConnection = clientConnection;
        this.operationId = operationId;
        this.parentCrudForm = parentForm;
        this.userId = userId;

        setTitle("Редактировать операцию");
        setSize(500, 400);
        setDefaultCloseOperation(DISPOSE_ON_CLOSE);
        setLocationRelativeTo(parentForm);

        JPanel panel = new JPanel(new GridLayout(6, 2, 10, 10));
        panel.setBorder(BorderFactory.createEmptyBorder(20, 20, 20, 20));

        JLabel productLabel = new JLabel("ID продукта:");
        JTextField productField = new JTextField(productId);

        JLabel warehouseLabel = new JLabel("ID склада:");
        JTextField warehouseField = new JTextField(warehouseId);

        JLabel operationTypeLabel = new JLabel("Тип операции:");
        JComboBox<String> operationTypeComboBox = new JComboBox<>(new String[]{"приём", "перемещение", "списание"});
        operationTypeComboBox.setSelectedItem(operationType);

        JLabel quantityLabel = new JLabel("Количество:");
        JTextField quantityField = new JTextField(quantity);

        JButton updateButton = createStyledButton("Обновить");
        JButton cancelButton = createStyledButton("Отмена");

        updateButton.addActionListener(e -> {
            try {
                int updatedProductId = Integer.parseInt(productField.getText().trim());
                int updatedWarehouseId = Integer.parseInt(warehouseField.getText().trim());
                int updatedQuantity = Integer.parseInt(quantityField.getText().trim());
                String updatedOperationType = (String) operationTypeComboBox.getSelectedItem();

                clientConnection.send("UPDATE_OPERATION");
                clientConnection.send(String.valueOf(operationId));
                clientConnection.send(String.valueOf(updatedProductId));
                clientConnection.send(String.valueOf(userId));
                clientConnection.send(String.valueOf(updatedWarehouseId));
                clientConnection.send(updatedOperationType);
                clientConnection.send(String.valueOf(updatedQuantity));

                String response = clientConnection.receive();
                if ("SUCCESS".equals(response)) {
                    JOptionPane.showMessageDialog(this, "Операция успешно обновлена!");
                    parentForm.loadOperations();
                    dispose();
                } else {
                    JOptionPane.showMessageDialog(this, "Ошибка при обновлении операции!", "Ошибка", JOptionPane.ERROR_MESSAGE);
                }
            } catch (NumberFormatException ex) {
                JOptionPane.showMessageDialog(this, "Введите корректные данные!", "Ошибка", JOptionPane.ERROR_MESSAGE);
            }
        });

        cancelButton.addActionListener(e -> dispose());

        panel.add(productLabel);
        panel.add(productField);
        panel.add(warehouseLabel);
        panel.add(warehouseField);
        panel.add(operationTypeLabel);
        panel.add(operationTypeComboBox);
        panel.add(quantityLabel);
        panel.add(quantityField);
        panel.add(updateButton);
        panel.add(cancelButton);

        add(panel);
        setVisible(true);
        this.parentForm = null;
    }

    private JButton createStyledButton(String text) {
        JButton button = new JButton(text);
        button.setFont(new Font("Arial", Font.BOLD, 16));
        button.setForeground(Color.BLACK);
        button.setBackground(new Color(173, 216, 230));
        button.setFocusPainted(false);
        button.setCursor(new Cursor(Cursor.HAND_CURSOR));
        button.setBorder(BorderFactory.createEmptyBorder(10, 20, 10, 20));

        button.addMouseListener(new MouseAdapter() {
            public void mouseEntered(MouseEvent evt) {
                button.setBackground(new Color(135, 206, 235));
            }

            public void mouseExited(MouseEvent evt) {
                button.setBackground(new Color(173, 216, 230));
            }
        });

        return button;
    }
}
