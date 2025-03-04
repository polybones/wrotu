package polyskull.wrotu.init;

import polyskull.wrotu.shop.Shop;

import java.util.ArrayList;

public class ModShops {
    private static final ArrayList<Shop> SHOPS = new ArrayList<>();
    public static final Shop PERSONAL_COMPUTER = register("shop_entries");

    private static Shop register(String path) {
        final Shop shop = new Shop(path);
        SHOPS.add(shop);
        return shop;
    }

    public static ArrayList<Shop> getShops() {
        return SHOPS;
    }
}