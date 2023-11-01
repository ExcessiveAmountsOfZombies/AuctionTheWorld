package com.epherical.auctionworld.menu;

import com.epherical.auctionworld.listener.RegisterListener;
import net.minecraft.world.Container;
import net.minecraft.world.SimpleContainer;
import net.minecraft.world.entity.player.Inventory;
import net.minecraft.world.entity.player.Player;
import net.minecraft.world.inventory.AbstractContainerMenu;
import net.minecraft.world.inventory.Slot;
import net.minecraft.world.item.ItemStack;

public class BrowseAuctionMenu extends AbstractContainerMenu {


    public BrowseAuctionMenu(int id, Inventory inventory) {
        this(id, inventory, new SimpleContainer(9));
    }

    public BrowseAuctionMenu(int id, Inventory inventory, Container container) {
        super(RegisterListener.BROWSE_AUCTION_MENU, id);


        for (int row = 0; row < 3; ++row) {
            for (int col = 0; col < 3; ++col){
                this.addSlot(new Slot(container, col + row * 3, 13 + col * 18, 256 + row * 18) {
                    @Override
                    public boolean mayPlace(ItemStack pStack) {
                        return false;
                    }
                });
            }
        }

        // player inventory
        for (int row = 0; row < 3; ++row) {
            for (int col = 0; col < 9; ++col) {
                this.addSlot(new Slot(inventory, col + row * 9 + 9, 176 + col * 18, 258 + row * 18) {
                    @Override
                    public boolean mayPlace(ItemStack pStack) {
                        return false;
                    }

                    @Override
                    public boolean mayPickup(Player pPlayer) {
                        return false;
                    }
                });
            }
        }

        // player hotbar
        for (int row = 0; row < 9; ++row) {
            this.addSlot(new Slot(inventory, row, 176 + row * 18, 316) {
                @Override
                public boolean mayPlace(ItemStack pStack) {
                    return false;
                }

                @Override
                public boolean mayPickup(Player pPlayer) {
                    return false;
                }
            });
        }

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
