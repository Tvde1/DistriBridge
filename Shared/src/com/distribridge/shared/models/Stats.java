package com.distribridge.shared.models;

import java.io.Serializable;

public class Stats implements Serializable {
    private int _wins;
    private int _losses;
    private int _logins;

    public Stats(int wins, int losses, int logins) {
        _wins = wins;
        _losses = losses;
        _logins = logins;
    }

    public int getWins() {
        return _wins;
    }

    public int getLosses() {
        return _losses;
    }

    public int getLogins() {
        return _logins;
    }

    public static Stats Empty() {
        return new Stats(0, 0, 1);
    }

    public void setWins(int wins) {
        _wins = wins;
    }

    public void setLosses(int losses) {
        _losses = losses;
    }

    public void setLogins(int logins) {
        _logins = logins;
    }
}
