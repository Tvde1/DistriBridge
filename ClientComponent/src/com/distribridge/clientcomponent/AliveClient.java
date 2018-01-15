package com.distribridge.clientcomponent;

import com.distribridge.shared.interfaces.IAliveClient;

import java.rmi.RemoteException;
import java.rmi.server.UnicastRemoteObject;

public class AliveClient extends UnicastRemoteObject implements IAliveClient {
    AliveClient() throws RemoteException {
    }

    @Override
    public boolean isAlive() throws RemoteException {
        return true;
    }
}
