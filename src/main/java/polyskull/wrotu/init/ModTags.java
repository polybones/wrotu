package polyskull.wrotu.init;

import net.minecraft.resources.ResourceLocation;
import net.minecraft.tags.ItemTags;
import net.minecraft.tags.TagKey;
import net.minecraft.world.item.Item;
import polyskull.wrotu.Wrotu;

public class ModTags {
    public static class Items {
        public static final TagKey<Item> WALLETS = register("wallets");

        private static TagKey<Item> register(String name) {
            return ItemTags.create(new ResourceLocation(Wrotu.MOD_ID, name));
        }
    }
}
