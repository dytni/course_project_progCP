package com.hatsukha.nikolai.repository;

import java.util.List;
import java.util.Map;

public class DatabaseManager {
    private static DatabaseManager instance;
    private final UserRepository userRepository;
    private final ProductRepository productRepository;
    private final WarehouseRepository warehouseRepository;
    private final ProductOperationRepository productOperationRepository;
    private final DatabaseConnection databaseConnection;

    private DatabaseManager() {
        databaseConnection = DatabaseConnection.getInstance();
        userRepository = new UserRepository();
        productRepository = new ProductRepository();
        warehouseRepository = new WarehouseRepository();
        productOperationRepository = new ProductOperationRepository();
    }

    public static synchronized DatabaseManager getInstance() {
        if (instance == null) {
            instance = new DatabaseManager();
        }
        return instance;
    }

    // -------------------- User Repository --------------------
    public int getUserId(String username) {
        return userRepository.getUserId(username);
    }

    public boolean isUsernameTaken(String username) {
        return userRepository.isUsernameTaken(username);
    }

    public boolean registerUser(String username, String hashedPassword, String role) {
        return userRepository.registerUser(username, hashedPassword, role);
    }

    public String authenticateUserWithHash(String username, String hashedPassword) {
        return userRepository.authenticateUserWithHash(username, hashedPassword);
    }

    public boolean deleteUser(int id) {
        return userRepository.deleteUser(id);
    }

    public List<String> getAllUsers() {
        return userRepository.getAllUsers();
    }

    public boolean updateUserInfo(int userId, String newUsername, String newRole) {
        return userRepository.updateUser(userId, newUsername, newRole);
    }

    // -------------------- Product Repository --------------------
    public boolean addProduct(String name, String description, String category, double weight, double volume) {
        return productRepository.addProduct(name, description, category, weight, volume);
    }

    public boolean updateProduct(int id, String name, String description, String category, double weight, double volume, String location) {
        return productRepository.updateProduct(id, name, description, category, weight, volume, location);
    }

    public boolean deleteProduct(int id) {
        return productRepository.deleteProduct(id);
    }

    public List<String> getAllProducts() {
        return productRepository.getAllProducts();
    }

    public boolean setProductLocation(int productId, String location) {
        return productRepository.setProductLocation(productId, location);
    }

    // -------------------- Warehouse Repository --------------------
    public boolean addWarehouse(String name, String location) {
        return warehouseRepository.addWarehouse(name, location);
    }

    public boolean updateWarehouse(int warehouseId, String name, String location) {
        return warehouseRepository.updateWarehouse(warehouseId, name, location);
    }

    public boolean deleteWarehouse(int warehouseId) {
        return warehouseRepository.deleteWarehouse(warehouseId);
    }

    public List<String> getAllWarehouses() {
        return warehouseRepository.getAllWarehouses();
    }


    // -------------------- Product Operations Repository --------------------
    public boolean addOperation(int productId, int warehouseId, int userId, String operationType, int quantity) {
        return productOperationRepository.addOperation(productId, warehouseId,userId,operationType,quantity);
    }

    public List<String> getAllOperations() {
        return productOperationRepository.getAllOperations();
    }

    public boolean updateProductOperation(int operationId, int productId, int userId, int warehouseId, String operationType, int quantity) {
        return productOperationRepository.updateOperation(operationId, productId, userId, warehouseId, operationType, quantity);
    }

    public boolean deleteProductOperation(int operationId) {
        return productOperationRepository.deleteOperation(operationId);
    }

    public List<String> getAllOperationsByUser(int userId) {
        return productOperationRepository.getOperationsByUser(userId);
    }

    // -------------------- Connection Management --------------------
    public void closeConnection() {
        databaseConnection.closeConnection();
    }

    public List<String> getOperationsByUser(int userId) {
        return productOperationRepository.getOperationsByUser(userId);
    }

    public Map<String, Integer> getUserOperationAnalytics() {
        return productOperationRepository.getOperationTypeAnalytics();
    }
}
