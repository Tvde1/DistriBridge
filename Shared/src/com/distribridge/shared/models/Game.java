package com.distribridge.shared.models;

import com.distribridge.shared.Constants;
import com.distribridge.shared.enums.Direction;
import com.distribridge.shared.enums.GameState;
import com.distribridge.shared.interfaces.IUser;

import java.io.Serializable;
import java.rmi.RemoteException;
import java.util.*;

class Game implements Serializable {
    private int number;
    private HashMap<Direction, IUser> players;
    private HashMap<Direction, Hand> hands;
    private GameState state;
    private Table table;
    private Direction turn;
    private HashMap<Card, Direction> currentCards = new HashMap<>();
    private Contract contract;
    private LinkedHashMap<Bid, Direction> bids = new LinkedHashMap<>();

    Game(int number, HashMap<Direction, IUser> players, HashMap<Direction, Hand> hands, Table table) {
        this.number = number;
        this.players = players;
        this.hands = hands;
        this.table = table;
        state = GameState.NO_GAME;
    }

    static Game newGame(int number, List<IUser> players, Table table) {
        HashMap<Direction, Hand> hands = new HashMap<>();
        HashMap<Direction, IUser> playersMap = new HashMap<>();

        int i = 0;
        for (IUser p : players) {
            playersMap.put(Direction.values()[i++], p);
        }

        ArrayList<Card> allCards = new ArrayList<>();
        Collections.addAll(allCards, Card.allCards());

        Direction currentDirection = Direction.N;

        hands.put(Direction.N, new Hand());
        hands.put(Direction.E, new Hand());
        hands.put(Direction.S, new Hand());
        hands.put(Direction.W, new Hand());

        for (int j = 0; j < 52; j++) {
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

    public static Game Empty(Table table) {
        return new Game(-1, new HashMap<>(), new HashMap<>(), table);
    }

    int getNumber() {
        return number;
    }

    Hand getHand(Direction d) {
        return hands.get(d);
    }

    void start() throws RemoteException {
        for (Map.Entry<Direction, Hand> set : hands.entrySet()) {
            table.broadcast(Constants.PROPERTY_HAND, new Object[]{ set.getValue(), set.getValue() });
        }

        state = GameState.BIDDING;

        turn = Direction.values()[getNumber() % 4];
        table.broadcast(Constants.PROPERTY_GAME_STATE, state);
    }

    boolean playCard(Direction direction, Card card) throws RemoteException {
        if (direction != turn) {
            return false;
        }
        boolean result = getHand(direction).removeCard(card);
        if (!result) {
            return false;
        }
        table.broadcast(Constants.PROPERTY_CARD_PLAYED, new Object[]{ direction, card });
        currentCards.put(card, direction);
        nextTurn();
        if (currentCards.size() != 4) {
            return true;
        }

        Card highestCard = Card.getHighest(new ArrayList<>(currentCards.keySet()), contract.getSuit());
        turn = currentCards.get(highestCard);
        table.broadcast(Constants.PROPERTY_TURN, turn);
        return true;
    }

    void playBid(Direction direction, Bid bid) throws RemoteException {
        if (direction != turn) {
            return; //TODO show error
        }

        bids.put(bid, direction);
        table.broadcast(Constants.PROPERTY_BID_PLAYED, new Object[]{ direction, bid });

        if (bids.size() < 4) {
            nextTurn();
            return;
        }

        if (!Contract.isValid(bids)) {
            return;
        }

        contract = new Contract(bids);

        turn = contract.getDirection();
        nextTurn();

        state = GameState.PLAYING;

        table.broadcast(Constants.PROPERTY_GAME_STATE, state);
    }

    GameState getState() {
        return state;
    }

    Hand getDummyHand() {
        if (contract == null) {
            return null;
        }
        switch (contract.getDirection()) {
            case N:
                return getHand(Direction.S);
            case E:
                return getHand(Direction.W);
            case S:
                return getHand(Direction.N);
            case W:
                return getHand(Direction.E);
            default:
                return null;
        }
    }

    private void nextTurn() {
        turn = Direction.values()[(turn.ordinal() + 1) % 4];
    }

    public Direction getLeader() {
        return contract == null ? null : contract.getDirection();
    }

    public LinkedHashMap<Bid, Direction> getBids() {
        return new LinkedHashMap<>(bids);
    }

    public Contract getContract() {
        return contract;
    }
}
