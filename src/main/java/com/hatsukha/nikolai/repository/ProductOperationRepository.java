package com.hatsukha.nikolai.repository;

import java.sql.Connection;
import java.sql.PreparedStatement;
import com.hatsukha.nikolai.server.ServerLogger;

import java.sql.ResultSet;
import java.sql.SQLException;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;


public class ProductOperationRepository {
    private final Connection connection;


    public ProductOperationRepository() {
        this.connection = DatabaseConnection.getInstance().getConnection();
    }

    public boolean addOperation(int productId, int warehouseId, int userId, String operationType, int quantity) {
        String query = "INSERT INTO ProductOperations (product_id, warehouse_id, user_id, operation_type, quantity) VALUES (?, ?, ?, ?, ?)";
        try (PreparedStatement stmt = connection.prepareStatement(query)) {
            stmt.setInt(1, productId);
            stmt.setInt(2, warehouseId);
            stmt.setInt(3, userId);
            stmt.setString(4, operationType);
            stmt.setInt(5, quantity);
            stmt.executeUpdate();
            return true;
        } catch (SQLException e) {
            e.printStackTrace();
            return false;
        }
    }

    public List<String> getAllOperations() {
        String query = "SELECT operation_id, product_id, warehouse_id, user_id, operation_type, quantity, operation_date FROM ProductOperations";
        List<String> operations = new ArrayList<>();
        try (PreparedStatement stmt = connection.prepareStatement(query);
             ResultSet rs = stmt.executeQuery()) {
            while (rs.next()) {
                operations.add(
                        rs.getInt("operation_id") + "," +
                                rs.getInt("product_id") + "," +
                                rs.getInt("warehouse_id") + "," +
                                rs.getInt("user_id") + "," +
                                rs.getString("operation_type") + "," +
                                rs.getInt("quantity") + "," +
                                rs.getTimestamp("operation_date")
                );
            }
        } catch (SQLException e) {
            e.printStackTrace();
        }
        return operations;
    }

    public boolean updateOperation(int operationId, int productId, int warehouseId, int userId, String operationType, int quantity) {
        String query = "UPDATE ProductOperations SET product_id = ?, warehouse_id = ?, user_id = ?, operation_type = ?, quantity = ? WHERE operation_id = ?";
        try (PreparedStatement stmt = connection.prepareStatement(query)) {
            stmt.setInt(1, productId);
            stmt.setInt(2, warehouseId);
            stmt.setInt(3, userId);
            stmt.setString(4, operationType);
            stmt.setInt(5, quantity);
            stmt.setInt(6, operationId);
            stmt.executeUpdate();
            return true;
        } catch (SQLException e) {
            e.printStackTrace();
            return false;
        }
    }

    public boolean deleteOperation(int operationId) {
        String query = "DELETE FROM ProductOperations WHERE operation_id = ?";
        try (PreparedStatement stmt = connection.prepareStatement(query)) {
            stmt.setInt(1, operationId);
            stmt.executeUpdate();
            return true;
        } catch (SQLException e) {
            e.printStackTrace();
            return false;
        }
    }

    public List<String> getOperationsByUser(int userId) {
        String query = "SELECT * FROM ProductOperations WHERE user_id = ?";
        List<String> operations = new ArrayList<>();
        try (PreparedStatement stmt = connection.prepareStatement(query)) {
            stmt.setInt(1, userId);
            try (ResultSet rs = stmt.executeQuery()) {
                while (rs.next()) {
                    operations.add(
                            rs.getInt("operation_id") + "," +
                                    rs.getInt("product_id") + "," +
                                    rs.getInt("warehouse_id") + "," +
                                    rs.getInt("user_id") + "," +
                                    rs.getString("operation_type") + "," +
                                    rs.getInt("quantity") + "," +
                                    rs.getTimestamp("operation_date")
                    );
                }
            }
        } catch (SQLException e) {
            e.printStackTrace();
        }
        return operations;
    }


    public Map<String, Integer> getOperationTypeAnalytics() {
        String query = "SELECT operation_type, COUNT(*) AS operation_count " +
                "FROM ProductOperations " +
                "GROUP BY operation_type";
        Map<String, Integer> analytics = new HashMap<>();
        try (PreparedStatement ps = connection.prepareStatement(query);
             ResultSet rs = ps.executeQuery()) {
            while (rs.next()) {
                analytics.put(rs.getString("operation_type"), rs.getInt("operation_count"));
            }
        } catch (SQLException e) {

            e.printStackTrace();
        }
        return analytics;
    }



}