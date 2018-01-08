package com.distribridge.shared.models;

import java.io.Serializable;

public class Player implements Serializable {
    private User _user;

    Player(User user) {
        _user = user;
    }


    User getUser() {
        return _user;
    }

    void setUser(User user) {
        _user = user;
    }
}
