package com.distribridge.shared.interfaces;

import com.distribridge.shared.enums.Direction;
import com.distribridge.shared.models.Bid;
import com.distribridge.shared.models.Card;

public interface IClientForTable {
    void playedCard(Card card, Direction direction);

    void playedBid(Bid bid, Direction direction);

    void messageSent(String userName, String message);
}


