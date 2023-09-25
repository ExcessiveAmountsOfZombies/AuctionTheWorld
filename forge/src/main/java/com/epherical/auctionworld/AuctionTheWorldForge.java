package com.epherical.auctionworld;

import com.epherical.auctionworld.client.AModClient;
import com.epherical.auctionworld.data.AuctionStorage;
import com.epherical.auctionworld.data.FlatAuctionStorage;
import com.epherical.auctionworld.networking.CreateAuctionListing;
import com.epherical.auctionworld.networking.OpenCreateAuction;
import com.epherical.epherolib.CommonPlatform;
import com.epherical.epherolib.ForgePlatform;
import com.epherical.epherolib.networking.ForgeNetworking;
import net.minecraft.resources.ResourceLocation;
import net.minecraft.server.MinecraftServer;
import net.minecraft.server.level.ServerLevel;
import net.minecraft.world.level.storage.LevelResource;
import net.minecraftforge.api.distmarker.Dist;
import net.minecraftforge.common.MinecraftForge;
import net.minecraftforge.event.level.LevelEvent;
import net.minecraftforge.event.server.ServerStartedEvent;
import net.minecraftforge.event.server.ServerStoppingEvent;
import net.minecraftforge.eventbus.api.SubscribeEvent;
import net.minecraftforge.fml.DistExecutor;
import net.minecraftforge.fml.common.Mod;
import net.minecraftforge.fml.event.lifecycle.FMLClientSetupEvent;
import net.minecraftforge.fml.event.lifecycle.FMLCommonSetupEvent;
import net.minecraftforge.fml.javafmlmod.FMLJavaModLoadingContext;

@Mod(Constants.MOD_ID)
public class AuctionTheWorldForge extends AuctionTheWorld {

    private static final ResourceLocation MOD_CHANNEL = new ResourceLocation(Constants.MOD_ID, "packets");

    private static AuctionTheWorldForge mod;

    private AuctionStorage storage;
    private AuctionManager auctionManager;


    public AuctionTheWorldForge() {
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


        MinecraftForge.EVENT_BUS.register(this);
    }

    private void clientInit(FMLClientSetupEvent event) {
        DistExecutor.unsafeRunWhenOn(Dist.CLIENT, () -> {
            auctionManager = new AuctionManager(null, true);
            return AModClient::initClient;
        });
        MinecraftForge.EVENT_BUS.register(new AModClient());
    }

    @SubscribeEvent
    public void serverStarting(ServerStartedEvent event) {
        storage = new FlatAuctionStorage(LevelResource.ROOT, event.getServer(), "epherical/auctiontw");
        auctionManager = new AuctionManager(storage, false);
    }

    @SubscribeEvent
    public void serverLevelSaveEvent(LevelEvent.Save event) {
        MinecraftServer server = event.getLevel().getServer();
        ServerLevel overWorld = server.overworld();
        if (overWorld.equals(event.getLevel())) {
            auctionManager.saveAuctionItems();
        }
    }

    @SubscribeEvent
    public void serverStoppingEvent(ServerStoppingEvent event) {
        auctionManager.saveAuctionItems();
    }



    private void commonInit(FMLCommonSetupEvent event) {

    }

    public static AuctionTheWorldForge getInstance() {
        return mod;
    }

    public AuctionManager getAuctionManager() {
        return auctionManager;
    }
}
