package com.distribridge.clientcomponent;

import com.distribridge.clientcomponent.utils.ClientConstants;
import com.distribridge.shared.Constants;
import com.distribridge.shared.models.User;
import com.sun.javaws.progress.Progress;
import com.sun.media.jfxmediaimpl.platform.Platform;
import javafx.application.Application;
import javafx.fxml.FXMLLoader;
import javafx.scene.Parent;
import javafx.scene.Scene;
import javafx.stage.Stage;

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
    private ServerConnector _serverConnector;
    private User _user;

    @Override
//    @SuppressWarnings("squid:S2696")
    public void start(Stage primaryStage) throws Exception {
        // Set singleton
        instance = this;

        // Load fonts
//        Font.loadFont(getClass().getResource("/fonts/helvetica.ttf").toExternalForm(), 14);

        // Set login view
        URL view = getClass().getResource(Constants.VIEW_LOGIN);
        Parent loaded = FXMLLoader.load(view);

        scene = new Scene(loaded, WIDTH, HEIGHT);
        url = Constants.VIEW_LOGIN;

        // Set icon
//        primaryStage.getIcons().add(new Image(getClass().getResourceAsStream("/img/Logo.png")));

        // Set window
        primaryStage.setScene(scene);
        primaryStage.setTitle("Distribridge");
        primaryStage.setMinWidth(WIDTH);
        primaryStage.setMinHeight(HEIGHT);

        _serverConnector = new ServerConnector();

        Runtime.getRuntime().addShutdownHook(new Thread(() -> {
            try {
                _serverConnector.logout();
            } catch (RemoteException e) {
                //Ignore
            }
        }));

        // Show window
        primaryStage.show();
    }

    public static void main(String[] args) {
        launch(args);
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

    public static Main getSingleton() {
        return instance;
    }

    public ServerConnector getServerConnector() {
        return _serverConnector;
    }

    public User getUser() {
        return _user;
    }

    public void setUser(User user) {
        _user = user;
    }
}
