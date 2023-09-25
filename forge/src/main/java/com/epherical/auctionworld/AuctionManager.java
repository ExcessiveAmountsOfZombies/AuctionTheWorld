package com.epherical.auctionworld;

import com.epherical.auctionworld.data.AuctionStorage;
import com.epherical.auctionworld.object.AuctionItem;
import com.mojang.logging.LogUtils;
import org.slf4j.Logger;

import java.util.ArrayList;
import java.util.List;

public class AuctionManager {

    private final Logger LOGGER = LogUtils.getLogger();

    private AuctionStorage storage;
    private List<AuctionItem> auctions;

    public AuctionManager(AuctionStorage storage, boolean client) {
        this.storage = storage;
        if (!client) {
            this.auctions = storage.loadAuctionItems();
        } else {
            this.auctions = new ArrayList<>();
        }
    }


    public void saveAuctionItems() {
        if (storage.saveAuctionItems(auctions)) {
            LOGGER.info("Saved All Auction Items");
        }
    }

    public void addAuctionItem(AuctionItem auctionItem) {
        auctions.add(auctionItem);
    }

    public List<AuctionItem> getAuctions() {
        return auctions;
    }
}
