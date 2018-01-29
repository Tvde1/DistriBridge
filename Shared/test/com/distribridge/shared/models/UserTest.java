package com.distribridge.shared.models;

import com.distribridge.shared.interfaces.IAliveClient;
import org.junit.Before;
import org.junit.Test;

import java.rmi.RemoteException;

import static org.junit.Assert.*;

public class UserTest {
    private final static String userName = "Jan";
    private User user;
    private Stats stats;

    @Before
    public void setup() {
        stats = new Stats(1, 2, 3);
        user = new User(userName, stats);
    }

    @Test
    public void getUsername() {
        try {
            assertSame(userName, user.getUsername());
        } catch (RemoteException e) {
            fail();
        }
    }

    @Test
    public void getStats() {
        try {
            assertSame(stats, user.getStats());
        } catch (RemoteException e) {
            fail();
        }
    }

    @Test
    public void isAlive() {
        user.setAliveClient(null);
        try {
            assertFalse(user.isAlive());
        } catch (RemoteException e) {
            fail();
        }

        user.setAliveClient(new MockAliveClient());
        try {
            assertTrue(user.isAlive());
        } catch (RemoteException e) {
            fail();
        }
    }

    class MockAliveClient implements IAliveClient {
        @Override
        public boolean isAlive() throws RemoteException {
            return true;
        }
    }
}