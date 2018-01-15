package com.distribridge.shared.enums;

public enum GameState {
    NO_GAME,
    IDLE,
    BIDDING,
    PLAYING;

    public boolean isActive() {
        return this == BIDDING || this == PLAYING;
    }
}
