package com.distribridge.clientcomponent;

import com.distribridge.shared.Constants;
import com.distribridge.shared.interfaces.IUser;
import com.distribridge.shared.models.Hand;
import fontyspublisher.RemotePublisher;
import javafx.application.Application;
import javafx.fxml.FXMLLoader;
import javafx.scene.Parent;
import javafx.scene.Scene;
import javafx.stage.Stage;

import javax.swing.*;
import java.io.IOException;
import java.net.URL;
import java.rmi.RemoteException;
import java.util.logging.Level;
import java.util.logging.Logger;

public class Main extends Application {

    private static final Logger LOG = Logger.getLogger(Main.class.getName());

    /* Width & Height */
    private static final int WIDTH = 1280;
    private static final int HEIGHT = 720;

    /* Singleton & stage */
    private static Main instance;
    private Scene scene;
    private String url;
    private ServerConnector serverConnector;
    private IUser user;
    private TableConnector table;
    private RemotePublisher publisher;
    private Hand hand;

    public static void main(String[] args) {

//        for (Bid bid : Bid.allBids()) {
//            System.out.println(bid.rank());
//        }


        launch(args);
    }

    public static Main getSingleton() {
        return instance;
    }

    public static void showError(String message) {
        JOptionPane.showMessageDialog(null, message);
    }

    @Override
//    @SuppressWarnings("squid:S2696")
    public void start(Stage primaryStage) throws Exception {
        instance = this;

        URL view = getClass().getResource(Constants.VIEW_LOGIN);
        Parent loaded = FXMLLoader.load(view);

        scene = new Scene(loaded, WIDTH, HEIGHT);
        url = Constants.VIEW_LOGIN;

        // Set icon
//        primaryStage.getIcons().add(new Image(getClass().getResourceAsStream("/img/Logo.png")));

        primaryStage.setScene(scene);
        primaryStage.setTitle("Distribridge");
        primaryStage.setMinWidth(WIDTH);
        primaryStage.setMinHeight(HEIGHT);

        serverConnector = new ServerConnector();

        Runtime.getRuntime().addShutdownHook(new Thread(() -> {
            try {
                if (serverConnector.getIsLoggedIn()) {
                    serverConnector.logout();
                }
            } catch (RemoteException e) {
                //Ignore
            }
        }));

        // Show window
        primaryStage.show();
    }

    public void changeView(String view) {
        try {
            if (view.equals("")) {
                throw new IllegalArgumentException();
            } else {
                url = view;
                FXMLLoader loader = new FXMLLoader(getClass().getResource(view));
                Parent root = loader.load();
                scene.setRoot(root);
            }
        } catch (IOException e) {
            LOG.log(Level.SEVERE, null, e);
        }
    }

    public String getCurrentView() {
        return url;
    }

    public ServerConnector getServerConnector() {
        return serverConnector;
    }

    public IUser getUser() {
        return user;
    }

    public void setUser(IUser user) {
        this.user = user;
    }

    public TableConnector getTable() {
        return table;
    }

    public void setTable(TableConnector table) {
        this.table = table;
    }

    public RemotePublisher getPublisher() {
        return this.publisher;
    }

    public void setPublisher(RemotePublisher publisher) {
        this.publisher = publisher;
    }

    public Hand getHand() {
        return this.hand;
    }

    public void setHand(Hand hand) {
        this.hand = hand;
    }
}
