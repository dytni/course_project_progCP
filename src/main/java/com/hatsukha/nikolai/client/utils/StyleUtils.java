package com.hatsukha.nikolai.client.utils;

import javax.swing.*;
import javax.swing.border.Border;
import javax.swing.border.TitledBorder;
import java.awt.*;

public class StyleUtils {
    public static final Color HEADER_BG_COLOR = new Color(135, 206, 250); // Голубой цвет для шапки таблицы
    public static final Color HEADER_TEXT_COLOR = Color.BLACK; // Чёрный текст для шапки таблицы
    public static final Color SELECTION_BG_COLOR = new Color(173, 216, 230); // Светло-голубая подсветка строки
    public static final Color SELECTION_TEXT_COLOR = Color.BLACK; // Чёрный текст при выделении
    public static final Color GRID_COLOR = new Color(135, 206, 250); // Голубой цвет линий сетки
    public static final Color TITLE_COLOR = new Color(173, 216, 230); // Цвет для заголовков
    public static final Color BUTTON_BG_COLOR = new Color(100, 149, 237); // Основной голубой цвет кнопки
    public static final Color BUTTON_HOVER_COLOR = new Color(135, 206, 250); // Светло-голубой при наведении
    public static final Color TEXT_FIELD_BG_COLOR = Color.WHITE; // Белый фон текстового поля
    public static final Color TEXT_FIELD_TEXT_COLOR = Color.BLACK; // Чёрный текст в текстовом поле
    public static final Color CURSOR_COLOR = Color.BLACK; // Чёрный курсор

    public static JLabel createStyledLabel(String text) {
        JLabel label = new JLabel(text);
        label.setFont(new Font("Arial", Font.BOLD, 18));
        label.setForeground(HEADER_TEXT_COLOR);
        return label;
    }

    public static JTextField createStyledTextField(String text) {
        JTextField textField = new JTextField(text, 15);
        textField.setFont(new Font("Arial", Font.PLAIN, 16));
        textField.setBackground(TEXT_FIELD_BG_COLOR);
        textField.setForeground(TEXT_FIELD_TEXT_COLOR);
        textField.setCaretColor(CURSOR_COLOR);
        textField.setBorder(BorderFactory.createLineBorder(HEADER_BG_COLOR, 2));
        return textField;
    }

    public static JTable createStyledTable() {
        JTable table = new JTable();
        table.setFont(new Font("Arial", Font.PLAIN, 16));
        table.setRowHeight(30);
        table.getTableHeader().setFont(new Font("Arial", Font.BOLD, 18));
        table.getTableHeader().setBackground(TITLE_COLOR);
        table.getTableHeader().setForeground(HEADER_TEXT_COLOR);
        table.setSelectionBackground(SELECTION_BG_COLOR);
        table.setSelectionForeground(SELECTION_TEXT_COLOR);
        table.setGridColor(GRID_COLOR);
        table.setBackground(TEXT_FIELD_BG_COLOR);
        table.setForeground(TEXT_FIELD_TEXT_COLOR);
        table.setShowGrid(true);

        table.setAutoCreateRowSorter(true);

        return table;
    }

    public static JButton createStyledButton(String text) {
        JButton button = new JButton(text);
        button.setFont(new Font("Arial", Font.BOLD, 18));
        button.setForeground(TEXT_FIELD_BG_COLOR);
        button.setBackground(BUTTON_BG_COLOR);
        button.setFocusPainted(false);
        button.setCursor(new Cursor(Cursor.HAND_CURSOR));
        button.setBorder(BorderFactory.createEmptyBorder(10, 20, 10, 20));

        button.addMouseListener(new java.awt.event.MouseAdapter() {
            public void mouseEntered(java.awt.event.MouseEvent evt) {
                button.setBackground(BUTTON_HOVER_COLOR);
            }

            public void mouseExited(java.awt.event.MouseEvent evt) {
                button.setBackground(BUTTON_BG_COLOR);
            }
        });

        return button;
    }

    public static JTextField createStyledTextField() {
        JTextField textField = new JTextField(15);
        textField.setFont(new Font("Arial", Font.PLAIN, 16));
        textField.setBackground(TEXT_FIELD_BG_COLOR);
        textField.setForeground(TEXT_FIELD_TEXT_COLOR);
        return textField;
    }

    public static JPasswordField createStyledPasswordField() {
        JPasswordField passwordField = new JPasswordField(15);
        passwordField.setFont(new Font("Arial", Font.PLAIN, 16));
        passwordField.setBackground(TEXT_FIELD_BG_COLOR);
        passwordField.setForeground(TEXT_FIELD_TEXT_COLOR);
        passwordField.setCaretColor(CURSOR_COLOR);
        return passwordField;
    }

    public static Border createStyledLineBorder() {
        return BorderFactory.createLineBorder(HEADER_BG_COLOR, 2);
    }

}
