package polyskull.wrotu.init;

import net.mcreator.wheat_death_of_the_universe.init.WheatdeathoftheuniverseModItems;
import net.minecraft.core.registries.Registries;
import net.minecraft.network.chat.Component;
import net.minecraft.world.item.CreativeModeTab;
import net.minecraft.world.item.ItemStack;
import net.minecraft.world.item.Items;
import net.minecraftforge.eventbus.api.IEventBus;
import net.minecraftforge.registries.DeferredRegister;
import net.minecraftforge.registries.RegistryObject;
import polyskull.wrotu.Wrotu;

public class ModCreativeTabs {
    private static final DeferredRegister<CreativeModeTab> CREATIVE_MODE_TABS =
            DeferredRegister.create(Registries.CREATIVE_MODE_TAB, Wrotu.MOD_ID);

    public static final RegistryObject<CreativeModeTab> WROTU_TAB = CREATIVE_MODE_TABS.register("wrotu_tab",
            () -> CreativeModeTab.builder()
                    .icon(() -> new ItemStack(WheatdeathoftheuniverseModItems.HOMONCULUSSLAB.get()))
                    .title(Component.translatable("item_group." + Wrotu.MOD_ID + ".wrotu_tab"))
                    .displayItems((params, output) -> {
                        output.accept(Items.DIRT);
                    })
                    .build());

    public static void register(IEventBus eventBus) {
        CREATIVE_MODE_TABS.register(eventBus);
    }
}
