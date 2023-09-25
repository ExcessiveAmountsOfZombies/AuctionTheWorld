package com.epherical.auctionworld.networking;

import com.epherical.auctionworld.AuctionTheWorldForge;
import com.epherical.epherolib.networking.AbstractNetworking;
import net.minecraft.server.level.ServerPlayer;

import java.util.UUID;

public record UserSubmitBid(int index, UUID listing, int bidAmount) {


    public static void handle(UserSubmitBid bid, AbstractNetworking.Context<?> context) {
        ServerPlayer player = context.getPlayer();
        // todo; usermanager
        AuctionTheWorldForge.getInstance().getAuctionManager().userBid();
    }

}
