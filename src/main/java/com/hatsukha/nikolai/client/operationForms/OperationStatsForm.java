package com.hatsukha.nikolai.client.operationForms;

import org.jfree.chart.ChartFactory;
import org.jfree.chart.ChartPanel;
import org.jfree.chart.JFreeChart;
import org.jfree.data.general.DefaultPieDataset;

import javax.swing.*;
import java.awt.*;
import java.util.Map;

public class OperationStatsForm extends JFrame {
    private final Map<String,Integer> data;

    public OperationStatsForm(Map<String,Integer> data) {
        this.data = data;
        initialize();
    }

    private void initialize() {
        setTitle("Аналитика операций");
        setSize(800, 600);
        setDefaultCloseOperation(DISPOSE_ON_CLOSE);
        setLocationRelativeTo(null);

        JPanel mainPanel = new JPanel(new BorderLayout());

        // Создание диаграммы
        JFreeChart pieChart = createPieChart(data);
        ChartPanel chartPanel = new ChartPanel(pieChart);
        chartPanel.setBorder(BorderFactory.createEmptyBorder(15, 15, 15, 15));

        // Кнопка "Назад"
        JButton backButton = createStyledButton();
        backButton.addActionListener(e -> dispose());

        JPanel buttonPanel = new JPanel();
        buttonPanel.setBorder(BorderFactory.createEmptyBorder(10, 10, 10, 10));
        buttonPanel.add(backButton);

        mainPanel.add(chartPanel, BorderLayout.CENTER);
        mainPanel.add(buttonPanel, BorderLayout.SOUTH);

        add(mainPanel);
        setVisible(true);
    }

    private JFreeChart createPieChart(Map<String, Integer> operationData) {
        // Создаем набор данных для круговой диаграммы
        DefaultPieDataset dataset = new DefaultPieDataset();
        for (Map.Entry<String, Integer> entry : operationData.entrySet()) {
            dataset.setValue(entry.getKey(), entry.getValue());
        }

        // Создаем диаграмму
        return ChartFactory.createPieChart(
                "Распределение операций по типам", // Заголовок диаграммы
                dataset,                          // Данные
                true,                             // Легенда
                true,                             // Подсказки
                false                             // URL
        );
    }

    private JButton createStyledButton() {
        JButton button = new JButton("Назад");
        button.setFont(new Font("Arial", Font.BOLD, 18));
        button.setForeground(Color.WHITE);
        button.setBackground(new Color(100, 149, 237));
        button.setFocusPainted(false);
        button.setBorder(BorderFactory.createEmptyBorder(10, 20, 10, 20));
        button.setCursor(new Cursor(Cursor.HAND_CURSOR));

        button.addMouseListener(new java.awt.event.MouseAdapter() {
            public void mouseEntered(java.awt.event.MouseEvent evt) {
                button.setBackground(new Color(135, 206, 250));
            }

            public void mouseExited(java.awt.event.MouseEvent evt) {
                button.setBackground(new Color(100, 149, 237));
            }
        });

        return button;
    }

}
