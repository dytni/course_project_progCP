package com.hatsukha.nikolai.client.clientForms.user;

import com.hatsukha.nikolai.client.ClientConnection;

import javax.swing.*;
import javax.swing.table.DefaultTableModel;
import javax.swing.table.TableRowSorter;
import java.awt.*;
import java.util.ArrayList;
import java.util.List;

public class UserTablePanel extends JPanel {
    private final ClientConnection clientConnection;
    private final JTable userTable;

    public UserTablePanel(ClientConnection clientConnection) {
        this.clientConnection = clientConnection;
        this.userTable = createStyledTable();
        setLayout(new BorderLayout());

        JScrollPane scrollPane = new JScrollPane(userTable);
        scrollPane.setBorder(BorderFactory.createTitledBorder(
                BorderFactory.createLineBorder(new Color(100, 149, 237), 2), // Синий оттенок для рамки
                "Список пользователей",
                0,
                0,
                new Font("Arial", Font.BOLD, 16),
                new Color(70, 130, 180) // Синий текст заголовка
        ));

        add(scrollPane, BorderLayout.CENTER);
        loadUsers();
    }

    public JTable getUserTable() {
        return userTable;
    }

    public void loadUsers() {
        clientConnection.send("VIEW_USERS");

        try {
            int userCount = Integer.parseInt(clientConnection.receive());
            List<Object[]> rowData = new ArrayList<>();
            for (int i = 0; i < userCount; i++) {
                String[] data = clientConnection.receive().split(",");
                rowData.add(new Object[]{data[0], data[1], data[2]});
            }
            String[] columnNames = {"ID", "Имя пользователя", "Роль"};
            DefaultTableModel model = new DefaultTableModel(
                    rowData.toArray(new Object[0][]), columnNames
            );
            userTable.setModel(model);

            // Добавляем сортировку через TableRowSorter
            TableRowSorter<DefaultTableModel> sorter = new TableRowSorter<>(model);
            userTable.setRowSorter(sorter);

        } catch (NumberFormatException e) {
            JOptionPane.showMessageDialog(this, "Ошибка загрузки пользователей. Неверный формат ответа сервера!",
                    "Ошибка", JOptionPane.ERROR_MESSAGE);
            e.printStackTrace();
        }
    }

    private JTable createStyledTable() {
        JTable table = new JTable();
        table.setFont(new Font("Arial", Font.PLAIN, 16));
        table.setRowHeight(30);
        table.getTableHeader().setFont(new Font("Arial", Font.BOLD, 18));
        table.getTableHeader().setBackground(new Color(100, 149, 237)); // Светлый синий для шапки
        table.getTableHeader().setForeground(Color.WHITE); // Белый текст заголовка
        table.setSelectionBackground(new Color(173, 216, 230)); // Светло-голубая подсветка строки
        table.setSelectionForeground(Color.BLACK); // Чёрный текст при выделении
        table.setGridColor(new Color(211, 211, 211)); // Светло-серая сетка
        table.setBackground(Color.WHITE); // Белый фон
        table.setForeground(Color.BLACK); // Чёрный текст
        table.setShowGrid(true);

        // Включение сортировки
        table.setAutoCreateRowSorter(true);

        return table;
    }
}
