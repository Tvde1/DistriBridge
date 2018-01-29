package com.distribridge.shared.enums;

import org.junit.Test;

import static org.junit.Assert.assertEquals;

public class DirectionTest {
    @Test
    public void opposite() {
        assertEquals(Direction.S, Direction.N.opposite());
    }

    @Test
    public void adjustToMe() {
        assertEquals(2, Direction.S.adjustToMe(Direction.S));
    }

}