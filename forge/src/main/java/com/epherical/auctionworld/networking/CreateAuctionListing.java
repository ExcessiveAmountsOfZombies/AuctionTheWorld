package com.epherical.auctionworld.networking;

import com.epherical.epherolib.networking.AbstractNetworking;

public record CreateAuctionListing(int timeInHours, int startPrice, int buyoutPrice) {

    public static void handle(CreateAuctionListing listing, AbstractNetworking.Context<?> context) {

    }

}
