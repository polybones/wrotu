package polyskull.wrotu.init;

import net.minecraft.resources.ResourceLocation;
import net.minecraft.sounds.SoundEvent;
import net.minecraftforge.eventbus.api.IEventBus;
import net.minecraftforge.registries.DeferredRegister;
import net.minecraftforge.registries.ForgeRegistries;
import net.minecraftforge.registries.RegistryObject;
import polyskull.wrotu.Wrotu;

public class ModSounds {
    private static final DeferredRegister<SoundEvent> SOUND_EVENTS =
            DeferredRegister.create(ForgeRegistries.SOUND_EVENTS, Wrotu.MOD_ID);

    public static final RegistryObject<SoundEvent> BUY_ITEM_SUCCESS =
            registerFixedSoundEvent("buy_item_success");

    private static RegistryObject<SoundEvent> registerFixedSoundEvent(String name) {
        return SOUND_EVENTS.register(name, () -> SoundEvent.createFixedRangeEvent(
                new ResourceLocation(Wrotu.MOD_ID, name), 1.0F));
    }

    public static void register(IEventBus eventBus) {
        SOUND_EVENTS.register(eventBus);
    }
}
