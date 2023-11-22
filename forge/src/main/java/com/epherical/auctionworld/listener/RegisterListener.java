package com.epherical.auctionworld.listener;

import com.epherical.auctionworld.block.AuctionBlock;
import com.epherical.auctionworld.menu.BrowseAuctionMenu;
import com.epherical.auctionworld.menu.CreateAuctionMenu;
import net.minecraft.resources.ResourceLocation;
import net.minecraft.world.flag.FeatureFlags;
import net.minecraft.world.inventory.MenuType;
import net.minecraft.world.item.Item;
import net.minecraft.world.item.ItemNameBlockItem;
import net.minecraft.world.item.Items;
import net.minecraft.world.level.block.Blocks;
import net.minecraft.world.level.block.SoundType;
import net.minecraft.world.level.block.state.BlockBehaviour;
import net.minecraft.world.level.block.state.properties.NoteBlockInstrument;
import net.minecraft.world.level.material.MapColor;
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
            AUCTION_HOUSE = new AuctionBlock(BlockBehaviour.Properties.of().mapColor(MapColor.WOOD).instrument(NoteBlockInstrument.BASS).strength(2.5F).sound(SoundType.WOOD).ignitedByLava());
            event.register(ForgeRegistries.Keys.BLOCKS, id("auction_house"), () -> AUCTION_HOUSE);
        } else if (event.getRegistryKey().equals(ForgeRegistries.Keys.ITEMS)) {
            event.register(ForgeRegistries.Keys.ITEMS, id("auction_house"), () -> new ItemNameBlockItem(AUCTION_HOUSE, new Item.Properties()));
        }
    }



    public static ResourceLocation id(String value) {
        return new ResourceLocation("auction_the_world", value);
    }
}
