package com.epherical.auctionworld.menu;

import com.epherical.auctionworld.listener.RegisterListener;
import net.minecraft.world.entity.player.Inventory;
import net.minecraft.world.entity.player.Player;
import net.minecraft.world.inventory.AbstractContainerMenu;
import net.minecraft.world.item.ItemStack;

public class CreateAuctionMenu extends AbstractContainerMenu {

    public CreateAuctionMenu(int id, Inventory inventory) {
        super(RegisterListener.CREATE_AUTION_MENU, id);
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
