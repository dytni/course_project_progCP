package com.hatsukha.nikolai.repository;

import java.sql.Connection;
import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.util.ArrayList;
import java.util.List;

public class ProductRepository {
    private final Connection connection;

    public ProductRepository() {
        this.connection = DatabaseConnection.getInstance().getConnection();
    }

    public boolean addProduct(String name, String description, String category, double weight, double volume) {
        String query = "INSERT INTO products (name, description, category, weight, volume, location) VALUES (?, ?, ?, ?, ?, ?)";
        try (PreparedStatement stmt = connection.prepareStatement(query)) {
            stmt.setString(1, name);
            stmt.setString(2, description);
            stmt.setString(3, category);
            stmt.setDouble(4, weight);
            stmt.setDouble(5, volume);
            stmt.setString(6, "Не указанно");
            stmt.executeUpdate();
            return true;
        } catch (Exception e) {
            e.printStackTrace();
            return false;
        }
    }

    public boolean updateProduct(int id, String name, String description, String category, double weight, double volume, String location) {
        String query = "UPDATE products SET name = ?, description = ?, category = ?, weight = ?, volume = ?, location = ? WHERE product_id = ?";
        try (PreparedStatement stmt = connection.prepareStatement(query)) {
            stmt.setString(1, name);
            stmt.setString(2, description);
            stmt.setString(3, category);
            stmt.setDouble(4, weight);
            stmt.setDouble(5, volume);
            stmt.setString(6, location);
            stmt.setInt(7, id);
            stmt.executeUpdate();
            return true;
        } catch (Exception e) {
            e.printStackTrace();
            return false;
        }
    }

    public boolean deleteProduct(int id) {
        String query = "DELETE FROM products WHERE product_id = ?";
        try (PreparedStatement stmt = connection.prepareStatement(query)) {
            stmt.setInt(1, id);
            stmt.executeUpdate();
            return true;
        } catch (Exception e) {
            e.printStackTrace();
            return false;
        }
    }

    public List<String> getAllProducts() {
        String query = "SELECT product_id, name, description, category, weight, volume, location FROM products";
        List<String> products = new ArrayList<>();
        try (PreparedStatement stmt = connection.prepareStatement(query);
             ResultSet rs = stmt.executeQuery()) {
            while (rs.next()) {
                products.add(
                        rs.getInt("product_id") + "," +
                                rs.getString("name") + "," +
                                rs.getString("description") + "," +
                                rs.getString("category") + "," +
                                rs.getDouble("weight") + "," +
                                rs.getDouble("volume") + "," +
                                rs.getString("location")
                );
            }
        } catch (Exception e) {
            e.printStackTrace();
        }
        return products;
    }
    // Установка location для продукта
    public boolean setProductLocation(int productId, String location) {
        String sql = "UPDATE Products SET location = ? WHERE product_id = ?";
        try (PreparedStatement stmt = connection.prepareStatement(sql)) {
            stmt.setString(1, location);
            stmt.setInt(2, productId);
            return stmt.executeUpdate() > 0;
        } catch (SQLException e) {
            e.printStackTrace();
            return false;
        }
    }
}
