package com.hatsukha.nikolai.report;

import java.io.FileWriter;
import java.sql.Connection;
import java.sql.PreparedStatement;
import java.sql.ResultSet;

public class ReportGenerator {

    private final Connection connection;
    private final String filePath = System.getProperty("user.dir") + "\\report\\";

    public ReportGenerator(Connection connection) {
        this.connection = connection;
    }

    public boolean generateReport() {
        String query = "SELECT " +
                "po.operation_id, u.username AS user_name, w.name AS warehouse_name, " +
                "p.name AS product_name, po.operation_type, po.quantity, po.operation_date " +
                "FROM productoperations po " +
                "JOIN users u ON po.user_id = u.user_id " +
                "JOIN warehouse w ON po.warehouse_id = w.warehouse_id " +
                "JOIN products p ON po.product_id = p.product_id " +
                "ORDER BY po.operation_date DESC";

        try (PreparedStatement stmt = connection.prepareStatement(query);
             ResultSet rs = stmt.executeQuery();
             FileWriter writer = new FileWriter(filePath)) {

            // Заголовки
            writer.append("Operation ID,User Name,Warehouse Name,Product Name,Operation Type,Quantity,Operation Date\n");

            // Запись данных
            while (rs.next()) {
                writer.append(String.valueOf(rs.getInt("operation_id"))).append(",")
                        .append(rs.getString("user_name")).append(",")
                        .append(rs.getString("warehouse_name")).append(",")
                        .append(rs.getString("product_name")).append(",")
                        .append(rs.getString("operation_type")).append(",")
                        .append(String.valueOf(rs.getInt("quantity"))).append(",")
                        .append(rs.getTimestamp("operation_date").toString()).append("\n");
            }

            System.out.println("Отчет успешно сгенерирован: " + filePath);
            return true;

        } catch (Exception e) {
            System.err.println("Ошибка генерации отчета: " + e.getMessage());
            return false;
        }

    }

}
