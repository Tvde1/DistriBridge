package com.distribridge.clientcomponent;

import com.distribridge.shared.Constants;
import com.distribridge.shared.interfaces.IServerForClient;
import com.distribridge.shared.interfaces.IServerForClientLogin;
import com.distribridge.shared.models.SimpleTable;
import com.distribridge.shared.models.User;

import java.rmi.RemoteException;
import java.util.List;

public class ServerConnector {// implements IServerForClient, IServerForClientLogin {
    private IServerForClient _server;
    private IServerForClientLogin _loginServer;

    private String _username;

    ServerConnector() {
        _loginServer = (IServerForClientLogin) Constants.getServer();
        if (_loginServer == null) {
            System.exit(1);
        }
    }

//    @Override
    public User fetchUser(String username) throws RemoteException {
        if (_server == null) {
            throw new Error("Not logged in.");
        }
        return _server.fetchUser(username);
    }

//    @Override
public List<SimpleTable> getTables() throws RemoteException {
        if (_server == null) {
            throw new Error("Not logged in.");
        }
        return _server.getTables();
    }

//    @Override
    public void logout() throws RemoteException {
        if (_server == null) {
            throw new Error("Not logged in.");
        }
        _loginServer = (IServerForClientLogin) Constants.getServer();
        _server.logout(_username);
        _server = null;
    }

//    @Override
    public User login(String username, String password) throws RemoteException {
        if (_loginServer == null) {
            throw new Error("You are already logged in.");
        }
        User user = _loginServer.login(username, password);
        if (user != null) {
            getServer();
            _username = username;
        }
        return user;
    }

//    @Override
    public User signup(String username, String password) throws RemoteException {
        if (_loginServer == null) {
            throw new Error("You are already logged in.");
        }
        User user = _loginServer.signup(username, password);
        if (user != null) {
            getServer();
        }
        return user;
    }

    private void getServer() {
        _server = (IServerForClient) Constants.getServer();
        if (_server == null) {
            System.exit(1);
        }
        _loginServer = null;
    }
}
