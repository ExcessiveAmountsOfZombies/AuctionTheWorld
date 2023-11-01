package com.epherical.auctionworld.command;

import com.epherical.auctionworld.AuctionTheWorldForge;
import com.epherical.auctionworld.object.ClaimedItem;
import com.epherical.auctionworld.object.User;
import com.mojang.brigadier.CommandDispatcher;
import com.mojang.brigadier.arguments.IntegerArgumentType;
import com.mojang.brigadier.context.CommandContext;
import com.mojang.brigadier.exceptions.CommandSyntaxException;
import net.minecraft.commands.CommandSourceStack;
import net.minecraft.commands.Commands;
import net.minecraft.core.NonNullList;
import net.minecraft.network.chat.Component;
import net.minecraft.network.chat.HoverEvent;
import net.minecraft.network.chat.MutableComponent;
import net.minecraft.network.chat.Style;
import net.minecraft.server.level.ServerPlayer;

public class ClaimCommand {

    public static void registerCommand(CommandDispatcher<CommandSourceStack> stack) {
        stack.register(Commands.literal("atw")
                .then(Commands.literal("claim")
                        .executes(ClaimCommand::listClaims)
                        .then(Commands.argument("claim", IntegerArgumentType.integer(1))
                                .executes(ClaimCommand::claimItem))));
    }

    private static int claimItem(CommandContext<CommandSourceStack> context) throws CommandSyntaxException {
        int claim = IntegerArgumentType.getInteger(context, "claim") - 1;
        ServerPlayer player = context.getSource().getPlayerOrException();
        AuctionTheWorldForge mod = AuctionTheWorldForge.getInstance();
        User user = mod.getUserManager().getUserByID(player.getUUID());
        NonNullList<ClaimedItem> items = user.getClaimedItems();
        if (items.size() > claim) {
            ClaimedItem claimedItem = user.getClaimedItems().get(claim);
            // todo; write and validate

        }

        return 1;
    }

    private static int listClaims(CommandContext<CommandSourceStack> context) throws CommandSyntaxException {
        ServerPlayer player = context.getSource().getPlayerOrException();
        AuctionTheWorldForge mod = AuctionTheWorldForge.getInstance();
        User userByID = mod.getUserManager().getUserByID(player.getUUID());
        Component component = Component.literal("Unclaimed Items");
        player.sendSystemMessage(component);
        int itemInc= 1;
        for (ClaimedItem claimedItem : userByID.getClaimedItems()) {
            MutableComponent claim = (Component.translatable("[%s]", itemInc++)
                    .withStyle(Style.EMPTY.withColor(claimedItem.type().getColor())
                            .withHoverEvent(new HoverEvent(HoverEvent.Action.SHOW_ITEM, new HoverEvent.ItemStackInfo(claimedItem.itemStack())))
                            ));
            claim.append(Component.literal(" "));
        }
        return 1;
    }
}
