package com.epherical.auctionworld.menu;

import com.epherical.auctionworld.listener.RegisterListener;
import net.minecraft.world.entity.player.Inventory;
import net.minecraft.world.entity.player.Player;
import net.minecraft.world.inventory.AbstractContainerMenu;
import net.minecraft.world.inventory.Slot;
import net.minecraft.world.item.ItemStack;

public class CreateAuctionMenu extends AbstractContainerMenu {

    public CreateAuctionMenu(int id, Inventory inventory) {
        super(RegisterListener.CREATE_AUCTION_MENU, id);

        // player inventory
        for(int row = 0; row < 3; ++row) {
            for(int col = 0; col < 9; ++col) {
                this.addSlot(new Slot(inventory, col + row * 9 + 9, 176 + col * 18, 258 + row * 18));
            }
        }

        // player hotbar
        for(int row = 0; row < 9; ++row) {
            this.addSlot(new Slot(inventory, row, 176 + row * 18, 316));
        }
    }

    @Override
    public ItemStack quickMoveStack(Player player, int slot) {
        return null;
    }

    @Override
    public boolean stillValid(Player player) {
        return true;
    }
}
