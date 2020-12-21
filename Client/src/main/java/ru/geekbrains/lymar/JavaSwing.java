package ru.geekbrains.lymar;

import javax.swing.*;
import java.awt.*;
import java.awt.event.*;
import java.io.IOException;



public class JavaSwing extends JFrame {
    private JTextArea outText;
    private JTextField text;
    private Client client;
    private String title;
    private JTextArea listClient;

    public JavaSwing(final Client client, String title) {
        this.client = client;
        this.title = title;

        setDefaultCloseOperation(EXIT_ON_CLOSE);
        setBounds(600, 200, 400, 500);
        setTitle("Окно чата: " + title);
        setLayout(new BorderLayout());

        listClient = new JTextArea();
        listClient.setEditable(false);
        listClient.setMinimumSize(new Dimension(100, 450));
        listClient.setBackground(Color.LIGHT_GRAY);
        add(listClient, BorderLayout.WEST);

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
        if (outText.getText().equals("/end")) {
            try {
                client.finishConnection();
            } catch (IOException e) {
                e.printStackTrace();
            }
        }
    }

    public void clearText() {
        text.setText("");
        text.requestFocus();
    }

    public void listClientInfo(String[] list) {
        listClient.setText("");
        for(int i = 1; i < list.length; i++) {
            listClient.setText(listClient.getText() + list[i] + System.lineSeparator());
        }
    }
}
