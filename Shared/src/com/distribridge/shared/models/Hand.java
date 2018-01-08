package com.distribridge.shared.models;

import com.distribridge.shared.enums.Card;

import java.util.ArrayList;
import java.util.Collections;
import java.util.List;

class Hand {
    private ArrayList<Card> _cards;

    Hand() {

    }

    Hand(ArrayList<Card> cards) {
        if (cards.size() != 13) {
            throw new RuntimeException();
        }

        _cards = cards;
    }

    void removeCard(Card card) {
        if (!_cards.contains(card)) {
            throw new RuntimeException();
        }
        _cards.remove(card);
    }

    List<Card> getCards() {
        return Collections.unmodifiableList(_cards);
    }

    void addCard(Card card) {
        if (_cards.contains(card)) {
            throw new RuntimeException();
        }

        _cards.add(card);
    }
}
