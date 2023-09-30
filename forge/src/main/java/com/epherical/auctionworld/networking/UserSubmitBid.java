package com.epherical.auctionworld.networking;

import com.epherical.auctionworld.AuctionTheWorldForge;
import com.epherical.auctionworld.UserManager;
import com.epherical.epherolib.networking.AbstractNetworking;
import net.minecraft.server.level.ServerPlayer;

import java.util.UUID;

public record UserSubmitBid(UUID listing, int bidAmount) {


    public static void handle(UserSubmitBid bid, AbstractNetworking.Context<?> context) {
        ServerPlayer player = context.getPlayer();
        // todo; execute on server
        // todo; you can't bid on your own listing.
        AuctionTheWorldForge instance = AuctionTheWorldForge.getInstance();
        UserManager userManager = instance.getUserManager();
        instance.getAuctionManager().userBid(userManager.getUserByID(player.getUUID()), bid.listing, bid.bidAmount);
    }

}