package com.hatsukha.nikolai.service;

import com.hatsukha.nikolai.repository.DatabaseManager;
import com.hatsukha.nikolai.server.ServerLogger;

import java.io.BufferedReader;
import java.io.IOException;
import java.io.PrintWriter;
import java.util.List;
import java.util.Map;

public class WarehouseService {
    private final ServerLogger logger;
    private final DatabaseManager databaseManager;

    public WarehouseService(ServerLogger logger, DatabaseManager databaseManager) {
        this.logger = logger;
        this.databaseManager = databaseManager;
    }

    public void getOrdersByWarehouse(PrintWriter out) {
        try {
            // Получение статистики из репозитория
            Map<String, Integer> warehouseStats = databaseManager.getOrdersByWarehouse();

            // Отправка количества записей
            out.println(warehouseStats.size());

            // Отправка данных по каждому складу
            warehouseStats.forEach((warehouseName, totalOrders) ->
                    out.println(warehouseName + "," + totalOrders)
            );

            logger.log("Статистика заказов по складам успешно отправлена клиенту.");
        } catch (Exception e) {
            out.println("FAILURE");
            logger.log("Ошибка при обработке статистики заказов по складам: " + e.getMessage());
            e.printStackTrace();
        }
    }

    public void getWarehouses(PrintWriter out) {
        List<String> warehouses = databaseManager.getAllWarehouses();
        out.println(warehouses.size());
        for (String warehouse : warehouses) {
            out.println(warehouse);
        }
        logger.log("Отправлен список складов клиенту");
    }

    public void addWarehouse(BufferedReader in, PrintWriter out) throws IOException {
        String name = in.readLine();
        String location = in.readLine();

        boolean added = databaseManager.addWarehouse(name, location);
        out.println(added ? "SUCCESS" : "FAILURE");

        logger.log(added ? "Склад добавлен: " + name + ", " + location : "Ошибка добавления склада");
    }

    public void updateWarehouse(BufferedReader in, PrintWriter out) throws IOException {
        int warehouseId = Integer.parseInt(in.readLine());
        String name = in.readLine();
        String location = in.readLine();

        boolean updated = databaseManager.updateWarehouse(warehouseId, name, location);
        out.println(updated ? "SUCCESS" : "FAILURE");

        logger.log(updated ? "Склад обновлен: ID " + warehouseId : "Ошибка обновления склада: ID " + warehouseId);
    }

    public void deleteWarehouse(BufferedReader in, PrintWriter out) throws IOException {
        int warehouseId = Integer.parseInt(in.readLine());

        boolean deleted = databaseManager.deleteWarehouse(warehouseId);
        out.println(deleted ? "SUCCESS" : "FAILURE");

        logger.log(deleted ? "Склад удален: ID " + warehouseId : "Ошибка удаления склада: ID " + warehouseId);
    }


}
