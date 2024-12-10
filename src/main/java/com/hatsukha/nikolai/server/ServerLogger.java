package com.hatsukha.nikolai.server;

import com.hatsukha.nikolai.repository.DatabaseConnection;

import java.io.FileWriter;
import java.io.IOException;
import java.io.PrintWriter;
import java.sql.Connection;
import java.sql.PreparedStatement;
import java.time.LocalDateTime;
import java.time.format.DateTimeFormatter;

public class ServerLogger {
    private static final String LOG_FILE = "server_logs.txt";
    private final PrintWriter writer;
    private final Connection connection;


    public ServerLogger() {
        this.connection = DatabaseConnection.getInstance().getConnection();
        try {
            writer = new PrintWriter(new FileWriter(LOG_FILE, true), true); // true для добавления в файл
        } catch (IOException e) {
            throw new RuntimeException("Ошибка при инициализации логгера", e);
        }
    }

    public synchronized void log(String message) {
        String timestamp = LocalDateTime.now().format(DateTimeFormatter.ofPattern("yyyy-MM-dd HH:mm:ss"));
        writer.println("[" + timestamp + "] - " + message);
        if (message.equals("Сервер остановлен.")){
            return;
        }
        saveMessage(message);

    }

    private void saveMessage(String message) {
        String query = "INSERT INTO LogMessages (message) VALUES (?)";
        try (PreparedStatement stmt = connection.prepareStatement(query)) {
            stmt.setString(1, message);
            stmt.executeUpdate();
        } catch (Exception e) {
            throw new RuntimeException(e);
        }
    }


    public void close() {
        writer.close();
    }
}
