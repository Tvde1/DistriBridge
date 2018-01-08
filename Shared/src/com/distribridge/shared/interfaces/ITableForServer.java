package com.distribridge.shared.interfaces;

import java.rmi.Remote;
import java.rmi.RemoteException;

public interface ITableForServer extends Remote {
    boolean isAlive() throws RemoteException;
}
