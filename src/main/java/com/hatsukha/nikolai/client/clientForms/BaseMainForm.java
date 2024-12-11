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
        frame.setSize(800, 650);
        frame.setDefaultCloseOperation(JFrame.EXIT_ON_CLOSE);
        frame.setLocationRelativeTo(null);

        JPanel panel = new JPanel(new GridBagLayout());
        GridBagConstraints gbc = new GridBagConstraints();
        gbc.insets = new Insets(10, 10, 10, 10);
        gbc.fill = GridBagConstraints.HORIZONTAL;

        JLabel roleLabel = StyleUtils.createStyledLabel("Ваша роль: " + role);
        JButton logoutButton = StyleUtils.createStyledButton("Выйти");

        gbc.gridx = 0;
        gbc.gridy = 0;
        panel.add(roleLabel, gbc);

        gbc.gridy = 1;
        panel.add(logoutButton, gbc);

        frame.add(panel, BorderLayout.NORTH);

        logoutButton.addActionListener(e -> {
            frame.dispose();
            new LoginForm(clientConnection).show();
        });

        addCustomComponents(panel, gbc, frame);

        // Отображение окна
        frame.setVisible(true);
    }

    protected abstract void addCustomComponents(JPanel panel, GridBagConstraints gbc, JFrame frame);
}
