package ru.geekbrains.lymar;



import javax.swing.*;
import java.io.*;
import java.net.Socket;
import java.util.ArrayList;



public class Client {
    private Socket socket;
    private DataInputStream in;
    private DataOutputStream out;
    private JavaSwing javaSwing;
    private Authorization authorization;
    private JDialog dialog;
    private DefaultListModel<String> model;
    private File file;

    Client() {
        try {
            if (socket == null || socket.isClosed()) {
                socket = new Socket("localhost", 8189);
                in = new DataInputStream(socket.getInputStream());
                out = new DataOutputStream(socket.getOutputStream());
                authorization = new Authorization(this);
                model = new DefaultListModel<>();

                new Thread(() -> {
                    try {
                        while (true) {
                            String str = in.readUTF();
                            String[] token = str.split(" ", 2);
                            if (token[0].equals("/authok")) {
                                authorization.close();
                                javaSwing = new JavaSwing(this, token[1]);
                                this.file = new File("history_" + token[1]);
                                inputHistory();
                                break;
                            }
                            if (str.equals("Registration complete")) {
                                messageService("Registration complete");
                                authorization.panelOn();

                            }
                            if (str.equals("nick change")) {
                                messageService("complete");
                                authorization.panelOn();

                            } else {
                                messageService(str);
                            }
                        }

                        while (true) {
                            String str = in.readUTF();
                            if (!str.startsWith("/")) {
                                javaSwing.receiveMsg(str);
                                outHistory(str);
                            } else if (str.startsWith("/list")) {
                                model.clear();
                                String[] token = str.split(" ");
                                javaSwing.listClientInfo(token);
                            }

                        }

                    } catch (IOException e) {
                        e.printStackTrace();
                    }
                }).start();


            }
        } catch (IOException e) {
            e.printStackTrace();
        }
    }


    public void sendMsg(String str) {
        try {
            out.writeUTF(str);
        } catch (IOException e) {
            e.printStackTrace();
        }
    }
        public void finishConnection () throws IOException {
            in.close();
            out.close();
            socket.close();
            System.exit(0);
        }

        public void messageService(String message) {
            dialog = new JDialog();
            dialog.setBounds(600, 200, 150, 150);
            dialog.setVisible(true);
            JLabel label = new JLabel(message);
            dialog.add(label);
        }

        private void  outHistory(String message){
            try (FileWriter writer = new FileWriter(file, true)) {
                writer.write(message + System.lineSeparator());
            } catch (IOException e) {
                e.printStackTrace();
            }
        }

        private void inputHistory () {
            ArrayList<String> history = new ArrayList<>();
            String line = "";
            try (BufferedReader reader = new BufferedReader(new FileReader(file))) {
                do {
                    line = reader.readLine();
                    if (line != null) {
                        history.add(line);
                    }
                } while (line != null);

                if (history.size() < 100) {
                    for (int i = 0; i < history.size(); i++) {
                        javaSwing.receiveMsg(history.get(i));
                    }
                } else {
                    int index = history.size() - 100;
                    for (int i = index; index < history.size(); i++) {
                        javaSwing.receiveMsg(history.get(i));
                    }
                }
            } catch (IOException e) {
                javaSwing.getOutText().setText("Истории сообщений нет" + System.lineSeparator());
            }
        }
    }


