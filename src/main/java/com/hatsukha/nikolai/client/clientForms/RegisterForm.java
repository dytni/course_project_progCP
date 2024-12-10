package com.hatsukha.nikolai.client.clientForms;

import com.hatsukha.nikolai.client.ClientConnection;
import com.hatsukha.nikolai.client.utils.StyleUtils;

import javax.swing.*;
import java.awt.*;

public class RegisterForm {
    private final ClientConnection clientConnection;

    public RegisterForm(ClientConnection clientConnection) {
        this.clientConnection = clientConnection;
    }

    public void show() {
        JFrame frame = new JFrame("Регистрация");
        frame.setSize(800, 600);
        frame.setDefaultCloseOperation(JFrame.EXIT_ON_CLOSE);
        frame.setLocationRelativeTo(null);

        JPanel panel = new JPanel(new GridBagLayout());
        GridBagConstraints gbc = new GridBagConstraints();
        gbc.insets = new Insets(10, 10, 10, 10);
        gbc.fill = GridBagConstraints.HORIZONTAL;

        JLabel usernameLabel = StyleUtils.createStyledLabel("Имя пользователя:");
        JTextField usernameField = StyleUtils.createStyledTextField();

        JLabel passwordLabel = StyleUtils.createStyledLabel("Пароль:");
        JPasswordField passwordField = StyleUtils.createStyledPasswordField();

        JButton registerButton = StyleUtils.createStyledButton("Зарегистрироваться");
        JButton backButton = StyleUtils.createStyledButton("Назад");

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
        gbc.gridwidth = 2;
        panel.add(registerButton, gbc);

        gbc.gridy = 3;
        panel.add(backButton, gbc);

        registerButton.addActionListener(e -> handleRegister(frame, usernameField, passwordField));

        backButton.addActionListener(e -> {
            frame.dispose();
            new LoginForm(clientConnection).show();
        });

        frame.add(panel);
        frame.setVisible(true);
    }

    private void handleRegister(JFrame frame, JTextField usernameField, JPasswordField passwordField) {
        String username = usernameField.getText();
        String password = new String(passwordField.getPassword());

        if (username.isEmpty() || password.isEmpty()) {
            JOptionPane.showMessageDialog(frame, "Все поля должны быть заполнены!", "Ошибка", JOptionPane.WARNING_MESSAGE);
            return;
        }

        clientConnection.send("REGISTER");
        clientConnection.send(username);
        clientConnection.send(password);

        String response = clientConnection.receive();
        if ("SUCCESS".equals(response)) {
            JOptionPane.showMessageDialog(frame, "Регистрация успешна!", "Успех", JOptionPane.INFORMATION_MESSAGE);
            frame.dispose();
            new LoginForm(clientConnection).show();
        } else {
            JOptionPane.showMessageDialog(frame, "Ошибка регистрации! Возможно, пользователь уже существует.", "Ошибка", JOptionPane.ERROR_MESSAGE);
        }
    }
}
