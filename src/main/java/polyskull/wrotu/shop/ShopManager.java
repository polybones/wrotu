package polyskull.wrotu.shop;

import com.google.gson.Gson;
import com.google.gson.JsonElement;
import com.google.gson.JsonObject;
import com.google.gson.JsonSyntaxException;
import net.mcreator.wheat_death_of_the_universe.init.WheatdeathoftheuniverseModItems;
import net.minecraft.client.player.LocalPlayer;
import net.minecraft.resources.ResourceLocation;
import net.minecraft.server.packs.resources.ResourceManager;
import net.minecraft.server.packs.resources.SimpleJsonResourceReloadListener;
import net.minecraft.util.GsonHelper;
import net.minecraft.util.profiling.ProfilerFiller;
import net.minecraft.world.entity.player.Player;
import net.minecraft.world.item.Item;
import net.minecraft.world.item.ItemStack;
import org.jetbrains.annotations.NotNull;
import polyskull.wrotu.Wrotu;

import java.util.ArrayList;
import java.util.Map;
import java.util.Optional;

public class ShopManager extends SimpleJsonResourceReloadListener {
    private static final Gson GSON = new Gson();
    private ArrayList<ShopEntry> VALUES;
    public static ShopManager INSTANCE = new ShopManager();

    public ShopManager() {
        super(GSON, "shop_entries");
    }

    @Override
    protected void apply(Map<ResourceLocation, JsonElement> loaded, @NotNull ResourceManager manager, @NotNull ProfilerFiller profiler) {
        final ArrayList<ShopEntry> readValues = new ArrayList<>();
        loaded.forEach((resource, json) ->
                parse(json.getAsJsonObject(), resource).ifPresent(readValues::add));
        VALUES = readValues;
        Wrotu.LOGGER.info("Loaded {} shop entries", readValues.size());
    }

    private static Optional<ShopEntry> parse(JsonObject json, ResourceLocation path) {
        try {
            final int cost = GsonHelper.getAsInt(json, "cost");
            final Item item = GsonHelper.getAsItem(json, "item");
            final int amount = GsonHelper.getAsInt(json, "amount");
            return Optional.of(new ShopEntry(new ItemStack(item, amount), cost));
        } catch(JsonSyntaxException ex) {
            Wrotu.LOGGER.warn("Error loading shop entry '{}': {}", path, ex.getMessage());
            return Optional.empty();
        }
    }

    public ArrayList<ShopEntry> getEntries() {
        return VALUES;
    }

    public static boolean playerHasEnoughMoney(Player player, int cost) {
        return player.getInventory().countItem(WheatdeathoftheuniverseModItems.DOLLARBILL.get()) >= cost;
    }
}