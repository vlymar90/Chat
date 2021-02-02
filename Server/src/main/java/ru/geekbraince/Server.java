package ru.geekbraince;

import java.io.IOException;
import java.net.ServerSocket;
import java.net.Socket;
import java.util.ArrayList;
import java.util.Arrays;
import java.util.Vector;

public class Server {
    private ArrayList<String> censorList = new ArrayList<>(Arrays.asList("сука","лошара","гондон","черный","шалава","никто"));
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
        client.sendMsg("Добро пожаловать в чат!");
        broadcastMsg(client.getNickName() + " в сети");
        broadcastMsg("/list " + listClient(clients));
    }

    public void unSubscribe(HandlerClient client) {
        clients.remove(client);
        client.sendMsg("Вы вышли из чата!");
        broadcastMsg(client.getNickName() + " ушёл из чата");
        broadcastMsg("/list " + listClient(clients));
    }

    public void broadcastMsg(String str) {
        String message = censor(str);
        for(HandlerClient c : clients) {
            c.sendMsg(message);
        }
    }
    public void broadcastMsg(String str, String nick) {
        String message = censor(str);
        for(HandlerClient c : clients) {
            if (c.getNickName().equals(nick)) {
                c.sendMsg(message);
                return;
            }
        }
        String [] token = str.split(" ");
        broadcastMsg(nick + " не существует или он не в сети.", token[0]);
    }

    public String listClient(Vector<HandlerClient> clients) {
        String list = "";
        for(int i = 0; i < clients.size(); i++) {
            list += clients.get(i).getNickName() + " ";
        }
        return list;
    }

    private String censor(String line) {
        String result = "";
        if(line != null) {
            String[] list = line.split(" ");
            int index = 0;
            while (index < list.length) {
                for (int i = 0; i < censorList.size(); i++) {
                    if (censorList.get(i).equals(list[index])) {
                        list[index] = "*****";
                    }
                }
                result += list[index] + " ";
                index++;
            }
            return result;
        }
        return null;
    }
}
