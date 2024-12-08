package com.hatsukha.nikolai.client.clientForms;

import com.hatsukha.nikolai.client.ClientConnection;
import com.hatsukha.nikolai.client.utils.StyleUtils;

import javax.swing.*;
import java.awt.*;

public class LoginForm {
    private final ClientConnection clientConnection;

    public LoginForm(ClientConnection clientConnection) {
        this.clientConnection = clientConnection;
    }

    public void show() {
        JFrame frame = new JFrame("Вход");
        frame.setSize(800, 600);
        frame.setDefaultCloseOperation(JFrame.EXIT_ON_CLOSE);
        frame.setLocationRelativeTo(null);

        // Создание панели с элементами
        JPanel panel = new JPanel(new GridBagLayout());
        GridBagConstraints gbc = new GridBagConstraints();
        gbc.insets = new Insets(10, 10, 10, 10);
        gbc.fill = GridBagConstraints.HORIZONTAL;

        // Элементы интерфейса
        JLabel usernameLabel = StyleUtils.createStyledLabel("Имя пользователя:");
        JTextField usernameField = StyleUtils.createStyledTextField();

        JLabel passwordLabel = StyleUtils.createStyledLabel("Пароль:");
        JPasswordField passwordField = StyleUtils.createStyledPasswordField();

        JButton loginButton = StyleUtils.createStyledButton("Войти");
        JButton registerButton = StyleUtils.createStyledButton("Регистрация");

        // Расположение элементов
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
        panel.add(loginButton, gbc);

        gbc.gridy = 3;
        panel.add(registerButton, gbc);

        loginButton.addActionListener(e -> handleLogin(frame, usernameField, passwordField));

        registerButton.addActionListener(e -> {
            frame.dispose();
            new RegisterForm(clientConnection).show();
        });

        frame.add(panel);
        frame.setVisible(true);
    }

    private void handleLogin(JFrame frame, JTextField usernameField, JPasswordField passwordField) {
        String username = usernameField.getText();
        String password = new String(passwordField.getPassword());

        if (username.isEmpty() || password.isEmpty()) {
            showMessage(frame, "Все поля должны быть заполнены!", JOptionPane.WARNING_MESSAGE);
            return;
        }

        clientConnection.send("LOGIN");
        clientConnection.send(username);
        clientConnection.send(password);

        String response = clientConnection.receive();
        if ("SUCCESS".equals(response)) {
            String userId = clientConnection.receive();
            String role = clientConnection.receive();
            showMessage(frame, "Успешный вход! Ваша роль: " + role, JOptionPane.INFORMATION_MESSAGE);
            frame.dispose();

            if ("ADMIN".equals(role)) {
                new AdminMainForm(clientConnection, Integer.parseInt(userId)).show();
            } else {
                new UserMainForm(clientConnection, Integer.parseInt(userId)).show();
            }
        } else {
            showMessage(frame, "Неверное имя пользователя или пароль!", JOptionPane.ERROR_MESSAGE);
        }
    }

    private void showMessage(JFrame frame, String message, int messageType) {
        JOptionPane.showMessageDialog(frame, message, "Информация", messageType);
    }
}
