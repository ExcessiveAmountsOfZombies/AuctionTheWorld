package com.epherical.auctionworld.networking;

import com.epherical.auctionworld.AuctionTheWorldForge;
import com.epherical.auctionworld.object.Page;
import com.epherical.auctionworld.object.User;
import com.epherical.epherolib.networking.AbstractNetworking;
import net.minecraft.server.level.ServerPlayer;

public record C2SPageChange(int newPage) {

    public static void handle(C2SPageChange listing, AbstractNetworking.Context<?> context) {
        ServerPlayer player = context.getPlayer();
        player.getServer().execute(() -> {
            AuctionTheWorldForge mod = AuctionTheWorldForge.getInstance();
            User userByID = mod.getUserManager().getUserByID(player.getUUID());
            userByID.setCurrentPage(new Page(listing.newPage, 10));
            mod.getNetworking().sendToClient(new S2CSendAuctionListings(mod.getAuctionManager().getAuctionItemsByPage(userByID.getCurrentPage())), player);
        });
    }

}
