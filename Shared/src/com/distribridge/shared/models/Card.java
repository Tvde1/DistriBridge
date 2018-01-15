package com.distribridge.shared.models;

import com.distribridge.shared.enums.Direction;
import com.distribridge.shared.enums.Suit;

import java.io.Serializable;
import java.util.ArrayList;
import java.util.Comparator;
import java.util.List;

public class Card implements Serializable {
    private final Suit suit;
    private final int value;
    private Direction playedBy;

    public Card(Suit suit, int value) {
        this.suit = suit;
        this.value = value;
    }

    public static List<Card> sortCards(List<Card> cards, Suit trump) {
        ArrayList<Card> l = new ArrayList<>(cards);
        l.sort(Comparator.comparingInt((Card o) -> {
            int value = o.getSuit().ordinal() * 10 + o.getValue();
            if (o.getSuit() == trump) {
                value += 60;
            }
            return value;
        }));
        return l;
    }

    static Card getHighest(List<Card> cards, Suit trump) {
        if (cards.size() == 0) {
            return null;
        }
        Card highestCard = cards.get(0);
        for (Card card : cards) {
            if ((card.getSuit() == highestCard.getSuit() || card.getSuit() == trump) && highestCard.getValue() < card.getValue()) {
                highestCard = card;
            }
        }
        return highestCard;
    }

    public static Card[] allCards() {
        Card[] list = new Card[52];
        int counter = 0;
        for (int i = 14; i > 1; i--) {
            for (Suit suit : Suit.cardSuits()) {
                list[counter++] = new Card(suit, i);
            }
        }
        return list;
    }

    private Suit getSuit() {
        return suit;
    }

    public int getValue() {
        return value;
    }

    public Direction getPlayedBy() {
        return playedBy;
    }

    public void setPlayedBy(Direction direction) {
        this.playedBy = direction;
    }

    public String toPngName(boolean b) {
        String baseUrl = "/images/cards/";
        if (b) {
            baseUrl += "rotated/";
        }
        String v;
        switch (value) {
            case 11:
                v = "J";
                break;
            case 12:
                v = "Q";
                break;
            case 13:
                v = "K";
                break;
            case 14:
                v = "A";
                break;
            default:
                v = String.valueOf(value);
                break;

        }

        return baseUrl + v + this.suit.name() + ".png";
    }
}
