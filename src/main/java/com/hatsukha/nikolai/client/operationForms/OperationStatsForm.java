package com.hatsukha.nikolai.client.operationForms;

import com.hatsukha.nikolai.client.utils.StyleUtils;
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

        JFreeChart pieChart = createPieChart(data);
        ChartPanel chartPanel = new ChartPanel(pieChart);
        chartPanel.setBorder(BorderFactory.createEmptyBorder(15, 15, 15, 15));

        JButton backButton = StyleUtils.createStyledButton("Назад");
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

}
