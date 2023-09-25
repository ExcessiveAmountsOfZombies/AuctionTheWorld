package com.epherical.auctionworld.menu;

import com.epherical.auctionworld.listener.RegisterListener;
import net.minecraft.world.entity.player.Inventory;
import net.minecraft.world.entity.player.Player;
import net.minecraft.world.inventory.AbstractContainerMenu;
import net.minecraft.world.item.ItemStack;

public class BrowseAuctionMenu extends AbstractContainerMenu {


    public BrowseAuctionMenu(int id, Inventory playerInventory) {
        super(RegisterListener.BROWSE_AUCTION_MENU, id);
    }

    @Override
    public ItemStack quickMoveStack(Player p_38941_, int p_38942_) {
        return null;
    }

    @Override
    public boolean stillValid(Player p_38874_) {
        return true;
    }

}
