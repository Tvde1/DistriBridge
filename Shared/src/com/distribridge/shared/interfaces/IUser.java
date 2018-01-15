package com.distribridge.shared.interfaces;

import com.distribridge.shared.models.Stats;

import java.rmi.Remote;
import java.rmi.RemoteException;

public interface IUser extends Remote {
    String getUsername() throws RemoteException;

    Stats getStats() throws RemoteException;

    void setAliveClient(IAliveClient client) throws RemoteException;

    boolean isAlive() throws RemoteException;
}
