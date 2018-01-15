package com.distribridge.clientcomponent;

import com.distribridge.shared.Constants;
import com.distribridge.shared.interfaces.IAliveClient;
import com.distribridge.shared.interfaces.IServerForClient;
import com.distribridge.shared.interfaces.IServerForClientLogin;
import com.distribridge.shared.interfaces.IUser;
import com.distribridge.shared.models.SimpleTable;

import java.rmi.RemoteException;
import java.util.List;

public class ServerConnector {// implements IServerForClient, IServerForClientLogin {
    private IServerForClient server;
    private IServerForClientLogin loginServer;

    private String username;
    private boolean isLoggedIn;
    private IAliveClient aliveClient;

    ServerConnector() {
        loginServer = (IServerForClientLogin) Constants.getServer();
        if (loginServer == null) {
            System.exit(1);
        }
    }

    //    @Override
    public IUser fetchUser(String username) throws RemoteException {
        if (server == null) {
            throw new Error("Not logged in.");
        }
        return server.fetchUser(username);
    }

    //    @Override
    public List<SimpleTable> getTables() throws RemoteException {
        if (server == null) {
            throw new Error("Not logged in.");
        }
        return server.getTables();
    }

    //    @Override
    public void logout() throws RemoteException {
        if (server == null) {
            throw new Error("Not logged in.");
        }
        loginServer = (IServerForClientLogin) Constants.getServer();
        server.logout(username);
        aliveClient = null;
        isLoggedIn = false;
        server = null;
    }

    //    @Override
    public IUser login(String username, String password) throws RemoteException {
        if (loginServer == null) {
            throw new Error("You are already logged in.");
        }

        aliveClient = new AliveClient();
        IUser user = loginServer.login(username, password, aliveClient);
        if (user != null) {
            getServer();
            this.username = username;
        }
        isLoggedIn = true;
        return user;
    }

    //    @Override
    public IUser signup(String username, String password) throws RemoteException {
        if (loginServer == null) {
            throw new Error("You are already logged in.");
        }

        aliveClient = new AliveClient();
        IUser user = loginServer.signup(username, password, aliveClient);
        if (user != null) {
            getServer();
        }
        return user;
    }

    private void getServer() {
        server = (IServerForClient) Constants.getServer();
        if (server == null) {
            System.exit(1);
        }
        loginServer = null;
    }

    boolean getIsLoggedIn() {
        return isLoggedIn;
    }
}
