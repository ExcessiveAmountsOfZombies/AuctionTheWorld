package com.epherical.auctionworld.object;

import net.minecraft.util.StringRepresentable;
import net.minecraft.world.item.ItemStack;

public record ClaimedItem(ClaimType type, ItemStack itemStack) {



    public enum ClaimType implements StringRepresentable {
        EXPIRED_LISTING("expired_listing"),
        WON_LISTING("won_listing");

        private final String type;

        ClaimType(String type) {
            this.type = type;
        }

        @Override
        public String getSerializedName() {
            return type;
        }
    }


}
