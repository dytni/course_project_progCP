package com.hatsukha.nikolai.server;

import com.hatsukha.nikolai.repository.DatabaseManager;
import com.hatsukha.nikolai.service.*;

import java.io.*;
import java.net.Socket;

public class ClientHandler implements Runnable {
    private final Socket clientSocket;
    private final LoginService loginService;
    private final DatabaseManager dbManager;
    private final ServerLogger logger;

    public ClientHandler(Socket clientSocket, LoginService loginService, DatabaseManager dbManager, ServerLogger logger) {
        this.clientSocket = clientSocket;
        this.loginService = loginService;
        this.dbManager = dbManager;
        this.logger = logger;
    }

    @Override
    public void run() {
        try (BufferedReader in = new BufferedReader(new InputStreamReader(clientSocket.getInputStream()));
             PrintWriter out = new PrintWriter(clientSocket.getOutputStream(), true)) {

            UserService userService = new UserService(logger, dbManager, loginService);
            ProductService productService = new ProductService(logger, dbManager);
            WarehouseService warehouseService = new WarehouseService(logger, dbManager);
            ProductOperationService productOperationService = new ProductOperationService(logger, dbManager);

            String command;
            while ((command = in.readLine()) != null) {
                logger.log("Получена команда от клиента: " + command);

                switch (command.toUpperCase()) {
                    // -------------------- Пользователи --------------------
                    case "REGISTER":
                        String username = in.readLine();
                        String password = in.readLine();
                        boolean registered = loginService.registerUser(username, password, "USER");
                        out.println(registered ? "SUCCESS" : "FAILURE");
                        break;

                    case "LOGIN":
                        username = in.readLine();
                        password = in.readLine();
                        String role = loginService.authenticateUser(username, password);
                        if (role != null) {
                            int userId = dbManager.getUserId(username);
                            if (userId != -1) {
                                out.println("SUCCESS");
                                out.println(userId);
                                out.println(role);
                            } else {
                                out.println("FAILURE");
                            }
                        } else {
                            out.println("FAILURE");
                        }
                        break;

                    case "VIEW_USERS":
                        userService.getUsers(out);
                        break;

                    case "EDIT_USER":
                        userService.editUser(in, out);
                        break;

                    case "ADD_USER":
                        userService.addUser(in, out);
                        break;

                    case "DELETE_USER":
                        userService.deleteUser(in, out);
                        break;

                    // -------------------- Товары --------------------
                    case "VIEW_PRODUCTS":
                        productService.getProducts(out);
                        break;

                    case "ADD_PRODUCT":
                        productService.addProduct(in, out);
                        break;

                    case "UPDATE_PRODUCT":
                        productService.updateProduct(in, out);
                        break;

                    case "SET_LOCATION":
                        productService.setProductLocation(in, out);
                        break;

                    case "DELETE_PRODUCT":
                        productService.deleteProduct(in, out);
                        break;

                    // -------------------- Склады --------------------
                    case "VIEW_WAREHOUSES":
                        warehouseService.getWarehouses(out);
                        break;

                    case "ADD_WAREHOUSE":
                        warehouseService.addWarehouse(in, out);
                        break;

                    case "UPDATE_WAREHOUSE":
                        warehouseService.updateWarehouse(in, out);
                        break;

                    case "DELETE_WAREHOUSE":
                        warehouseService.deleteWarehouse(in, out);
                        break;

                    case "GET_ORDERS_BY_WAREHOUSE":
                        warehouseService.getOrdersByWarehouse(out);
                        break;


                    // -------------------- Операции с продуктами --------------------
                    case "ADD_OPERATION":
                        productOperationService.addOperation(in, out);
                        break;

                    case "VIEW_OPERATION":
                        productOperationService.getOperations(out);
                        break;

                    case "UPDATE_OPERATION":
                        productOperationService.updateOperation(in, out);
                        break;

                    case "DELETE_OPERATION":
                        productOperationService.deleteOperation(in, out);
                        break;

                    case "GET_OPERATION_STATS":
                        productOperationService.GetOperationStats(out);
                        break;

                    case "VIEW_OPERATION_BY_ID":
                        productOperationService.getOperationsByUser(in, out);
                        break;

                    case "VIEW_LOGS":
                        productService.getServerLogs(out);
                        break;

                    // -------------------- Неизвестная команда --------------------
                    default:
                        logger.log("Неизвестная команда от клиента: " + command);
                        out.println("UNKNOWN_COMMAND");
                        break;
                }
            }
        } catch (IOException e) {
            logger.log("Ошибка связи с клиентом: " + e.getMessage());
        } finally {
            try {
                clientSocket.close();
                logger.log("Клиент отключился");
            } catch (IOException e) {
                logger.log("Ошибка при закрытии соединения с клиентом: " + e.getMessage());
            }
        }
    }
}
