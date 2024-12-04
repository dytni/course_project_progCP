package com.hatsukha.nikolai.server;

import java.io.FileWriter;
import java.io.IOException;
import java.io.PrintWriter;
import java.time.LocalDateTime;
import java.time.format.DateTimeFormatter;

public class ServerLogger {
    private static final String LOG_FILE = "server_logs.txt";
    private final PrintWriter writer;

    public ServerLogger() {
        try {
            writer = new PrintWriter(new FileWriter(LOG_FILE, true), true); // true для добавления в файл
        } catch (IOException e) {
            throw new RuntimeException("Ошибка при инициализации логгера", e);
        }
    }

    public synchronized void log(String message) {
        String timestamp = LocalDateTime.now().format(DateTimeFormatter.ofPattern("yyyy-MM-dd HH:mm:ss"));
        writer.println("[" + timestamp + "] - " + message);
    }


    public void close() {
        writer.close();
    }
}
