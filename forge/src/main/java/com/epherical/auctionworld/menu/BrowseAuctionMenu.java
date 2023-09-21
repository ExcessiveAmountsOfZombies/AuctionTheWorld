package com.epherical.auctionworld.menu;

import com.epherical.auctionworld.listener.RegisterListener;
import com.epherical.auctionworld.object.AuctionItem;
import net.minecraft.world.entity.player.Inventory;
import net.minecraft.world.entity.player.Player;
import net.minecraft.world.inventory.AbstractContainerMenu;
import net.minecraft.world.item.ItemStack;
import net.minecraft.world.item.Items;

import java.time.Instant;
import java.util.ArrayList;
import java.util.List;

public class BrowseAuctionMenu extends AbstractContainerMenu {


    public static List<AuctionItem> auctionItems = new ArrayList<>();


    static {
        auctionItems.add(new AuctionItem(Instant.now(), Instant.now().plusSeconds(5000), 100, "Bozo", List.of(new ItemStack(Items.STONE))));
        auctionItems.add(new AuctionItem(Instant.now(), Instant.now().plusSeconds(5000), 100, "Bozo", List.of(new ItemStack(Items.AXOLOTL_SPAWN_EGG))));
        auctionItems.add(new AuctionItem(Instant.now(), Instant.now().plusSeconds(5000), 100, "Bozo", List.of(new ItemStack(Items.GOLDEN_AXE))));
        auctionItems.add(new AuctionItem(Instant.now(), Instant.now().plusSeconds(5000), 100, "Bozo", List.of(new ItemStack(Items.NETHERITE_AXE))));
        auctionItems.add(new AuctionItem(Instant.now(), Instant.now().plusSeconds(5000), 100, "Bozo", List.of(new ItemStack(Items.DANDELION))));
        auctionItems.add(new AuctionItem(Instant.now(), Instant.now().plusSeconds(5000), 100, "Bozo", List.of(new ItemStack(Items.ACACIA_WOOD))));
    }

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

    public List<AuctionItem> getAuctionItems() {
        return auctionItems;
    }
}
