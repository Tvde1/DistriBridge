package com.distribridge.shared.models;

import java.io.Serializable;
import java.util.ArrayList;
import java.util.Collections;
import java.util.List;

public class Hand implements Serializable {
    private ArrayList<Card> cards;

    Hand() {
        this(new ArrayList<>());
    }

    Hand(ArrayList<Card> cards) {
        this.cards = cards;
    }

    Hand(Hand hand) {
        cards = new ArrayList<>(hand.getCards());
    }

    public boolean removeCard(Card card) {
        if (!cards.contains(card)) {
            throw new RuntimeException();
        }
        return cards.remove(card);
    }

    public List<Card> getCards() {
        return Collections.unmodifiableList(cards);
    }

    void addCard(Card card) {
        if (cards.contains(card)) {
            throw new RuntimeException();
        }

        cards.add(card);
    }
}
