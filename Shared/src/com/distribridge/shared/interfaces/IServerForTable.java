package com.distribridge.shared.interfaces;

import com.distribridge.shared.models.SimpleTable;
import com.distribridge.shared.models.User;

import java.rmi.Remote;
import java.rmi.RemoteException;

public interface IServerForTable extends Remote {
    int registerTable(SimpleTable table) throws RemoteException;

    void removeTable(int table) throws RemoteException;

    User fetchUser(String ownerUsername) throws RemoteException;

    void addWin(String username) throws RemoteException;

    void addLoss(String username) throws RemoteException;
}
