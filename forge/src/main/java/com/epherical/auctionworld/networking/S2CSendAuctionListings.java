package com.epherical.auctionworld.networking;

import com.epherical.epherolib.networking.AbstractNetworking;

public record S2CSendAuctionListings() {

    public static void handle(S2CSendAuctionListings auctions, AbstractNetworking.Context<?> context) {
        // dumb code, handled it in AuctionTheWorldForge...
    }
}
