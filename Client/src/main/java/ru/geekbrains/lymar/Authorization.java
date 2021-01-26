package ru.geekbrains.lymar;

import javax.swing.*;
import java.awt.*;
import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;
import java.util.concurrent.ExecutorService;
import java.util.concurrent.Executors;
import java.util.concurrent.atomic.AtomicInteger;

public class Authorization extends JFrame{
    public boolean isTime = true;
    private Client client;
    private JPanel frameAuth;
    private JPanel frameReg;
    private JPanel frameChanceNick;
    private AtomicInteger counter = new AtomicInteger(60);
    private JLabel infoTime;
    ExecutorService service = Executors.newFixedThreadPool(3);
    TimeCounter timeCounter;

    Authorization(Client client) {
        timeCounter = new TimeCounter(this);
        this.client = client;
        setBounds(600, 200, 230, 300);

        frameAuth = new JPanel(new FlowLayout());
        frameAuth.add(new JLabel("Авторизация"), BorderLayout.NORTH);

        frameReg = new JPanel(new FlowLayout());
        frameReg.add(new JLabel("Регистрация"), BorderLayout.NORTH);

        frameChanceNick = new JPanel(new FlowLayout());

        JPanel login = new JPanel(new FlowLayout());
        JLabel label = new JLabel("Логин");
        final JTextField lineLogin = new JTextField(15);
        login.add(label);
        login.add(lineLogin);

        JPanel password = new JPanel(new FlowLayout());
        JLabel label1 = new JLabel("Пароль");
        final JPasswordField linePassword = new JPasswordField(15);
        password.add(label1);
        password.add(linePassword);

        JButton button = new JButton("авторизоваться");
        button.addActionListener(new ActionListener() {
            @Override
            public void actionPerformed(ActionEvent e) {
                isTime = false;
                client.sendMsg("/auth " + lineLogin.getText() + " " + linePassword.getText());
                lineLogin.setText("");
                linePassword.setText("");

            }
        });
        JButton button1 = new JButton("Зарегистрироваться");
        button1.addActionListener(new ActionListener() {
            @Override
            public void actionPerformed(ActionEvent e) {
                isTime = false;
                frameAuth.setVisible(false);
                add(frameReg);
                frameReg.setVisible(true);

            }
        });

        JButton button3 = new JButton("Сменить ник");
        button3.addActionListener(new ActionListener() {
            @Override
            public void actionPerformed(ActionEvent e) {
                isTime = false;
                frameAuth.setVisible(false);
                add(frameChanceNick);
            }
        });

        JLabel time = new JLabel("Время на авторизацию осталось:");
        infoTime = new JLabel("" + counter);


        frameAuth.add(login);
        frameAuth.add(password);
        frameAuth.add(button);
        frameAuth.add(button1);
        frameAuth.add(button3);
        frameAuth.add(time);
        frameAuth.add(infoTime);

        service.execute(timeCounter);

        JPanel loginReg = new JPanel(new FlowLayout());
        JLabel labelReg = new JLabel("Логин ");
        final JTextField lineLoginReg = new JTextField(15);
        loginReg.add(labelReg);
        loginReg.add(lineLoginReg);

        JPanel passwordReg = new JPanel(new FlowLayout());
        JLabel label1Reg = new JLabel("Пароль");
        final JTextField linePasswordReg = new JTextField(15);
        passwordReg.add(label1Reg);
        passwordReg.add(linePasswordReg);

        JPanel nick = new JPanel(new FlowLayout());
        JLabel label2 = new JLabel("Ник   ");
        final JTextField lineNick = new JTextField(15);
        nick.add(label2);
        nick.add(lineNick);
        add(nick);

        JButton button2 = new JButton("Регистрация");
        button2.addActionListener(new ActionListener() {
            @Override
            public void actionPerformed(ActionEvent e) {
                client.sendMsg("/registr " + lineLoginReg.getText() + " " + linePasswordReg.getText() + " " + lineNick.getText());
                lineLoginReg.setText("");
                linePasswordReg.setText("");
                lineNick.setText("");


            }
        });
        frameReg.add(loginReg);
        frameReg.add(passwordReg);
        frameReg.add(nick);
        frameReg.add(button2);

        JPanel panelChanceNick = new JPanel(new FlowLayout());
        JLabel labelChanceNick = new JLabel("новый ник");
        final JTextField newNick = new JTextField(15);
        JLabel labelChanceNick1 = new JLabel("старый ник");
        final JTextField oldNick = new JTextField(15);

        JButton button4 = new JButton("Изменить");
        button4.addActionListener(new ActionListener() {
            @Override
            public void actionPerformed(ActionEvent e) {
                client.sendMsg("/change " + newNick.getText() + " " + oldNick.getText());
                newNick.setText("");
                oldNick.setText("");
            }
        });
        frameChanceNick.add(labelChanceNick);
        frameChanceNick.add(newNick);
        frameChanceNick.add(labelChanceNick1);
        frameChanceNick.add(oldNick);
        frameChanceNick.add(button4);

        add(frameAuth);
        setVisible(true);
    }
    public void close() {
        this.setVisible(false);
    }

    public void panelOn() {
        frameAuth.setVisible(true);
        frameReg.setVisible(false);
        frameChanceNick.setVisible(false);
        isTime = true;
        service.execute(timeCounter);

    }

    public AtomicInteger getCounter() {
        return counter;
    }

    public JLabel getInfoTime() {
        return infoTime;
    }
}
