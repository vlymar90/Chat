package ru.geekbrains.lymar;

import java.io.DataInputStream;
import java.io.DataOutputStream;
import java.io.IOException;
import java.net.Socket;


 public class Client {
        private Socket socket;
        private DataInputStream in;
        private DataOutputStream out;
        private JavaSwing javaSwing;



        Client(final JavaSwing javaSwing) {
            this.javaSwing = javaSwing;
            try {
                socket = new Socket("localhost", 8189);
                in = new DataInputStream(socket.getInputStream());
                out = new DataOutputStream(socket.getOutputStream());

                new Thread((new Runnable() {
                    public void run() {
                        while (true) {
                            try {
                                String str = in.readUTF();
                                javaSwing.receiveMsg(str);

                            } catch (IOException e) {
                                e.printStackTrace();
                            }
                        }
                    }})).start();

                }catch (IOException e) {
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

        }

