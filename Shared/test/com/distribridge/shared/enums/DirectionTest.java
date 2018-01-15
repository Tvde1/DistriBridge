package com.distribridge.shared.enums;

import org.junit.Test;

import static org.junit.Assert.*;

public class DirectionTest {
    @Test
    public void opposite() throws Exception {
        assertEquals(Direction.S, Direction.N.opposite());
    }

    @Test
    public void adjustToMe() throws Exception {
        assertEquals(2, Direction.S.adjustToMe(Direction.S));
    }

}