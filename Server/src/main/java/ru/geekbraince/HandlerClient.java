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
                            if (str.startsWith("/auth")) {
                                String[] token = str.split(" ", 3);
                                if (token.length == 3) {
                                    String nickNameBD = SQLHandler.getNickByLoginAndPassword(token[1], token[2]);
                                    if (nickNameBD != null) {
                                        nickName = nickNameBD;
                                        sendMsg("/authok " + nickName);
                                        server.subscribe(this);
                                        break;
                                    } else {
                                        sendMsg("data used");
                                    }
                                } else {
                                    sendMsg("Invalid data entry");
                                }
                            }
                            if (str.startsWith("/registr")) {
                                String[] subStr = str.split(" ");
                                // /registration login pass nick
                                if (subStr.length == 4) {
                                    if (SQLHandler.tryToRegister(subStr[1], subStr[2], subStr[3])) {
                                        sendMsg("Registration complete");
                                    } else {
                                        sendMsg("Incorrect login/password/nickname");
                                    }
                                }
                            }
                            if(str.startsWith("/change")) {
                                String [] token = str.split(" ");
                                if(token.length == 3) {
                                    SQLHandler.changeNick(token[1], token[2]);
                                    sendMsg("nick change");
                                }
                                else {
                                    sendMsg("Nick is use");
                                }
                            }
                        }

                    while (true) {
                        String str = in.readUTF();
                        String[] token = str.split(" ", 3);
                        if (token[0].equals("/w")) {
                            server.broadcastMsg(nickName + " : " + token[2], token[1]);
                        } else {
                            server.broadcastMsg(getNickName() + ": " + str);
                        }
                        if (str.equals("/end")) {
                            break;
                        }

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
