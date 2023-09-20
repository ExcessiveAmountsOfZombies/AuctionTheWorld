package com.epherical.auctionworld.client.screen;

import com.epherical.auctionworld.data.AuctionFilterManager;
import com.epherical.auctionworld.listener.RegisterListener;
import com.epherical.auctionworld.listener.TagListener;
import com.epherical.auctionworld.menu.AuctionMenu;
import net.minecraft.client.gui.GuiGraphics;
import net.minecraft.client.gui.screens.inventory.AbstractContainerScreen;
import net.minecraft.network.chat.Component;
import net.minecraft.resources.ResourceLocation;
import net.minecraft.world.entity.player.Inventory;
import net.minecraft.world.item.Item;

public class AuctionScreen extends AbstractContainerScreen<AuctionMenu> {
    private static final ResourceLocation AUCTION_LOCATION = RegisterListener.id("textures/gui/container/auction.png");

    public AuctionScreen(AuctionMenu menu, Inventory inventory, Component title) {
        super(menu, inventory, title);
    }

    @Override
    protected void init() {
        imageWidth = 512;
        imageHeight = 512;
        super.init();
    }


    @Override
    public void render(GuiGraphics graphics, int x, int y, float delta) {
        this.renderBackground(graphics);
        super.render(graphics, x, y, delta);
    }

    @Override
    protected void renderBg(GuiGraphics graphics, float delta, int x, int y) {
        int left = this.leftPos;
        int center = (this.height - this.imageHeight) / 2;
        graphics.blit(AUCTION_LOCATION, left, center, 0, 0, this.imageWidth, this.imageHeight, 512, 512);
    }

    @Override
    protected void renderLabels(GuiGraphics graphics, int x, int y) {
        AuctionFilterManager.Node<Item> tree = TagListener.manager.getTree();
        tree.renderText(graphics, this.font, this.titleLabelX, this.titleLabelY, 1);
        /*graphics.drawString(this.font, this.title, this.titleLabelX, this.titleLabelY, 4210752, false);
        graphics.drawString(this.font, this.playerInventoryTitle, this.inventoryLabelX, this.inventoryLabelY, 4210752, false);*/

    }
}
