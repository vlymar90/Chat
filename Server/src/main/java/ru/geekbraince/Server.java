package ru.geekbraince;

import java.io.IOException;
import java.net.ServerSocket;
import java.net.Socket;
import java.util.Vector;

public class Server {

   private Vector<HandlerClient> clients;

    Server() {
        try {
            SQLHandler.connect();
            ServerSocket server = new ServerSocket(8189);
            clients = new Vector();
            System.out.println("Ждём поключения клиента");

            while (true) {
                Socket socket = server.accept();
                System.out.println("Клиент подключился");
                HandlerClient client = new HandlerClient(socket, this);
            }
        } catch (IOException e) {
            e.printStackTrace();
        }
        finally {
            SQLHandler.disconnect();
        }
    }

    public void subscribe(HandlerClient client) {
        clients.add(client);
    }

    public void unSubscribe(HandlerClient client) {
        clients.remove(client);
    }

    public void broadcastMsg(String str) {
        for(HandlerClient c : clients) {
            c.sendMsg(str);
        }
    }
    public void broadcastMsg(String str, String nick) {
        for(HandlerClient c : clients) {
            if(c.getNickName().equals(nick)) {
                c.sendMsg(str);
            }
        }
    }
}
