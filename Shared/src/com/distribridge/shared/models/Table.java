package com.distribridge.shared.models;

import com.distribridge.shared.enums.Bid;
import com.distribridge.shared.enums.Card;
import com.distribridge.shared.enums.Direction;
import com.distribridge.shared.enums.GameState;
import com.distribridge.shared.interfaces.ITableForClient;
import com.distribridge.shared.interfaces.ITableForServer;
import fontyspublisher.RemotePublisher;
import sun.reflect.generics.reflectiveObjects.NotImplementedException;

import java.rmi.RemoteException;
import java.rmi.server.UnicastRemoteObject;
import java.util.ArrayList;
import java.util.Collections;
import java.util.List;

public class Table extends UnicastRemoteObject implements ITableForClient, ITableForServer {
    private User _owner;
    private ArrayList<Player> _players;
    private Game _game;
    private int _id;
    private RemotePublisher _remotePublisher;
    private int gamesPlayed = 0;

    public Table(User owner) throws RemoteException {
        super();
        _owner = owner;
        _players = new ArrayList<>();

        try {
            _remotePublisher = new RemotePublisher(new String[] {"newMessage", "bid", "card"});
        } catch (RemoteException e) {
            e.printStackTrace();
        }
    }

    User getOwner() throws RemoteException {
        return _owner;
    }

    List<Player> getPlayers() throws RemoteException {
        return Collections.unmodifiableList(_players);
    }

    Game getGame() throws RemoteException {
        return _game;
    }

    public SimpleTable toSimpleTable() throws RemoteException {
        if (_game == null) {
            return new SimpleTable(_id, _owner.getUsername(), 0, _players.size(), GameState.NoGame, this);
        }
        return new SimpleTable(_id, _owner.getUsername(), _game.getNumber(), _players.size(), _game.getState(), this);
    }

    public void setId(int id) throws RemoteException {
        _id = id;
    }

    private void startGame() throws RemoteException {
        Game game = Game.newGame(gamesPlayed++, Collections.unmodifiableList(_players), this);
        _game = game;
        game.start();
    }

    private RemotePublisher getPublisher() throws RemoteException {
        return _remotePublisher;
    }

    @Override
    public void playCard(Direction direction, Card card) throws RemoteException {
        _game.playCard(direction, card);
        throw new NotImplementedException();
    }

    @Override
    public void playBid(Direction direction, Bid bid) throws RemoteException {
        _game.playBid(direction, bid);
    }

    public void join(User user) throws RemoteException {

        Player player = new Player(user);

        _players.add(player);

        if (_players.size() == 4) {
            startGame();
        }
    }

    @Override
    public void leave(String username) throws RemoteException {
        throw new NotImplementedException();
    }

    @Override
    public void sendMessage(String username, String message) throws RemoteException {
        _remotePublisher.inform("newMessage", null, new String[] {username, message});
    }

    void broadcast(String property, Object value) throws RemoteException {
        throw new NotImplementedException();
    }

    @Override
    public boolean isAlive() throws RemoteException {
        return false;
    }
}
