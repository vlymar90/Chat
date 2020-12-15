package ru.geekbraince;

import java.io.IOException;
import java.net.ServerSocket;
import java.net.Socket;
import java.util.Vector;

public class Server {
   private static int count = 1;
   private Vector<HandlerClient> clients;

    Server() {
        try {
            SQLHandler.connect();
            System.out.println("подключились к базе данных");
            ServerSocket server = new ServerSocket(8189);
            clients = new Vector();
            System.out.println("Ждём поключения клиента");

            while (true) {
                Socket socket = server.accept();
                System.out.println("Клиент подключился");
                HandlerClient client = new HandlerClient("Клиент №" + count + " : ",socket, this);
                subscribe(client);
                count++;
            }
        } catch (IOException e) {
            e.printStackTrace();
        }
    }

    public void subscribe(HandlerClient client) {
        clients.add(client);
    }

    public void unSubscribe(Runnable client) {
        clients.remove(client);
    }

    public void broadcastMsg(String str) {
        for(HandlerClient c : clients) {
            c.sendMsg(str);
        }
    }

}
