package com.distribridge.clientcomponent.controllers;

import com.distribridge.clientcomponent.Main;
import com.distribridge.shared.Constants;
import com.distribridge.shared.interfaces.IUser;
import javafx.event.ActionEvent;
import javafx.fxml.FXML;
import javafx.scene.control.Button;
import javafx.scene.control.TextField;

import java.rmi.RemoteException;

public class LoginController {
    @FXML
    TextField tfUsername;

    @FXML
    TextField tfPassword;

    @FXML
    Button btnLogin;

    @FXML
    Button btnRegister;

    @FXML
    void onBtnLoginClick(ActionEvent event) {
        String username = tfUsername.getText();
        String password = tfPassword.getText();

        if (username.isEmpty() || password.isEmpty()) {
            Main.showError("You didn't input credentials.");
            return;
        }

        try {
            IUser user = Main.getSingleton().getServerConnector().login(username, password);

            if (user == null) {
                Main.showError("Could not be authenticated.");
                return;
            }

            Main.getSingleton().setUser(user);
            Main.getSingleton().changeView(Constants.VIEW_MENU);
        } catch (RemoteException e) {
            Constants.logException(e);
        }
    }

    @FXML
    void onBtnRegisterClick(ActionEvent event) {
        Main.getSingleton().changeView(Constants.VIEW_REGISTER);
    }
}
