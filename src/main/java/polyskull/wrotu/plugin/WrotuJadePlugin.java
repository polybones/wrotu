package polyskull.wrotu.plugin;

import net.minecraft.resources.ResourceLocation;
import net.minecraft.world.entity.item.ItemEntity;
import polyskull.wrotu.Wrotu;
import snownee.jade.api.IWailaClientRegistration;
import snownee.jade.api.IWailaPlugin;
import snownee.jade.api.WailaPlugin;

@WailaPlugin
public class WrotuJadePlugin implements IWailaPlugin {
    public static final ResourceLocation WALLET = provider("wallet");

    @Override
    public void registerClient(IWailaClientRegistration registration) {
        registration.registerEntityComponent(WalletComponentProvider.INSTANCE, ItemEntity.class);
    }

    private static ResourceLocation provider(String name) {
        return new ResourceLocation(Wrotu.MOD_ID, name);
    }
}