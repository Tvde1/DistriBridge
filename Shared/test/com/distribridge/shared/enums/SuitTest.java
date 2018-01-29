package com.distribridge.shared.enums;

import org.junit.Test;

import static org.junit.Assert.assertArrayEquals;

public class SuitTest {
    @Test
    public void cardSuits() {
        Suit[] cards = new Suit[]{ Suit.S, Suit.H, Suit.D, Suit.C };
        assertArrayEquals(cards, Suit.cardSuits());
    }
}