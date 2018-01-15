package com.distribridge.shared.enums;

import org.junit.Test;

import static org.junit.Assert.*;

public class SuitTest {
    @Test
    public void cardSuits() throws Exception {
        Suit[] cards = new Suit[] { Suit.S, Suit.H, Suit.D, Suit.C };
        assertArrayEquals(cards, Suit.cardSuits());
    }
}