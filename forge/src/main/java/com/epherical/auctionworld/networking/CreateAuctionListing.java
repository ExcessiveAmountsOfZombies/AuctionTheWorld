package com.epherical.auctionworld.networking;

import com.epherical.auctionworld.menu.CreateAuctionMenu;
import com.epherical.auctionworld.menu.slot.SelectableSlot;
import com.epherical.auctionworld.object.AuctionItem;
import com.epherical.epherolib.networking.AbstractNetworking;
import net.minecraft.server.level.ServerPlayer;
import net.minecraft.world.inventory.AbstractContainerMenu;
import net.minecraft.world.inventory.Slot;
import net.minecraft.world.item.ItemStack;

import java.time.Instant;
import java.time.temporal.ChronoUnit;
import java.util.ArrayList;
import java.util.List;

import static com.epherical.auctionworld.menu.BrowseAuctionMenu.auctionItems;

public record CreateAuctionListing(int timeInHours, int startPrice, int buyoutPrice) {

    public static void handle(CreateAuctionListing listing, AbstractNetworking.Context<?> context) {
        ServerPlayer player = context.getPlayer();
        AbstractContainerMenu containerMenu = player.containerMenu;
        List<ItemStack> itemStacks = new ArrayList<>();
        if (containerMenu instanceof CreateAuctionMenu menu) {
            SelectableSlot firstSlot = menu.getFirstSlot();
            if (firstSlot != null) {
                for (Slot slot : menu.slots) {
                    SelectableSlot select = (SelectableSlot) slot;
                    if (select.isSelected()) {
                        itemStacks.add(select.getItem().copyAndClear());
                    }
                }
            }
        }
        AuctionItem auctionItem = new AuctionItem(Instant.now(), Instant.now().plus(listing.timeInHours, ChronoUnit.HOURS), listing.buyoutPrice, player.getScoreboardName(), itemStacks);
        auctionItems.add(auctionItem);
        player.closeContainer();
    }

}
