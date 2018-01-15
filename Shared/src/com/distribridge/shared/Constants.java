package com.distribridge.shared;

import java.rmi.NotBoundException;
import java.rmi.Remote;
import java.rmi.RemoteException;
import java.rmi.registry.LocateRegistry;
import java.rmi.registry.Registry;
import java.util.logging.Level;
import java.util.logging.Logger;

public class Constants {
    private final static String IP = "localhost";
    public final static int PORT = 9876;
    public final static String DATABASE_URL = "mongodb://distribridge:distribridge@ds235877.mlab.com:35877/distribridge";

    //Client view paths.
    public static final String VIEW_LOGIN = "/scenes/login.fxml";
    public static final String VIEW_MENU = "/scenes/menu.fxml";
    public static final String VIEW_TABLES = "/scenes/tables.fxml";
    public static final String VIEW_REGISTER = "/scenes/register.fxml";
    public static final String VIEW_GAME = "/scenes/game.fxml";

    public static final String PROPERTY_NEW_MESSAGE = "newMessage";
    public static final String PROPERTY_PLAYER_JOINED = "playerJoined";
    public static final String PROPERTY_PLAYER_LEFT = "playerLeft";
    public static final String PROPERTY_CARD_PLAYED = "cardPlayed";
    public static final String PROPERTY_BID_PLAYED = "bidPlayed";
    public static final String PROPERTY_GAME_STATE = "gameState";
    public static final String PROPERTY_HAND = "hand";
    public static final String PROPERTY_TURN = "turn";

    public static Remote getServer() {
        Registry registry;
        try {
            System.out.println();
            registry = LocateRegistry.getRegistry(Constants.IP, Constants.PORT);
            return registry.lookup("server");
        } catch (RemoteException e) {
            Logger.getGlobal().log(Level.SEVERE, "Could not get registry.");
        } catch (NotBoundException e) {
            Logger.getGlobal().log(Level.SEVERE, "Could not find server in registry.");
        }
        return null;
    }

    public static void logException(Exception e) {
        //lol
    }
}