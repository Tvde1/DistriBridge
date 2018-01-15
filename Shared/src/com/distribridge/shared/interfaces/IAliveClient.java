package com.distribridge.shared.interfaces;

import java.io.Serializable;
import java.rmi.Remote;
import java.rmi.RemoteException;

public interface IAliveClient extends Remote, Serializable {
    boolean isAlive() throws RemoteException;
}
