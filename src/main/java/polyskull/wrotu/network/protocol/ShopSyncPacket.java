package polyskull.wrotu.network.protocol;

import net.minecraft.network.FriendlyByteBuf;
import net.minecraft.world.item.ItemStack;
import net.minecraftforge.api.distmarker.Dist;
import net.minecraftforge.fml.DistExecutor;
import net.minecraftforge.network.NetworkEvent;
import polyskull.wrotu.init.ModShops;
import polyskull.wrotu.shop.Shop;
import polyskull.wrotu.shop_legacy.ClientShopManager;

import java.util.ArrayList;
import java.util.function.Supplier;

public record ShopSyncPacket(int id, ArrayList<Shop.Entry> entries) {
    public void encoder(FriendlyByteBuf buf) {
        buf.writeInt(this.id);
        this.entries.forEach(shopEntry -> {
            buf.writeInt(shopEntry.cost());
            buf.writeItem(shopEntry.stack());
        });
        buf.writeInt(0x00); // null terminator byte
    }

    public static ShopSyncPacket decoder(FriendlyByteBuf buf) {
        final ArrayList<Shop.Entry> entries = new ArrayList<>();
        final int id = buf.readInt();
        int readInt; // cost
        while((readInt = buf.readInt()) != 0x00) {
            final ItemStack stack = buf.readItem();
            entries.add(new Shop.Entry(stack, readInt));
        }
        return new ShopSyncPacket(id, entries);
    }

    @SuppressWarnings("all")
    public static void handle(ShopSyncPacket msg, Supplier<NetworkEvent.Context> ctx) {
        ctx.get().enqueueWork(() -> {
            // DistExecutor.unsafeRunWhenOn(Dist.CLIENT, () -> () -> ClientShopManager.handleClient(msg, ctx));
            DistExecutor.unsafeRunWhenOn(Dist.CLIENT, () -> () -> {
                final Shop shop = ModShops.getShops().get(msg.id());
                if(shop != null) {
                    shop.getClientManager().handleClient(msg, ctx);
                }
            });
        });
        ctx.get().setPacketHandled(true);
    }
}
