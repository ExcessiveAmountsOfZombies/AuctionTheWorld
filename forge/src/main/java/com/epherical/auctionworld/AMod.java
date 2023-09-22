package com.epherical.auctionworld;

import com.epherical.auctionworld.client.AModClient;
import com.epherical.auctionworld.networking.CreateAuctionClick;
import com.epherical.epherolib.CommonPlatform;
import com.epherical.epherolib.ForgePlatform;
import com.epherical.epherolib.networking.ForgeNetworking;
import net.minecraft.core.registries.BuiltInRegistries;
import net.minecraft.resources.ResourceLocation;
import net.minecraft.world.inventory.MenuType;
import net.minecraftforge.api.distmarker.Dist;
import net.minecraftforge.common.MinecraftForge;
import net.minecraftforge.fml.DistExecutor;
import net.minecraftforge.fml.common.Mod;
import net.minecraftforge.fml.event.lifecycle.FMLClientSetupEvent;
import net.minecraftforge.fml.event.lifecycle.FMLCommonSetupEvent;
import net.minecraftforge.fml.javafmlmod.FMLJavaModLoadingContext;
import net.minecraftforge.registries.DeferredRegister;

import java.util.TreeMap;

@Mod(Constants.MOD_ID)
public class AMod extends AuctionTheWorld {

    private static final ResourceLocation MOD_CHANNEL = new ResourceLocation(Constants.MOD_ID, "packets");

    private static AMod mod;


    public AMod() {
        super(new ForgeNetworking(MOD_CHANNEL, "1", s -> true, s -> true));
        mod = this;
        CommonPlatform.create(new ForgePlatform());

        FMLJavaModLoadingContext.get().getModEventBus().addListener(this::clientInit);
        FMLJavaModLoadingContext.get().getModEventBus().addListener(this::commonInit);

        int id = 0;
        networking.registerClientToServer(id++, CreateAuctionClick.class,
                (createAuctionClick, friendlyByteBuf) -> {},
                friendlyByteBuf -> new CreateAuctionClick(), CreateAuctionClick::handle);



        //MinecraftForge.EVENT_BUS.register(this);
    }

    private void clientInit(FMLClientSetupEvent event) {
        DistExecutor.unsafeRunWhenOn(Dist.CLIENT, () -> AModClient::initClient);
        MinecraftForge.EVENT_BUS.register(new AModClient());
    }

    private void commonInit(FMLCommonSetupEvent event) {

    }

    public static AMod getInstance() {
        return mod;
    }


}
