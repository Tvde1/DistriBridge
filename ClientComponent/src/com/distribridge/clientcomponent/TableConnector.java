package com.distribridge.clientcomponent;

import com.distribridge.clientcomponent.controllers.GameController;
import com.distribridge.shared.Constants;
import com.distribridge.shared.enums.Direction;
import com.distribridge.shared.enums.GameState;
import com.distribridge.shared.interfaces.ITableForClient;
import com.distribridge.shared.interfaces.IUser;
import com.distribridge.shared.models.Bid;
import com.distribridge.shared.models.Card;
import com.distribridge.shared.models.Contract;
import com.distribridge.shared.models.Hand;
import fontyspublisher.IRemotePropertyListener;
import javafx.application.Platform;

import java.beans.PropertyChangeEvent;
import java.rmi.RemoteException;
import java.rmi.server.UnicastRemoteObject;
import java.util.LinkedHashMap;
import java.util.Map;

public class TableConnector extends UnicastRemoteObject implements IRemotePropertyListener {
    private ITableForClient table;
    private Direction direction;
    private GameController gameController;

    public TableConnector(ITableForClient table, Direction direction) throws RemoteException {
        super();
        this.table = table;
        this.direction = direction;
        try {
            Main.getSingleton().setHand(table.getHand(direction));
            table.getRemotePublisher().subscribeRemoteListener(this, Constants.PROPERTY_BID_PLAYED);
            table.getRemotePublisher().subscribeRemoteListener(this, Constants.PROPERTY_CARD_PLAYED);
            table.getRemotePublisher().subscribeRemoteListener(this, Constants.PROPERTY_NEW_MESSAGE);
            table.getRemotePublisher().subscribeRemoteListener(this, Constants.PROPERTY_PLAYER_LEFT);
            table.getRemotePublisher().subscribeRemoteListener(this, Constants.PROPERTY_GAME_STATE);
            table.getRemotePublisher().subscribeRemoteListener(this, Constants.PROPERTY_HAND);
            table.getRemotePublisher().subscribeRemoteListener(this, Constants.PROPERTY_TURN);
        } catch (RemoteException e) {
            Constants.logException(e);
        }
    }

    @Override
    public void propertyChange(PropertyChangeEvent propertyChangeEvent) throws RemoteException {
        switch (propertyChangeEvent.getPropertyName()) {
            case Constants.PROPERTY_BID_PLAYED: {
                Object[] data = (Object[]) propertyChangeEvent.getNewValue();
//                Platform.runLater(() -> gameController.addBidPlayed((Direction) data[0], (Bid) data[1]));
                Platform.runLater(() -> gameController.refreshBids());
                break;
            }
            case Constants.PROPERTY_CARD_PLAYED: {
                Object[] data = (Object[]) propertyChangeEvent.getNewValue();
                Platform.runLater(() -> gameController.addCardPlayed((Direction) data[0], (Card) data[1]));
                break;
            }
            case Constants.PROPERTY_GAME_STATE:
                GameState gs = (GameState) propertyChangeEvent.getNewValue();
                if (gs == GameState.BIDDING) {
                    Main.getSingleton().setHand(table.getHand(direction));
                }
                Platform.runLater(gameController::initialize);
                break;
            case Constants.PROPERTY_NEW_MESSAGE: {
                Object[] args = (Object[]) propertyChangeEvent.getNewValue();
                Platform.runLater(() -> gameController.addMessage((Direction) args[0], (String) args[1]));
                break;
            }
            case Constants.PROPERTY_PLAYER_JOINED: {
                Platform.runLater(() -> gameController.addJoin((String) propertyChangeEvent.getNewValue()));
                break;
            }
            case Constants.PROPERTY_PLAYER_LEFT: {
                Platform.runLater(() -> gameController.addLeave((String) propertyChangeEvent.getNewValue()));
                break;
            }
            case Constants.PROPERTY_TURN: {
                Platform.runLater(() -> gameController.setTurn((Direction) propertyChangeEvent.getNewValue()));
                break;
            }
        }
    }

    public boolean playCard(Card card) throws RemoteException {
        return table.playCard(direction, card);
    }

    public void playBid(Bid bid) throws RemoteException {
        table.playBid(direction, bid);
    }

    public void leave() throws RemoteException {
        table.leave(direction);
    }

    public void sendMessage(String message) throws RemoteException {
        table.sendMessage(direction, message);
    }

    public void setController(GameController gameController) {
        this.gameController = gameController;
    }

    public IUser getPlayer(Direction direction) throws RemoteException {
        return table.getPlayers().get(direction);
    }

    public Direction getMyDirection() {
        return direction;
    }

    public Hand getDummyHand() throws RemoteException {
        return table.getDummyHand();
    }

    public GameState getGameState() throws RemoteException {
        return table.getGameState();
    }

    public Direction getLeaderDirection() throws RemoteException {
        return table.getLeader();
    }

    public LinkedHashMap<Bid, Direction> getBids() throws RemoteException {
        return table.getBids();
    }

    public Contract getContract() throws RemoteException {
        return table.getContract();
    }
}
