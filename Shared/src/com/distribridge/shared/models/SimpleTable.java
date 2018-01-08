package com.distribridge.shared.models;

import com.distribridge.shared.enums.GameState;
import com.distribridge.shared.interfaces.ITableForClient;
import com.distribridge.shared.interfaces.ITableForServer;

import java.io.Serializable;
import java.rmi.Remote;
import java.rmi.server.UnicastRemoteObject;

public class SimpleTable implements Serializable {
    private int _id;
    private String _ownerName;
    private int _game;
    private int _playerCount;
    private GameState _gameState;
    private Remote _table;

    SimpleTable(int id, String ownerName, int game, int playerCount, GameState gameState, Table table) {
        _id = id;
        _ownerName = ownerName;
        _game = game;
        _playerCount = playerCount;
        _gameState = gameState;
        _table = table;
    }

    public int getId() {
        return _id;
    }

    public int getGame() {
        return _game;
    }

    public String getOwnerName() {
        return _ownerName;
    }

    public int getPlayerCount() {
        return _playerCount;
    }

    public GameState getGameState() {
        return _gameState;
    }

    public ITableForServer getTableForServer() {
        return (ITableForServer) _table;
    }

    public ITableForClient getTableForClient() {
        return (ITableForClient) _table;
    }

    void setPlayerCount(int playerCount) {
        _playerCount = playerCount;
    }

    void setId(int id) {
        _id = id;
    }
}
