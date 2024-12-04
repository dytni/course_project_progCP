package com.hatsukha.nikolai.client;

import com.hatsukha.nikolai.client.clientForms.LoginForm;

import javax.swing.*;

public class ClientApp {
    public static void main(String[] args) {
        SwingUtilities.invokeLater(() -> {
            ClientConnection clientConnection = new ClientConnection("localhost", 8080);
            new LoginForm(clientConnection).show();
        });
    }
}
