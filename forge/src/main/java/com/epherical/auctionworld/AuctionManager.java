package com.epherical.auctionworld;

import com.epherical.auctionworld.config.ConfigBasics;
import com.epherical.auctionworld.data.AuctionStorage;
import com.epherical.auctionworld.networking.S2CSendAuctionListings;
import com.epherical.auctionworld.object.AuctionItem;
import com.epherical.auctionworld.object.Bid;
import com.epherical.auctionworld.object.Page;
import com.epherical.auctionworld.object.User;
import com.mojang.logging.LogUtils;
import net.minecraft.network.FriendlyByteBuf;
import net.minecraft.network.chat.Component;
import net.minecraft.world.item.ItemStack;
import org.slf4j.Logger;

import java.time.Instant;
import java.util.ArrayDeque;
import java.util.ArrayList;
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

    private Instant lastUpdated;


    private AuctionStorage storage;
    private UserManager userManager;
    private Map<UUID, AuctionItem> auctions;
    private List<AuctionItem> auctionList;

    private ScheduledExecutorService service = Executors.newSingleThreadScheduledExecutor();
    private ScheduledFuture<?> future;


    public AuctionManager(AuctionStorage storage, boolean client, UserManager userManager) {
        this.storage = storage;
        this.userManager = userManager;
        this.auctionList =new ArrayList<>();
        if (client) {
            this.auctions = new HashMap<>();
            future = service.scheduleAtFixedRate(() -> {
                if (!auctions.isEmpty()) {
                    List<UUID> expiredAuctions = new ArrayList<>();
                    for (Map.Entry<UUID, AuctionItem> entry : auctions.entrySet()) {
                        AuctionItem auction = entry.getValue();
                        auction.decrementTime();
                        if (auction.isExpired()) {
                            expiredAuctions.add(entry.getKey());
                            auctionList.remove(entry.getValue());
                        }
                    }
                    for (UUID expiredAuction : expiredAuctions) {
                        auctions.remove(expiredAuction);
                    }

                }
            }, 1L, 1L, TimeUnit.SECONDS);
        } else {
            this.auctions = storage.loadAuctionItems();
            this.lastUpdated = Instant.now();
            future = service.scheduleAtFixedRate(() -> {
                if (!auctions.isEmpty()) {
                    Instant now = Instant.now();
                    List<UUID> expiredAuctions = new ArrayList<>();
                    for (Map.Entry<UUID, AuctionItem> entry : auctions.entrySet()) {
                        AuctionItem auction = entry.getValue();
                        auction.decrementTime();
                        if (auction.isExpired()) {
                            auction.finishAuction(this.userManager::getUserByID);
                            expiredAuctions.add(entry.getKey());
                            auctionList.remove(entry.getValue());
                        }
                    }
                    for (UUID expiredAuction : expiredAuctions) {
                        auctions.remove(expiredAuction);
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

    public void networkSerializeAuctions(FriendlyByteBuf byteBuf, S2CSendAuctionListings listings) {
        byteBuf.writeInt(listings.items().size());
        listings.items().forEach(item -> item.networkSerialize(byteBuf));
    }

    // This method is called on the client.
    public List<AuctionItem> networkDeserialize(FriendlyByteBuf buf) {
        auctions.clear();
        auctionList.clear();
        int size = buf.readInt();
        for (int i = 0; i < size; i++) {
            AuctionItem auctionItem = AuctionItem.networkDeserialize(buf);
            auctions.put(auctionItem.getAuctionID(), auctionItem);
            auctionList.add(auctionItem);
        }
        return auctionList;
    }

    public void userBuyOut(User user, UUID auctionId) {
        AuctionItem auctionItem = getAuctionItem(auctionId);
        if (auctionItem != null) {
            // todo; abstract it out??
            if (auctionItem.getSellerID().equals(user.getUuid())) {
                return;
            }
            if (auctionItem.isExpired()) {
                return;
            }
            if (user.hasEnough(auctionItem.getBuyoutPrice())) {
                auctionItem.finishAuctionWithBuyOut(user);
                lastUpdated = Instant.now();
            } else {
                // todo; send a message saying they don't have enough money;
            }
        }
    }

    public AuctionItem getAuctionItem(UUID auctionId) {
        return auctions.get(auctionId);
    }

    public void userBid(User user, UUID auctionId, int bidAmount) {
        AuctionItem auctionItem = getAuctionItem(auctionId);
        if (auctionItem != null) {
            if (auctionItem.getSellerID().equals(user.getUuid())) {
                System.out.println("User bid on own");
                return; // todo; user can't bid on their own item. send a message
            }
            if (auctionItem.isExpired()) {
                System.out.println("expired");
                // todo; check if auction has expired
                return;
            }
            if (bidAmount <= auctionItem.getCurrentBidPrice()) {
                System.out.println("Bid is too low");
                // todo; this bid cannot happen.
                return;
            }

            System.out.println("Bidding nowwww");

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
            lastUpdated = Instant.now();
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
     * Add a new auction item to the list, checking for existing auctions with the same UUID. If they have the same UUID, call the method again
     * with another random UUID being generated.
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
        lastUpdated = Instant.now();
    }

    public Instant getLastUpdated() {
        return lastUpdated;
    }

    public void setLastUpdated(Instant lastUpdated) {
        this.lastUpdated = lastUpdated;
    }

    public List<AuctionItem> getAuctions() {
        return auctionList;
    }

    public List<AuctionItem> getAuctionItemsByPage(Page currentPage) {
        List<AuctionItem> pagedAuctionItems = auctionList.subList(currentPage.getPageOffset(),
                Math.min(currentPage.getPagedItems(), auctions.size()));
    }
}
