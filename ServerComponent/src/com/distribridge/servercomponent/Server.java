package com.distribridge.servercomponent;

import com.distribridge.shared.interfaces.IServerForClient;
import com.distribridge.shared.interfaces.IServerForClientLogin;
import com.distribridge.shared.interfaces.IServerForTable;
import com.distribridge.shared.models.SimpleTable;
import com.distribridge.shared.models.User;

import java.rmi.Remote;
import java.util.*;

public class Server implements IServerForClientLogin, IServerForClient, IServerForTable, Remote {

    private int tableCounter = 0;
    private HashMap<Integer, SimpleTable> _tables = new HashMap<>();
    private DatabaseConnector _databaseConnector = new DatabaseConnector();
    private HashMap<String, User> _users = new HashMap<>();

    Server() {
        System.out.println("man");
        //xd


        Timer t = new Timer();
        t.scheduleAtFixedRate(new TimerTask() {
            @Override
            public void run() { for (Map.Entry<Integer, SimpleTable> tableEntry : _tables.entrySet()) {
                    try {
                        tableEntry.getValue().getTableForServer().isAlive();
                    } catch(Exception ex) {
                        removeTable(tableEntry.getKey());
                    }
                }
            }
        }, 1000 * 60, 1000 * 60);
    }

    @Override
    public User fetchUser(String username) {
        return _databaseConnector.getUser(username);
    }

    @Override
    public void addWin(String username) {
        User user = _users.get(username);
        if (user == null) {
            return;
        }
        user.getStats().setWins(user.getStats().getWins() + 1);
    }

    @Override
    public void addLoss(String username) {
        User user = _users.get(username);
        if (user == null) {
            return;
        }
        user.getStats().setLosses(user.getStats().getLosses() + 1);
    }

    @Override
    public List<SimpleTable> getTables() {
        return new ArrayList<>(_tables.values());
    }

    @Override
    public void logout(String username) {
        User user = _users.get(username);
        if (user == null) {
            return;
        }
        _databaseConnector.editUser(user);
        _users.remove(username);
    }

    @Override
    public int registerTable(SimpleTable table) {
        _tables.put(tableCounter, table);
        return tableCounter++;
    }

    public void removeTable(int table) {
        if (!_tables.containsKey(table)) {
            throw new Error("Table id not present.");
        }
        _tables.remove(table);
    }

    @Override
    public User login(String username, String password) {
        User user = _databaseConnector.getUserWithCredentials(username, password);
        if (user != null) {
            _users.put(username, user);
            user.getStats().setLogins(user.getStats().getLogins() + 1);
        }
        return user;
    }

    @Override
    public User signup(String username, String password) {
        User user = _databaseConnector.createUser(username, password);
        if (user != null) {
            _users.put(username, user);
        }
        return user;
    }
}
