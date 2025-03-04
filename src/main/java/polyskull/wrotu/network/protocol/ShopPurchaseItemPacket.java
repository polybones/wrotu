package polyskull.wrotu.network.protocol;

import net.mcreator.wheat_death_of_the_universe.init.WheatdeathoftheuniverseModItems;
import net.minecraft.network.FriendlyByteBuf;
import net.minecraft.server.level.ServerPlayer;
import net.minecraft.sounds.SoundSource;
import net.minecraft.world.item.ItemStack;
import net.minecraftforge.network.NetworkEvent;
import polyskull.wrotu.init.ModShops;
import polyskull.wrotu.init.ModSounds;
import polyskull.wrotu.shop.Shop;
import polyskull.wrotu.shop_legacy.ShopEntry;
import polyskull.wrotu.shop_legacy.ShopManager;

import java.util.function.Supplier;

public record ShopPurchaseItemPacket(int id, int shopIndex) {
    public void encoder(FriendlyByteBuf buffer) {
        buffer.writeInt(id);
        buffer.writeInt(shopIndex);
    }

    public static ShopPurchaseItemPacket decoder(FriendlyByteBuf buffer) {
        final int id = buffer.readInt();
        final int index = buffer.readInt();
        return new ShopPurchaseItemPacket(id, index);
    }

    @SuppressWarnings("all")
    public static void handle(ShopPurchaseItemPacket msg, Supplier<NetworkEvent.Context> ctx) {
        // FIXME -> Buying item with full inventory removes money but doesnt give item.
        ctx.get().enqueueWork(() -> {
            ServerPlayer sender = ctx.get().getSender();
            // final ShopEntry shopEntry = ShopManager.INSTANCE.getEntries().get(msg.shopIndex());
            final Shop shop = ModShops.getShops().get(msg.id());
            if(shop != null) {
                final Shop.Entry shopEntry = shop.getManager().getEntry(msg.shopIndex());
                int totalCost = shopEntry.cost();
                if(ShopManager.playerHasEnoughMoney(sender, totalCost)) {
                    int i = 0;
                    boolean completed = false;
                    while(!completed) {
                        final ItemStack stack = sender.getInventory().getItem(i);
                        if(stack.is(WheatdeathoftheuniverseModItems.DOLLARBILL.get())) {
                            final int count = stack.getCount();
                            if(Math.max(totalCost - count, 0) == 0) {
                                stack.shrink(totalCost);
                                completed = true;
                            }
                            else {
                                stack.setCount(0);
                                totalCost -= count;
                            }
                        }
                        i++;
                    }
                    sender.addItem(shopEntry.stack().copy());
                    sender.level().playSound(
                            sender,
                            sender.blockPosition(),
                            ModSounds.BUY_ITEM_SUCCESS.get(),
                            SoundSource.PLAYERS
                    );
                }
            }
        });
        ctx.get().setPacketHandled(true);
    }
}