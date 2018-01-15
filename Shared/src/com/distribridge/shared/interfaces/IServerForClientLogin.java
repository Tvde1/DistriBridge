package com.distribridge.shared.interfaces;

import java.rmi.Remote;
import java.rmi.RemoteException;

public interface IServerForClientLogin extends Remote {
    IUser login(String username, String password, IAliveClient client) throws RemoteException;

    IUser signup(String username, String password, IAliveClient client) throws RemoteException;
}
