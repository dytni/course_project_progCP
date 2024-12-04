package com.hatsukha.nikolai.service;

import com.hatsukha.nikolai.repository.DatabaseManager;
import com.hatsukha.nikolai.server.ServerLogger;

import java.io.BufferedReader;
import java.io.FileReader;
import java.io.IOException;
import java.io.PrintWriter;
import java.util.List;

public class ProductService {

    private final ServerLogger logger;
    private final DatabaseManager dbManager;

    public ProductService(ServerLogger logger, DatabaseManager dbManager) {
        this.logger = logger;
        this.dbManager = dbManager;
    }

    public void getProducts(PrintWriter out) {
        try {
            List<String> products = dbManager.getAllProducts();
            out.println(products.size());
            for (String product : products) {
                out.println(product);
            }
            logger.log("INFO: Список товаров успешно отправлен клиенту.");
        } catch (Exception e) {
            logger.log("ERROR: Ошибка при получении списка товаров: " + e.getMessage());
            out.println("FAILURE");
        }
    }

    public void addProduct(BufferedReader in, PrintWriter out) throws IOException {
        try {
            String name = in.readLine();
            String description = in.readLine();
            String category = in.readLine();
            double weight = Double.parseDouble(in.readLine());
            double volume = Double.parseDouble(in.readLine());

            boolean added = dbManager.addProduct(name, description, category, weight, volume);
            out.println(added ? "SUCCESS" : "FAILURE");
            logger.log(added
                    ? "INFO: Товар '" + name + "' успешно добавлен."
                    : "ERROR: Не удалось добавить товар '" + name + "'.");
        } catch (Exception e) {
            logger.log("ERROR: Ошибка при добавлении товара: " + e.getMessage());
            out.println("FAILURE");
        }
    }

    public void updateProduct(BufferedReader in, PrintWriter out) throws IOException {
        try {
            int productId = Integer.parseInt(in.readLine());
            String name = in.readLine();
            String description = in.readLine();
            String category = in.readLine();
            double weight = Double.parseDouble(in.readLine());
            double volume = Double.parseDouble(in.readLine());
            String location = in.readLine();

            boolean updated = dbManager.updateProduct(productId, name, description, category, weight, volume, location);
            out.println(updated ? "SUCCESS" : "FAILURE");
            logger.log(updated
                    ? "INFO: Товар ID " + productId + " успешно обновлен."
                    : "ERROR: Не удалось обновить товар ID " + productId + ".");
        } catch (Exception e) {
            logger.log("ERROR: Ошибка при обновлении товара: " + e.getMessage());
            out.println("FAILURE");
        }
    }

    public void deleteProduct(BufferedReader in, PrintWriter out) throws IOException {
        try {
            int productId = Integer.parseInt(in.readLine());
            boolean deleted = dbManager.deleteProduct(productId);
            out.println(deleted ? "SUCCESS" : "FAILURE");
            logger.log(deleted
                    ? "INFO: Товар ID " + productId + " успешно удален."
                    : "ERROR: Не удалось удалить товар ID " + productId + ".");
        } catch (Exception e) {
            logger.log("ERROR: Ошибка при удалении товара: " + e.getMessage());
            out.println("FAILURE");
        }
    }

    public void setProductLocation(BufferedReader in, PrintWriter out) throws IOException {
        try {
            int productId = Integer.parseInt(in.readLine());
            String location = in.readLine();
            boolean updated = dbManager.setProductLocation(productId, location);
            out.println(updated ? "SUCCESS" : "FAILURE");
            logger.log(updated
                    ? "INFO: Локация для товара ID " + productId + " успешно установлена на '" + location + "'."
                    : "ERROR: Не удалось установить локацию для товара ID " + productId + ".");
        } catch (Exception e) {
            logger.log("ERROR: Ошибка при установлении локации товара: " + e.getMessage());
            out.println("FAILURE");
        }
    }
    public void getServerLogs(PrintWriter out) {
        String logFilePath = "C:\\Users\\Nikol\\my stuf\\CP labs\\courseProgCP\\server_logs.txt";

        try (BufferedReader reader = new BufferedReader(new FileReader(logFilePath))) {
            String line;
            while ((line = reader.readLine()) != null) {
                out.println(line); // Отправка строки лога
            }
            out.println("END_OF_LOGS"); // Маркер окончания логов
            logger.log("INFO: Логи сервера успешно отправлены клиенту.");
        } catch (IOException e) {
            out.println("ERROR: Не удалось загрузить логи сервера."); // Сообщение об ошибке
            logger.log("ERROR: Ошибка при чтении файла логов: " + e.getMessage());
        }
    }
}
