package com.epherical.auctionworld.mixin;

import io.netty.buffer.ByteBuf;
import net.minecraft.network.FriendlyByteBuf;
import net.minecraft.world.item.ItemStack;
import net.minecraft.world.level.ItemLike;
import org.spongepowered.asm.mixin.Mixin;
import org.spongepowered.asm.mixin.Shadow;
import org.spongepowered.asm.mixin.injection.At;
import org.spongepowered.asm.mixin.injection.Redirect;

@Mixin(FriendlyByteBuf.class)
public abstract class FriendlyByteBufItemStackSizeMixin {


    @Shadow public abstract short readShort();

    @Redirect(method = "writeItem", at = @At(value = "INVOKE", target = "Lnet/minecraft/network/FriendlyByteBuf;writeByte(I)Lio/netty/buffer/ByteBuf;"))
    public ByteBuf auctionTheWorld$changeByteToShort(FriendlyByteBuf instance, int count) {
        System.out.println("writin");
        instance.writeShort(count);
        return instance;
    }

    @Redirect(method = "readItem", at = @At(value = "INVOKE", target = "Lnet/minecraft/network/FriendlyByteBuf;readByte()B"))
    public byte auctionTheWorld$ignoreByteRead(FriendlyByteBuf instance) {
        System.out.println("mixxxxinn");
        return 0;
        // todo; implement mixin when we get back.
    }

    @Redirect(method = "readItem", at = @At(value = "INVOKE_ASSIGN", target = "Lnet/minecraft/world/item/ItemStack;<init>(Lnet/minecraft/world/level/ItemLike;I)V"))
    public void auctionTheWorld$readShortInsteadOfByte(ItemStack instance, ItemLike item, int oldInt) {
        System.out.println("misin workin");
        instance.setCount(this.readShort());
    }
}
