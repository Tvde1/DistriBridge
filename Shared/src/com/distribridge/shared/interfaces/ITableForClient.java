package com.distribridge.shared.interfaces;

import com.distribridge.shared.enums.Direction;
import com.distribridge.shared.enums.GameState;
import com.distribridge.shared.models.Bid;
import com.distribridge.shared.models.Card;
import com.distribridge.shared.models.Contract;
import com.distribridge.shared.models.Hand;
import fontyspublisher.IRemotePublisherForListener;

import java.rmi.Remote;
import java.rmi.RemoteException;
import java.util.HashMap;
import java.util.LinkedHashMap;
import java.util.Map;

public interface ITableForClient extends Remote {
    boolean playCard(Direction direction, Card card) throws RemoteException;

    void playBid(Direction direction, Bid bid) throws RemoteException;

    Direction join(IUser user) throws RemoteException;

    void leave(Direction username) throws RemoteException;

    void sendMessage(Direction username, String message) throws RemoteException;

    HashMap<Direction, IUser> getPlayers() throws RemoteException;

    IRemotePublisherForListener getRemotePublisher() throws RemoteException;

    Hand getDummyHand() throws RemoteException;

    GameState getGameState() throws RemoteException;

    Direction getLeader() throws RemoteException;

    LinkedHashMap<Bid, Direction> getBids() throws RemoteException;

    Hand getHand(Direction direction) throws RemoteException;

    Contract getContract() throws RemoteException;
}
