package com.epherical.auctionworld.data;

import com.epherical.auctionworld.object.AuctionItem;
import com.epherical.epherolib.data.WorldBasedStorage;
import com.google.gson.Gson;
import com.google.gson.GsonBuilder;
import net.minecraft.nbt.CompoundTag;
import net.minecraft.nbt.Tag;
import net.minecraft.server.MinecraftServer;
import net.minecraft.world.level.storage.LevelResource;

import java.io.IOException;
import java.nio.file.Path;
import java.util.ArrayList;
import java.util.List;

public class FlatAuctionStorage extends WorldBasedStorage implements AuctionStorage {


    public FlatAuctionStorage(LevelResource resource, MinecraftServer server, String path) {
        super(resource, server, path);
    }

    @Override
    protected Gson buildGson(GsonBuilder builder) {
        return builder.create();
    }

    public Path resolve() {
        return basePath.resolve("auctions.json");
    }

    public List<AuctionItem> loadAuctionItems() {
        try {
            Tag tag = readTagFromFile(resolve());
            return AuctionItem.loadAuctions((CompoundTag) tag);
        } catch (IOException ignored) {
            // file does not currently exist.
        }
        return new ArrayList<>();
    }

    @Override
    public boolean saveAuctionItems(List<AuctionItem> items) {
        try {
            this.writeTagToFile(AuctionItem.saveAuctions(items), resolve());
            return true;
        } catch (IOException e) {
            e.printStackTrace();
        }
        return false;
    }
}
