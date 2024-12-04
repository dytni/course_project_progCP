package com.hatsukha.nikolai.client.clientForms;

import com.hatsukha.nikolai.client.ClientConnection;
import com.hatsukha.nikolai.client.operationForms.GetOperationsByUserForm;
import com.hatsukha.nikolai.client.operationForms.OperationStatsForm;
import com.hatsukha.nikolai.client.productForms.ProductTablePanel;
import com.hatsukha.nikolai.client.productForms.old.ViewProductsForm;
import com.hatsukha.nikolai.client.utils.StyleUtils;

import javax.swing.*;
import java.awt.*;

public class UserMainForm extends BaseMainForm {
    private final int userId;

    public UserMainForm(ClientConnection clientConnection, int userId) {
        super(clientConnection, "USER");
        this.userId = userId;
    }

    @Override
    protected void addCustomComponents(JPanel panel, GridBagConstraints gbc, JFrame frame) {
        // Создание кнопок с использованием стилей
        JButton viewProductsButton = StyleUtils.createStyledButton("Просмотреть товары");
        JButton addOperationButton = StyleUtils.createStyledButton("Заказать товары");

        // Расположение кнопок
        gbc.gridy = 2;
        panel.add(viewProductsButton, gbc);
        gbc.gridy = 3;
        panel.add(addOperationButton, gbc);


        // Обработчики событий
        viewProductsButton.addActionListener(e -> {
            frame.dispose();
            new ViewProductsForm(clientConnection, "USER",userId).show();
        });

        addOperationButton.addActionListener(e -> {
            frame.dispose();
            new GetOperationsByUserForm(clientConnection, userId).show();
        });
    }

    public void show() {
        JFrame frame = new JFrame("Пользователь");
        showBase(frame);
    }
}
