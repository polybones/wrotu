package polyskull.wrotu.mixin;

import net.mcreator.wheat_death_of_the_universe.world.inventory.UtilityinternetuiMenu;
import net.minecraft.network.FriendlyByteBuf;
import net.minecraft.world.entity.player.Inventory;
import net.minecraft.world.inventory.AbstractContainerMenu;
import net.minecraft.world.inventory.MenuType;
import org.jetbrains.annotations.Nullable;
import org.spongepowered.asm.mixin.Mixin;
import org.spongepowered.asm.mixin.injection.At;
import org.spongepowered.asm.mixin.injection.Inject;
import org.spongepowered.asm.mixin.injection.callback.CallbackInfo;

@Mixin(UtilityinternetuiMenu.class)
public abstract class MixinInternetMenu extends AbstractContainerMenu {
    private MixinInternetMenu(@Nullable MenuType<?> p_38851_, int p_38852_) {
        super(p_38851_, p_38852_);
    }

    @Inject(method = "<init>", at = @At("TAIL"))
    private void wrotu$internetUiMenu$removeDefaultSlots(int id, Inventory inv, FriendlyByteBuf extraData, CallbackInfo ci) {
        // yoink lol

        this.slots.subList(0, 16).clear();
        this.slots.forEach(slot -> slot.index -= 16);
    }
}