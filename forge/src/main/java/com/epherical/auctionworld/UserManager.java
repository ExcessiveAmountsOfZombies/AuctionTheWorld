package com.epherical.auctionworld;

import com.epherical.auctionworld.data.PlayerStorage;
import com.epherical.auctionworld.object.User;
import net.minecraft.server.level.ServerPlayer;

import java.util.HashMap;
import java.util.Map;
import java.util.UUID;

public class UserManager {


    private Map<UUID, User> players = new HashMap<>();
    private PlayerStorage playerStorage;


    public UserManager(PlayerStorage playerStorage) {
        this.playerStorage = playerStorage;
    }

    public User getUserByID(UUID uuid) {
        return players.get(uuid);
    }

    public Map<UUID, User> getPlayers() {
        return players;
    }

    public void playerJoined(ServerPlayer player) {
        if (!players.containsKey(player.getUUID())) {
            User user = new User(player.getUUID(), player.getScoreboardName(), 0);
            players.put(player.getUUID(), user);
            playerStorage.savePlayer(user);
        }
    }

    public void saveAllPlayers() {
        playerStorage.saveAllPlayers(players);
    }

    public void playerLeft(ServerPlayer player) {
        // todo??? might not care about when players leave
    }

    public PlayerStorage getPlayerStorage() {
        return playerStorage;
    }
}
