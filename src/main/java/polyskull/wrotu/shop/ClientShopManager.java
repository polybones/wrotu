package polyskull.wrotu.shop;

import net.minecraft.client.Minecraft;
import net.minecraft.client.renderer.entity.ItemRenderer;
import net.minecraft.client.resources.model.BakedModel;
import net.minecraftforge.network.NetworkEvent;
import net.minecraftforge.network.PacketDistributor;
import org.jetbrains.annotations.NotNull;
import polyskull.wrotu.network.PacketHandler;
import polyskull.wrotu.network.protocol.ShopPurchaseItemPacket;
import polyskull.wrotu.network.protocol.ShopSyncPacket;

import java.util.ArrayList;
import java.util.function.Supplier;

public class ClientShopManager {
    private static ArrayList<ShopEntry> SHOP_ENTRIES;
    private static int shopIndex = 0;
    private static BakedModel MODEL;

    @SuppressWarnings("unused")
    public static void handleClient(@NotNull ShopSyncPacket msg, Supplier<NetworkEvent.Context> ctx) {
        SHOP_ENTRIES = msg.entries();
        final int newSize = SHOP_ENTRIES.size() - 1;
        if(shopIndex > newSize) {
            shopIndex = newSize;
        }
        updateCachedModel();
    }

    public static ShopEntry getEntry() {
        return SHOP_ENTRIES.get(shopIndex);
    }

    public static BakedModel getCachedModel() {
        return MODEL;
    }

    public static int getShopIndex() {
        return shopIndex;
    }

    public static void nextItem() {
        if(shopIndex < SHOP_ENTRIES.size() - 1) {
            shopIndex++;
        }
        else {
            shopIndex = 0;
        }
        updateCachedModel();
    }

    public static void prevItem() {
        if(shopIndex > 0) {
            shopIndex--;
        }
        else {
            shopIndex = SHOP_ENTRIES.size() - 1;
        }
        updateCachedModel();
    }

    private static void updateCachedModel() {
        // this should not produce a NullPointerException because the
        // item renderer exists long before this method ever gets called

        final Minecraft client = Minecraft.getInstance();
        final ItemRenderer itemRenderer = client.getItemRenderer();
        final ShopEntry shopEntry = ClientShopManager.getEntry();
        MODEL = itemRenderer.getModel(
                shopEntry.itemStack(),
                client.level,
                client.player,
                0
        );
    }
}