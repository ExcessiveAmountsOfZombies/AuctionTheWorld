package com.epherical.auctionworld;

import com.epherical.auctionworld.client.AModClient;
import com.epherical.auctionworld.networking.CreateAuctionListing;
import com.epherical.auctionworld.networking.OpenCreateAuction;
import com.epherical.epherolib.CommonPlatform;
import com.epherical.epherolib.ForgePlatform;
import com.epherical.epherolib.networking.ForgeNetworking;
import net.minecraft.resources.ResourceLocation;
import net.minecraftforge.api.distmarker.Dist;
import net.minecraftforge.common.MinecraftForge;
import net.minecraftforge.fml.DistExecutor;
import net.minecraftforge.fml.common.Mod;
import net.minecraftforge.fml.event.lifecycle.FMLClientSetupEvent;
import net.minecraftforge.fml.event.lifecycle.FMLCommonSetupEvent;
import net.minecraftforge.fml.javafmlmod.FMLJavaModLoadingContext;

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
        networking.registerClientToServer(id++, OpenCreateAuction.class,
                (createAuctionClick, friendlyByteBuf) -> {},
                friendlyByteBuf -> new OpenCreateAuction(), OpenCreateAuction::handle);
        networking.registerClientToServer(id++, CreateAuctionListing.class,
                (createAuctionListing, friendlyByteBuf) -> {
                    friendlyByteBuf.writeInt(createAuctionListing.timeInHours());
                    friendlyByteBuf.writeInt(createAuctionListing.startPrice());
                    friendlyByteBuf.writeInt(createAuctionListing.buyoutPrice());
                }, friendlyByteBuf -> new CreateAuctionListing(friendlyByteBuf.readInt(), friendlyByteBuf.readInt(), friendlyByteBuf.readInt()),
                CreateAuctionListing::handle);


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
