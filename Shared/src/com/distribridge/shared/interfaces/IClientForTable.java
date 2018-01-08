package com.distribridge.shared.interfaces;

import com.distribridge.shared.enums.Bid;
import com.distribridge.shared.enums.Card;
import com.distribridge.shared.enums.Direction;

public interface IClientForTable {
    void playedCard(Card card, Direction direction);

    void playedBid(Bid bid, Direction direction);

    void messageSent(String userName, String message);
}


