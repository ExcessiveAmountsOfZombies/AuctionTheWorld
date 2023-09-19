package com.epherical.auctionworld.client;

import com.epherical.auctionworld.client.screen.AuctionScreen;
import com.epherical.auctionworld.listener.RegisterListener;
import net.minecraft.client.gui.screens.MenuScreens;
import net.minecraftforge.api.distmarker.Dist;
import net.minecraftforge.api.distmarker.OnlyIn;

@OnlyIn(Dist.CLIENT)
public class AModClient {

    private static CommonClient commonClient;

    public static void initClient() {
        commonClient = new CommonClient();

        MenuScreens.register(RegisterListener.AUCTION_MENU, AuctionScreen::new);
    }

}
