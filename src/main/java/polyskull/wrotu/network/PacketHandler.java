package polyskull.wrotu.network;

import net.minecraft.resources.ResourceLocation;
import net.minecraftforge.network.NetworkRegistry;
import net.minecraftforge.network.simple.SimpleChannel;
import polyskull.wrotu.Wrotu;
import polyskull.wrotu.network.protocol.ShopPurchaseItemPacket;
import polyskull.wrotu.network.protocol.ShopSyncPacket;

public class PacketHandler {
    private static final String PROTOCOL_VERSION = "1";
    public static final SimpleChannel INSTANCE = NetworkRegistry.newSimpleChannel(
            new ResourceLocation(Wrotu.MOD_ID, "main"),
            () -> PROTOCOL_VERSION,
            PROTOCOL_VERSION::equals,
            PROTOCOL_VERSION::equals
    );

    public static void register() {
        INSTANCE.messageBuilder(ShopSyncPacket.class, 0)
                .encoder(ShopSyncPacket::encoder)
                .decoder(ShopSyncPacket::decoder)
                .consumerMainThread(ShopSyncPacket::handle)
                .add();
        INSTANCE.messageBuilder(ShopPurchaseItemPacket.class, 1)
                .encoder(ShopPurchaseItemPacket::encoder)
                .decoder(ShopPurchaseItemPacket::decoder)
                .consumerMainThread(ShopPurchaseItemPacket::handle)
                .add();
    }
}