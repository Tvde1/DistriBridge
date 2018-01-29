package com.distribridge.shared.models;

import com.distribridge.shared.enums.Direction;
import com.distribridge.shared.enums.Suit;
import javafx.util.Pair;

import java.io.Serializable;
import java.util.LinkedHashMap;

public class Bid implements Serializable {
    private Suit suit;
    private int value;
    private boolean isPass;
    private boolean isDouble;
    private boolean isRedouble;

    private Bid(Suit suit, int value, boolean isPass, boolean isDouble, boolean isRedouble) {
        this.suit = suit;
        this.value = value;
        this.isPass = isPass;
        this.isDouble = isDouble;
        this.isRedouble = isRedouble;
    }

    Bid(Suit suit, int value) {
        if (suit == null) {
            throw new IllegalArgumentException();
        }
        if (value < 1 || value > 7) {
            throw new IllegalArgumentException();
        }

        this.suit = suit;
        this.value = value;
        this.isPass = false;
        this.isDouble = false;
        this.isRedouble = false;
    }

    public static Bid Pass() {
        return new Bid(null, -1, true, false, false);
    }

    public static Bid Double() {
        return new Bid(null, -1, false, true, false);
    }

    public static Bid Redouble() {
        return new Bid(null, -1, false, false, true);
    }

    public static Bid[] allBids() {
        Bid[] allBids = new Bid[35];
        int counter = 0;
        for (int i = 1; i < 8; i++) {
            for (Suit suit : Suit.values()) {
                allBids[counter++] = new Bid(suit, i);
            }
        }
        return allBids;
    }

    public static boolean canDouble(LinkedHashMap<Bid, Direction> rawBids, Direction direction) {
        Contract contract = new Contract(rawBids);
        return contract.getLastBid() != null && contract.getDirection().ordinal() % 2 != direction.ordinal() % 2 && !contract.getIsDoubled() && !contract.getIsRedoubled();
    }

    public static boolean canRedouble(LinkedHashMap<Bid, Direction> rawBids, Direction direction) {
        Contract contract = new Contract(rawBids);
        return contract.getLastBid() != null && contract.getDirection().ordinal() % 2 == direction.ordinal() % 2 && contract.getIsDoubled() && !contract.getIsRedoubled();
    }

    public static Pair<Bid, Direction> getLastBid(LinkedHashMap<Bid, Direction> bids) {
        Contract contract = new Contract(bids);
        return contract.getLastBid();
    }

    public Suit getSuit() {
        return suit;
    }

    public int getValue() {
        return value;
    }

    public int rank() {
        return (value - 1) * 5 + 5 - suit.ordinal();
    }

    public boolean getIsPass() {
        return isPass;
    }

    public boolean getIsDouble() {
        return isDouble;
    }

    public boolean getIsRedouble() {
        return isRedouble;
    }
}
