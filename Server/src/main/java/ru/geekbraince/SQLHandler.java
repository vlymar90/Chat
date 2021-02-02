package ru.geekbraince;

import java.sql.*;
import java.util.ArrayList;

public class SQLHandler {

    private static Connection connection;
    private static Statement statement;

    public static void connect() {
        try {
            Class.forName("org.sqlite.JDBC");
            connection = DriverManager.getConnection("jdbc:sqlite:C:\\Users\\tatia\\OneDrive\\Рабочий стол\\java project\\JavaChat\\src\\ru\\geekbrains\\server\\database.db");
            statement = connection.createStatement();
        } catch (Exception e) {
            e.printStackTrace();
        }
    }

    public static void disconnect() {
        try {
            connection.close();
        } catch (SQLException e) {
            e.printStackTrace();
        }
    }

    public static String getNickByLoginAndPassword(String login, String password) {
        try {
            ResultSet rs = statement.executeQuery("SELECT nickname FROM users WHERE login ='" + login + "' AND password = '" + password + "'");
            if (rs.next()) {
                return rs.getString("nickname");
            }
        } catch (SQLException e) {
            e.printStackTrace();
        }
        return null;
    }
    public static boolean tryToRegister(String login, String password, String nickname) {
        try {
            statement.executeUpdate("INSERT INTO users (login, password, nickname) VALUES ('" + login + "','" + password + "','" + nickname + "')");
            return true;
        } catch (SQLException e) {
            return false;
        }
    }

    public static void changeNick(String newNick, String oldNick) {
        try {
            statement.executeUpdate("UPDATE users SET nickname = '"+newNick+"' WHERE nickname = '"+oldNick+"'");
        } catch (SQLException throwables) {
            throwables.printStackTrace();
        }
    }
}
