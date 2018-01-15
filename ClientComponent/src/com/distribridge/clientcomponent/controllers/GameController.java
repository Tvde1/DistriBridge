package com.distribridge.clientcomponent.controllers;

import com.distribridge.clientcomponent.Main;
import com.distribridge.shared.Constants;
import com.distribridge.shared.enums.Direction;
import com.distribridge.shared.enums.GameState;
import com.distribridge.shared.enums.Suit;
import com.distribridge.shared.models.Bid;
import com.distribridge.shared.models.Card;
import com.distribridge.shared.models.Contract;
import com.distribridge.shared.models.Hand;
import javafx.event.ActionEvent;
import javafx.fxml.FXML;
import javafx.scene.Node;
import javafx.scene.control.Button;
import javafx.scene.control.TextArea;
import javafx.scene.control.TextField;
import javafx.scene.image.ImageView;
import javafx.scene.layout.HBox;
import javafx.scene.layout.Pane;
import javafx.scene.layout.VBox;
import javafx.util.Pair;

import java.net.URL;
import java.rmi.RemoteException;
import java.util.LinkedHashMap;
import java.util.List;
import java.util.Map;

public class GameController {
    @FXML
    Button btnLeave;

    @FXML
    Button btnSend;

    @FXML
    TextField tfChat;

    @FXML
    TextArea tbChat;

    @FXML
    HBox hbUp;

    @FXML
    HBox hbDown;

    @FXML
    VBox vbLeft;

    @FXML
    VBox vbRight;

    @FXML
    VBox vbBids;

    @FXML
    VBox vbBidRight;

    @FXML
    VBox vbBidLeft;

    @FXML
    HBox hbBidTop;

    @FXML
    HBox hbBidBottom;

    public GameController() {
        Main.getSingleton().getTable().setController(this);
    }

    @FXML
    public void initialize() {
        GameState gameState;

        try {
            gameState = Main.getSingleton().getTable().getGameState();
        } catch (RemoteException e) {
            Constants.logException(e);
            return;
        }

        switch (gameState) {
            case BIDDING:
                refreshBids();
                refreshCards();
                break;
            case PLAYING:
                flushBidPanels(false);
                refreshCards();
                break;
        }
    }

    private void refreshCards() {
        flushBidPanels(true);

        Direction dummyDirection = null;
        Hand dummyHand;
        Hand myHand;

        try {
            Direction leaderDirection = Main.getSingleton().getTable().getLeaderDirection();
            if (leaderDirection != null) {
                dummyDirection = leaderDirection.opposite();
            }
            dummyHand = Main.getSingleton().getTable().getDummyHand();
            myHand = Main.getSingleton().getHand();
        } catch (RemoteException e) {
            Constants.logException(e);
            return;
        }
        drawCards(myHand, dummyDirection, dummyHand);
    }

    public void refreshBids() {
        flushBidPanels(false);
        LinkedHashMap<Bid, Direction> bids;
        Direction myDirection;

        try {
            bids = Main.getSingleton().getTable().getBids();
            myDirection = Main.getSingleton().getTable().getMyDirection();
        } catch (RemoteException e) {
            Constants.logException(e);
            return;
        }

        Contract contract = new Contract(bids);

        Pair<Bid, Direction> lastBid = contract.getLastBid();
        Bid startBid = null;
        if (lastBid != null) {
            startBid = lastBid.getKey();
        }
        drawBidButtons(startBid, Bid.canDouble(bids, myDirection), Bid.canRedouble(bids, myDirection));
        for (Map.Entry<Bid, Direction> bid : bids.entrySet()) {
            addBidPlayed(bid.getValue(), bid.getKey());
        }
    }

    @FXML
    public void onBtnLeaveClick(ActionEvent event) {
        try {
            Main.getSingleton().getTable().leave();
        } catch (RemoteException e) {
            Constants.logException(e);
        }
        Main.getSingleton().changeView(Constants.VIEW_MENU);
    }

