package com.epherical.auctionworld.client.screen;

import com.epherical.auctionworld.listener.RegisterListener;
import com.epherical.auctionworld.menu.CreateAuctionMenu;
import com.epherical.auctionworld.menu.slot.SelectableSlot;
import net.minecraft.client.gui.GuiGraphics;
import net.minecraft.client.gui.components.Button;
import net.minecraft.client.gui.components.EditBox;
import net.minecraft.client.gui.components.Tooltip;
import net.minecraft.client.gui.screens.inventory.AbstractContainerScreen;
import net.minecraft.network.chat.Component;
import net.minecraft.resources.ResourceLocation;
import net.minecraft.world.entity.player.Inventory;
import net.minecraft.world.inventory.Slot;

public class CreateAuctionScreen extends AbstractContainerScreen<CreateAuctionMenu> {
    private static final ResourceLocation AUCTION_LOCATION = RegisterListener.id("textures/gui/container/auction.png");


    private Button createAuction;
    private EditBox timeSelection;
    private Button days;
    private Button hours;
    private EditBox bidIncrement;
    private EditBox startingBid;
    private EditBox buyoutPrice;




    public CreateAuctionScreen(CreateAuctionMenu menu, Inventory inventory, Component title) {
        super(menu, inventory, title);
        // buyout price
        // time selection - days button, hours button, textbox to input a time
        // min bid increment - editbox


        // item display to show all items in the listing
    }

    @Override
    protected void init() {
        imageWidth = 512;
        imageHeight = 512;
        super.init();

        createAuction = addRenderableWidget(Button.builder(Component.translatable("Create Auction"), button -> {
            System.out.println("Wewooweooo");
        }).pos(343 + leftPos, 249 + topPos).width(80).build());

        timeSelection = addRenderableWidget(new EditBox(font, 126 + leftPos, 44 + topPos, 100, 20, Component.translatable("Time Selection")));
        timeSelection.setFilter(s -> s.matches("[0-9]+") || s.isEmpty());
        timeSelection.setTooltip(Tooltip.create(Component.translatable("How much time until the auction expires. Max: 7D or 168H")));

        days = addRenderableWidget(Button.builder(Component.literal("D"), button -> {
            System.out.println("D");
        }).pos(231 + leftPos, 44 + topPos).width(20).tooltip(Tooltip.create(Component.translatable("How many days until the auction expires"))).build());
        hours = addRenderableWidget(Button.builder(Component.literal("H"), button -> {
            System.out.println("H");
        }).pos(263 + leftPos, 44 + topPos).width(20).tooltip(Tooltip.create(Component.translatable("How many hours until the auction expires"))).build());
        /*bidIncrement = addRenderableWidget(new EditBox(font, 126 + leftPos, 64 + topPos, 100, 20, Component.translatable("Bid Increment")));
        bidIncrement.setFilter(s -> s.matches("[0-9]+") || s.isEmpty());
        bidIncrement.setHint(Component.literal("Bid Increment"));
        bidIncrement.setTooltip(Tooltip.create(Component.translatable("Whenever a player bids, how much they have to bid at a minimum.")));*/
        startingBid = addRenderableWidget(new EditBox(font, 126 + leftPos, 84 + topPos, 100, 20, Component.translatable("Starting Bid")));
        startingBid.setFilter(s -> s.matches("[0-9]+") || s.isEmpty());
        startingBid.setTooltip(Tooltip.create(Component.translatable("What the starting bid will be. This is the minimum price someone must pay to receive the item")));
        buyoutPrice = addRenderableWidget(new EditBox(font, 126 + leftPos, 124 + topPos, 100, 20, Component.translatable("Buyout Price")));
        buyoutPrice.setFilter(s -> s.matches("[0-9]+") || s.isEmpty());
        buyoutPrice.setTooltip(Tooltip.create(Component.translatable("How much a user will have to pay to just straight up buy the item. Leave blank to not set one.")));
    }


    @Override
    public void render(GuiGraphics graphics, int x, int y, float delta) {
        this.renderBackground(graphics);
        super.render(graphics, x, y, delta);

        graphics.pose().pushPose();
        graphics.pose().translate(leftPos, topPos, -500.0F);
        if (this.menu.getFirstSlot() != null) {
            for (Slot slot : this.menu.slots) {
                SelectableSlot select = (SelectableSlot) slot;
                int slotX = select.x;
                int slotY = select.y;
                if (select.isSelected()) {
                    renderSlotHighlight(graphics, slotX, slotY, 0, 0x33FFFF00);
                }
            }
            int slotX = menu.getFirstSlot().x;
            int slotY = menu.getFirstSlot().y;
            renderSlotHighlight(graphics, slotX, slotY, 0, 0x3300FF00);
        }
        graphics.pose().popPose();

        this.renderTooltip(graphics, x, y);
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
