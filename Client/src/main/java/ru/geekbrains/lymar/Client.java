package ru.geekbrains.lymar;



import javax.swing.*;
import java.io.DataInputStream;
import java.io.DataOutputStream;
import java.io.IOException;
import java.net.Socket;


 public class Client {
        private Socket socket;
        private DataInputStream in;
        private DataOutputStream out;
        private JavaSwing javaSwing;
        private Authorization authorization;
        private JDialog dialog;
        private DefaultListModel<String> model;


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
                                    break;
                                }
                                if(str.equals("Registration complete")) {
                                      messageService("Registration complete");
                                      authorization.panelOn();
                                }
                                if(str.equals("nick change")) {
                                    messageService("complete");
                                    authorization.panelOn();
                                }
                                else {
                                    messageService(str);
                                }
                            }

                            while (true) {
                                String str = in.readUTF();
                                if(!str.startsWith("/")) {
                                    javaSwing.receiveMsg(str);
                                }

                                else
                                   if(str.startsWith("/list")) {
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
                }catch(IOException e){
                    e.printStackTrace();
                }
        }
        public void sendMsg (String str) {
            try {
                out.writeUTF(str);
            } catch (IOException e) {
                e.printStackTrace();
            }
        }

        public void finishConnection() throws IOException {
            in.close();
            out.close();
            socket.close();
            System.exit(0);
     }
     public void messageService(String message) {
            dialog = new JDialog();
            dialog.setBounds(600,200, 150, 150);
            dialog.setVisible(true);
            JLabel label = new JLabel(message);
            dialog.add(label);
        }
 }

