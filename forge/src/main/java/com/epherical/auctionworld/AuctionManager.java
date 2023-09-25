package com.epherical.auctionworld;

import com.epherical.auctionworld.data.AuctionStorage;
import com.epherical.auctionworld.object.AuctionItem;
import com.epherical.auctionworld.object.Bid;
import com.epherical.auctionworld.object.User;
import com.mojang.logging.LogUtils;
import org.slf4j.Logger;

import java.time.Instant;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;
import java.util.UUID;
import java.util.concurrent.Executors;
import java.util.concurrent.ScheduledExecutorService;
import java.util.concurrent.TimeUnit;

public class AuctionManager {

    private final Logger LOGGER = LogUtils.getLogger();

    private AuctionStorage storage;
    private Map<UUID, AuctionItem> auctions;

    private ScheduledExecutorService service = Executors.newSingleThreadScheduledExecutor();


    public AuctionManager(AuctionStorage storage, boolean client) {
        this.storage = storage;
        if (client) {
            this.auctions = new HashMap<>();
        } else {
            this.auctions = storage.loadAuctionItems();
            service.scheduleAtFixedRate(() -> {
                if (!auctions.isEmpty()) {
                    Instant now = Instant.now();
                    for (AuctionItem auction : auctions.values()) {
                        if (now.isAfter(auction.getAuctionEnds())) {
                            // todo; end the auction here
                            // todo; remove the players currency, give them the items

                            // todo; we need to keep track of who has bid, and if they have enough currency
                            //  if they don't have enough currency at the end, (trying to game the system? already will have checks in place to stop fraud bids)
                            //  we need to go down to the next person and use their bid.
                        } else {

                        }
                    }

                }
            }, 1L, 1L, TimeUnit.SECONDS);
        }
    }


    public void userBid(User user, UUID uuid, int bidAmount) {
        AuctionItem auctionItem = auctions.get(uuid);
        if (auctionItem != null) {
            // todo; check if auction has expired
            Bid bid = new Bid(user, bidAmount);
            auctionItem.addBid(bid);
            // todo; notify previous bidders
            // todo; add additional time
        }
    }


    public void saveAuctionItems() {
        if (storage.saveAuctionItems(auctions)) {
            LOGGER.info("Saved All Auction Items");
        }
    }

    public void addAuctionItem(AuctionItem auctionItem) {
        // todo; handle collision here...
        //  containskey check, if it does, make a new UUID
        auctions.add(auctionItem);
    }

    public List<AuctionItem> getAuctions() {
        return auctions;
    }
}
