package com.distribridge.clientcomponent.controllers;

import com.distribridge.clientcomponent.Main;
import com.distribridge.shared.Constants;
import javafx.event.ActionEvent;
import javafx.fxml.FXML;
import javafx.scene.control.Button;
import sun.reflect.generics.reflectiveObjects.NotImplementedException;

import java.rmi.RemoteException;

public class MenuController {
    @FXML
    Button btnTables;

    @FXML
    Button btnStartTable;

    @FXML
    Button btnStats;

    @FXML
    Button btnLogout;

    @FXML
    public void onBtnTablesClick(ActionEvent event) {
        Main.getSingleton().changeView(Constants.VIEW_TABLES);
    }

    @FXML
    public void onBtnLogoutClick(ActionEvent event) {
        try {
            Main.getSingleton().getServerConnector().logout();
        } catch (RemoteException e) {
            e.printStackTrace();
        }
        Main.getSingleton().changeView(Constants.VIEW_LOGIN);
    }

    @FXML
    void onBtnStartTableClick(ActionEvent event) {
        throw new NotImplementedException();
    }

    @FXML
    void onBtnStatsClick(ActionEvent event) {
        throw new NotImplementedException();
    }
}
