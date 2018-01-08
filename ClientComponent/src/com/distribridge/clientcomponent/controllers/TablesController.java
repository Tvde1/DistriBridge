package com.distribridge.clientcomponent.controllers;

import com.distribridge.clientcomponent.Main;
import com.distribridge.shared.Constants;
import com.distribridge.shared.models.SimpleTable;
import javafx.collections.FXCollections;
import javafx.collections.ObservableList;
import javafx.event.ActionEvent;
import javafx.fxml.FXML;
import javafx.scene.control.Button;
import javafx.scene.control.TableColumn;
import javafx.scene.control.TableView;
import javafx.scene.control.cell.PropertyValueFactory;
import sun.reflect.generics.reflectiveObjects.NotImplementedException;

import java.rmi.RemoteException;
import java.util.List;

public class TablesController {

    private ObservableList<SimpleTable> _tables;

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

    @FXML
    public void initialize() {
        try {
            List<SimpleTable> tables = Main.getSingleton().getServerConnector().getTables();
//            List<SimpleTable> tables = new ArrayList<>();
//            tables.add(new SimpleTable(1, "test", 12, 4, GameState.Bidding));
            _tables = FXCollections.observableList(tables);

            colId.setCellValueFactory(new PropertyValueFactory<>("id"));
            colOwner.setCellValueFactory(new PropertyValueFactory<>("ownerName"));
            colGame.setCellValueFactory(new PropertyValueFactory<>("game"));
            colPlayers.setCellValueFactory(new PropertyValueFactory<>("playerCount"));
            colState.setCellValueFactory(new PropertyValueFactory<>("gameState"));

            tblTables.setItems(_tables);
        } catch (RemoteException e) {
            e.printStackTrace();
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
            e.printStackTrace();
        }
    }

    @FXML
    void onBtnConnectClick(ActionEvent event) {
        throw new NotImplementedException();
    }
}