    public void onBtnSendClick(ActionEvent event) {
        String message = tfChat.getText();
        if (message.isEmpty()) {
            return;
        }
        try {
            Main.getSingleton().getTable().sendMessage(message);
        } catch (RemoteException e) {
            Main.showError("Cannot send message because the connection to the table has been lost.");
        }
    }

    public void addMessage(Direction direction, String message) {
        try {
            String username = Main.getSingleton().getTable().getPlayer(direction).getUsername();
            tbChat.setText(tbChat.getText() + "\n" + username + ": " + message);
        } catch (RemoteException e) {
            Constants.logException(e);
        }
    }

    public void addLeave(String username) {
        tbChat.setText(tbChat.getText() + "\n" + username + " left the game.");
    }

    public void addJoin(String username) {
        tbChat.setText(tbChat.getText() + "\n" + username + " joined the game.");
    }

    private void drawCards(Hand myHand, Direction dummy, Hand dummyHand) {
        List<Card> cards = myHand.getCards();
        Suit trump = null;
        try {
            Contract con = Main.getSingleton().getTable().getContract();
            if (con != null) {
                trump = con.getSuit();
            }
        } catch (RemoteException e) {
            Constants.logException(e);
        }
        cards = Card.sortCards(cards, trump);
        for (Card card : cards) {
            String png = card.toPngName(false);
            URL url = getClass().getResource(png);
            String urlString = String.valueOf(url);
            ImageView cardImg = new ImageView(urlString);
            cardImg.setFitHeight(132);
            cardImg.setFitWidth(86.375);
            cardImg.setOnMouseClicked(event -> cardClicked(card));
            hbDown.getChildren().add(cardImg);
        }

        int amount = cards.size();
        int d = -1;
        if (dummy != null) {
            Direction dir = Main.getSingleton().getTable().getMyDirection();
            d = (dir.ordinal() - (dummy.ordinal() + 2)) % 4;
        }

        int i = 0;
        while (i != amount) {
            i++;

            String cardUrl;
            if (d == 0) {
                cardUrl = dummyHand.getCards().get(i).toPngName(false);
            } else {
                cardUrl = "/images/cards/red_back.png";
            }
            String url = String.valueOf(getClass().getResource(cardUrl));
            ImageView cardImg = new ImageView(url);
            cardImg.setFitHeight(132);
            cardImg.setFitWidth(86.375);
            hbUp.getChildren().add(cardImg);

            if (d == 0) {
                cardUrl = dummyHand.getCards().get(i).toPngName(true);
            } else {
                cardUrl = "/images/cards/rotated/red_back.png";
            }
            url = String.valueOf(getClass().getResource(cardUrl));
            cardImg = new ImageView(url);
            cardImg.setFitHeight(86.375);
            cardImg.setFitWidth(132);
            vbRight.getChildren().add(cardImg);

            if (d == 0) {
                cardUrl = dummyHand.getCards().get(i).toPngName(true);
            } else {
                cardUrl = "/images/cards/rotated/red_back.png";
            }

            url = String.valueOf(getClass().getResource(cardUrl));
            cardImg = new ImageView(url);
            cardImg.setFitHeight(86.375);
            cardImg.setFitWidth(132);
            vbLeft.getChildren().add(cardImg);
        }
    }

    private void cardClicked(Card card) {
        boolean result = false;
        try {
            result = Main.getSingleton().getTable().playCard(card);
        } catch (RemoteException e) {
            Constants.logException(e);
        }
        if (result) {
            Main.getSingleton().getHand().removeCard(card);
        }
    }

