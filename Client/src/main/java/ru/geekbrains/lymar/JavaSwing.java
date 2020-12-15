package ru.geekbrains.lymar;

import javax.swing.*;
import java.awt.*;
import java.awt.event.*;


public class JavaSwing extends JFrame {
    JTextArea outText;
    JTextField text;

    public JavaSwing() {
        final Client client = new Client(this);
        setDefaultCloseOperation(EXIT_ON_CLOSE);
        setBounds(600, 200, 400, 500);
        setTitle("Мой чат");
        setLayout(new BorderLayout());

        JPanel outPut = new JPanel(new BorderLayout());
        outText = new JTextArea();
        outText.setEditable(false);
        JScrollPane scrollPane = new JScrollPane(outText);
        outText.setLineWrap(true);
        scrollPane.setHorizontalScrollBarPolicy(ScrollPaneConstants.HORIZONTAL_SCROLLBAR_NEVER);
        scrollPane.setVerticalScrollBarPolicy(ScrollPaneConstants.VERTICAL_SCROLLBAR_ALWAYS);
        outPut.add(scrollPane);
        add(outPut, BorderLayout.CENTER);

        JPanel inputText = new JPanel(new BorderLayout());
        text = new JTextField();
        text.addKeyListener(new KeyAdapter() {
            @Override
            public void keyPressed(KeyEvent e) {
                int key = e.getKeyCode();
                if (key == KeyEvent.VK_ENTER) {
                    client.sendMsg(text.getText());
                    clearText();
                }
            }
        });

        JButton submit = new JButton("Отправить");
        submit.addActionListener(new ActionListener() {
            @Override
            public void actionPerformed(ActionEvent e) {
                client.sendMsg(text.getText());
                clearText();
            }
        });
        inputText.add(text, BorderLayout.CENTER);
        inputText.add(submit, BorderLayout.EAST);
        add(inputText, BorderLayout.SOUTH);

        setVisible(true);
    }
    public void receiveMsg(String str) {
        outText.setText(outText.getText() + str + "\n");
    }

    public void clearText() {
        text.setText("");
        text.requestFocus();
    }
}
