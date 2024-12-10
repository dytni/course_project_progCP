package com.hatsukha.nikolai.client.utils;

import org.w3c.dom.Document;
import org.w3c.dom.Element;
import org.w3c.dom.NodeList;

import javax.swing.*;
import javax.swing.border.Border;
import javax.xml.parsers.DocumentBuilder;
import javax.xml.parsers.DocumentBuilderFactory;
import java.awt.*;
import java.io.File;
import java.util.HashMap;
import java.util.Map;

public class StyleUtils {
    private static final Map<String, Color> COLORS = new HashMap<>();

    static {
        try {
            loadStyles();
        } catch (Exception e) {
            System.err.println("Ошибка при загрузке стилей: " + e.getMessage());
            // Используем значения по умолчанию
            setDefaultColors();
        }
    }

    private static void setDefaultColors() {
        COLORS.put("HEADER_BG_COLOR", new Color(135, 206, 250));
        COLORS.put("HEADER_TEXT_COLOR", Color.BLACK);
        COLORS.put("SELECTION_BG_COLOR", new Color(173, 216, 230));
        COLORS.put("SELECTION_TEXT_COLOR", Color.BLACK);
        COLORS.put("GRID_COLOR", new Color(135, 206, 250));
        COLORS.put("TITLE_COLOR", new Color(173, 216, 230));
        COLORS.put("BUTTON_BG_COLOR", new Color(100, 149, 237));
        COLORS.put("BUTTON_HOVER_COLOR", new Color(135, 206, 250));
        COLORS.put("TEXT_FIELD_BG_COLOR", Color.WHITE);
        COLORS.put("TEXT_FIELD_TEXT_COLOR", Color.BLACK);
        COLORS.put("CURSOR_COLOR", Color.BLACK);
    }

    public static void loadStyles() {
        try {
            File file = new File("src/main/resources/settings.xml");
            if (!file.exists()) {
                throw new RuntimeException("Файл settings.xml не найден");
            }

            DocumentBuilderFactory factory = DocumentBuilderFactory.newInstance();
            DocumentBuilder builder = factory.newDocumentBuilder();
            Document doc = builder.parse(file);
            doc.getDocumentElement().normalize();

            NodeList colorNodes = doc.getElementsByTagName("color");
            for (int i = 0; i < colorNodes.getLength(); i++) {
                Element element = (Element) colorNodes.item(i);
                String name = element.getAttribute("name");
                String value = element.getTextContent();
                COLORS.put(name, Color.decode(value));
            }
        } catch (Exception e) {
            throw new RuntimeException("Ошибка загрузки стилей из settings.xml: " + e.getMessage());
        }
    }

    private static Color getColor(String name, Color defaultColor) {
        return COLORS.getOrDefault(name, defaultColor);
    }

    // Инициализация констант через getColor
    public static final Color HEADER_BG_COLOR = getColor("HEADER_BG_COLOR", new Color(135, 206, 250));
    public static final Color HEADER_TEXT_COLOR = getColor("HEADER_TEXT_COLOR", Color.BLACK);
    public static final Color SELECTION_BG_COLOR = getColor("SELECTION_BG_COLOR", new Color(173, 216, 230));
    public static final Color SELECTION_TEXT_COLOR = getColor("SELECTION_TEXT_COLOR", Color.BLACK);
    public static final Color GRID_COLOR = getColor("GRID_COLOR", new Color(135, 206, 250));
    public static final Color TITLE_COLOR = getColor("TITLE_COLOR", new Color(173, 216, 230));
    public static final Color BUTTON_BG_COLOR = getColor("BUTTON_BG_COLOR", new Color(100, 149, 237));
    public static final Color BUTTON_HOVER_COLOR = getColor("BUTTON_HOVER_COLOR", new Color(135, 206, 250));
    public static final Color TEXT_FIELD_BG_COLOR = getColor("TEXT_FIELD_BG_COLOR", Color.WHITE);
    public static final Color TEXT_FIELD_TEXT_COLOR = getColor("TEXT_FIELD_TEXT_COLOR", Color.BLACK);
    public static final Color CURSOR_COLOR = getColor("CURSOR_COLOR", Color.BLACK);


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
