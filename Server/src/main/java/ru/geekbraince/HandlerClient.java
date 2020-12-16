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
   private String nickName;


    HandlerClient(final Socket socket, final Server server) {
        try {
            this.socket = socket;
            this.server = server;
            in = new DataInputStream(socket.getInputStream());
            out = new DataOutputStream(socket.getOutputStream());

            new Thread(() -> {
                    try {
                        while (true) {
                            String str = in.readUTF();
                            String [] token  = str.split(" ");
                            if(token.length >= 3 && token[0].equals("/auth")) {
                                String nickNameBD = SQLHandler.getNickByLoginAndPassword(token[1], token[2]);
                                if(nickNameBD != null) {
                                    sendMsg("/authok");
                                    server.subscribe(this);
                                    nickName = nickNameBD;
                                    break;
                                }
                            }
                        }

                    while (true) {
                        String str = in.readUTF();
                        if (str.equals("/end")) {
                            break;
                        }
                        server.broadcastMsg(getNickName() + ": " + str);
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
            }).start();
    }catch (IOException e) {
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

    public String getNickName() {
        return nickName;
    }
}