    private void drawBidButtons(Bid from, boolean canDouble, boolean canRedouble) {
        vbBids.getChildren().clear();
        HBox previous = new HBox();
        int i = 0;
        for (Bid bid : Bid.allBids()) {
            i++;
            if (i == 6) {
                i = 1;
                vbBids.getChildren().add(previous);
                previous = new HBox();
            }

            if (from != null && bid.rank() <= from.rank()) {
                continue;
            }

            Button btn = new Button();
            btn.setPrefHeight(50);
            btn.setPrefWidth(50);
            btn.setText(String.valueOf(bid.getValue()) + " " + bid.getSuit().name());
            btn.onActionProperty().setValue((event) -> {
                try {
                    Main.getSingleton().getTable().playBid(bid);
                } catch (RemoteException e) {
                    Constants.logException(e);
                }
            });
            previous.getChildren().add(btn);
        }

        HBox hBox = new HBox();

        Button pass = new Button();
        pass.setPrefHeight(50);
        pass.setPrefWidth(100);
        pass.setText("Pass");
        pass.onActionProperty().set((event) -> {
            try {
                Main.getSingleton().getTable().playBid(Bid.Pass());
            } catch (RemoteException e) {
                Constants.logException(e);
            }
        });
        hBox.getChildren().add(pass);

        Button doubleBtn = new Button();
        doubleBtn.setPrefHeight(50);
        doubleBtn.setPrefWidth(50);
        if (canDouble) {
            doubleBtn.setText("X");
            doubleBtn.onActionProperty().set((event) -> {
                try {
                    Main.getSingleton().getTable().playBid(Bid.Double());
                } catch (RemoteException e) {
                    Constants.logException(e);
                }
            });
        } else {
            doubleBtn.setOpacity(0);
        }
        hBox.getChildren().add(doubleBtn);

        Button reDoubleBtn = new Button();
        reDoubleBtn.setPrefHeight(50);
        reDoubleBtn.setPrefWidth(50);
        if (canRedouble) {
            reDoubleBtn.setText("XX");
            reDoubleBtn.onActionProperty().set((event) -> {
                try {
                    Main.getSingleton().getTable().playBid(Bid.Double());
                } catch (RemoteException e) {
                    Constants.logException(e);
                }
            });
        } else {
            reDoubleBtn.setOpacity(0);
        }
        hBox.getChildren().add(reDoubleBtn);


        vbBids.getChildren().add(hBox);
    }

    private void addBidPlayed(Direction direction, Bid bid) {
        getBidPane(direction).getChildren().add(createBidCard(bid));
//        drawBidButtons(fromBid, canDouble, canRedouble);
    }

    private Node createBidCard(Bid bid) {
        Button btn = new Button();
        btn.setPrefHeight(50);
        btn.setPrefWidth(50);

        String text;

        if (bid.getIsPass()) {
            text = "Pass";
        } else if (bid.getIsDouble()) {
            text = "X";
        } else if (bid.getIsRedouble()) {
            text = "XX";
        } else {
            text = String.valueOf(bid.getValue()) + " " + bid.getSuit().name();
        }

        btn.setText(text);
        btn.setDisable(true);
        return btn;
    }

    public void addCardPlayed(Direction direction, Card card) {
        flushBidPanels(true);
        String cardUrl = card.toPngName(false);
        String url = String.valueOf(getClass().getResource(cardUrl));
        ImageView cardImg = new ImageView(url);
        cardImg.setFitHeight(132);
        cardImg.setFitWidth(86.375);

        getBidPane(direction).getChildren().add(cardImg);
    }

    private void flushBidPanels(boolean onlyIfAllFull) {
        if (onlyIfAllFull) {
            if (vbBidRight.getChildren().size() == 0 ||
                    vbBidLeft.getChildren().size() == 0 ||
                    hbBidTop.getChildren().size() == 0 ||
                    hbBidBottom.getChildren().size() == 0) {
                return;
            }
        }
        vbBidLeft.getChildren().clear();
        vbBidRight.getChildren().clear();
        vbBids.getChildren().clear();
        hbBidBottom.getChildren().clear();
        hbBidTop.getChildren().clear();
    }

    private Pane getBidPane(Direction direction) {
        Direction myDirection = Main.getSingleton().getTable().getMyDirection();
        int dir = direction.adjustToMe(myDirection);
        Pane node;
        if (dir == 0) {
            node = hbBidTop;
        } else if (dir == 1) {
            node = vbBidRight;
        } else if (dir == 2) {
            node = hbBidBottom;
        } else if (dir == 3) {
            node = vbBidLeft;
        } else {
            throw new IndexOutOfBoundsException();
        }
        return node;
    }

    public void setTurn(Direction direction) {
        //TODO: Show turn.
    }
}
