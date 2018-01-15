package com.distribridge.servercomponent;

import com.distribridge.shared.models.User;

public interface IDatabaseConnector {
    User createUser(String username, String password);

    User getUserWithCredentials(String username, String password);

    User getUser(String username);

    void editUser(User user);
}
