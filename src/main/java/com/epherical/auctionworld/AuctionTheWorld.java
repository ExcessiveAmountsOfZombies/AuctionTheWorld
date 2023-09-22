package com.epherical.auctionworld;

import com.epherical.epherolib.networking.AbstractNetworking;

public abstract class AuctionTheWorld {



    protected final AbstractNetworking<?, ?> networking;

    public AuctionTheWorld(AbstractNetworking<?, ?> networking) {
        this.networking = networking;
        int id = 0;

        // TODO; determine packets

        // todo; write networking to handle sending auction listings to player
        // todo; handle player bidding
        // todo; todo; handle player buyout
        // todo; handle player filters

    }


    public AbstractNetworking<?, ?> getNetworking() {
        return networking;
    }
}
