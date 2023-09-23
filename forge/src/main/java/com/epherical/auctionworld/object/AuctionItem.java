package com.epherical.auctionworld.object;

import net.minecraft.world.item.ItemStack;

import java.time.Instant;
import java.time.temporal.ChronoUnit;
import java.util.List;
import java.util.concurrent.TimeUnit;

public class AuctionItem {
    private List<ItemStack> auctionItems;
    private Instant auctionStarted;
    private Instant auctionEnds;
    private int currencyPrice;
    private final int buyoutPrice;
    private int bids;
    private String lastBidder;
    private String seller;

    private int minBidIncrement;


    public AuctionItem(Instant auctionStarted, Instant auctionEnds, int buyoutPrice, String seller, List<ItemStack> itemStacks) {
        this.auctionItems = itemStacks;
        this.auctionStarted = auctionStarted;
        this.auctionEnds = auctionEnds;
        this.buyoutPrice = buyoutPrice;
        this.seller = seller;
    }


    public List<ItemStack> getAuctionItems() {
        return auctionItems;
    }

    public void setAuctionItems(List<ItemStack> auctionItems) {
        this.auctionItems = auctionItems;
    }

    public String formatTimeLeft() {
        long until = Instant.now().until(auctionEnds, ChronoUnit.MILLIS);
        return String.format("%02dH:%02dM:%02dS",
                TimeUnit.MILLISECONDS.toHours(until),
                TimeUnit.MILLISECONDS.toMinutes(until) - TimeUnit.HOURS.toMinutes(TimeUnit.MILLISECONDS.toHours(until)),
                TimeUnit.MILLISECONDS.toSeconds(until) - TimeUnit.MINUTES.toSeconds(TimeUnit.MILLISECONDS.toMinutes(until)));
    }

    public Instant getAuctionStarted() {
        return auctionStarted;
    }

    public void setAuctionStarted(Instant auctionStarted) {
        this.auctionStarted = auctionStarted;
    }

    public Instant getAuctionEnds() {
        return auctionEnds;
    }

    public void setAuctionEnds(Instant auctionEnds) {
        this.auctionEnds = auctionEnds;
    }

    public int getCurrencyPrice() {
        return currencyPrice;
    }

    public void setCurrencyPrice(int currencyPrice) {
        this.currencyPrice = currencyPrice;
    }

    public int getBuyoutPrice() {
        return buyoutPrice;
    }

    public int getBids() {
        return bids;
    }

    public void setBids(int bids) {
        this.bids = bids;
    }

    public String getLastBidder() {
        return lastBidder;
    }

    public void setLastBidder(String lastBidder) {
        this.lastBidder = lastBidder;
    }

    public String getSeller() {
        return seller;
    }

    public void setSeller(String seller) {
        this.seller = seller;
    }

    public int getMinBidIncrement() {
        return minBidIncrement;
    }

    public void setMinBidIncrement(int minBidIncrement) {
        this.minBidIncrement = minBidIncrement;
    }
}
