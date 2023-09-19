package com.epherical.auctionworld.object;

import net.minecraft.world.item.ItemStack;

import java.time.Instant;
import java.util.List;

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


    public AuctionItem(Instant auctionStarted, Instant auctionEnds, int buyoutPrice, String seller) {
        this.auctionStarted = auctionStarted;
        this.auctionEnds = auctionEnds;
        this.buyoutPrice = buyoutPrice;
        this.seller = seller;
    }
}
