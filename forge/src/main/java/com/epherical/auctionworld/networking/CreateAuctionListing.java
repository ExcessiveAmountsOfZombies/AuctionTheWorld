package com.epherical.auctionworld.networking;

import com.epherical.auctionworld.AuctionManager;
import com.epherical.auctionworld.AuctionTheWorldForge;
import com.epherical.auctionworld.menu.CreateAuctionMenu;
import com.epherical.auctionworld.menu.slot.SelectableSlot;
import com.epherical.epherolib.networking.AbstractNetworking;
import net.minecraft.server.level.ServerPlayer;
import net.minecraft.world.inventory.AbstractContainerMenu;
import net.minecraft.world.inventory.Slot;
import net.minecraft.world.item.ItemStack;

import java.time.Duration;
import java.time.Instant;
import java.util.ArrayList;
import java.util.List;

public record CreateAuctionListing(int timeInHours, int startPrice, int buyoutPrice) {

    public static void handle(CreateAuctionListing listing, AbstractNetworking.Context<?> context) {

        // todo; make sure the auction listing has at least one item.
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
        AuctionManager auctionManager = AuctionTheWorldForge.getInstance().getAuctionManager();
        auctionManager.addAuctionItem(itemStacks, Instant.now(), Duration.ofHours(listing.timeInHours).getSeconds(),
                listing.startPrice, listing.buyoutPrice, player.getScoreboardName(), player.getUUID());
        player.closeContainer();
    }

}
