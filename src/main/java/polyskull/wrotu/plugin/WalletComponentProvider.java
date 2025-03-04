package polyskull.wrotu.plugin;

import net.mcreator.wheat_death_of_the_universe.init.WheatdeathoftheuniverseModItems;
import net.minecraft.network.chat.Component;
import net.minecraft.resources.ResourceLocation;
import net.minecraft.world.entity.item.ItemEntity;
import net.minecraft.world.item.ItemStack;
import net.minecraft.world.phys.Vec2;
import net.minecraftforge.common.capabilities.ForgeCapabilities;
import polyskull.wrotu.init.ModTags;
import snownee.jade.api.EntityAccessor;
import snownee.jade.api.IEntityComponentProvider;
import snownee.jade.api.ITooltip;
import snownee.jade.api.config.IPluginConfig;
import snownee.jade.api.ui.IElement;
import snownee.jade.api.ui.IElementHelper;

import java.util.concurrent.atomic.AtomicInteger;

public enum WalletComponentProvider implements IEntityComponentProvider {
    INSTANCE;

    @Override
    public void appendTooltip(ITooltip tooltip, EntityAccessor entity, IPluginConfig config) {
        if(entity.getEntity() instanceof ItemEntity itemEntity) {
            final ItemStack stack = itemEntity.getItem();
            if(stack.is(ModTags.Items.WALLETS)) {
                IElementHelper elements = tooltip.getElementHelper();
                IElement icon = elements.item(new ItemStack(
                        WheatdeathoftheuniverseModItems.DOLLARBILL.get()), 0.5f)
                        .size(new Vec2(10, 10))
                        .translate(new Vec2(0, -1));
                tooltip.add(icon);
                AtomicInteger count = new AtomicInteger();
                stack.getCapability(ForgeCapabilities.ITEM_HANDLER).ifPresent(handler -> {
                    for(int i = 0; i < handler.getSlots(); ++i) {
                        ItemStack slotStack = handler.getStackInSlot(i);
                        if(slotStack.is(WheatdeathoftheuniverseModItems.DOLLARBILL.get())) {
                            count.addAndGet(slotStack.getCount());
                        }
                    }
                });
                tooltip.append(Component.literal(String.format("%,d", count.get())));
            }
        }
    }

    @Override
    public ResourceLocation getUid() {
        return WrotuJadePlugin.WALLET;
    }
}