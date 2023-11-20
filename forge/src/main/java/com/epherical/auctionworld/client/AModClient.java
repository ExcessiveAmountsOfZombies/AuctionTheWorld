package com.epherical.auctionworld.client;

import com.epherical.auctionworld.AuctionTheWorldForge;
import com.epherical.auctionworld.client.screen.BrowseAuctionScreen;
import com.epherical.auctionworld.client.screen.CreateAuctionScreen;
import com.epherical.auctionworld.client.tooltip.BiddingTooltipClientComponent;
import com.epherical.auctionworld.listener.RegisterListener;
import com.epherical.auctionworld.object.AuctionItem;
import net.minecraft.client.Minecraft;
import net.minecraft.client.gui.screens.MenuScreens;
import net.minecraftforge.api.distmarker.Dist;
import net.minecraftforge.api.distmarker.OnlyIn;
import net.minecraftforge.client.event.RegisterClientTooltipComponentFactoriesEvent;

@OnlyIn(Dist.CLIENT)
public class AModClient {

    public static int maxPages = 100;
    private static CommonClient commonClient;

    public static void initClient() {
        commonClient = new CommonClient();

        MenuScreens.register(RegisterListener.BROWSE_AUCTION_MENU, BrowseAuctionScreen::new);
        MenuScreens.register(RegisterListener.CREATE_AUCTION_MENU, CreateAuctionScreen::new);

        AuctionTheWorldForge.auctionListeners.add(() -> {
            Minecraft minecraft = Minecraft.getInstance();
            minecraft.execute(() -> {
                if (minecraft.screen instanceof BrowseAuctionScreen screen) {
                    minecraft.setScreen(screen);
                }
            });
        });
    }

    public static void tooltipRegister(RegisterClientTooltipComponentFactoriesEvent event) {
        event.register(AuctionItem.class, BiddingTooltipClientComponent::new);
    }

}
