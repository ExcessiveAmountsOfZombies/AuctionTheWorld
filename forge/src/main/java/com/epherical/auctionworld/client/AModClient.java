package com.epherical.auctionworld.client;

import com.epherical.auctionworld.client.screen.BrowseAuctionScreen;
import com.epherical.auctionworld.client.screen.CreateAuctionScreen;
import com.epherical.auctionworld.listener.RegisterListener;
import net.minecraft.client.gui.screens.MenuScreens;
import net.minecraftforge.api.distmarker.Dist;
import net.minecraftforge.api.distmarker.OnlyIn;

@OnlyIn(Dist.CLIENT)
public class AModClient {

    private static CommonClient commonClient;

    public static void initClient() {
        commonClient = new CommonClient();

        MenuScreens.register(RegisterListener.BROWSE_AUCTION_MENU, BrowseAuctionScreen::new);
        MenuScreens.register(RegisterListener.CREATE_AUCTION_MENU, CreateAuctionScreen::new);
    }

}
