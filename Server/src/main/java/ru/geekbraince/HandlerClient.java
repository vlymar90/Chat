package ru.geekbraince;

import java.io.DataInputStream;
import java.io.DataOutputStream;
import java.io.IOException;
import java.net.Socket;

public class HandlerClient {

   private Socket socket;
   private Server server;
   private DataInputStream in;
   private DataOutputStream out;
   private String name;


    HandlerClient(String name,final Socket socket,final Server server) {
        try {
            this.socket = socket;
            this.server = server;
            this.name = name;
            in = new DataInputStream(socket.getInputStream());
            out = new DataOutputStream(socket.getOutputStream());

            new Thread(new Runnable() {
                @Override
                public void run() {
                    try {
                    while (true) {
                        String str = in.readUTF();
                        if (str.equals("/end")) {
                            break;
                        }
                        server.broadcastMsg(getName() + str);
                        }
                        } catch (IOException e) {
                            e.printStackTrace();
                        }
                        finally {
                            try {
                                in.close();
                            } catch (IOException e) {
                                e.printStackTrace();
                            }
                            try {
                                out.close();
                            } catch (IOException e) {
                                e.printStackTrace();
                            }
                            try {
                                socket.close();
                            } catch (IOException e) {
                                e.printStackTrace();
                            }
                            server.unSubscribe(this);
                        }
                    }

            }).start();
        }
        catch (IOException e) {
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

    public String getName() {
        return name;
    }
}
