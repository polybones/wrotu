package polyskull.wrotu.network.protocol;

import net.minecraft.network.FriendlyByteBuf;
import net.minecraft.world.item.ItemStack;
import net.minecraftforge.api.distmarker.Dist;
import net.minecraftforge.fml.DistExecutor;
import net.minecraftforge.network.NetworkEvent;
import polyskull.wrotu.shop_legacy.ClientShopManager;
import polyskull.wrotu.shop_legacy.ShopEntry;

import java.util.ArrayList;
import java.util.function.Supplier;

public record ShopSyncPacket(ArrayList<ShopEntry> entries) {
    public void encoder(FriendlyByteBuf buffer) {
        this.entries.forEach(shopEntry -> {
            buffer.writeByte(shopEntry.cost());
            buffer.writeItem(shopEntry.itemStack());
        });
        buffer.writeByte(0x00); // null terminator byte
    }

    public static ShopSyncPacket decoder(FriendlyByteBuf buffer) {
        final ArrayList<ShopEntry> entries = new ArrayList<>();
        byte readByte; // cost
        while((readByte = buffer.readByte()) != 0x00) {
            final ItemStack stack = buffer.readItem();
            entries.add(new ShopEntry(stack, readByte));
        }
        return new ShopSyncPacket(entries);
    }

    @SuppressWarnings("all")
    public static void handle(ShopSyncPacket msg, Supplier<NetworkEvent.Context> ctx) {
        ctx.get().enqueueWork(() -> {
            DistExecutor.unsafeRunWhenOn(Dist.CLIENT, () -> () -> ClientShopManager.handleClient(msg, ctx));
        });
        ctx.get().setPacketHandled(true);
    }
}
