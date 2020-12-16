package ru.geekbrains.lymar;

import java.io.DataInputStream;
import java.io.DataOutputStream;
import java.io.IOException;
import java.net.Socket;


 public class Client {
        private Socket socket;
        private DataInputStream in;
        private DataOutputStream out;
        private boolean isAuthorization = false;
        private JavaSwing javaSwing;
        private Authorization authorization;


        Client() {
            try {
                if (socket == null || socket.isClosed()) {
                    socket = new Socket("localhost", 8189);
                    in = new DataInputStream(socket.getInputStream());
                    out = new DataOutputStream(socket.getOutputStream());
                    authorization = new Authorization(this);

                    new Thread(() -> {
                        try {
                            while (true) {
                                String str = in.readUTF();
                                if (str.equals("/authok")) {
                                    authorization.close();
                                    javaSwing = new JavaSwing(this);
                                    break;
                                }
                            }
                            while (true) {
                                String str = in.readUTF();
                                javaSwing.receiveMsg(str);
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

     public boolean isAuthorization() {
         return isAuthorization;
     }


     public void finishConnection() throws IOException {
            in.close();
            out.close();
            socket.close();
            System.exit(0);
     }
 }

