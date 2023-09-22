package com.epherical.auctionworld.listener;

import com.epherical.auctionworld.block.AuctionBlock;
import com.epherical.auctionworld.menu.BrowseAuctionMenu;
import com.epherical.auctionworld.menu.CreateAuctionMenu;
import net.minecraft.resources.ResourceLocation;
import net.minecraft.world.flag.FeatureFlags;
import net.minecraft.world.inventory.MenuType;
import net.minecraft.world.level.block.state.BlockBehaviour;
import net.minecraftforge.eventbus.api.SubscribeEvent;
import net.minecraftforge.fml.common.Mod;
import net.minecraftforge.registries.ForgeRegistries;
import net.minecraftforge.registries.RegisterEvent;

@Mod.EventBusSubscriber(bus = Mod.EventBusSubscriber.Bus.MOD)
public class RegisterListener {

    public static AuctionBlock AUCTION_HOUSE;

    public static final MenuType<BrowseAuctionMenu> BROWSE_AUCTION_MENU = new MenuType<>(BrowseAuctionMenu::new, FeatureFlags.VANILLA_SET);
    public static final MenuType<CreateAuctionMenu> CREATE_AUCTION_MENU = new MenuType<>(CreateAuctionMenu::new, FeatureFlags.VANILLA_SET);


    @SubscribeEvent
    public static void onRegister(RegisterEvent event) {
        if (event.getRegistryKey().equals(ForgeRegistries.Keys.MENU_TYPES)) {
            event.register(ForgeRegistries.Keys.MENU_TYPES, id("browse_auction_menu"), () -> BROWSE_AUCTION_MENU);
            event.register(ForgeRegistries.Keys.MENU_TYPES, id("create_auction_menu"), () -> CREATE_AUCTION_MENU);
        } else if (event.getRegistryKey().equals(ForgeRegistries.Keys.BLOCKS)) {
            event.register(ForgeRegistries.Keys.BLOCKS, id("auction_house"), () -> new AuctionBlock(BlockBehaviour.Properties.of()));
        }
    }



    public static ResourceLocation id(String value) {
        return new ResourceLocation("auction_the_world", value);
    }
}
