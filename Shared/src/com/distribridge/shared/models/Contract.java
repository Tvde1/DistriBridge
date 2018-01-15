package com.distribridge.shared.models;

import com.distribridge.shared.enums.Direction;
import com.distribridge.shared.enums.Suit;
import javafx.util.Pair;

import java.io.Serializable;
import java.util.LinkedHashMap;
import java.util.Map;

public class Contract implements Serializable {
    private final Suit suit;
    private final int value;
    private final boolean isDoubled;
    private final boolean isRedoubled;
    private final Direction direction;

    private Map.Entry<Bid, Direction> lastBid = null;

    public Contract(LinkedHashMap<Bid, Direction> bids) {
        boolean isDoubled = false;
        boolean isRedoubled = false;

        for (Map.Entry<Bid, Direction> entry : bids.entrySet()) {
            if (lastBid == null) {
                lastBid = entry;
            }
            if (entry.getKey().getIsDouble()) {
                isDoubled = true;
            } else if (entry.getKey().getIsRedouble()) {
                isRedoubled = true;
            } else if (!entry.getKey().getIsPass()) {
                lastBid = entry;
                isDoubled = false;
                isRedoubled = false;
            }
        }

        if (lastBid == null) {
            this.suit = null;
            this.value = -1;
            this.direction = null;
        } else {
            this.suit = lastBid.getKey().getSuit();
            this.value = lastBid.getKey().getValue();
            this.direction = lastBid.getValue();
        }
        this.isDoubled = isDoubled;
        this.isRedoubled = isRedoubled;
    }

    public static boolean isValid(Map<Bid, Direction> bids) {
        int passCounter = 0;
        for (Map.Entry<Bid, Direction> entry : bids.entrySet()) {
            if (entry.getKey().getIsPass()) {
                passCounter++;
                if (passCounter == 3) {
                    return true;
                }
            } else {
                passCounter = 0;
            }
        }
        return false;
    }

    public Suit getSuit() {
        return suit;
    }

    public int getValue() {
        return value;
    }

    public boolean getIsDoubled() {
        return isDoubled;
    }

    public boolean getIsRedoubled() {
        return isRedoubled;
    }

    public Direction getDirection() {
        return direction;
    }

    public boolean isRoundPass() {
        return suit == null && value == -1;
    }

    public Pair<Bid, Direction> getLastBid() {
        if (lastBid == null) {
            return null;
        }
        return new Pair<>(lastBid.getKey(), lastBid.getValue());
    }
}
