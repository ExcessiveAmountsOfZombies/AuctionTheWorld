package com.epherical.auctionworld.object;

import net.minecraft.core.registries.BuiltInRegistries;
import net.minecraft.nbt.CompoundTag;
import net.minecraft.network.chat.Component;
import net.minecraft.resources.ResourceLocation;
import net.minecraft.server.level.ServerPlayer;
import net.minecraft.world.item.Item;
import net.minecraft.world.item.ItemStack;
import org.jetbrains.annotations.Nullable;

import java.util.ArrayList;
import java.util.List;
import java.util.UUID;

public class User {

    private final UUID uuid;
    private String name;
    private int currencyAmount;
    @Nullable
    private transient ServerPlayer player;

    private List<ItemStack> wonAuctionItems = new ArrayList<>();

    // We can take this last known currency item, and if the item changes in the config
    // we can withdraw all the deposited currency from the block into some other block for
    // the player to withdraw.
    private Item lastKnownCurrencyItem;

    public User(UUID uuid, String name, int currency) {
        this.uuid = uuid;
        this.name = name;
        this.currencyAmount = currency;
    }

    public void setPlayer(ServerPlayer player) {
        this.player = player;
    }


    public void sendPlayerMessageIfOnline(Component component) {
        if (player != null) {
            player.sendSystemMessage(component);
        }
    }

    public boolean hasEnough(int needed)  {
        return currencyAmount >= needed;
    }

    public int getCurrencyAmount() {
        return currencyAmount;
    }

    public Item getLastKnownCurrencyItem() {
        return lastKnownCurrencyItem;
    }

    public UUID getUuid() {
        return uuid;
    }

    public void takeCurrency(int amountToTake) {
        this.currencyAmount -= amountToTake;
    }

    public List<ItemStack> getWonAuctionItems() {
        return wonAuctionItems;
    }

    public void setWonAuctionItems(List<ItemStack> wonAuctionItems) {
        this.wonAuctionItems = wonAuctionItems;
    }

    public void addWinnings(List<ItemStack> items) {
        this.wonAuctionItems.addAll(items);
    }

   /* public static User loadUser(CompoundTag tag) {
        Item item = BuiltInRegistries.ITEM.get(new ResourceLocation(tag.getString("lastKnownItem")));
        return new User()
                // todo; create user.
    }

    public static CompoundTag saveUser() {
        return new CompoundTag(); // todo; save user.
    }*/

}
