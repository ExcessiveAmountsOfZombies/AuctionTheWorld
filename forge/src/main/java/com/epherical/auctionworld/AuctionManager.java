package com.epherical.auctionworld;

import com.epherical.auctionworld.config.ConfigBasics;
import com.epherical.auctionworld.data.AuctionStorage;
import com.epherical.auctionworld.object.AuctionItem;
import com.epherical.auctionworld.object.Bid;
import com.epherical.auctionworld.object.User;
import com.mojang.logging.LogUtils;
import net.minecraft.network.chat.Component;
import net.minecraft.world.item.ItemStack;
import org.slf4j.Logger;

import java.time.Instant;
import java.util.ArrayDeque;
import java.util.Collection;
import java.util.HashMap;
import java.util.HashSet;
import java.util.List;
import java.util.Map;
import java.util.Set;
import java.util.UUID;
import java.util.concurrent.Executors;
import java.util.concurrent.ScheduledExecutorService;
import java.util.concurrent.ScheduledFuture;
import java.util.concurrent.TimeUnit;

public class AuctionManager {

    private final Logger LOGGER = LogUtils.getLogger();

    private AuctionStorage storage;
    private UserManager userManager;
    private Map<UUID, AuctionItem> auctions;

    private ScheduledExecutorService service = Executors.newSingleThreadScheduledExecutor();
    private ScheduledFuture<?> future;


    public AuctionManager(AuctionStorage storage, boolean client, UserManager userManager) {
        this.storage = storage;
        this.userManager = userManager;
        if (client) {
            this.auctions = new HashMap<>();
        } else {
            this.auctions = storage.loadAuctionItems();
            future = service.scheduleAtFixedRate(() -> {
                if (!auctions.isEmpty()) {
                    Instant now = Instant.now();
                    for (AuctionItem auction : auctions.values()) {
                        auction.decrementTime();
                        if (auction.isExpired()) {
                            auction.finishAuction(this.userManager::getUserByID);
                        }
                        /*if (now.isAfter(auction.getAuctionEnds())) {
                            auction.finishAuction(this.userManager::getUserByID);
                            // todo; end the auction here
                            // todo; remove the players currency, give them the items

                            // todo; we need to keep track of who has bid, and if they have enough currency
                            //  if they don't have enough currency at the end, (trying to game the system? already will have checks in place to stop fraud bids)
                            //  we need to go down to the next person and use their bid.
                        } else {

                        }*/
                    }

                }
            }, 1L, 1L, TimeUnit.SECONDS);
        }
    }

    public void stop() {
        if (future != null) {
            future.cancel(false);
        }
    }


    public void userBid(User user, UUID auctionId, int bidAmount) {
        AuctionItem auctionItem = auctions.get(auctionId);
        if (auctionItem != null) {
            if (auctionItem.isExpired()) {
                // todo; check if auction has expired
                return;
            }
            if (bidAmount <= auctionItem.getCurrentBidPrice()) {
                // todo; this bid cannot happen.
                return;
            }


            Bid bid = new Bid(user.getUuid(), bidAmount);

            Set<UUID> sentMessages = new HashSet<>();
            for (Bid previousBids : auctionItem.getBidStack()) {
                UUID previousID = previousBids.user();
                if (!sentMessages.contains(previousID)) {
                    sentMessages.add(previousID);
                    sendPlayerMessageIfOnline(previousBids.user(), Component.translatable("Someone has outbid you for item, %s", "BINGUS"));
                }
            }
            auctionItem.addBid(bid);
            auctionItem.addTime(ConfigBasics.addTimeAfterBid > -1 ? ConfigBasics.addTimeAfterBid : 0);
        }
    }

    public void sendPlayerMessageIfOnline(UUID uuid, Component message) {
        userManager.getUserByID(uuid).sendPlayerMessageIfOnline(message);
    }


    public void saveAuctionItems() {
        if (storage.saveAuctionItems(auctions)) {
            LOGGER.info("Saved All Auction Items");
        }
    }

    /**
     * Add a new auction item to the list, checking for existing auctions with the same UUID.
     */
    public void addAuctionItem(List<ItemStack> auctionItems, Instant auctionStarted, long timeLeft, int currentPrice, int buyoutPrice,
                               String seller, UUID sellerID) {
        UUID uuid = UUID.randomUUID();
        if (!auctions.containsKey(uuid)) {
            AuctionItem item = new AuctionItem(uuid, auctionItems, auctionStarted, timeLeft, currentPrice, buyoutPrice, seller, sellerID, new ArrayDeque<>());
            auctions.put(uuid, item);
        } else {
            addAuctionItem(auctionItems, auctionStarted, timeLeft, currentPrice, buyoutPrice, seller, sellerID);
        }
    }

    public Collection<AuctionItem> getAuctions() {
        return auctions.values();
    }
}
