package com.distribridge.shared.enums;

public enum Direction {
    N,
    E,
    S,
    W;

    public Direction opposite() {
        return values()[this.ordinal() ^ 2];
    }

    public int adjustToMe(Direction direction) {
        return (this.ordinal() + 2 - direction.ordinal()) % 4;
    }
}
