package com.distribridge.servercomponent;

import com.distribridge.shared.Constants;

import java.rmi.Remote;
import java.rmi.registry.LocateRegistry;
import java.rmi.registry.Registry;
import java.rmi.server.UnicastRemoteObject;
import java.util.logging.Level;
import java.util.logging.Logger;

public class Main {

    private static Logger logger = Logger.getLogger(Main.class.getName());

    private Main() {
        logger.log(Level.INFO, "Starting component");

        Server server;
        try {
            server = new Server();
        } catch (Exception e) {
            logger.log(Level.SEVERE, "Could not create server.");
            System.exit(1);
            return;
        }

        logger.log(Level.FINE, "Server created.");

        Registry registry;
        try {
            registry = LocateRegistry.createRegistry(Constants.PORT);
        } catch (Exception e) {
            logger.log(Level.SEVERE, "Could not create registry");
            System.exit(1);
            return;
        }

        logger.log(Level.FINE, "Registry created.");

        try {
            Remote stub = UnicastRemoteObject.exportObject(server, 0);
            registry.rebind("server", stub);
        } catch (Exception e) {
            logger.log(Level.SEVERE, "Could not bind server.");
            System.exit(1);
            return;
        }

        logger.log(Level.FINE, "Server bound.");
        logger.log(Level.INFO, "Component started.");
    }

    public static void main(String[] args) {
        System.out.println("aaa");
        Main main = new Main();
    }
}