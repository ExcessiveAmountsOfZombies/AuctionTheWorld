package com.epherical.auctionworld.listener;

import com.epherical.auctionworld.block.AuctionBlock;
import com.epherical.auctionworld.menu.AuctionMenu;
import net.minecraft.resources.ResourceLocation;
import net.minecraft.world.flag.FeatureFlags;
import net.minecraft.world.inventory.MenuType;
import net.minecraft.world.level.block.Block;
import net.minecraft.world.level.block.state.BlockBehaviour;
import net.minecraftforge.eventbus.api.SubscribeEvent;
import net.minecraftforge.fml.common.Mod;
import net.minecraftforge.registries.ForgeRegistries;
import net.minecraftforge.registries.RegisterEvent;

@Mod.EventBusSubscriber(bus = Mod.EventBusSubscriber.Bus.MOD)
public class RegisterListener {


    public static AuctionBlock AUCTION_HOUSE;

    public static final MenuType<AuctionMenu> AUCTION_MENU = new MenuType<>(AuctionMenu::new, FeatureFlags.VANILLA_SET);


    @SubscribeEvent
    public static void onRegister(RegisterEvent event) {
        if (event.getRegistryKey().equals(ForgeRegistries.Keys.MENU_TYPES)) {
            event.register(ForgeRegistries.Keys.MENU_TYPES, id("auction_menu"), () -> AUCTION_MENU);
        } else if (event.getRegistryKey().equals(ForgeRegistries.Keys.BLOCKS)) {
            event.register(ForgeRegistries.Keys.BLOCKS, id("auction_house"), () -> new AuctionBlock(BlockBehaviour.Properties.of()));
        }
    }


    public static ResourceLocation id(String value) {
        return new ResourceLocation("auction_the_world", value);
    }
}
