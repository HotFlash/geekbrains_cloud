package kz.geekbrains.cloud.server.service.auth;

import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.extern.log4j.Log4j2;
import kz.geekbrains.cloud.common.Constants;

import java.io.File;
import java.nio.file.Paths;
import java.sql.*;
import java.util.HashMap;

@Log4j2
public class AuthService {

    private static Connection connection;
    private static Statement statement;

    @Data
    @AllArgsConstructor
    private class Entry {

        private String login;
        private String password;
        private int capacity;
    }

    private final HashMap<String, Entry> entries = new HashMap<>();

    public void insertUser(String login, String password, int capacity) throws SQLException {
        try (PreparedStatement ps = connection.prepareStatement("INSERT INTO users (login, password, capacity) VALUES (?, ?, ?)")) {
            ps.setString(1, login);
            ps.setString(2, password);
            ps.setInt(3, capacity);
            ps.executeUpdate();

            entries.put(login, new Entry(login, password, capacity));
            log.info("User " + login + " added to users.db");
            createFolder(Paths.get(Constants.SERVER_REP, login).toString());
        }
    }

    private String getUserByLogin(String login) {
        String password;
        int capacity;
        ResultSet rs;
        try (PreparedStatement ps = connection.prepareStatement("SELECT * FROM users WHERE login = ?")) {
            ps.setString(1, login);
            ps.execute();
            rs = ps.getResultSet();
            password = rs.getString("password");
            capacity = rs.getInt("capacity");
            ps.close();
            rs.close();
        } catch (SQLException e) {
            log.error("no login found cause: " + e);
            return null;
        }
        if (password == null) {
            return null;
        } else {
            if (!password.isEmpty())
            entries.put(login, new Entry(login, password, capacity));
            return password;
        }
    }


    public boolean authUser(String login, String password) {
        String rs = getUserByLogin(login);
        log.info(entries);
        if (rs == null) {
            return false;
        }
        if (rs.equals(password)) {
            createFolder(Paths.get(Constants.SERVER_REP, login).toString());
            return true;
        }
        return false;
    }

    public void start() {
        log.info("AuthService started");

        try {
            createServerFolders();
            connect();
            createTable();
        } catch (SQLException e) {
            log.error(e);
            log.debug(e.toString(), e);
        }
    }

    private void connect() throws SQLException {
        connection = DriverManager.getConnection("jdbc:sqlite:db/users.db");
        statement = connection.createStatement();
    }

    private void createTable() throws SQLException {
        statement.executeUpdate("CREATE TABLE IF NOT EXISTS users (\n" +
                "id INTEGER PRIMARY KEY AUTOINCREMENT NOT NULL,\n" +
                "login TEXT NOT NULL UNIQUE,\n" +
                "password TEXT NOT NULL,\n" +
                "capacity INT NOT NULL\n" +
                ");"
        );
    }

    private void readUsers() throws SQLException {
        try (ResultSet rs = statement.executeQuery("SELECT * FROM users")) {
            while (rs.next()) {
                entries.put(
                        rs.getString("login"),
                        new Entry(rs.getString("login"),
                                rs.getString("password"),
                                rs.getInt("capacity")));
            }

        }
    }

    private void createServerFolders() {
        createFolder(Constants.SERVER_REP);
    }

    private void createFolder(String pathname) {
        File folder = new File(pathname);
        if (!folder.exists()) {
            if (folder.mkdir()) {
                log.info("Folder " + folder.getPath() + " created");
            } else {
                log.error("can't create folder: " + folder.getPath());
            }
        }
    }

    public Boolean ChangeUserPassword(String login, String password) {
        try (PreparedStatement ps = connection.prepareStatement("UPDATE users SET password = ? WHERE login = ?;")) {
            ps.setString(1, password);
            ps.setString(2, login);
            ps.executeUpdate();

            entries.get(login).setPassword(password);
            log.info("Password for User " + login + " updated ");
            return true;
        } catch (SQLException e) {
            log.error("can't change the password cause: " + e);
            return false;
        }
    }

    public Integer ChangeUserCapacity(String login, int capacity) throws SQLException {
        try (PreparedStatement ps = connection.prepareStatement("UPDATE users SET capacity = ? WHERE login = ?;")) {
            ps.setInt(1, capacity);
            ps.setString(2, login);
            ps.executeUpdate();
            entries.get(login).setCapacity(capacity);
            log.info("Capacity for User " + login + " updated ");
        }
        return entries.get(login).getCapacity();
    }

    public Integer ReadCapacity(String login) throws SQLException {
        return entries.get(login).getCapacity();

    }



    public void stop() {
        log.info("AuthService stopped");
    }
}
