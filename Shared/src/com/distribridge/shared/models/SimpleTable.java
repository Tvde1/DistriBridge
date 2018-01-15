package com.distribridge.shared.models;

import com.distribridge.shared.Constants;
import com.distribridge.shared.enums.GameState;
import com.distribridge.shared.interfaces.ITableForClient;
import com.distribridge.shared.interfaces.ITableForServer;
import com.distribridge.shared.interfaces.ITableForSimpleTable;

import java.io.Serializable;
import java.rmi.RemoteException;

public class SimpleTable implements Serializable {
    private int id;
    private String ownerName;
    private int game;
    private int playerCount;
    private GameState gameState;
    private ITableForSimpleTable table;

    SimpleTable(int id, String ownerName, int game, int playerCount, GameState gameState, ITableForSimpleTable table) {
        this.id = id;
        this.ownerName = ownerName;
        this.game = game;
        this.playerCount = playerCount;
        this.gameState = gameState;
        this.table = table;
    }

    public int getId() {
        return id;
    }

    void setId(int id) {
        this.id = id;
    }

    public int getGame() {
        return game;
    }

    public String getOwnerName() {
        return ownerName;
    }

    public int getPlayerCount() {
        return playerCount;
    }

    void setPlayerCount(int playerCount) {
        this.playerCount = playerCount;
    }

    public GameState getGameState() {
        return gameState;
    }

    public ITableForServer getTableForServer() {
        return (ITableForServer) table;
    }

    public ITableForClient getTableForClient() {
        return (ITableForClient) table;
    }

    public void refresh() {
        try {
            gameState = table.getGameState();
            playerCount = table.getPlayerCount();
        } catch (RemoteException e) {
            Constants.logException(e);
        }
    }
}
