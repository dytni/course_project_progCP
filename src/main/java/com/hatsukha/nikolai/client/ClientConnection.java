package com.hatsukha.nikolai.client;

import javax.swing.*;
import java.io.BufferedReader;
import java.io.IOException;
import java.io.InputStreamReader;
import java.io.PrintWriter;
import java.net.Socket;

public class ClientConnection {
    private final Socket socket;
    private BufferedReader in;
    private PrintWriter out;

    public ClientConnection(String serverAddress, int serverPort) {
        try {
            socket = new Socket(serverAddress, serverPort);
        } catch (IOException e) {
            throw new RuntimeException(e);
        }
        connect();
    }

    private void connect() {
        try {
            in = new BufferedReader(new InputStreamReader(socket.getInputStream()));
            out = new PrintWriter(socket.getOutputStream(), true);
        } catch (Exception e) {
            JOptionPane.showMessageDialog(null, "Ошибка подключения к серверу!");
            System.exit(1);
        }
    }

    public void send(String message) {
        out.println(message);
    }

    public String receive() {
        try {
            return in.readLine();
        } catch (Exception e) {
            return null;
        }
    }
}
