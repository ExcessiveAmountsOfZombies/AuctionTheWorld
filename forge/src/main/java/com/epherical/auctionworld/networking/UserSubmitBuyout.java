package com.epherical.auctionworld.networking;

import com.epherical.epherolib.networking.AbstractNetworking;

import java.util.UUID;

public record UserSubmitBuyout(UUID listing) {


    public static void handle(UserSubmitBuyout bid, AbstractNetworking.Context<?> context) {
        // todo; write buyout attempt.

    }

}
