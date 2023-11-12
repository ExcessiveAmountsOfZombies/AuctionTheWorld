package com.epherical.auctionworld.client;

import net.minecraftforge.fml.DistExecutor;
import net.minecraftforge.fml.javafmlmod.FMLJavaModLoadingContext;


public class What {

    public static DistExecutor.SafeCallable<Object> get() {
        FMLJavaModLoadingContext.get().getModEventBus().addListener(AModClient::tooltipRegister);
        return What::new;
    }
}
