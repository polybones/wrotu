package polyskull.wrotu.shop;

import net.minecraftforge.network.NetworkEvent;
import polyskull.wrotu.network.protocol.ShopSyncPacket;

import java.util.ArrayList;
import java.util.function.Supplier;

public class ClientShopManager {
    private static ArrayList<ShopEntry> SHOP_ENTRIES;

    public static void handleClient(ShopSyncPacket msg, Supplier<NetworkEvent.Context> ctx) {
        SHOP_ENTRIES = msg.entries();
    }

    public static ShopEntry getEntry(int index) {
        return SHOP_ENTRIES.get(index);
    }

    public static ArrayList<ShopEntry> getAllEntries() {
        return SHOP_ENTRIES;
    }
}