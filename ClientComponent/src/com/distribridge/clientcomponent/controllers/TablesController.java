package com.distribridge.clientcomponent.controllers;

import com.distribridge.clientcomponent.Main;
import com.distribridge.clientcomponent.TableConnector;
import com.distribridge.shared.Constants;
import com.distribridge.shared.enums.Direction;
import com.distribridge.shared.interfaces.ITableForClient;
import com.distribridge.shared.models.SimpleTable;
import fontyspublisher.RemotePublisher;
import javafx.collections.FXCollections;
import javafx.collections.ObservableList;
import javafx.event.ActionEvent;
import javafx.fxml.FXML;
import javafx.scene.control.Button;
import javafx.scene.control.TableColumn;
import javafx.scene.control.TableView;
import javafx.scene.control.cell.PropertyValueFactory;

import java.rmi.RemoteException;
import java.util.List;

public class TablesController {

    @FXML
    TableView<SimpleTable> tblTables;
    @FXML
    TableColumn<SimpleTable, String> colId;
    @FXML
    TableColumn<SimpleTable, String> colOwner;
    @FXML
    TableColumn<SimpleTable, String> colGame;
    @FXML
    TableColumn<SimpleTable, String> colPlayers;
    @FXML
    TableColumn<SimpleTable, String> colState;
    @FXML
    Button btnBack;
    @FXML
    Button btnRefresh;
    @FXML
    Button btnConnect;
    private ObservableList<SimpleTable> _tables;

    @FXML
    public void initialize() {
        try {
            List<SimpleTable> tables = Main.getSingleton().getServerConnector().getTables();
            _tables = FXCollections.observableList(tables);

            colId.setCellValueFactory(new PropertyValueFactory<>("id"));
            colOwner.setCellValueFactory(new PropertyValueFactory<>("ownerName"));
            colGame.setCellValueFactory(new PropertyValueFactory<>("game"));
            colPlayers.setCellValueFactory(new PropertyValueFactory<>("playerCount"));
            colState.setCellValueFactory(new PropertyValueFactory<>("gameState"));

            tblTables.setItems(_tables);
        } catch (RemoteException e) {
            Constants.logException(e);
        }
    }

    @FXML
    void onBtnBackClick(ActionEvent event) {
        Main.getSingleton().changeView(Constants.VIEW_MENU);
    }

    @FXML
    void onBtnRefreshClick(ActionEvent event) {
        try {
            List<SimpleTable> tables = Main.getSingleton().getServerConnector().getTables();
            _tables.setAll(tables);
        } catch (RemoteException e) {
            Constants.logException(e);
        }
    }

    @FXML
    void onBtnConnectClick(ActionEvent event) {
        SimpleTable t = tblTables.getSelectionModel().getSelectedItem();
        if (t.getPlayerCount() == 4) {
            Main.showError("This table is full.");
            return;
        }
        RemotePublisher p;
        try {
            ITableForClient table = t.getTableForClient();
            Direction dir = table.join(Main.getSingleton().getUser());
            TableConnector tc = new TableConnector(table, dir);
            Main.getSingleton().setTable(tc);
            Main.getSingleton().changeView(Constants.VIEW_GAME);
        } catch (RemoteException e) {
            Constants.logException(e);
        }
    }
}