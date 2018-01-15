package com.distribridge.shared.models;

import com.distribridge.shared.interfaces.IAliveClient;
import com.distribridge.shared.models.Stats;
import com.distribridge.shared.models.User;
import org.junit.Before;
import org.junit.BeforeClass;
import org.junit.Test;
import org.junit.jupiter.api.BeforeAll;

import java.rmi.RemoteException;

import static org.junit.Assert.*;

public class UserTest {
    private User user;
    private final static String userName = "Jan";
    private Stats stats;

    @Before
    public void setup() {
        stats = new Stats(1, 2, 3);
        user = new User(userName, stats);
    }

    @Test
    public void getUsername() throws Exception {
        assertSame(userName, user.getUsername());
    }

    @Test
    public void getStats() throws Exception {
        assertSame(stats, user.getStats());
    }

    @Test
    public void isAlive() throws Exception {
        user.setAliveClient(null);
        assertFalse(user.isAlive());

        user.setAliveClient(new MockAliveClient());
        assertTrue(user.isAlive());
    }

    class MockAliveClient implements IAliveClient {
        @Override
        public boolean isAlive() throws RemoteException {
            return true;
        }
    }
}