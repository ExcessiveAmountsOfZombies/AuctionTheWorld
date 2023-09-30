package com.epherical.auctionworld.client;

import com.epherical.auctionworld.config.ConfigBasics;
import com.epherical.auctionworld.object.AuctionItem;
import com.mojang.blaze3d.vertex.PoseStack;
import net.minecraft.client.Minecraft;
import net.minecraft.client.gui.Font;
import net.minecraft.client.gui.GuiGraphics;
import net.minecraft.client.gui.components.ContainerObjectSelectionList;
import net.minecraft.client.gui.components.events.GuiEventListener;
import net.minecraft.client.gui.narration.NarratableEntry;
import net.minecraft.client.gui.screens.Screen;
import net.minecraft.network.chat.Component;
import net.minecraft.world.item.ItemStack;

import java.util.Collection;
import java.util.List;

public class AuctionListWidget extends ContainerObjectSelectionList<AuctionListWidget.Entry> {


    public AuctionListWidget(Minecraft minecraft, int width, int height, int y0, int y1, int itemHeight) {
        super(minecraft, width, height, y0, y1, itemHeight);
    }

    public void addEntries(Collection<AuctionItem> items) {
        for (AuctionItem item : items) {
            addEntry(new Entry(item));
        }
    }


    @Override
    public int getRowWidth() {
        return 384;
    }

    @Override
    protected int getScrollbarPosition() {
        return this.width / 2 + 190;
    }

    @Override
    public int getRowLeft() {
        return super.getRowLeft();
    }

    @Override
    public int addEntry(Entry entry) {
        return super.addEntry(entry);
    }


    public class Entry extends ContainerObjectSelectionList.Entry<Entry> {

        private AuctionItem item;

        public Entry(AuctionItem item) {
            this.item = item;
        }

        @Override
        public List<? extends NarratableEntry> narratables() {
            return List.of();
        }

        @Override
        public void render(GuiGraphics graphics, int row, int top, int left, int width, int height, int x, int y, boolean hovered, float delta) {
            //left -= 246;
            Font font = AuctionListWidget.this.minecraft.font;
            ItemStack itemStack = item.getAuctionItems().get(0);
            graphics.renderFakeItem(itemStack, left, top + 4);
            graphics.drawString(font, item.formatTimeLeft(), left + 120, top + 8, 0xFFFFFF, false);

            int width1 = font.width(itemStack.getHoverName());
            if (width1 >= 95) {
                PoseStack poseStack = graphics.pose();
                poseStack.pushPose();
                poseStack.scale(0.5f, 0.5f, 0.5f);
                poseStack.translate((left + 24), (top + 8), 1f);
                //poseStack.translate((1/scale), (1/scale), 1f);
                graphics.drawString(font, itemStack.getHoverName(), left + 24, top + 8, 0xFFFFFF, false);
                poseStack.scale(2f, 2f, 2f);
                poseStack.popPose();
            } else {
                graphics.drawString(font, itemStack.getHoverName(), left + 24, top + 8, 0xFFFFFF, false);
            }

            graphics.drawString(font, item.getSeller(), left + 220, top + 8, 0xFFFFFF, false);
            graphics.drawString(font, String.valueOf(item.getCurrentPrice()), left + 328, top + 2, 0xFFFFFF, false);
            graphics.drawString(font, String.valueOf(item.getBuyoutPrice()), left + 328, top + 15, 0xFFFFFF, false);

            ItemStack currency = new ItemStack(ConfigBasics.CURRENCY);

            PoseStack pose = graphics.pose();
            if (hovered) {
                pose.pushPose();
                pose.translate(0f, 10f, 800f);
                if (x > left && x < left + 115) {
                    List<Component> tooltipFromItem = Screen.getTooltipFromItem(minecraft, itemStack);
                    graphics.renderTooltip(font, tooltipFromItem, itemStack.getTooltipImage(), itemStack, x, y);
                } else if (x > left + 316 && x < left + 328) {
                    // this is for the currency tooltip
                    graphics.renderTooltip(font, currency, x, y);
                } else if (x > left + 328 && y < top + 8) {
                    graphics.renderTooltip(font, Component.translatable("Current Bidding Price"), x, y);
                } else if (x > left + 328 && y > top + 8 && y < top + 25) {
                    graphics.renderTooltip(font, Component.translatable("Buyout Price"), x, y);
                }
                pose.popPose();
            }


            pose = graphics.pose();
            pose.pushPose();
            pose.scale(0.5f, 0.5f, 0.5f);
            pose.translate(left + 308, top + 15, 0f);
            graphics.renderFakeItem(currency, left + 328, top + 15);
            pose.scale(2f, 2f, 2f);
            graphics.pose().popPose();
        }

        @Override
        public void renderBack(GuiGraphics graphics, int row, int top, int left, int width, int height, int x, int y, boolean hovered, float delta) {
            super.renderBack(graphics, row, top, left, width, height, x, y, hovered, delta);
            Entry hovered1 = AuctionListWidget.this.getHovered();
            if (hovered) {
                graphics.fill(left, top, left + width - 3, top + 24, 0xff215581);
            } else {
                graphics.fill(left, top, left + width - 3, top + 24, 0xff42a4f5);
            }
            //System.out.println(hovered1);

        }

        @Override
        public List<? extends GuiEventListener> children() {
            return List.of();
        }
    }
}
