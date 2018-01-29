package com.distribridge.shared.enums;

import org.junit.Test;

import static org.junit.Assert.assertFalse;
import static org.junit.Assert.assertTrue;

public class GameStateTest {
    @Test
    public void isActive() {
        assertTrue(GameState.BIDDING.isActive());
        assertTrue(GameState.PLAYING.isActive());
        assertFalse(GameState.NO_GAME.isActive());
        assertFalse(GameState.IDLE.isActive());
    }

}