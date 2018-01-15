package com.distribridge.shared.enums;

public enum Suit {
    NT,
    S,
    H,
    D,
    C;

    public static Suit[] cardSuits() {
        return new Suit[]{ S, H, D, C };
    }
}
