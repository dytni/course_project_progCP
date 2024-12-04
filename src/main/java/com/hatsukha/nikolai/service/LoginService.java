package com.hatsukha.nikolai.service;

import com.hatsukha.nikolai.repository.DatabaseManager;

import java.security.MessageDigest;
import java.security.NoSuchAlgorithmException;

public class LoginService {
    private static final String HASH_ALGORITHM = "SHA-256";
    private final DatabaseManager dbManager;


    public LoginService(DatabaseManager dbManager) {
        this.dbManager = dbManager;
    }

    public boolean registerUser(String username, String password, String role) {
        if (dbManager.isUsernameTaken(username)) {
            System.out.println("Имя пользователя уже занято: " + username);
            return false;
        }
        String hashedPassword = hashPassword(password);
        return dbManager.registerUser(username, hashedPassword, role);
    }

    public String authenticateUser(String username, String password) {
        String hashedPassword = hashPassword(password);
        return dbManager.authenticateUserWithHash(username, hashedPassword);
    }


    public String hashPassword(String password) {
        try {
            MessageDigest digest = MessageDigest.getInstance(HASH_ALGORITHM);
            byte[] hash = digest.digest(password.getBytes());
            StringBuilder hexString = new StringBuilder();
            for (byte b : hash) {
                String hex = Integer.toHexString(0xff & b);
                if (hex.length() == 1) {
                    hexString.append('0');
                }
                hexString.append(hex);
            }
            return hexString.toString();
        } catch (NoSuchAlgorithmException e) {
            throw new RuntimeException("Ошибка хеширования пароля", e);
        }
    }
}
