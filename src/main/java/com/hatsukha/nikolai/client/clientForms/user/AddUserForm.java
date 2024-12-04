package com.hatsukha.nikolai.client.clientForms.user;

import com.hatsukha.nikolai.client.ClientConnection;

import javax.swing.*;
import java.awt.*;

public class AddUserForm extends JFrame {
    private final ClientConnection clientConnection;
    private final UserTablePanel userTablePanel;

    public AddUserForm(ClientConnection clientConnection, UserTablePanel userTablePanel) {
        this.clientConnection = clientConnection;
        this.userTablePanel = userTablePanel;

        setTitle("Добавить пользователя");
        setSize(500, 350);
        setDefaultCloseOperation(DISPOSE_ON_CLOSE);
        setLocationRelativeTo(null);

        JPanel panel = new JPanel(new GridBagLayout());
        GridBagConstraints gbc = new GridBagConstraints();
        gbc.insets = new Insets(10, 10, 10, 10);
        gbc.fill = GridBagConstraints.HORIZONTAL;

        JLabel usernameLabel = createStyledLabel("Имя пользователя:");
        JTextField usernameField = createStyledTextField();

        JLabel passwordLabel = createStyledLabel("Пароль:");
        JPasswordField passwordField = new JPasswordField(15);
        passwordField.setFont(new Font("Arial", Font.PLAIN, 16));

        JLabel roleLabel = createStyledLabel("Роль:");
        JComboBox<String> roleComboBox = new JComboBox<>(new String[]{"USER", "ADMIN"});
        roleComboBox.setFont(new Font("Arial", Font.PLAIN, 16));

        JButton addButton = createStyledButton("Добавить");
        JButton cancelButton = createStyledButton("Отмена");

        gbc.gridx = 0;
        gbc.gridy = 0;
        panel.add(usernameLabel, gbc);
        gbc.gridx = 1;
        panel.add(usernameField, gbc);

        gbc.gridx = 0;
        gbc.gridy = 1;
        panel.add(passwordLabel, gbc);
        gbc.gridx = 1;
        panel.add(passwordField, gbc);

        gbc.gridx = 0;
        gbc.gridy = 2;
        panel.add(roleLabel, gbc);
        gbc.gridx = 1;
        panel.add(roleComboBox, gbc);

        gbc.gridx = 0;
        gbc.gridy = 3;
        gbc.gridwidth = 2;
        panel.add(addButton, gbc);

        gbc.gridy = 4;
        panel.add(cancelButton, gbc);

        addButton.addActionListener(e -> {
            String username = usernameField.getText();
            String password = new String(passwordField.getPassword());
            String role = (String) roleComboBox.getSelectedItem();

            if (username.isEmpty() || password.isEmpty()) {
                JOptionPane.showMessageDialog(this, "Все поля должны быть заполнены!");
                return;
            }

            clientConnection.send("ADD_USER");
            clientConnection.send(username);
            clientConnection.send(password);
            clientConnection.send(role);

            String response = clientConnection.receive();
            if ("SUCCESS".equals(response)) {
                JOptionPane.showMessageDialog(this, "Пользователь успешно добавлен!");
                userTablePanel.loadUsers();
                dispose();
            } else {
                JOptionPane.showMessageDialog(this, "Ошибка добавления пользователя!");
            }
        });

        cancelButton.addActionListener(e -> dispose());

        add(panel);
        setVisible(true);
    }

    private JLabel createStyledLabel(String text) {
        JLabel label = new JLabel(text);
        label.setFont(new Font("Arial", Font.BOLD, 16));
        label.setForeground(new Color(60, 60, 60));
        return label;
    }

    private JTextField createStyledTextField() {
        JTextField textField = new JTextField(15);
        textField.setFont(new Font("Arial", Font.PLAIN, 16));
        return textField;
    }

    private JButton createStyledButton(String text) {
        JButton button = new JButton(text);
        button.setFont(new Font("Arial", Font.BOLD, 18));
        button.setForeground(Color.WHITE);
        button.setBackground(new Color(255, 105, 180));
        button.setFocusPainted(false);
        button.setCursor(new Cursor(Cursor.HAND_CURSOR));
        button.setBorder(BorderFactory.createEmptyBorder(10, 20, 10, 20));

        button.addMouseListener(new java.awt.event.MouseAdapter() {
            public void mouseEntered(java.awt.event.MouseEvent evt) {
                button.setBackground(new Color(255, 182, 193));
            }

            public void mouseExited(java.awt.event.MouseEvent evt) {
                button.setBackground(new Color(255, 105, 180));
            }
        });

        return button;
    }
}
