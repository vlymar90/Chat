package ru.geekbraince;

import java.io.DataInputStream;
import java.io.DataOutputStream;
import java.io.IOException;
import java.net.Socket;
import java.util.concurrent.ExecutorService;
import java.util.concurrent.Executors;

public class HandlerClient {
   private HandlerClient client;
   private Socket socket;
   private Server server;
   private DataInputStream in;
   private DataOutputStream out;
   private String nickName;
   private ExecutorService executorService;

    HandlerClient(final Socket socket, final Server server) {
        try {
            client = this;
            this.socket = socket;
            this.server = server;
            in = new DataInputStream(socket.getInputStream());
            out = new DataOutputStream(socket.getOutputStream());
            executorService = Executors.newSingleThreadExecutor();
            executorService.execute(new Runnable() {
                @Override
                public void run() {
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
                                        server.subscribe(getClient());
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
                            if (str.startsWith("/change")) {
                                String[] token = str.split(" ");
                                if (token.length == 3) {
                                    SQLHandler.changeNick(token[1], token[2]);
                                    sendMsg("nick change");
                                } else {
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
                    } finally {
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
                        server.unSubscribe(getClient());
                    }
                }
            });
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

    public String getNickName() {
        return nickName;
    }

    public HandlerClient getClient() {
        return client;
    }
}
