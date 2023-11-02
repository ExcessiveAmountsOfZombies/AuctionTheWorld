package com.epherical.auctionworld;

import com.epherical.auctionworld.client.AModClient;
import com.epherical.auctionworld.command.ClaimCommand;
import com.epherical.auctionworld.data.AuctionStorage;
import com.epherical.auctionworld.data.FlatAuctionStorage;
import com.epherical.auctionworld.data.FlatPlayerStorage;
import com.epherical.auctionworld.data.PlayerStorage;
import com.epherical.auctionworld.networking.CreateAuctionListing;
import com.epherical.auctionworld.networking.OpenCreateAuction;
import com.epherical.epherolib.CommonPlatform;
import com.epherical.epherolib.ForgePlatform;
import com.epherical.epherolib.networking.ForgeNetworking;
import net.minecraft.resources.ResourceLocation;
import net.minecraft.server.MinecraftServer;
import net.minecraft.server.level.ServerLevel;
import net.minecraft.server.level.ServerPlayer;
import net.minecraft.world.level.storage.LevelResource;
import net.minecraftforge.api.distmarker.Dist;
import net.minecraftforge.common.MinecraftForge;
import net.minecraftforge.event.RegisterCommandsEvent;
import net.minecraftforge.event.entity.player.PlayerEvent;
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

    private AuctionStorage auctionStorage;
    private PlayerStorage playerStorage;
    private AuctionManager auctionManager;
    private UserManager userManager;


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

    @SubscribeEvent
    public void commandRegisterEvent(RegisterCommandsEvent event) {
        ClaimCommand.registerCommand(event.getDispatcher());
    }

    private void clientInit(FMLClientSetupEvent event) {
        DistExecutor.unsafeRunWhenOn(Dist.CLIENT, () -> {
            // todo; ... usermanager null here might be a problem?
            auctionManager = new AuctionManager(null, true, null);
            return AModClient::initClient;
        });
        MinecraftForge.EVENT_BUS.register(new AModClient());
    }

    @SubscribeEvent
    public void serverStarted(ServerStartedEvent event) {
        auctionStorage = new FlatAuctionStorage(LevelResource.ROOT, event.getServer(), "epherical/auctiontw");
        playerStorage = new FlatPlayerStorage(LevelResource.ROOT, event.getServer(), "epherical/auctiontw/players");
        userManager = new UserManager(playerStorage);
        auctionManager = new AuctionManager(auctionStorage, false, userManager);
        userManager.loadPlayers();
    }

    @SubscribeEvent
    public void serverLevelSaveEvent(LevelEvent.Save event) {
        MinecraftServer server = event.getLevel().getServer();
        ServerLevel overWorld = server.overworld();
        if (overWorld.equals(event.getLevel())) {
            auctionManager.saveAuctionItems();
            userManager.saveAllPlayers();
        }
    }

    @SubscribeEvent
    public void serverStoppingEvent(ServerStoppingEvent event) {
        auctionManager.saveAuctionItems();
        auctionManager.stop();
    }

    @SubscribeEvent
    public void playerJoinEvent(PlayerEvent.PlayerLoggedInEvent event) {
        userManager.playerJoined((ServerPlayer) event.getEntity());
    }

    @SubscribeEvent
    public void playerLeaveEvent(PlayerEvent.PlayerLoggedOutEvent event) {
        userManager.playerLeft((ServerPlayer) event.getEntity());
    }



    private void commonInit(FMLCommonSetupEvent event) {

    }

    public static AuctionTheWorldForge getInstance() {
        return mod;
    }

    public AuctionManager getAuctionManager() {
        return auctionManager;
    }

    public UserManager getUserManager() {
        return userManager;
    }
}
