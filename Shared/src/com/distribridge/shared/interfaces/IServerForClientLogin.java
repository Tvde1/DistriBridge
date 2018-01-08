package com.distribridge.shared.interfaces;

import com.distribridge.shared.models.User;

import java.rmi.Remote;
import java.rmi.RemoteException;

public interface IServerForClientLogin extends Remote {
    User login(String username, String password) throws RemoteException;

    User signup(String username, String password) throws RemoteException;
}
