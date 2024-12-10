package com.hatsukha.nikolai.client.operationForms;

import com.hatsukha.nikolai.client.ClientConnection;
import com.hatsukha.nikolai.client.clientForms.AdminMainForm;
import com.hatsukha.nikolai.client.clientForms.UserMainForm;
import com.hatsukha.nikolai.client.utils.StyleUtils;
import org.jfree.chart.ChartFactory;
import org.jfree.chart.ChartPanel;
import org.jfree.chart.JFreeChart;
import org.jfree.data.category.DefaultCategoryDataset;

import javax.swing.*;
import java.awt.*;
import java.util.ArrayList;
import java.util.List;

public class WarehouseOrdersChartForm extends JFrame {
    private final ClientConnection clientConnection;
    private final int userId;
    private final String role;

    public WarehouseOrdersChartForm(ClientConnection clientConnection, String role, int userId) {
        this.clientConnection = clientConnection;
        this.userId = userId;
        this.role = role;
        initialize();
    }

    private void initialize() {
        setTitle("Статистика заказов по складам");
        setSize(800, 600);
        setDefaultCloseOperation(DISPOSE_ON_CLOSE);
        setLocationRelativeTo(null);

        JPanel mainPanel = new JPanel(new BorderLayout());
        DefaultCategoryDataset dataset = fetchWarehouseOrdersData();
        JFreeChart barChart = ChartFactory.createBarChart(
                "Заказы по складам",
                "Склады",
                "Количество заказов",
                dataset
        );

        ChartPanel chartPanel = new ChartPanel(barChart);
        chartPanel.setPreferredSize(new Dimension(800, 500));
        chartPanel.setBorder(StyleUtils.createStyledLineBorder());

        JButton backButton = StyleUtils.createStyledButton("Назад");
        backButton.addActionListener(e ->{
            dispose();
            if (role.equals("USER")) {
                new UserMainForm(clientConnection, userId).show();
            }else {
                new AdminMainForm(clientConnection, userId).show();
            }
        });

        JPanel buttonPanel = new JPanel();
        buttonPanel.setBorder(BorderFactory.createEmptyBorder(10, 10, 10, 10));
        buttonPanel.add(backButton);

        mainPanel.add(chartPanel, BorderLayout.CENTER);
        mainPanel.add(buttonPanel, BorderLayout.SOUTH);

        add(mainPanel);
        setVisible(true);
    }

    private DefaultCategoryDataset fetchWarehouseOrdersData() {
        DefaultCategoryDataset dataset = new DefaultCategoryDataset();

        clientConnection.send("GET_ORDERS_BY_WAREHOUSE");

        try {
            int rowCount = Integer.parseInt(clientConnection.receive());
            List<String[]> data = new ArrayList<>();

            for (int i = 0; i < rowCount; i++) {
                String[] row = clientConnection.receive().split(",");
                data.add(row);
            }
            for (String[] row : data) {
                String warehouseName = row[0];
                int totalOrders = Integer.parseInt(row[1]);
                dataset.addValue(totalOrders, "Заказы", warehouseName);
            }
        } catch (NumberFormatException e) {
            JOptionPane.showMessageDialog(this, "Ошибка загрузки данных!", "Ошибка", JOptionPane.ERROR_MESSAGE);
        }

        return dataset;
    }
}
