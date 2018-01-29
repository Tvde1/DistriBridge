package com.distribridge.shared.models;

import com.distribridge.shared.enums.Suit;
import org.junit.Before;
import org.junit.Test;

import java.util.ArrayList;

import static org.junit.Assert.assertSame;
import static org.junit.Assert.fail;

public class HandTest {
    private Hand hand;

    @Before
    public void init() {
        ArrayList<Card> cards = new ArrayList<>();

        cards.add(new Card(Suit.C, 2));
        cards.add(new Card(Suit.S, 14));

        hand = new Hand(cards);

        new Hand();
        new Hand(new Hand());
    }

    @Test
    public void removeCard() {
        try {
            hand.removeCard(new Card(Suit.D, 4));
            fail();
        } catch (Exception ignored) {

        }

        Card c = new Card(Suit.C, 5);
        hand.addCard(c);
        assertSame(3, hand.getCards().size());
        hand.removeCard(c);
        assertSame(2, hand.getCards().size());
    }

    @Test
    public void getCards() {
        assertSame(2, hand.getCards().size());
    }

    @Test
    public void addCard() throws Exception {
        Card c = new Card(Suit.H, 13);

        hand.addCard(c);
        assertSame(3, hand.getCards().size());

        try {
            hand.addCard(c);
            fail();
        } catch (Exception ignored) {

        }
    }

}