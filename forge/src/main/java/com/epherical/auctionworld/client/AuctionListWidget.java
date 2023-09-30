package com.epherical.auctionworld.client;

import com.epherical.auctionworld.object.AuctionItem;
import net.minecraft.client.Minecraft;
import net.minecraft.client.gui.Font;
import net.minecraft.client.gui.GuiGraphics;
import net.minecraft.client.gui.components.ContainerObjectSelectionList;
import net.minecraft.client.gui.components.events.GuiEventListener;
import net.minecraft.client.gui.narration.NarratableEntry;

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
            graphics.renderFakeItem(item.getAuctionItems().get(0), left, top);
            graphics.drawString(font, item.formatTimeLeft(), left + 120, top + 6, 0xFFFFFF, false);
            graphics.drawString(font, item.getAuctionItems().get(0).getHoverName(), left + 24, top + 6, 0xFFFFFF, false);
            graphics.drawString(font, item.getSeller(), left + 220, top + 6, 0xFFFFFF, false);
            graphics.drawString(font, String.valueOf(item.getBuyoutPrice()), left + 320, top + 6, 0xFFFFFF, false);
            graphics.drawString(font, String.valueOf(item.getCurrentPrice()), left + 320, top + -2, 0xFFFFFF, false);
        }

        @Override
        public void renderBack(GuiGraphics graphics, int row, int top, int left, int width, int height, int x, int y, boolean hovered, float delta) {
            super.renderBack(graphics, row, top, left, width, height, x, y, hovered, delta);
            Entry hovered1 = AuctionListWidget.this.getHovered();
            if (hovered) {
                graphics.fill(left, top, left + width -3, top + 24, 0xff215580);
            } else {
                graphics.fill(left, top, left + width -3, top + 24, 0xff42a4f5);
            }
            //System.out.println(hovered1);

        }

        @Override
        public List<? extends GuiEventListener> children() {
            return List.of();
        }
    }
}
