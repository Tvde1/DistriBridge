package com.distribridge.servercomponent;

import com.distribridge.shared.models.Stats;
import com.distribridge.shared.models.User;

public class MockDatabaseConnector implements IDatabaseConnector {
    @Override
    public User createUser(String username, String password) {
        return new User(username, Stats.Empty());
    }

    @Override
    public User getUserWithCredentials(String username, String password) {
        return new User(username, Stats.Empty());
    }

    @Override
    public User getUser(String username) {
        return new User(username, Stats.Empty());
    }

    @Override
    public void editUser(User user) {

    }
}
