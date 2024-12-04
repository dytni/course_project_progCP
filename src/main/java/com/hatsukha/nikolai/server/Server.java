package com.hatsukha.nikolai.server;

import com.hatsukha.nikolai.repository.DatabaseManager;
import com.hatsukha.nikolai.service.LoginService;

import java.io.IOException;
import java.net.ServerSocket;
import java.net.Socket;

public class Server {
    private static final int PORT = 8080;

    public static void main(String[] args) {
        DatabaseManager dbManager = DatabaseManager.getInstance();
        LoginService loginService = new LoginService(dbManager);
        ServerLogger logger = new ServerLogger();

        try (ServerSocket serverSocket = new ServerSocket(PORT)) {
            logger.log("Сервер запущен на порту " + PORT);

            while (true) {
                Socket clientSocket = serverSocket.accept();
                logger.log("Новое подключение от клиента: " + clientSocket.getInetAddress());
                ClientHandler clientHandler = new ClientHandler(clientSocket, loginService, dbManager, logger);
                new Thread(clientHandler).start();
            }
        } catch (IOException e) {
            logger.log("Ошибка сервера: " + e.getMessage());
        } finally {
            dbManager.closeConnection();
            logger.close();
        }
    }
}
