package com.distribridge.shared.models;

import com.distribridge.shared.enums.Direction;
import com.distribridge.shared.enums.Suit;
import javafx.util.Pair;
import org.junit.Assert;
import org.junit.Test;

import java.util.HashMap;
import java.util.LinkedHashMap;

public class BidTest {
    @Test
    public void badConstructorTest() throws Exception {
        try {
            new Bid(null, 1);
            Assert.fail();
        } catch (Exception ignored) {

        }

        try {
            new Bid(Suit.C, 0);
            Assert.fail();
        } catch (Exception ignored) {

        }

        try {
            new Bid(Suit.C, 8);
            Assert.fail();
        } catch (Exception ignored) {

        }
    }

    @Test
    public void pass() throws Exception {
        Bid pass = Bid.Pass();
        Assert.assertTrue(pass.getIsPass());
        Assert.assertFalse(pass.getIsDouble());
        Assert.assertFalse(pass.getIsRedouble());
    }

    @Test
    public void Double() throws Exception {
        Bid pass = Bid.Double();
        Assert.assertFalse(pass.getIsPass());
        Assert.assertTrue(pass.getIsDouble());
        Assert.assertFalse(pass.getIsRedouble());
    }

    @Test
    public void redouble() throws Exception {
        Bid pass = Bid.Redouble();
        Assert.assertFalse(pass.getIsPass());
        Assert.assertFalse(pass.getIsDouble());
        Assert.assertTrue(pass.getIsRedouble());
    }

    @Test
    public void allBids() throws Exception {
        Bid[] allBids = Bid.allBids();
        Assert.assertEquals(7 * 5, allBids.length);
    }

    @Test
    public void canDouble() throws Exception {
        LinkedHashMap<Bid, Direction> bids = new LinkedHashMap<>();
        Bid bid = new Bid(Suit.C, 1);
        bids.put(bid, Direction.N);

        Assert.assertFalse(Bid.canDouble(bids, Direction.N));
        Assert.assertTrue(Bid.canDouble(bids, Direction.E));
        Assert.assertFalse(Bid.canDouble(bids, Direction.S));
        Assert.assertTrue(Bid.canDouble(bids, Direction.E));
    }

    @Test
    public void canRedouble() throws Exception {
        LinkedHashMap<Bid, Direction> bids = new LinkedHashMap<>();
        Bid bid = new Bid(Suit.C, 1);
        Bid bid2 = Bid.Double();
        bids.put(bid, Direction.N);
        bids.put(bid2, Direction.E);

        Assert.assertTrue(Bid.canRedouble(bids, Direction.N));
        Assert.assertFalse(Bid.canRedouble(bids, Direction.E));
        Assert.assertTrue(Bid.canRedouble(bids, Direction.S));
        Assert.assertFalse(Bid.canRedouble(bids, Direction.E));
    }

    @Test
    public void getLastBid() throws Exception {
        LinkedHashMap<Bid, Direction> bids = new LinkedHashMap<>();

        bids.put(new Bid(Suit.C, 1), Direction.N);
        bids.put(Bid.Pass(), Direction.E);
        bids.put(new Bid(Suit.S, 2), Direction.S);
        bids.put(Bid.Double(), Direction.W);
        bids.put(Bid.Pass(), Direction.N);
        bids.put(Bid.Pass(), Direction.E);
        bids.put(Bid.Pass(), Direction.S);

        Pair<Bid, Direction> highestBid = Bid.getLastBid(bids);
        Assert.assertSame(Direction.S, highestBid.getValue());
        Assert.assertSame(Suit.S, highestBid.getKey().getSuit());
        Assert.assertSame(2, highestBid.getKey().getValue());
    }

    @Test
    public void getSuit() throws Exception {
        Bid bid = new Bid(Suit.C, 2);
        Assert.assertSame(Suit.C, bid.getSuit());
    }

    @Test
    public void getValue() throws Exception {
        Bid bid = new Bid(Suit.C, 2);
        Assert.assertSame(2, bid.getValue());
    }

    @Test
    public void rank() throws Exception {
        Bid b1 = new Bid(Suit.C, 1);
        Bid b2 = new Bid(Suit.S, 1);
        Bid b3 = new Bid(Suit.C, 2);

        Assert.assertTrue(b1.rank() < b2.rank());
        Assert.assertTrue(b2.rank() < b3.rank());
    }

    @Test
    public void getIsPass() throws Exception {
        Bid pass = Bid.Pass();
        Bid bidDouble = Bid.Double();

        Assert.assertTrue(pass.getIsPass());
        Assert.assertFalse(bidDouble.getIsPass());
    }

    @Test
    public void getIsDouble() throws Exception {
        Bid pass = Bid.Pass();
        Bid bidDouble = Bid.Double();

        Assert.assertFalse(pass.getIsDouble());
        Assert.assertTrue(bidDouble.getIsDouble());
    }

    @Test
    public void getIsRedouble() throws Exception {
        Bid bidDouble = Bid.Double();
        Bid bidRedouble = Bid.Redouble();

        Assert.assertFalse(bidDouble.getIsRedouble());
        Assert.assertTrue(bidRedouble.getIsRedouble());
    }
}
