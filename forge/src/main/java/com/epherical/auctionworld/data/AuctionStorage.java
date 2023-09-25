package com.epherical.auctionworld.data;

import com.epherical.auctionworld.object.AuctionItem;

import java.util.List;

public interface AuctionStorage {


    List<AuctionItem> loadAuctionItems();

    boolean saveAuctionItems(List<AuctionItem> items);

}
