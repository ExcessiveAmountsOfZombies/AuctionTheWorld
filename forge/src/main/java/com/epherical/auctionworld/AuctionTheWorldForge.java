package com.epherical.auctionworld;

import com.epherical.auctionworld.client.AModClient;
import com.epherical.auctionworld.client.What;
import com.epherical.auctionworld.command.ClaimCommand;
import com.epherical.auctionworld.data.AuctionStorage;
import com.epherical.auctionworld.data.FlatAuctionStorage;
import com.epherical.auctionworld.data.FlatPlayerStorage;
import com.epherical.auctionworld.data.PlayerStorage;
import com.epherical.auctionworld.networking.C2SPageChange;
import com.epherical.auctionworld.networking.CreateAuctionListing;
import com.epherical.auctionworld.networking.OpenCreateAuction;
import com.epherical.auctionworld.networking.S2CAuctionUpdate;
import com.epherical.auctionworld.networking.S2CSendAuctionListings;
import com.epherical.auctionworld.networking.SlotManipulation;
import com.epherical.auctionworld.networking.UserSubmitBid;
import com.epherical.auctionworld.networking.UserSubmitBuyout;
import com.epherical.auctionworld.object.Action;
import com.epherical.auctionworld.object.AuctionItem;
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
import net.minecraftforge.event.server.ServerStoppedEvent;
import net.minecraftforge.event.server.ServerStoppingEvent;
import net.minecraftforge.eventbus.api.SubscribeEvent;
import net.minecraftforge.fml.DistExecutor;
import net.minecraftforge.fml.common.Mod;
import net.minecraftforge.fml.event.lifecycle.FMLClientSetupEvent;
import net.minecraftforge.fml.event.lifecycle.FMLCommonSetupEvent;
import net.minecraftforge.fml.javafmlmod.FMLJavaModLoadingContext;

import java.util.ArrayList;
import java.util.List;

@Mod(Constants.MOD_ID)
public class AuctionTheWorldForge extends AuctionTheWorld {

    private static final ResourceLocation MOD_CHANNEL = new ResourceLocation(Constants.MOD_ID, "packets");

    public static boolean client = false;

    private static AuctionTheWorldForge mod;

    private AuctionStorage auctionStorage;
    private PlayerStorage playerStorage;
    private AuctionManager auctionManager;
    private UserManager userManager;

    public static List<Runnable> auctionListeners = new ArrayList<>();

    // next
    // todo; when a user purchases an item, update the menu for all viewers
    // todo; fix bid on item being shoved to the back of the list for the client. (easy)

    // later
    // todo; implement pagination to claimed items
    // todo; implement changing of currencies
    // todo; implement EEP support
    // todo; when a user bids update all viewers (maybe some animation effect)?
    // todo; implement filters that will interact with the server
    // todo; implement translation keys for all the text
    // todo; implement a config

    // stretch
    // todo; implement messages in the UI so they're easier to see

    public AuctionTheWorldForge() {
        super(new ForgeNetworking(MOD_CHANNEL, "1", s -> true, s -> true));
        mod = this;
        CommonPlatform.create(new ForgePlatform());

        FMLJavaModLoadingContext.get().getModEventBus().addListener(this::clientInit);
        FMLJavaModLoadingContext.get().getModEventBus().addListener(this::commonInit);
        DistExecutor.unsafeCallWhenOn(Dist.CLIENT, What::get);

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
        networking.registerClientToServer(id++, UserSubmitBid.class, (bid, friendlyByteBuf) -> {
            friendlyByteBuf.writeUUID(bid.listing());
            friendlyByteBuf.writeInt(bid.bidAmount());
        }, friendlyByteBuf -> new UserSubmitBid(friendlyByteBuf.readUUID(), friendlyByteBuf.readInt()), UserSubmitBid::handle);
        networking.registerClientToServer(id++, UserSubmitBuyout.class, (userSubmitBuyout, friendlyByteBuf) -> {
            friendlyByteBuf.writeUUID(userSubmitBuyout.listing());
        }, friendlyByteBuf -> new UserSubmitBuyout(friendlyByteBuf.readUUID()), UserSubmitBuyout::handle);
        networking.registerClientToServer(id++, SlotManipulation.class, (slotManipulation, buf) -> {
            buf.writeVarInt(slotManipulation.slot());
            buf.writeEnum(slotManipulation.action());
        }, buf -> new SlotManipulation(buf.readVarInt(), buf.readEnum(Action.class)), SlotManipulation::handle);
        networking.registerServerToClient(id++, S2CSendAuctionListings.class, (s2CSendAuctionListings, friendlyByteBuf) -> {
            auctionManager.networkSerializeAuctions(friendlyByteBuf, s2CSendAuctionListings);
            friendlyByteBuf.writeInt(s2CSendAuctionListings.maxPages());
        }, friendlyByteBuf -> {
            // bad way to do this... but w/e
            List<AuctionItem> auctionItems = auctionManager.networkDeserialize(friendlyByteBuf);
            int maxPages = friendlyByteBuf.readInt();
            return new S2CSendAuctionListings(auctionItems, maxPages);
        }, S2CSendAuctionListings::handle);
        networking.registerClientToServer(id++, C2SPageChange.class,
                (c2SPageChange, buf) -> buf.writeInt(c2SPageChange.newPage()),
                buf -> new C2SPageChange(buf.readInt()),
                C2SPageChange::handle);
        networking.registerServerToClient(id++, S2CAuctionUpdate.class,
                (s2CBidUpdate, buf) -> s2CBidUpdate.auctionItem().networkSerialize(buf),
                buf -> new S2CAuctionUpdate(AuctionItem.networkDeserialize(buf)),
                S2CAuctionUpdate::handle);


        MinecraftForge.EVENT_BUS.register(this);
    }

    @SubscribeEvent
    public void commandRegisterEvent(RegisterCommandsEvent event) {
        ClaimCommand.registerCommand(event.getDispatcher());
    }

    private void clientInit(FMLClientSetupEvent event) {

        DistExecutor.unsafeRunWhenOn(Dist.CLIENT, () -> {
            client = true;
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
    public void serverStoppedEvent(ServerStoppedEvent event) {
        if (client) {
            auctionManager = new AuctionManager(null, true, null); // just in case for client players playing in SP then joining MP later?
        }
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
