package com.epherical.auctionworld.menu;

import com.epherical.auctionworld.listener.RegisterListener;
import com.epherical.auctionworld.object.User;
import net.minecraft.world.Container;
import net.minecraft.world.SimpleContainer;
import net.minecraft.world.entity.player.Inventory;
import net.minecraft.world.entity.player.Player;
import net.minecraft.world.inventory.AbstractContainerMenu;
import net.minecraft.world.inventory.ChestMenu;
import net.minecraft.world.inventory.Slot;
import net.minecraft.world.item.ItemStack;
import net.minecraft.world.item.Items;

public class BrowseAuctionMenu extends AbstractContainerMenu {


    public BrowseAuctionMenu(int id, Inventory inventory) {
        this(id, inventory, new SimpleContainer(11));
    }

    public BrowseAuctionMenu(int id, Inventory inventory, Container container) {
        super(RegisterListener.BROWSE_AUCTION_MENU, id);

        this.addSlot(new Slot(container, 9, 353, 253) {
            @Override
            public boolean mayPlace(ItemStack pStack) {
                return false;
            }
        });

        this.addSlot(new Slot(container, 10, 371, 253) {
            @Override
            public boolean mayPlace(ItemStack pStack) {
                // todo; config option
                return pStack.is(Items.DIAMOND);
            }
        });

        // 9 slots for auction winnings, expirations, commerce changes
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
                int finalCol = col;
                int finalRow = row;
                this.addSlot(new Slot(inventory, finalCol + finalRow * 9 + 9, 176 + finalCol * 18, 258 + finalRow * 18) {

                    @Override
                    public boolean mayPickup(Player pPlayer) {
                        ItemStack item = this.container.getItem(finalCol + finalRow * 9 + 9);
                        // todo; config option
                        return item.is(Items.DIAMOND);
                    }
                });
            }
        }

        // player hotbar
        for (int row = 0; row < 9; ++row) {
            int finalRow = row;
            this.addSlot(new Slot(inventory, finalRow, 176 + row * 18, 316) {

                @Override
                public boolean mayPickup(Player pPlayer) {
                    ItemStack item = this.container.getItem(finalRow);
                    // todo; config option
                    return item.is(Items.DIAMOND);
                }
            });
        }

    }

    @Override
    public ItemStack quickMoveStack(Player p_38941_, int slotP) {
        ItemStack itemstack = ItemStack.EMPTY;
        Slot slot = this.slots.get(slotP);
        if (slot != null && slot.hasItem()) {
            ItemStack slotItem = slot.getItem();
            itemstack = slotItem.copy();
            if (slot.getContainerSlot() == 9 && slot.container instanceof User user) {
                user.takeCurrency(slotItem.getMaxStackSize());
            }
            if (slotP < 10) {
                if (!this.moveItemStackTo(slotItem, 10, this.slots.size(), true)) {
                    return ItemStack.EMPTY;
                }
            } else if (!this.moveItemStackTo(slotItem, 0, 10, false)) {
                return ItemStack.EMPTY;
            }

            if (slotItem.isEmpty()) {
                slot.setByPlayer(ItemStack.EMPTY);
            } else {
                slot.setChanged();
            }
        }

        return itemstack;
    }

    @Override
    public boolean stillValid(Player p_38874_) {
        return true;
    }

}
