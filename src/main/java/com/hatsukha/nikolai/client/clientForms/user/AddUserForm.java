package com.hatsukha.nikolai.client.clientForms.user;

import com.hatsukha.nikolai.client.ClientConnection;
import com.hatsukha.nikolai.client.utils.StyleUtils;

import javax.swing.*;
import java.awt.*;

public class AddUserForm extends JFrame {

    public AddUserForm(ClientConnection clientConnection, UserTablePanel userTablePanel) {

        setTitle("Добавить пользователя");
        setSize(500, 350);
        setDefaultCloseOperation(DISPOSE_ON_CLOSE);
        setLocationRelativeTo(null);

        JPanel panel = new JPanel(new GridBagLayout());
        GridBagConstraints gbc = new GridBagConstraints();
        gbc.insets = new Insets(10, 10, 10, 10);
        gbc.fill = GridBagConstraints.HORIZONTAL;

        JLabel usernameLabel =  StyleUtils.createStyledLabel("Имя пользователя:");
        JTextField usernameField =  StyleUtils.createStyledTextField();

        JLabel passwordLabel =  StyleUtils.createStyledLabel("Пароль:");
        JPasswordField passwordField = new JPasswordField(15);
        passwordField.setFont(new Font("Arial", Font.PLAIN, 16));

        JLabel roleLabel =  StyleUtils.createStyledLabel("Роль:");
        JComboBox<String> roleComboBox = new JComboBox<>(new String[]{"USER", "ADMIN"});
        roleComboBox.setFont(new Font("Arial", Font.PLAIN, 16));

        JButton addButton =   StyleUtils.createStyledButton("Добавить");
        JButton cancelButton =  StyleUtils.createStyledButton("Отмена");

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

}
