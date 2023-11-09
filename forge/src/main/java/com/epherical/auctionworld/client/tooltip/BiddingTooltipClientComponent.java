package com.epherical.auctionworld.client.tooltip;

import com.epherical.auctionworld.object.AuctionItem;
import net.minecraft.client.gui.Font;
import net.minecraft.client.gui.GuiGraphics;
import net.minecraft.client.gui.screens.inventory.tooltip.ClientTooltipComponent;
import net.minecraft.client.renderer.MultiBufferSource;
import org.joml.Matrix4f;

public class BiddingTooltipClientComponent implements ClientTooltipComponent {

    private AuctionItem item;

    public BiddingTooltipClientComponent(AuctionItem item) {
        this.item = item;
    }

    @Override
    public int getHeight() {
        return 70;
    }

    @Override
    public int getWidth(Font pFont) {
        return 120;
    }

    @Override
    public void renderText(Font pFont, int pMouseX, int pMouseY, Matrix4f pMatrix, MultiBufferSource.BufferSource pBufferSource) {
        ClientTooltipComponent.super.renderText(pFont, pMouseX, pMouseY, pMatrix, pBufferSource);
        pFont.drawInBatch("howdy", (float)pMouseX, (float)pMouseY, -1, true, pMatrix, pBufferSource, Font.DisplayMode.NORMAL, 0, 15728880);
    }

    @Override
    public void renderImage(Font pFont, int pX, int pY, GuiGraphics pGuiGraphics) {
        ClientTooltipComponent.super.renderImage(pFont, pX, pY, pGuiGraphics);
    }
}
