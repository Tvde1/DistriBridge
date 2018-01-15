package com.distribridge.shared.models;

import java.io.Serializable;

public class Stats implements Serializable {
    private int wins;
    private int losses;
    private int logins;

    public Stats(int wins, int losses, int logins) {
        this.wins = wins;
        this.losses = losses;
        this.logins = logins;
    }

    public static Stats Empty() {
        return new Stats(0, 0, 1);
    }

    public int getWins() {
        return wins;
    }

    public void setWins(int wins) {
        this.wins = wins;
    }

    public int getLosses() {
        return losses;
    }

    public void setLosses(int losses) {
        this.losses = losses;
    }

    public int getLogins() {
        return logins;
    }

    public void setLogins(int logins) {
        this.logins = logins;
    }
}
