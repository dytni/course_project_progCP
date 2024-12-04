package com.hatsukha.nikolai.service;

import com.hatsukha.nikolai.repository.DatabaseManager;
import com.hatsukha.nikolai.server.ServerLogger;

import java.io.BufferedReader;
import java.io.IOException;
import java.io.PrintWriter;
import java.util.List;

public class UserService {

    private final ServerLogger logger;
    private final DatabaseManager dbManager;
    private final LoginService loginService;

    public UserService(ServerLogger logger, DatabaseManager dbManager, LoginService loginService) {
        this.logger = logger;
        this.dbManager = dbManager;
        this.loginService = loginService;
    }

    public void addUser(BufferedReader in, PrintWriter out) throws IOException {
        try {
            String username = in.readLine();
            String password = in.readLine();
            String role = in.readLine();

            boolean added = dbManager.registerUser(username, loginService.hashPassword(password), role);
            if (added) {
                logger.log("Пользователь добавлен: " + username);
                out.println("SUCCESS");
            } else {
                logger.log("Ошибка добавления пользователя: " + username);
                out.println("FAILURE");
            }
        } catch (IOException e) {
            logger.log("Ошибка обработки команды ADD_USER: " + e.getMessage());
            out.println("FAILURE");
        }
    }

    public void editUser(BufferedReader in, PrintWriter out) throws IOException {
        try {
            int userId = Integer.parseInt(in.readLine());
            String newUsername = in.readLine();
            String newRole = in.readLine();

            boolean updated = dbManager.updateUserInfo(userId, newUsername, newRole);
            if (updated) {
                logger.log("Пользователь с ID " + userId + " обновлен");
                out.println("SUCCESS");
            } else {
                logger.log("Ошибка обновления пользователя с ID " + userId);
                out.println("FAILURE");
            }
        } catch (IOException | NumberFormatException e) {
            logger.log("Ошибка обработки команды EDIT_USER: " + e.getMessage());
            out.println("FAILURE");
        }
    }

    public void getUsers(PrintWriter out) throws IOException {
        List<String> users = dbManager.getAllUsers();
        out.println(users.size());
        for (String user : users) {
            out.println(user);
        }
        logger.log("Список пользователей отправлен клиенту");
    }

    public void deleteUser(BufferedReader in, PrintWriter out) throws IOException {
        try {
            int userId = Integer.parseInt(in.readLine());

            boolean deleted = dbManager.deleteUser(userId);
            if (deleted) {
                logger.log("Пользователь с ID " + userId + " удален");
                out.println("SUCCESS");
            } else {
                logger.log("Ошибка удаления пользователя с ID " + userId);
                out.println("FAILURE");
            }
        } catch (IOException | NumberFormatException e) {
            logger.log("Ошибка обработки команды DELETE_USER: " + e.getMessage());
            out.println("FAILURE");
        }
    }
}
