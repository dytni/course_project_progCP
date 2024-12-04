package com.hatsukha.nikolai.repository;

import java.sql.Connection;
import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.util.ArrayList;
import java.util.List;

public class WarehouseRepository {
    private final Connection connection;

    public WarehouseRepository() {
        this.connection = DatabaseConnection.getInstance().getConnection();
    }

    // Добавление нового склада
    public boolean addWarehouse(String name, String location) {
        String query = "INSERT INTO warehouse (name, location) VALUES (?, ?)";
        try (PreparedStatement stmt = connection.prepareStatement(query)) {
            stmt.setString(1, name);
            stmt.setString(2, location);
            stmt.executeUpdate();
            return true;
        } catch (Exception e) {
            e.printStackTrace();
            return false;
        }
    }

    // Обновление данных склада
    public boolean updateWarehouse(int warehouseId, String name, String location) {
        String query = "UPDATE warehouse SET name = ?, location = ? WHERE warehouse_id = ?";
        try (PreparedStatement stmt = connection.prepareStatement(query)) {
            stmt.setString(1, name);
            stmt.setString(2, location);
            stmt.setInt(3, warehouseId);
            stmt.executeUpdate();
            return true;
        } catch (Exception e) {
            e.printStackTrace();
            return false;
        }
    }

    // Удаление склада по ID
    public boolean deleteWarehouse(int warehouseId) {
        String query = "DELETE FROM warehouse WHERE warehouse_id = ?";
        try (PreparedStatement stmt = connection.prepareStatement(query)) {
            stmt.setInt(1, warehouseId);
            stmt.executeUpdate();
            return true;
        } catch (Exception e) {
            e.printStackTrace();
            return false;
        }
    }

    // Получение всех складов
    public List<String> getAllWarehouses() {
        String query = "SELECT warehouse_id, name, location FROM warehouse";
        List<String> warehouses = new ArrayList<>();
        try (PreparedStatement stmt = connection.prepareStatement(query);
             ResultSet rs = stmt.executeQuery()) {
            while (rs.next()) {
                warehouses.add(
                        rs.getInt("warehouse_id") + "," +
                                rs.getString("name") + "," +
                                rs.getString("location")
                );
            }
        } catch (Exception e) {
            e.printStackTrace();
        }
        return warehouses;
    }

//    // Получение склада по ID
//    public String getWarehouseById(int warehouseId) {
//        String query = "SELECT warehouse_id, name, location FROM warehouse WHERE warehouse_id = ?";
//        try (PreparedStatement stmt = connection.prepareStatement(query)) {
//            stmt.setInt(1, warehouseId);
//            try (ResultSet rs = stmt.executeQuery()) {
//                if (rs.next()) {
//                    return rs.getInt("warehouse_id") + "," +
//                            rs.getString("name") + "," +
//                            rs.getString("location");
//                }
//            }
//        } catch (Exception e) {
//            e.printStackTrace();
//        }
//        return null;
//    }
//
//    // Получение склада по локации
//    public String getWarehouseByLocation(String location) {
//        String query = "SELECT warehouse_id, name, location FROM warehouse WHERE location = ?";
//        try (PreparedStatement stmt = connection.prepareStatement(query)) {
//            stmt.setString(1, location);
//            try (ResultSet rs = stmt.executeQuery()) {
//                if (rs.next()) {
//                    return rs.getInt("warehouse_id") + "," +
//                            rs.getString("name") + "," +
//                            rs.getString("location");
//                }
//            }
//        } catch (Exception e) {
//            e.printStackTrace();
//        }
//        return null;
//    }
//
//    // Получение склада по имени
//    public String getWarehouseByName(String name) {
//        String query = "SELECT warehouse_id, name, location FROM warehouse WHERE name = ?";
//        try (PreparedStatement stmt = connection.prepareStatement(query)) {
//            stmt.setString(1, name);
//            try (ResultSet rs = stmt.executeQuery()) {
//                if (rs.next()) {
//                    return rs.getInt("warehouse_id") + "," +
//                            rs.getString("name") + "," +
//                            rs.getString("location");
//                }
//            }
//        } catch (Exception e) {
//            e.printStackTrace();
//        }
//        return null;
//    }
}
