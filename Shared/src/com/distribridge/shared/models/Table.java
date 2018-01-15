package com.distribridge.shared.models;

import com.distribridge.shared.Constants;
import com.distribridge.shared.enums.Direction;
import com.distribridge.shared.enums.GameState;
import com.distribridge.shared.interfaces.ITableForClient;
import com.distribridge.shared.interfaces.ITableForServer;
import com.distribridge.shared.interfaces.ITableForSimpleTable;
import com.distribridge.shared.interfaces.IUser;
import fontyspublisher.RemotePublisher;

import java.rmi.RemoteException;
import java.rmi.server.UnicastRemoteObject;
import java.util.*;

public class Table extends UnicastRemoteObject implements ITableForClient, ITableForServer, ITableForSimpleTable {
    private IUser owner;
    private HashMap<String, IUser> players;
    private HashMap<Direction, String> playerSeats;
    private Game game;
    private int id;
    private RemotePublisher remotePublisher;
    private int gamesPlayed = 0;

    public Table(IUser owner) throws RemoteException {
        super();
        this.owner = owner;
        players = new HashMap<>();
        playerSeats = new HashMap<>();

        try {
            remotePublisher = new RemotePublisher(new String[]{ Constants.PROPERTY_BID_PLAYED,
                    Constants.PROPERTY_CARD_PLAYED, Constants.PROPERTY_NEW_MESSAGE, Constants.PROPERTY_PLAYER_JOINED,
                    Constants.PROPERTY_PLAYER_LEFT, Constants.PROPERTY_GAME_STATE, Constants.PROPERTY_HAND,
                    Constants.PROPERTY_TURN
            });
        } catch (RemoteException e) {
            Constants.logException(e);
        }

        Timer t = new Timer();
        t.scheduleAtFixedRate(new TimerTask() {
            @Override
            public void run() {
                for (Map.Entry<Direction, String> userEntry : playerSeats.entrySet()) {
                    try {
                        players.get(userEntry.getValue()).isAlive();
                    } catch (Exception ex) {
                        try {
                            leave(userEntry.getKey());
                        } catch (RemoteException e) {
                            Constants.logException(e);
                        }
                    }
                }
            }
        }, (long) 1000 * 60, (long) 1000 * 60);
    }

    IUser getOwner() throws RemoteException {
        return owner;
    }

    public HashMap<Direction, IUser> getPlayers() throws RemoteException {
        HashMap<Direction, IUser> returnMap = new HashMap<>();
        for (Map.Entry<Direction, String> set : playerSeats.entrySet()) {
            returnMap.put(set.getKey(), players.get(set.getValue()));
        }
        return returnMap;
    }

    Game getGame() throws RemoteException {
        return game;
    }

    public SimpleTable toSimpleTable() throws RemoteException {
        if (game == null) {
            return new SimpleTable(id, owner.getUsername(), 0, players.size(), GameState.NO_GAME, this);
        }
        return new SimpleTable(id, owner.getUsername(), game.getNumber(), players.size(), game.getState(), this);
    }

    public void setId(int id) throws RemoteException {
        this.id = id;
    }

    public void createGame() {
        game = Game.Empty(this);
    }

    private void startGame() throws RemoteException {
        Game game = Game.newGame(gamesPlayed++, new ArrayList<>(players.values()), this);
        this.game = game;
        game.start();
    }

    @Override
    public boolean playCard(Direction direction, Card card) throws RemoteException {
        return game.playCard(direction, card);
    }

    @Override
    public void playBid(Direction direction, Bid bid) throws RemoteException {
        game.playBid(direction, bid);
    }

    public Direction join(IUser user) throws RemoteException {
        Direction dir;

        if (!playerSeats.containsKey(Direction.N)) {
            dir = Direction.N;
        } else if (!playerSeats.containsKey(Direction.E)) {
            dir = Direction.E;
        } else if (!playerSeats.containsKey(Direction.S)) {
            dir = Direction.S;
        } else if (playerSeats.containsKey(Direction.W)) {
            throw new Error("Haha geen plek meer");
        } else {
            dir = Direction.W;
        }

        players.put(user.getUsername(), user);
        playerSeats.put(dir, user.getUsername());

        broadcast(Constants.PROPERTY_PLAYER_JOINED, user.getUsername());

        if (players.size() == 4) {
            startGame();
        }
        return dir;
    }

    @Override
    public void leave(Direction direction) throws RemoteException {
        String username = playerSeats.get(direction);
        remotePublisher.inform(Constants.PROPERTY_PLAYER_LEFT, null, username);
        playerSeats.remove(direction);
        players.remove(username);
    }

    @Override
    public void sendMessage(Direction username, String message) throws RemoteException {
        remotePublisher.inform(Constants.PROPERTY_NEW_MESSAGE, null, new Object[]{ username, message });
    }

    @Override
    public RemotePublisher getRemotePublisher() throws RemoteException {
        return remotePublisher;
    }

    @Override
    public Hand getDummyHand() throws RemoteException {
        if (game == null) return null;
        return game.getDummyHand();
    }

    void broadcast(String property, Object value) throws RemoteException {
        remotePublisher.inform(property, null, value);
    }

    @Override
    public boolean isAlive() throws RemoteException {
        return true;
    }

    @Override
    public int getPlayerCount() throws RemoteException {
        return players.size();
    }

    @Override
    public GameState getGameState() throws RemoteException {
        return game.getState();
    }

    @Override
    public Direction getLeader() {
        return game.getLeader();
    }

    @Override
    public LinkedHashMap<Bid, Direction> getBids() {
        return game.getBids();
    }

    @Override
    public Hand getHand(Direction direction) {
        Hand hand = game.getHand(direction);
        if (hand == null) {
            return null;
        }
        return new Hand(hand);
    }

    @Override
    public Contract getContract() throws RemoteException {
        return game.getContract();
    }
}
