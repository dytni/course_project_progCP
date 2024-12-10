package com.hatsukha.nikolai.client.utils;

import javax.swing.*;
import javax.swing.border.Border;
import java.awt.*;

public class StyleUtils {
    public static final Color HEADER_BG_COLOR = new Color(135, 206, 250); // Голубой цвет для шапки таблицы
    public static final Color HEADER_TEXT_COLOR = Color.BLACK; // Чёрный текст для шапки таблицы
    public static final Color SELECTION_BG_COLOR = new Color(173, 216, 230); // Светло-голубая подсветка строки
    public static final Color SELECTION_TEXT_COLOR = Color.BLACK; // Чёрный текст при выделении
    public static final Color GRID_COLOR = new Color(135, 206, 250);
    public static final Color TITLE_COLOR = new Color(135, 206, 250);

    public static JLabel createStyledLabel(String text) {
        JLabel label = new JLabel(text);
        label.setFont(new Font("Arial", Font.BOLD, 18));
        label.setForeground(Color.BLACK);
        return label;
    }
    // Создание стилизованного текстового поля
    public static JTextField createStyledTextField(String text) {
        JTextField textField = new JTextField(text, 15);
        textField.setFont(new Font("Arial", Font.PLAIN, 16));
        textField.setBackground(Color.WHITE); // Белый фон
        textField.setForeground(Color.BLACK); // Чёрный текст
        textField.setCaretColor(Color.BLACK); // Чёрный курсор
        textField.setBorder(BorderFactory.createLineBorder(new Color(135, 206, 250), 2)); // Голубая рамка
        return textField;
    }
    public static JTable createStyledTable() {
        JTable table = new JTable();
        table.setFont(new Font("Arial", Font.PLAIN, 16));
        table.setRowHeight(30);
        table.getTableHeader().setFont(new Font("Arial", Font.BOLD, 18));
        table.getTableHeader().setBackground(new Color(173, 216, 230)); // Голубая шапка
        table.getTableHeader().setForeground(Color.BLACK); // Чёрный текст заголовка
        table.setSelectionBackground(new Color(135, 206, 235)); // Голубая подсветка строки
        table.setSelectionForeground(Color.BLACK); // Чёрный текст при выделении
        table.setGridColor(new Color(173, 216, 230)); // Голубые линии сетки
        table.setBackground(Color.WHITE); // Белый фон таблицы
        table.setForeground(Color.BLACK); // Чёрный текст ячеек
        table.setShowGrid(true);

        // Включение сортировки
        table.setAutoCreateRowSorter(true);

        return table;
    }

    public static JButton createStyledButton(String text) {
        JButton button = new JButton(text);
        button.setFont(new Font("Arial", Font.BOLD, 18));
        button.setForeground(Color.WHITE);
        button.setBackground(new Color(100, 149, 237)); // Основной голубой цвет
        button.setFocusPainted(false);
        button.setCursor(new Cursor(Cursor.HAND_CURSOR));
        button.setBorder(BorderFactory.createEmptyBorder(10, 20, 10, 20));

        button.addMouseListener(new java.awt.event.MouseAdapter() {
            public void mouseEntered(java.awt.event.MouseEvent evt) {
                button.setBackground(new Color(135, 206, 250)); // Светло-голубой при наведении
            }

            public void mouseExited(java.awt.event.MouseEvent evt) {
                button.setBackground(new Color(100, 149, 237)); // Основной голубой цвет
            }
        });

        return button;
    }

    public static JTextField createStyledTextField() {
        JTextField textField = new JTextField(15);
        textField.setFont(new Font("Arial", Font.PLAIN, 16));
        return textField;
    }

    public static JPasswordField createStyledPasswordField() {
        JPasswordField passwordField = new JPasswordField(15);
        passwordField.setFont(new Font("Arial", Font.PLAIN, 16));
        return passwordField;
    }

    public static Border createStyledLineBorder() {
        return BorderFactory.createLineBorder(HEADER_BG_COLOR, 2);
    }
}
