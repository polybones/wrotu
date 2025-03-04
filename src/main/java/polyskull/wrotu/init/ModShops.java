package polyskull.wrotu.init;

import org.jetbrains.annotations.NotNull;
import polyskull.wrotu.shop.Shop;

import java.util.ArrayList;

public class ModShops {
    private static final ArrayList<Shop> SHOPS = new ArrayList<>();
    private static int globalId = 0;
    public static final Shop PERSONAL_COMPUTER = register("shop_entries");

    public static @NotNull Shop register(String path) {
        final Shop shop = new Shop(path, globalId);
        ++globalId;
        SHOPS.add(shop);
        return shop;
    }

    public static ArrayList<Shop> getShops() {
        return SHOPS;
    }
}