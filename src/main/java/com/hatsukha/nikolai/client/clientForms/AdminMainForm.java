package com.hatsukha.nikolai.client.clientForms;

import com.hatsukha.nikolai.client.ClientConnection;
import com.hatsukha.nikolai.client.clientForms.user.ManageUsersForm;
import com.hatsukha.nikolai.client.operationForms.OperationCrudForm;
import com.hatsukha.nikolai.client.operationForms.OperationStatsForm;
import com.hatsukha.nikolai.client.operationForms.WarehouseOrdersChartForm;
import com.hatsukha.nikolai.client.productForms.ProductCrudForm;
import com.hatsukha.nikolai.client.utils.ServerLogsForm;
import com.hatsukha.nikolai.client.utils.StyleUtils;
import com.hatsukha.nikolai.client.warehouseForms.WarehouseCrudForm;

import javax.swing.*;
import java.awt.*;
import java.util.HashMap;
import java.util.Map;

public class AdminMainForm extends BaseMainForm {

    private final int userId;

    public AdminMainForm(ClientConnection clientConnection, int userId) {
        super(clientConnection, "ADMIN");
        this.userId = userId;
    }

    @Override
    protected void addCustomComponents(JPanel panel, GridBagConstraints gbc, JFrame frame) {
        // Создание кнопок
        JButton viewProductsButton = StyleUtils.createStyledButton("Управление продуктами");
        JButton viewWarehousesButton = StyleUtils.createStyledButton("Управление складами");
        JButton manageUsersButton = StyleUtils.createStyledButton("Управление пользователями");
        JButton manageOperationsButton = StyleUtils.createStyledButton("Управление операциями");
        JButton viewStatsButton = StyleUtils.createStyledButton("Просмотреть статистику операций");
        JButton warehouseButton = StyleUtils.createStyledButton("Статистика по складам");
        JButton viewLogsButton = StyleUtils.createStyledButton("Просмотр логов");


        // Размещение кнопок
        gbc.gridy = 2;
        panel.add(viewProductsButton, gbc);

        gbc.gridy = 3;
        panel.add(viewWarehousesButton, gbc);

        gbc.gridy = 4;
        panel.add(manageUsersButton, gbc);

        gbc.gridy = 5;
        panel.add(manageOperationsButton, gbc);
        gbc.gridy = 6;
        panel.add(viewStatsButton, gbc);

        gbc.gridy = 7;
        panel.add(warehouseButton, gbc);

        gbc.gridy = 8;
        panel.add(viewLogsButton, gbc);

        viewLogsButton.addActionListener(e -> {
            frame.dispose();
            new ServerLogsForm(clientConnection,userId).setVisible(true);
        });

        viewStatsButton.addActionListener(e -> {
            Map<String, Integer> ordersData = fetchOrdersAnalytics();
            new OperationStatsForm(ordersData).setVisible(true);
        });


        // Обработчик для кнопки управления продуктами
        viewProductsButton.addActionListener(e -> {
            frame.dispose();
            new ProductCrudForm(clientConnection, role, userId).setVisible(true);
        });

        // Обработчик для кнопки управления складами
        viewWarehousesButton.addActionListener(e -> {
            frame.dispose();
            new WarehouseCrudForm(clientConnection).setVisible(true);
        });
        warehouseButton.addActionListener(e -> {
            frame.dispose();
            new WarehouseOrdersChartForm(clientConnection, "ADMIN", userId).setVisible(true);
        });


        manageUsersButton.addActionListener(e -> {
            frame.dispose();
            new ManageUsersForm(clientConnection, userId).setVisible(true);
        });

        manageOperationsButton.addActionListener(e -> {
            frame.dispose();
            new OperationCrudForm(clientConnection, userId).setVisible(true);
        });
    }
    private Map<String, Integer> fetchOrdersAnalytics() {
        Map<String, Integer> ordersData = new HashMap<>();
        try {
            clientConnection.send("GET_OPERATION_STATS");
            int categoryCount = Integer.parseInt(clientConnection.receive());
            for (int i = 0; i < categoryCount; i++) {
                String[] data = clientConnection.receive().split(",");
                String category = data[0];
                int count = Integer.parseInt(data[1]);
                ordersData.put(category, count);
            }
        } catch (Exception e) {
            JOptionPane.showMessageDialog(null, "Ошибка загрузки аналитики заказов!", "Ошибка", JOptionPane.ERROR_MESSAGE);
            e.printStackTrace();
        }
        return ordersData;
    }

    public void show() {
        JFrame frame = new JFrame("Администратор");
        showBase(frame);
    }
}
