package com.epherical.auctionworld.block;

import com.epherical.auctionworld.AuctionManager;
import com.epherical.auctionworld.AuctionTheWorldForge;
import com.epherical.auctionworld.menu.BrowseAuctionMenu;
import com.epherical.auctionworld.networking.S2CSendAuctionListings;
import com.epherical.auctionworld.object.User;
import net.minecraft.core.BlockPos;
import net.minecraft.network.chat.Component;
import net.minecraft.server.level.ServerPlayer;
import net.minecraft.world.InteractionHand;
import net.minecraft.world.InteractionResult;
import net.minecraft.world.MenuProvider;
import net.minecraft.world.SimpleMenuProvider;
import net.minecraft.world.entity.player.Player;
import net.minecraft.world.level.Level;
import net.minecraft.world.level.block.Block;
import net.minecraft.world.level.block.state.BlockState;
import net.minecraft.world.phys.BlockHitResult;
import org.jetbrains.annotations.Nullable;

import java.time.Instant;

public class AuctionBlock extends Block {
    private static final Component CONTAINER_TITLE = Component.translatable("auction_the_world.container.auction");

    public AuctionBlock(Properties properties) {
        super(properties);
    }

    @Override
    public InteractionResult use(BlockState state, Level level, BlockPos pos, Player player, InteractionHand hand, BlockHitResult hit) {
        if (level.isClientSide) {
            return InteractionResult.SUCCESS;
        } else {
            player.openMenu(state.getMenuProvider(level, pos));
            //player.awardStat(Stats.);
            return InteractionResult.CONSUME;
        }
    }

    @Nullable
    @Override
    public MenuProvider getMenuProvider(BlockState state, Level level, BlockPos pos) {
        return new SimpleMenuProvider((id, inventory, player) -> {
            AuctionTheWorldForge instance = AuctionTheWorldForge.getInstance();
            AuctionManager manager = instance.getAuctionManager();
            User user = instance.getUserManager().getUserByID(player.getUUID());
            if (user.getLastReceivedAuctions() == null || user.getLastReceivedAuctions().isBefore(manager.getLastUpdated())) {
                // todo; this will cause problems in singleplayer
                instance.getNetworking().sendToClient(new S2CSendAuctionListings(), (ServerPlayer) player);
                user.setLastReceivedAuctions(Instant.now());
            }
            return new BrowseAuctionMenu(id, inventory, user);
        }, CONTAINER_TITLE);
    }
}
