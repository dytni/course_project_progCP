package com.hatsukha.nikolai.client.utils;

import com.hatsukha.nikolai.client.ClientConnection;
import com.hatsukha.nikolai.client.clientForms.AdminMainForm;

import javax.swing.*;
import java.awt.*;

public class ServerLogsForm extends JFrame {
    private final ClientConnection clientConnection;

    public ServerLogsForm(ClientConnection clientConnection, int userId) {
        this.clientConnection = clientConnection;

        setTitle("Просмотр логов сервера");
        setSize(800, 600);
        setDefaultCloseOperation(DISPOSE_ON_CLOSE);
        setLocationRelativeTo(null);

        JPanel mainPanel = new JPanel(new BorderLayout());

        JTextArea logsArea = new JTextArea();
        logsArea.setEditable(false);
        logsArea.setFont(new Font("Monospaced", Font.PLAIN, 14));
        JScrollPane scrollPane = new JScrollPane(logsArea);

        JButton refreshButton = StyleUtils.createStyledButton("Обновить");
        JButton closeButton = StyleUtils.createStyledButton("Закрыть");

        JPanel buttonPanel = new JPanel(new FlowLayout());
        buttonPanel.add(refreshButton);
        buttonPanel.add(closeButton);

        refreshButton.addActionListener(e -> loadLogs(logsArea));
        closeButton.addActionListener(e -> {
            dispose();
            new AdminMainForm(clientConnection,userId).show();
        });

        mainPanel.add(scrollPane, BorderLayout.CENTER);
        mainPanel.add(buttonPanel, BorderLayout.SOUTH);

        add(mainPanel);
        setVisible(true);

        // Загрузка логов при открытии формы
        loadLogs(logsArea);
    }

    private void loadLogs(JTextArea logsArea) {
        logsArea.setText("Загрузка логов...\n");
        clientConnection.send("VIEW_LOGS");

        StringBuilder logs = new StringBuilder();
        try {
            String line;
            while (!(line = clientConnection.receive()).equals("END_OF_LOGS")) {
                logs.append(line).append("\n");
            }
        } catch (Exception e) {
            logsArea.setText("Ошибка загрузки логов.");
            return;
        }
        logsArea.setText(logs.toString());
    }
}
