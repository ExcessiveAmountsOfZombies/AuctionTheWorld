package com.epherical.auctionworld.object;

import net.minecraft.nbt.CompoundTag;
import net.minecraft.nbt.ListTag;
import net.minecraft.nbt.Tag;
import net.minecraft.world.item.ItemStack;

import java.time.Instant;
import java.util.ArrayDeque;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;
import java.util.UUID;
import java.util.concurrent.TimeUnit;

public class AuctionItem {

    private UUID auctionID;

    private List<ItemStack> auctionItems;
    private Instant auctionStarted;
    private long timeLeft; // todo; use a timeLeft system, so when the auction is saved, it doesn't become invalid if the server goes down for an extended period of time.
    private int currentPrice;
    private final int buyoutPrice;
    private String seller;
    private UUID sellerID;
    private int minBidIncrement;
    private ArrayDeque<Bid> bidStack = new ArrayDeque<>();


    public AuctionItem(Instant auctionStarted, Instant auctionEnds, int buyoutPrice, String seller, List<ItemStack> itemStacks) {
        this.auctionItems = itemStacks;
        this.auctionStarted = auctionStarted;
        this.auctionEnds = auctionEnds;
        this.buyoutPrice = buyoutPrice;
        this.seller = seller;
    }

    public AuctionItem(List<ItemStack> auctionItems, Instant auctionStarted, Instant auctionEnds, int currentPrice, int buyoutPrice,
                       int bids, String lastBidder, String seller, UUID sellerID/*, int minBidIncrement*/) {
        this.auctionItems = auctionItems;
        this.auctionStarted = auctionStarted;
        this.auctionEnds = auctionEnds;
        this.currentPrice = currentPrice;
        this.buyoutPrice = buyoutPrice;
        this.seller = seller;
        this.sellerID = sellerID;
        //this.minBidIncrement = minBidIncrement;
    }

    public List<ItemStack> getAuctionItems() {
        return auctionItems;
    }

    public void setAuctionItems(List<ItemStack> auctionItems) {
        this.auctionItems = auctionItems;
    }

    public boolean isExpired() {
        // todo; check if it is expired.
        return
    }

    public String formatTimeLeft() {
        // todo; may not work
        long until = timeLeft;
        return String.format("%02dH:%02dM:%02dS",
                TimeUnit.SECONDS.toHours(until),
                TimeUnit.SECONDS.toMinutes(until) - TimeUnit.HOURS.toMinutes(TimeUnit.SECONDS.toHours(until)),
                TimeUnit.SECONDS.toSeconds(until) - TimeUnit.MINUTES.toSeconds(TimeUnit.SECONDS.toMinutes(until)));
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

    public int getCurrentPrice() {
        return currentPrice;
    }

    public void setCurrentPrice(int currentPrice) {
        this.currentPrice = currentPrice;
    }

    public int getBuyoutPrice() {
        return buyoutPrice;
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

    public UUID getAuctionID() {
        return auctionID;
    }

    public void setAuctionID(UUID auctionID) {
        this.auctionID = auctionID;
    }

    public UUID getSellerID() {
        return sellerID;
    }

    public void setSellerID(UUID sellerID) {
        this.sellerID = sellerID;
    }

    public ArrayDeque<Bid> getBidStack() {
        return bidStack;
    }

    public void setBidStack(ArrayDeque<Bid> bidStack) {
        this.bidStack = bidStack;
    }

    public void addBid(Bid bid) {
        bidStack.add(bid);
    }

    public void finishAuction() {
        for (Bid bid : bidStack) {
            int bidAmount = bid.getBidAmount();
            User user = bid.getUser();
            // we will start at the top of the stack, and check if the user did a valid bid
            if (!user.hasEnough(bidAmount)) {
                // todo; decide if we want to punish the user for trying to game the system in submit fraudulent bids
                continue;
            } else {
                user.takeCurrency(bidAmount);
                user.addWinnings(this.auctionItems);
                // todo; find a way to end the auction now.
            }
        }
    }

    public static List<AuctionItem> loadAuctions(CompoundTag tag) {
        ListTag auctions = tag.getList("auctions", 0);
        Map<UUID, AuctionItem> auctionItems = new HashMap<>();
        for (Tag a : auctions) {
            CompoundTag auction = (CompoundTag) a;
            AuctionItem auctionItem = new AuctionItem(loadAllItems(auction),
                    Instant.ofEpochMilli(auction.getLong("startTime")),
                    Instant.ofEpochMilli(auction.getLong("endTime")),
                    auction.getInt("currentPirce"),
                    auction.getInt("buyoutPrice"),
                    auction.getInt("bids"),
                    auction.getString("lastBidder"),
                    auction.getString("seller"),
                    auction.getUUID("sellerId"));
            auctionItems.put(auctionItem.getAuctionID(), auctionItem);
        }

        // todo; finish

        return auctionItems;
    }

    public static CompoundTag saveAuctions(List<AuctionItem> auctionItems) {
        ListTag auctions = new ListTag();
        CompoundTag allTag = new CompoundTag();
        for (AuctionItem auction : auctionItems) {
            CompoundTag single = new CompoundTag();
            saveAllItems(single, auction.auctionItems);
            single.putLong("startTime", auction.auctionStarted.toEpochMilli());
            single.putLong("endTime", auction.auctionEnds.toEpochMilli());
            single.putInt("currentPrice", auction.currentPrice);
            single.putInt("buyoutPrice", auction.buyoutPrice);
            /*single.putInt("bids", auction.bids);
            single.putString("lastBidder", auction.lastBidder);*/
            single.putString("seller", auction.seller);
            single.putUUID("sellerId", auction.sellerID);
            single.putUUID("auctionId", auction.auctionID);
            //single.putInt("minBidIncr", auction.minBidIncrement);
            auctions.add(single);
        }
        allTag.put("auctions", auctions);
        return allTag;
    }

    private static CompoundTag saveAllItems(CompoundTag tag, List<ItemStack> list) {
        ListTag listOfItems = new ListTag();

        for (ItemStack itemStack : list) {
            if (!itemStack.isEmpty()) {
                CompoundTag slottedItem = new CompoundTag();
                itemStack.save(slottedItem);
                listOfItems.add(slottedItem);
            }
        }
        tag.put("Items", listOfItems);

        return tag;
    }

    public static List<ItemStack> loadAllItems(CompoundTag compoundTag) {
        ListTag items = compoundTag.getList("Items", 10);
        List<ItemStack> itemStacks = new ArrayList<>();

        for (int i = 0; i < items.size(); ++i) {
            CompoundTag slottedItem = items.getCompound(i);

            ItemStack stack = ItemStack.of(slottedItem);
            itemStacks.add(stack);
        }

        return itemStacks;
    }
}
