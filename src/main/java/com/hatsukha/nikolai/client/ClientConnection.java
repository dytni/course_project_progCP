package com.hatsukha.nikolai.client;

import javax.swing.*;
import java.io.BufferedReader;
import java.io.InputStreamReader;
import java.io.PrintWriter;
import java.net.Socket;

public class ClientConnection {
    private final String serverAddress;
    private final int serverPort;
    private Socket socket;
    private BufferedReader in;
    private PrintWriter out;

    public ClientConnection(String serverAddress, int serverPort) {
        this.serverAddress = serverAddress;
        this.serverPort = serverPort;
        connect();
    }

    private void connect() {
        try {
            socket = new Socket(serverAddress, serverPort);
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


    public void close() {
        try {
            socket.close();
        } catch (Exception e) {
            e.printStackTrace();
        }
    }
}
