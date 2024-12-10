package com.hatsukha.nikolai.server;

import com.hatsukha.nikolai.repository.DatabaseManager;
import com.hatsukha.nikolai.service.LoginService;

import java.io.IOException;
import java.net.ServerSocket;
import java.net.Socket;
import java.util.concurrent.ExecutorService;
import java.util.concurrent.Executors;

public class Server {
    private static final int PORT = 8080;
    private static final int THREAD_POOL_SIZE = 10;

    private static volatile boolean isRunning = true;
    private final ExecutorService clientThreadPool;
    private final DatabaseManager dbManager;
    private final ServerLogger logger;

    public Server() {
        this.clientThreadPool = Executors.newFixedThreadPool(THREAD_POOL_SIZE);
        this.dbManager = DatabaseManager.getInstance();
        this.logger = new ServerLogger();
    }

    public void start() {
        try (ServerSocket serverSocket = new ServerSocket(PORT)) {
            logger.log("Сервер запущен на порту " + PORT);

            while (isRunning) {
                try {
                    Socket clientSocket = serverSocket.accept();
                    logger.log("Новое подключение от клиента: " + clientSocket.getInetAddress());
                    handleClient(clientSocket);
                } catch (IOException e) {
                    if (isRunning) {
                        logger.log("Ошибка при подключении клиента: " + e.getMessage());
                    }
                }
            }
        } catch (IOException e) {
            logger.log("Не удалось запустить сервер: " + e.getMessage());
        } finally {
            shutdown();
        }
    }

    private void handleClient(Socket clientSocket) {
        clientThreadPool.submit(() -> {
            try {
                LoginService loginService = new LoginService(dbManager);
                ClientHandler clientHandler = new ClientHandler(clientSocket, loginService, dbManager, logger);
                clientHandler.run();
            } catch (Exception e) {
                logger.log("Ошибка при обработке клиента: " + e.getMessage());
            } finally {
                try {
                    clientSocket.close();
                } catch (IOException e) {
                    logger.log("Ошибка при закрытии клиентского сокета: " + e.getMessage());
                }
            }
        });
    }

    public void shutdown() {
        isRunning = false;
        clientThreadPool.shutdown();
        dbManager.closeConnection();
        logger.log("Сервер остановлен.");
        logger.close();
    }

    public static void main(String[] args) {
        Server server = new Server();
        Runtime.getRuntime().addShutdownHook(new Thread(server::shutdown)); // Graceful shutdown on termination signal
        server.start();
    }
}
