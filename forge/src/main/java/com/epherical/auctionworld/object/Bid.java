package com.epherical.auctionworld.object;

public class Bid {

    private final User user;
    private final int bidAmount;


    public Bid(User user, int bidAmount) {
        this.user = user;
        this.bidAmount = bidAmount;
    }


    public User getUser() {
        return user;
    }

    public int getBidAmount() {
        return bidAmount;
    }
}
