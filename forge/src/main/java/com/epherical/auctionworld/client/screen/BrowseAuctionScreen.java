package com.epherical.auctionworld.client.screen;

import com.epherical.auctionworld.AuctionTheWorldForge;
import com.epherical.auctionworld.client.AuctionListWidget;
import com.epherical.auctionworld.client.SortableButton;
import com.epherical.auctionworld.listener.RegisterListener;
import com.epherical.auctionworld.menu.BrowseAuctionMenu;
import com.epherical.auctionworld.networking.OpenCreateAuction;
import com.epherical.auctionworld.object.AuctionItem;
import net.minecraft.client.gui.GuiGraphics;
import net.minecraft.client.gui.components.Button;
import net.minecraft.client.gui.screens.inventory.AbstractContainerScreen;
import net.minecraft.network.chat.Component;
import net.minecraft.resources.ResourceLocation;
import net.minecraft.world.entity.player.Inventory;

import java.util.ArrayList;
import java.util.Comparator;
import java.util.List;

public class BrowseAuctionScreen extends AbstractContainerScreen<BrowseAuctionMenu> {
    private static final ResourceLocation AUCTION_LOCATION = RegisterListener.id("textures/gui/container/auction.png");


    private Button auctionScreenButton;
    //private Button browse;

    private SortableButton<AuctionItem> item;
    private SortableButton<AuctionItem> time;
    private SortableButton<AuctionItem> seller;
    private SortableButton<AuctionItem> bid;

    private AuctionListWidget list;


    public BrowseAuctionScreen(BrowseAuctionMenu menu, Inventory inventory, Component title) {
        super(menu, inventory, title);
    }

    @Override
    protected void init() {
        imageWidth = 512;
        imageHeight = 512;
        super.init();
        list = new AuctionListWidget(minecraft, this.width + 121, this.height, topPos + 45, 253, 25);
        list.setRenderBackground(false);
        list.setRenderTopAndBottom(false);
        //list.setLeftPos(leftPos);
        addWidget(list);
        auctionScreenButton = this.addRenderableWidget(Button.builder(Component.translatable("Create Auction"), press -> {
            AuctionTheWorldForge.getInstance().getNetworking().sendToServer(new OpenCreateAuction());
        }).width(80).pos(leftPos + 60, 258).build());

        time = new SortableButton<>(false, Comparator.comparing(AuctionItem::getTimeLeft), this.addRenderableWidget(Button.builder(Component.literal("Time -"),
                button -> {
                    button.setMessage(time.sortDirection("Time"));
                    time.setActivated(true);
                    item.setActivated(false);
                    seller.setActivated(false);
                    bid.setActivated(false);
                }).pos(leftPos + 242, topPos + 26).width(100).build()));
        item = new SortableButton<>(false, Comparator.comparing(auctionItem -> auctionItem.getAuctionItems().get(0).getHoverName().getString()),
                this.addRenderableWidget(Button.builder(Component.literal("Item -"),
                                button -> {
                                    button.setMessage(item.sortDirection("Item"));
                                    time.setActivated(false);
                                    item.setActivated(true);
                                    seller.setActivated(false);
                                    bid.setActivated(false);
                                })
                        .pos(leftPos + 125, topPos + 26).width(117)
                        .build()));
        seller = new SortableButton<>(false, Comparator.comparing(AuctionItem::getSeller), this.addRenderableWidget(Button.builder(Component.literal("Seller -"),
                        button -> {
                            button.setMessage(seller.sortDirection("Seller"));
                            seller.setActivated(true);
                            time.setActivated(false);
                            item.setActivated(false);
                            bid.setActivated(false);

                        })
                .pos(leftPos + 342, topPos + 26).width(100)
                .build()));
        bid = new SortableButton<>(false, Comparator.comparing(AuctionItem::getBuyoutPrice), this.addRenderableWidget(Button.builder(Component.literal("Buyout -"),
                        button -> {
                            button.setMessage(bid.sortDirection("Buyout"));
                            bid.setActivated(true);
                            time.setActivated(false);
                            item.setActivated(false);
                            seller.setActivated(false);
                        })
                .pos(leftPos + 442, topPos + 26).width(67)
                .build()));

        this.list.addEntries(AuctionTheWorldForge.getInstance().getAuctionManager().getAuctions());


        /*browse = this.addRenderableWidget(Button.builder(Component.translatable("Browse"), press -> {

        }).width(60).pos(leftPos, 258).build());*/
    }


    @Override
    public void render(GuiGraphics graphics, int x, int y, float delta) {
        this.renderBackground(graphics);
        super.render(graphics, x, y, delta);
        list.render(graphics, x, y, delta);
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
        /*seller.sort(auctionItems);
        bid.sort(auctionItems);
        item.sort(auctionItems);
        time.sort(auctionItems);*/
        /*graphics.drawString(this.font, this.title, this.titleLabelX, this.titleLabelY, 4210752, false);
        graphics.drawString(this.font, this.playerInventoryTitle, this.inventoryLabelX, this.inventoryLabelY, 4210752, false);*/

    }
}