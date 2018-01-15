package com.distribridge.shared.interfaces;

import com.distribridge.shared.models.SimpleTable;

import java.rmi.Remote;
import java.rmi.RemoteException;
import java.util.List;

public interface IServerForClient extends Remote {
    IUser fetchUser(String username) throws RemoteException;

    List<SimpleTable> getTables() throws RemoteException;

    void logout(String username) throws RemoteException;
}
