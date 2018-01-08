package com.distribridge.shared.models;

import com.distribridge.shared.enums.Bid;
import com.distribridge.shared.enums.Card;
import com.distribridge.shared.enums.Direction;
import com.distribridge.shared.enums.GameState;

import java.io.Serializable;
import java.rmi.RemoteException;
import java.util.*;

class Game implements Serializable {
    private int _number;
    private HashMap<Direction, Player> _players;
    private HashMap<Direction, Hand> _hands;
    private GameState _state;
    private Table _table;
    private Direction _turn;

    Game(int number, HashMap<Direction, Player> players, HashMap<Direction, Hand> hands, Table table) {
        _number = number;
        _players = players;
        _hands = hands;
        _table = table;
    }

    int getNumber() {
        return _number;
    }

    Hand getHand(Direction d) {
        return _hands.get(d);
    }

    Collection<Player> getPlayers() {
        return _players.values();
    }

    static Game newGame(int number, List<Player> players, Table table) {
        HashMap<Direction, Hand> hands = new HashMap<>();
        HashMap<Direction, Player> playersMap = new HashMap<>();

        int i = 0;
        for (Player p : players) {
            playersMap.put(Direction.values()[i++], p);
        }

        ArrayList<Card> allCards = new ArrayList<>();
        Collections.addAll(allCards, Card.values());

        Direction currentDirection = Direction.N;

        hands.put(Direction.N, new Hand());
        hands.put(Direction.E, new Hand());
        hands.put(Direction.S, new Hand());
        hands.put(Direction.W, new Hand());

        for (int j = 0; j < allCards.size(); j++) {
            Card randomCard = allCards.get((int) (Math.random() * allCards.size()));
            allCards.remove(randomCard);

            Hand hand = hands.get(currentDirection);
            hand.addCard(randomCard);

            hands.put(currentDirection, hand);
            switch (currentDirection) {
                case N:
                    currentDirection = Direction.E;
                    break;
                case E:
                    currentDirection = Direction.S;
                    break;
                case S:
                    currentDirection = Direction.W;
                    break;
                case W:
                    currentDirection = Direction.N;
                    break;
            }
        }

        return new Game(number, playersMap, hands, table);
    }

    void start() throws RemoteException {
        _state = GameState.Bidding;

        switch (getNumber() % 4) {
            case 0:
                _turn = Direction.W;
                break;
            case 1:
                _turn = Direction.N;
                break;
            case 2:
                _turn = Direction.E;
                break;
            case 3:
                _turn = Direction.S;
                break;
        }


        _table.broadcast("gameState", _state);
    }

    public void playCard(Direction direction, Card card) throws RemoteException {
        if (direction != _turn) {
            return; //TODO show error
        }
        _table.broadcast("cardPlayed", new Object[] { direction, card });
    }

    public void playBid(Direction direction, Bid bid) throws RemoteException {
        if (direction != _turn) {
            return; //TODO show error
        }
        _table.broadcast("bidPlayed", new Object[] { direction, bid });
    }

    public GameState getState() {
        return _state;
    }
}
