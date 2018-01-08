package com.distribridge.shared.models;

import java.io.Serializable;

public class User implements Serializable {
    private String _userName;
    private Stats _stats;

    public User(String userName, Stats stats) {
        _userName = userName;
        _stats = stats;
    }

    public String getUsername() {
        return _userName;
    }


    public Stats getStats() {
        return _stats;
    }
}
