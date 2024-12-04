package com.hatsukha.nikolai.client.clientForms.user;

import com.hatsukha.nikolai.client.ClientConnection;
import com.hatsukha.nikolai.client.clientForms.AdminMainForm;
import com.hatsukha.nikolai.client.utils.StyleUtils;

import javax.swing.*;
import java.awt.*;

public class ManageUsersForm extends JFrame {
    private final ClientConnection clientConnection;
    private final UserTablePanel userTablePanel;
    private final int userId;

    public ManageUsersForm(ClientConnection clientConnection, int userId) {
        this.clientConnection = clientConnection;
        this.userId = userId;

        setTitle("Управление пользователями");
        setSize(1000, 600);
        setDefaultCloseOperation(EXIT_ON_CLOSE);
        setLocationRelativeTo(null);

        userTablePanel = new UserTablePanel(clientConnection);
        JPanel buttonPanel = createButtonPanel();

        add(userTablePanel, BorderLayout.CENTER);
        add(buttonPanel, BorderLayout.SOUTH);

        setVisible(true);
    }

    private JPanel createButtonPanel() {
        JPanel buttonPanel = new JPanel(new GridLayout(1, 4, 10, 10));
        buttonPanel.setBorder(BorderFactory.createEmptyBorder(10, 20, 10, 20));

        JButton addUserButton = StyleUtils.createStyledButton("Добавить пользователя");
        JButton editUserButton = StyleUtils.createStyledButton("Редактировать пользователя");
        JButton deleteUserButton = StyleUtils.createStyledButton("Удалить пользователя");
        JButton backButton = StyleUtils.createStyledButton("Назад");

        addUserButton.addActionListener(e -> new AddUserForm(clientConnection, userTablePanel));
        editUserButton.addActionListener(e -> showEditUserForm());
        deleteUserButton.addActionListener(e -> deleteUser());
        backButton.addActionListener(e -> {
            dispose();
            new AdminMainForm(clientConnection, userId).show();
        });

        buttonPanel.add(addUserButton);
        buttonPanel.add(editUserButton);
        buttonPanel.add(deleteUserButton);
        buttonPanel.add(backButton);

        return buttonPanel;
    }

    private void showEditUserForm() {
        JTable userTable = userTablePanel.getUserTable();
        int selectedRow = userTable.getSelectedRow();
        if (selectedRow == -1) {
            JOptionPane.showMessageDialog(this, "Выберите пользователя для редактирования!");
            return;
        }

        String userId = userTable.getValueAt(selectedRow, 0).toString();
        String currentUsername = userTable.getValueAt(selectedRow, 1).toString();
        String currentRole = userTable.getValueAt(selectedRow, 2).toString();

        JFrame frame = new JFrame("Редактировать пользователя");
        frame.setSize(400, 300);
        frame.setDefaultCloseOperation(DISPOSE_ON_CLOSE);
        frame.setLocationRelativeTo(this);

        JPanel panel = new JPanel(new GridLayout(3, 2, 10, 10));
        panel.setBorder(BorderFactory.createEmptyBorder(20, 20, 20, 20));

        JLabel usernameLabel = StyleUtils.createStyledLabel("Имя пользователя:");
        JTextField usernameField = StyleUtils.createStyledTextField();
        usernameField.setText(currentUsername);

        JLabel roleLabel = StyleUtils.createStyledLabel("Роль:");
        JComboBox<String> roleComboBox = new JComboBox<>(new String[]{"USER", "ADMIN"});
        roleComboBox.setSelectedItem(currentRole);

        JButton saveButton = StyleUtils.createStyledButton("Сохранить");
        JButton cancelButton = StyleUtils.createStyledButton("Отмена");

        panel.add(usernameLabel);
        panel.add(usernameField);
        panel.add(roleLabel);
        panel.add(roleComboBox);
        panel.add(saveButton);
        panel.add(cancelButton);

        saveButton.addActionListener(e -> {
            String newUsername = usernameField.getText();
            String newRole = (String) roleComboBox.getSelectedItem();

            if (newUsername.isEmpty()) {
                JOptionPane.showMessageDialog(frame, "Имя пользователя не может быть пустым!");
                return;
            }

            clientConnection.send("EDIT_USER");
            clientConnection.send(userId);
            clientConnection.send(newUsername);
            clientConnection.send(newRole);

            String response = clientConnection.receive();
            if ("SUCCESS".equals(response)) {
                JOptionPane.showMessageDialog(frame, "Данные пользователя успешно обновлены!");
                userTablePanel.loadUsers();
                frame.dispose();
            } else {
                JOptionPane.showMessageDialog(frame, "Ошибка при обновлении данных пользователя!");
            }
        });

        cancelButton.addActionListener(e -> frame.dispose());

        frame.add(panel);
        frame.setVisible(true);
    }

    private void deleteUser() {
        JTable userTable = userTablePanel.getUserTable();
        int selectedRow = userTable.getSelectedRow();
        if (selectedRow == -1) {
            JOptionPane.showMessageDialog(this, "Выберите пользователя для удаления!");
            return;
        }

        String userId = userTable.getValueAt(selectedRow, 0).toString();

        int confirmation = JOptionPane.showConfirmDialog(this, "Вы уверены, что хотите удалить пользователя?", "Подтверждение", JOptionPane.YES_NO_OPTION);
        if (confirmation == JOptionPane.YES_OPTION) {
            clientConnection.send("DELETE_USER");
            clientConnection.send(userId);

            String response = clientConnection.receive();
            if ("SUCCESS".equals(response)) {
                JOptionPane.showMessageDialog(this, "Пользователь успешно удален!");
                userTablePanel.loadUsers();
            } else {
                JOptionPane.showMessageDialog(this, "Ошибка удаления пользователя!");
            }
        }
    }
}
