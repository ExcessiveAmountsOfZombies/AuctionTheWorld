package com.epherical.auctionworld.object;

import net.minecraft.world.item.Item;

import java.util.UUID;

public class User {

    private final UUID uuid;
    private String name;
    private int currencyAmount;

    // We can take this last known currency item, and if the item changes in the config
    // we can withdraw all the deposited currency from the block into some other block for
    // the player to withdraw.
    private Item lastKnownCurrencyItem;

    public User(UUID uuid, String name, int currency) {
        this.uuid = uuid;
        this.name = name;
        this.currencyAmount = currency;
    }







}
