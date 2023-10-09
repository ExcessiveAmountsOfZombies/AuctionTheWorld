package com.epherical.auctionworld.data;

import com.epherical.auctionworld.object.User;
import com.epherical.epherolib.data.WorldBasedStorage;
import com.google.gson.Gson;
import com.google.gson.GsonBuilder;
import net.minecraft.server.MinecraftServer;
import net.minecraft.world.level.storage.LevelResource;

import java.nio.file.Path;
import java.util.Map;
import java.util.UUID;

public class FlatPlayerStorage extends WorldBasedStorage implements PlayerStorage {


    public FlatPlayerStorage(LevelResource resource, MinecraftServer server, String path) {
        super(resource, server, path);
    }

    @Override
    protected Gson buildGson(GsonBuilder builder) {
        return builder.create();
    }


    public Path resolve(UUID uuid) {
        return basePath.resolve(uuid.toString() + ".json");
    }

    @Override
    public void savePlayer(User user) {


    }

    @Override
    public Map<UUID, User> loadUsers() {
        return null;
    }

    @Override
    public void saveAllPlayers(Map<UUID, User> players) {

    }
}
