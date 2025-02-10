package polyskull.wrotu.shop;

import net.minecraft.world.item.ItemStack;

public record ShopEntry(ItemStack itemStack, int cost) {}