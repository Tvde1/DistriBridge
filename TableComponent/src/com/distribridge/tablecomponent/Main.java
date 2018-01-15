package com.distribridge.tablecomponent;

import com.distribridge.shared.Constants;
import com.distribridge.shared.interfaces.IServerForTable;
import com.distribridge.shared.interfaces.IUser;
import com.distribridge.shared.models.SimpleTable;
import com.distribridge.shared.models.Table;

import java.rmi.RemoteException;
import java.util.logging.Level;
import java.util.logging.Logger;

public class Main {
    private static Logger logger = Logger.getLogger(Main.class.getName());

    public static void main(String[] args) throws RemoteException {
        String name = args.length == 0 ? "Tvde1" : args[0];
        logger.log(Level.INFO, "Starting table for owner: " + name + ".");

        IServerForTable server = (IServerForTable) Constants.getServer();
        if (server == null) {
            logger.log(Level.SEVERE, "Cannot connect to server. Stopping...");
            System.exit(1);
        }

        logger.log(Level.INFO, "Connected to server.");

        IUser owner = server.fetchUser(name);

        Table table = new Table(owner);
        SimpleTable st = table.toSimpleTable();
        int id = server.registerTable(st);
        table.setId(id);

        table.createGame();

        Runtime.getRuntime().addShutdownHook(new Thread(() -> {
            try {
                server.removeTable(id);
            } catch (RemoteException e) {
                Constants.logException(e);
            }
        }));

        logger.log(Level.INFO, "Started table!");
    }
}