package com.distribridge.shared.interfaces;

import com.distribridge.shared.models.SimpleTable;
import com.distribridge.shared.models.Table;
import com.distribridge.shared.models.User;

import java.rmi.Remote;
import java.rmi.RemoteException;
import java.util.List;

public interface IServerForClient extends Remote {
    User fetchUser(String username) throws RemoteException;

    List<SimpleTable> getTables() throws RemoteException;

    void logout(String username) throws RemoteException;
}
