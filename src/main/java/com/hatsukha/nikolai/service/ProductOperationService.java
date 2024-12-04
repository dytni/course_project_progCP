package com.hatsukha.nikolai.service;

import com.hatsukha.nikolai.repository.DatabaseManager;
import com.hatsukha.nikolai.server.ServerLogger;

import java.io.BufferedReader;
import java.io.IOException;
import java.io.PrintWriter;
import java.util.List;
import java.util.Map;

public class ProductOperationService {
    private final ServerLogger logger;
    private final DatabaseManager dbManager;

    public ProductOperationService(ServerLogger logger, DatabaseManager dbManager) {
        this.logger = logger;
        this.dbManager = dbManager;
    }

    public void addOperation(BufferedReader in, PrintWriter out) {
        try {
            int productId = Integer.parseInt(in.readLine());
            int warehouseId = Integer.parseInt(in.readLine());
            int userId = Integer.parseInt(in.readLine());
            String operationType = in.readLine();
            int quantity = Integer.parseInt(in.readLine());

            boolean success = dbManager.addOperation(productId, warehouseId, userId, operationType, quantity);
            out.println(success ? "SUCCESS" : "FAILURE");
            logger.log(success ? "Операция добавлена успешно" : "Ошибка добавления операции");
        } catch (IOException | NumberFormatException e) {
            out.println("FAILURE");
            logger.log("Ошибка обработки операции добавления: " + e.getMessage());
        }
    }

    public void getOperations(PrintWriter out) {
        List<String> operations = dbManager.getAllOperations();
        out.println(operations.size());
        for (String operation : operations) {
            out.println(operation);
        }
        logger.log("Список операций отправлен клиенту");
    }

    public void updateOperation(BufferedReader in, PrintWriter out) {
        try {
            int operationId = Integer.parseInt(in.readLine());
            int productId = Integer.parseInt(in.readLine());
            int warehouseId = Integer.parseInt(in.readLine());
            int userId = Integer.parseInt(in.readLine());
            String operationType = in.readLine();
            int quantity = Integer.parseInt(in.readLine());

            boolean success = dbManager.updateProductOperation(operationId, productId, warehouseId, userId, operationType, quantity);
            out.println(success ? "SUCCESS" : "FAILURE");
            logger.log(success ? "Операция обновлена успешно" : "Ошибка обновления операции");
        } catch (IOException | NumberFormatException e) {
            out.println("FAILURE");
            logger.log("Ошибка обработки операции обновления: " + e.getMessage());
        }
    }

    public void deleteOperation(BufferedReader in, PrintWriter out) {
        try {
            int operationId = Integer.parseInt(in.readLine());

            boolean success = dbManager.deleteProductOperation(operationId);
            out.println(success ? "SUCCESS" : "FAILURE");
            logger.log(success ? "Операция удалена успешно" : "Ошибка удаления операции");
        } catch (IOException | NumberFormatException e) {
            out.println("FAILURE");
            logger.log("Ошибка обработки операции удаления: " + e.getMessage());
        }
    }

    public void getOperationsByUser(BufferedReader in, PrintWriter out) {
        try {
            int userId = Integer.parseInt(in.readLine());

            List<String> operations = dbManager.getOperationsByUser(userId);
            out.println(operations.size());
            for (String operation : operations) {
                out.println(operation);
            }
            logger.log("Список операций пользователя отправлен клиенту");
        } catch (IOException | NumberFormatException e) {
            out.println("FAILURE");
            logger.log("Ошибка получения операций по пользователю: " + e.getMessage());
        }
    }

    public void GetOperationStats(PrintWriter out) {
        try {
            Map<String, Integer> stats = dbManager.getUserOperationAnalytics();

            out.println(stats.size());
            for (Map.Entry<String, Integer> entry : stats.entrySet()) {
                out.println(entry.getKey() + "," + entry.getValue());
            }

            logger.log("Статистика операций успешно отправлена клиенту");
        } catch (Exception e) {
            out.println("FAILURE");
            logger.log("Ошибка получения статистики операций: " + e.getMessage());
        }
    }


}
