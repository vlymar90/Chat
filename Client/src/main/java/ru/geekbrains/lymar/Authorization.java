package ru.geekbrains.lymar;

import javax.swing.*;
import java.awt.*;
import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;

public class Authorization extends JFrame{
    JavaSwing javaSwing;
    Client client;


    Authorization(Client client) {
        this.client = client;
        setLayout(new FlowLayout());
        add(new JLabel("Авторизация"), BorderLayout.NORTH);
        setBounds(600, 200, 230, 180);
        JPanel login = new JPanel(new FlowLayout());
        JLabel label = new JLabel("Логин");
        final JTextField lineLogin = new JTextField(15);
        login.add(label);
        login.add(lineLogin);
        add(login);

        JPanel password = new JPanel(new FlowLayout());
        JLabel label1 = new JLabel("Пароль");
        final JPasswordField linePassword = new JPasswordField(15);
        password.add(label1);
        password.add(linePassword);
        add(password);

        JButton button = new JButton("авторизоваться");
        button.addActionListener(new ActionListener() {
            @Override
            public void actionPerformed(ActionEvent e) {
                client.sendMsg("/auth " + lineLogin.getText() + " " + linePassword.getText());
                lineLogin.setText("");
                linePassword.setText("");

            }
        });
        add(button);
        setVisible(true);
    }
    public void close() {
        this.setVisible(false);
    }
}
