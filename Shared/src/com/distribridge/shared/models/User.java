package com.distribridge.shared.models;

import com.distribridge.shared.interfaces.IAliveClient;
import com.distribridge.shared.interfaces.IUser;

import java.io.Serializable;
import java.rmi.RemoteException;

public class User implements IUser, Serializable {
    private String userName;
    private Stats stats;
    private IAliveClient aliveClient;

    public User(String userName, Stats stats) {
        this.userName = userName;
        this.stats = stats;
    }

    @Override
    public String getUsername() throws RemoteException {
        return userName;
    }


    @Override
    public Stats getStats() throws RemoteException {
        return stats;
    }

    public void setAliveClient(IAliveClient client) {
        aliveClient = client;
    }

    @Override
    public boolean isAlive() throws RemoteException {
        if (aliveClient == null) {
            return false;
        }
        return aliveClient.isAlive();
    }
}
