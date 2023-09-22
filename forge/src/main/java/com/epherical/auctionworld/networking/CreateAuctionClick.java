package com.epherical.auctionworld.networking;

import com.epherical.auctionworld.menu.CreateAuctionMenu;
import com.epherical.epherolib.networking.AbstractNetworking;
import net.minecraft.network.chat.Component;
import net.minecraft.server.level.ServerPlayer;
import net.minecraft.world.SimpleMenuProvider;

public record CreateAuctionClick() {

    public static void handle(CreateAuctionClick auctions, AbstractNetworking.Context<?> context) {
        ServerPlayer player = context.getPlayer();
        if (player != null) {
            player.getServer().execute(() -> {
                player.openMenu(new SimpleMenuProvider((id, inventory, player1) -> {
                    return new CreateAuctionMenu(id, inventory);
                }, Component.translatable("CREATE_AUCTION")));
            });
        }

    }
}
