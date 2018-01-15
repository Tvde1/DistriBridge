package com.distribridge.shared.interfaces;

import com.distribridge.shared.enums.GameState;

import java.rmi.Remote;
import java.rmi.RemoteException;

public interface ITableForSimpleTable extends Remote {
    int getPlayerCount() throws RemoteException;

    GameState getGameState() throws RemoteException;
}
