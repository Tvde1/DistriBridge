package com.distribridge.shared.interfaces;

import com.distribridge.shared.enums.Bid;
import com.distribridge.shared.enums.Card;
import com.distribridge.shared.enums.Direction;
import com.distribridge.shared.models.User;

import java.rmi.Remote;
import java.rmi.RemoteException;

public interface ITableForClient extends Remote {
    void playCard(Direction direction, Card card) throws RemoteException;

    void playBid(Direction direction, Bid bid) throws RemoteException;

    void join(User user) throws RemoteException;

    void leave(String username) throws RemoteException;

    void sendMessage(String username, String message) throws RemoteException;
}
