package polyskull.wrotu.shop;

import com.google.gson.Gson;
import com.google.gson.JsonElement;
import com.google.gson.JsonObject;
import com.google.gson.JsonSyntaxException;
import net.minecraft.resources.ResourceLocation;
import net.minecraft.server.packs.resources.ResourceManager;
import net.minecraft.server.packs.resources.SimpleJsonResourceReloadListener;
import net.minecraft.util.GsonHelper;
import net.minecraft.util.profiling.ProfilerFiller;
import net.minecraft.world.item.Item;
import net.minecraft.world.item.ItemStack;
import org.jetbrains.annotations.NotNull;
import polyskull.wrotu.Wrotu;

import java.util.ArrayList;
import java.util.Map;
import java.util.Optional;

public class Shop {
    private final Manager manager;

    public Shop(String path) {
        this.manager = new Manager(path);
    }

    public Manager getManager() {
        return this.manager;
    }

    public static class Manager extends SimpleJsonResourceReloadListener {
        private final ArrayList<Entry> entries = new ArrayList<>();

        public Manager(String path) {
            super(new Gson(), path);
        }

        public ArrayList<Entry> getEntries() {
            return this.entries;
        }

        @Override
        protected void apply(Map<ResourceLocation, JsonElement> loaded, @NotNull ResourceManager manager, @NotNull ProfilerFiller profiler) {
            entries.clear();
            loaded.forEach((resource, json) ->
                    parse(json.getAsJsonObject(), resource).ifPresent(entries::add));
            Wrotu.LOGGER.info("Loaded {} shop entries", entries.size());
        }

        private static Optional<Entry> parse(JsonObject json, ResourceLocation path) {
            try {
                final int cost = GsonHelper.getAsInt(json, "cost");
                if(cost <= 0) {
                    throw new JsonSyntaxException("Invalid cost (cost must be greater than 0!)");
                }
                final Item item = GsonHelper.getAsItem(json, "item");
                final int amount = GsonHelper.getAsInt(json, "amount");
                if(amount < 1 || amount > 64) {
                    throw new JsonSyntaxException("Invalid amount (amount must be between 1-64!)");
                }
                return Optional.of(new Entry(new ItemStack(item, amount), cost));
            }
            catch(JsonSyntaxException ex) {
                Wrotu.LOGGER.warn("Error loading shop entry '{}': {}", path, ex.getMessage());
                return Optional.empty();
            }
        }
    }

    public record Entry(ItemStack stack, int cost) {}
}