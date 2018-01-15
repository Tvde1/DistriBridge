package com.distribridge.servercomponent;

import com.distribridge.shared.interfaces.*;
import com.distribridge.shared.models.SimpleTable;
import com.distribridge.shared.models.User;

import java.rmi.Remote;
import java.rmi.RemoteException;
import java.util.*;

public class Server implements IServerForClientLogin, IServerForClient, IServerForTable, Remote {

    private int tableCounter = 0;
    private HashMap<Integer, SimpleTable> tables = new HashMap<>();
    private IDatabaseConnector databaseConnector = new DatabaseConnector();
    private HashMap<String, User> users = new HashMap<>();

    Server() {
        Timer t = new Timer();
        t.scheduleAtFixedRate(new TimerTask() {
            @Override
            public void run() {
                for (Map.Entry<Integer, SimpleTable> tableEntry : tables.entrySet()) {
                    try {
                        tableEntry.getValue().getTableForServer().isAlive();
                    } catch (Exception ex) {
                        removeTable(tableEntry.getKey());
                    }
                }
            }
        }, (long) 1000 * 60, (long) 1000 * 60);

        t.scheduleAtFixedRate(new TimerTask() {
            @Override
            public void run() {
                for (Map.Entry<String, User> userEntry : users.entrySet()) {
                    try {
                        userEntry.getValue().isAlive();
                    } catch (Exception ex) {
                        logout(userEntry.getKey());
                    }
                }
            }
        }, (long) 1000 * 60, (long) 1000 * 60);
    }

    @Override
    public IUser fetchUser(String username) {
        return databaseConnector.getUser(username);
    }

    @Override
    public void addWin(String username) throws RemoteException {
        User user = users.get(username);
        if (user == null) {
            return;
        }
        user.getStats().setWins(user.getStats().getWins() + 1);
    }

    @Override
    public void addLoss(String username) throws RemoteException {
        User user = users.get(username);
        if (user == null) {
            return;
        }
        user.getStats().setLosses(user.getStats().getLosses() + 1);
    }

    @Override
    public List<SimpleTable> getTables() {
        ArrayList<SimpleTable> returnList = new ArrayList<>();
        for (SimpleTable t : tables.values()) {
            t.refresh();
            returnList.add(t);
        }
        return returnList;
    }

    @Override
    public void logout(String username) {
        User user = users.get(username);
        if (user == null) {
            return;
        }
        databaseConnector.editUser(user);
        users.remove(username);
    }

    @Override
    public int registerTable(SimpleTable table) {
        tables.put(tableCounter, table);
        return tableCounter++;
    }

    public void removeTable(int table) {
        if (!tables.containsKey(table)) {
            throw new Error("Table id not present.");
        }
        tables.remove(table);
    }

    @Override
    public User login(String username, String password, IAliveClient client) throws RemoteException {
        User user = databaseConnector.getUserWithCredentials(username, password);
        if (user != null) {
            users.put(username, user);
            user.getStats().setLogins(user.getStats().getLogins() + 1);
            user.setAliveClient(client);
        }
        return user;
    }

    @Override
    public User signup(String username, String password, IAliveClient client) {
        User user = databaseConnector.createUser(username, password);
        if (user != null) {
            users.put(username, user);
            user.setAliveClient(client);
        }
        return user;
    }
}
