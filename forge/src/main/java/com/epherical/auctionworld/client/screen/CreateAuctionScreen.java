package com.epherical.auctionworld.client.screen;

import com.epherical.auctionworld.listener.RegisterListener;
import com.epherical.auctionworld.menu.CreateAuctionMenu;
import net.minecraft.client.gui.GuiGraphics;
import net.minecraft.client.gui.screens.inventory.AbstractContainerScreen;
import net.minecraft.network.chat.Component;
import net.minecraft.resources.ResourceLocation;
import net.minecraft.world.entity.player.Inventory;

public class CreateAuctionScreen extends AbstractContainerScreen<CreateAuctionMenu> {
    private static final ResourceLocation AUCTION_LOCATION = RegisterListener.id("textures/gui/container/auction.png");


    public CreateAuctionScreen(CreateAuctionMenu menu, Inventory inventory, Component title) {
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
        //AuctionFilterManager.Node<Item> tree = TagListener.manager.getTree();
        //tree.beginRenderText(graphics, this.font, this.titleLabelX, this.titleLabelY, 1);


        int yInc = 0;
        /*for (AuctionItem auctionItem : getMenu().getAuctionItems()) {
            int itemX = this.titleLabelX + 118;
            int itemY = this.titleLabelY + 40 + yInc;
            graphics.renderFakeItem(auctionItem.getAuctionItems().get(0), itemX, itemY);
            graphics.drawString(this.font, auctionItem.formatTimeLeft(), itemX + 120, itemY + 6, 0xFFFFFF, false);
            graphics.drawString(this.font, auctionItem.getAuctionItems().get(0).getHoverName(), itemX + 24, itemY + 6, 0xFFFFFF, false);
            graphics.drawString(this.font, auctionItem.getSeller(), itemX + 220, itemY + 6, 0xFFFFFF, false);
            graphics.drawString(this.font, String.valueOf(auctionItem.getBuyoutPrice()), itemX + 320, itemY + 6, 0xFFFFFF, false);
            yInc += 18;
        }*/
        /*graphics.drawString(this.font, this.title, this.titleLabelX, this.titleLabelY, 4210752, false);
        graphics.drawString(this.font, this.playerInventoryTitle, this.inventoryLabelX, this.inventoryLabelY, 4210752, false);*/

    }
}
