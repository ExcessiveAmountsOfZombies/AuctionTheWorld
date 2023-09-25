package com.epherical.auctionworld.data;

import com.epherical.epherolib.data.WorldBasedStorage;
import com.google.gson.Gson;
import com.google.gson.GsonBuilder;
import net.minecraft.server.MinecraftServer;
import net.minecraft.world.level.storage.LevelResource;

public class FlatPlayerStorage extends WorldBasedStorage implements PlayerStorage {


    public FlatPlayerStorage(LevelResource resource, MinecraftServer server, String path) {
        super(resource, server, path);
    }

    @Override
    protected Gson buildGson(GsonBuilder builder) {
        return builder.create();
    }
}
