package com.hatsukha.nikolai.client.clientForms;

import com.hatsukha.nikolai.client.ClientConnection;
import com.hatsukha.nikolai.client.utils.StyleUtils;

import javax.swing.*;
import java.awt.*;

public abstract class BaseMainForm {
    protected final ClientConnection clientConnection;
    protected final String role;

    public BaseMainForm(ClientConnection clientConnection, String role) {
        this.clientConnection = clientConnection;
        this.role = role;
    }

    public void showBase(JFrame frame) {
        frame.setSize(800, 600);
        frame.setDefaultCloseOperation(JFrame.EXIT_ON_CLOSE);
        frame.setLocationRelativeTo(null);

        // Главная панель с GridBagLayout
        JPanel panel = new JPanel(new GridBagLayout());
        GridBagConstraints gbc = new GridBagConstraints();
        gbc.insets = new Insets(10, 10, 10, 10);
        gbc.fill = GridBagConstraints.HORIZONTAL;

        // Элементы UI
        JLabel roleLabel = StyleUtils.createStyledLabel("Ваша роль: " + role);
        JButton logoutButton = StyleUtils.createStyledButton("Выйти");

        // Размещение элементов
        gbc.gridx = 0;
        gbc.gridy = 0;
        panel.add(roleLabel, gbc);

        gbc.gridy = 1;
        panel.add(logoutButton, gbc);

        // Добавление панели в верхнюю часть окна
        frame.add(panel, BorderLayout.NORTH);

        // Обработчик кнопки "Выйти"
        logoutButton.addActionListener(e -> {
            frame.dispose();
            new LoginForm(clientConnection).show();
        });

        // Вызов пользовательских компонентов для наследников
        addCustomComponents(panel, gbc, frame);

        // Отображение окна
        frame.setVisible(true);
    }

    protected abstract void addCustomComponents(JPanel panel, GridBagConstraints gbc, JFrame frame);
}
