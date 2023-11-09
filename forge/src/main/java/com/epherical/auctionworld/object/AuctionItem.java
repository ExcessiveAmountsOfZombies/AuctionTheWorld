package com.epherical.auctionworld.object;

import net.minecraft.nbt.CompoundTag;
import net.minecraft.nbt.ListTag;
import net.minecraft.nbt.Tag;
import net.minecraft.network.chat.Component;
import net.minecraft.world.inventory.tooltip.TooltipComponent;
import net.minecraft.world.item.ItemStack;

import java.time.Instant;
import java.util.ArrayDeque;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;
import java.util.UUID;
import java.util.concurrent.TimeUnit;
import java.util.function.Function;

public class AuctionItem implements TooltipComponent {

    private UUID auctionID;

    private List<ItemStack> auctionItems;
    private Instant auctionStarted;
    private long timeLeft; // todo; use a timeLeft system, so when the auction is saved, it doesn't become invalid if the server goes down for an extended period of time.
    private int currentPrice;
    private final int buyoutPrice;
    private String seller;
    private UUID sellerID;
    private int minBidIncrement;
    private ArrayDeque<Bid> bidStack;



    public AuctionItem(UUID auctionID, List<ItemStack> auctionItems, Instant auctionStarted, long timeLeft, int currentPrice, int buyoutPrice,
                       String seller, UUID sellerID, ArrayDeque<Bid> bids) {
        this.auctionID = auctionID;
        this.auctionItems = auctionItems;
        this.auctionStarted = auctionStarted;
        this.timeLeft = timeLeft;
        this.currentPrice = currentPrice;
        this.buyoutPrice = buyoutPrice;
        this.seller = seller;
        this.sellerID = sellerID;
        this.bidStack = bids;
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
        return timeLeft <= 0;
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

    public int getCurrentPrice() {
        return currentPrice;
    }

    public int getBuyoutPrice() {
        return buyoutPrice;
    }

    public String getSeller() {
        return seller;
    }

    public int getMinBidIncrement() {
        return minBidIncrement;
    }

    public UUID getAuctionID() {
        return auctionID;
    }

    public UUID getSellerID() {
        return sellerID;
    }

    public ArrayDeque<Bid> getBidStack() {
        return bidStack;
    }

    public long getTimeLeft() {
        return timeLeft;
    }

    public void addTime(long timeToAdd) {
        this.timeLeft += timeToAdd;
    }

    public void decrementTime() {
        this.timeLeft--;
    }

    public int getCurrentBidPrice() {
        return bidStack.getLast().bidAmount();
    }

    public void addBid(Bid bid) {
        bidStack.add(bid);
    }

    public void finishAuction(Function<UUID, User> userGetter) {
        if (bidStack.isEmpty()) {
            User owner = userGetter.apply(this.sellerID);
            owner.addWinnings(this.auctionItems, ClaimedItem.ClaimType.EXPIRED_LISTING);
            owner.sendPlayerMessageIfOnline(Component.literal("No one bid on your listing, so it has been returned to you."));
            // todo; send better message.
            return;
        }

        for (Bid bid : bidStack) {
            int bidAmount = bid.bidAmount();
            User user = userGetter.apply(bid.user());
            // we will start at the top of the stack, and check if the user did a valid bid
            if (user.hasEnough(bidAmount)) {
                user.takeCurrency(bidAmount);
                user.addWinnings(this.auctionItems, ClaimedItem.ClaimType.WON_LISTING);
                return;
            } else {
                // todo; decide if we want to punish the user for trying to game the system in submit fraudulent bids
            }
        }
    }

    public static Map<UUID, AuctionItem> loadAuctions(CompoundTag tag) {
        ListTag auctions = tag.getList("auctions", 10);
        Map<UUID, AuctionItem> auctionItems = new HashMap<>();
        for (Tag a : auctions) {
            CompoundTag auction = (CompoundTag) a;
            ListTag bidders = auction.getList("bids", 10);

            ArrayDeque<Bid> bids = new ArrayDeque<>();
            for (int i = 0; i < bidders.size(); i++) {
                CompoundTag compound = bidders.getCompound(i);
                bids.add(Bid.deserialize(compound));
            }

            AuctionItem auctionItem = new AuctionItem(
                    auction.getUUID("auctionId"),
                    loadAllItems(auction),
                    Instant.ofEpochMilli(auction.getLong("startTime")),
                    auction.getLong("timeLeft"),
                    auction.getInt("currentPrice"),
                    auction.getInt("buyoutPrice"),
                    auction.getString("seller"),
                    auction.getUUID("sellerId"),
                    bids);
            auctionItems.put(auctionItem.getAuctionID(), auctionItem);
        }

        return auctionItems;
    }

    public static CompoundTag saveAuctions(Map<UUID, AuctionItem> auctionItems) {
        ListTag auctions = new ListTag();
        CompoundTag allTag = new CompoundTag();
        for (AuctionItem auction : auctionItems.values()) {
            CompoundTag single = new CompoundTag();
            saveAllItems(single, auction.auctionItems);
            saveAllBids(single, auction.bidStack);
            single.putUUID("auctionId", auction.auctionID);
            single.putLong("startTime", auction.auctionStarted.toEpochMilli());
            single.putLong("timeLeft", auction.timeLeft);
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

    private static CompoundTag saveAllBids(CompoundTag tag, ArrayDeque<Bid> bids) {
        ListTag bidList = new ListTag();
        bids.descendingIterator().forEachRemaining(bid -> bidList.add(Bid.serialize(bid)));
        tag.put("bids", bidList);
        return tag;
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
