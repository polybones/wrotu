package polyskull.wrotu.mixin;

import com.mojang.blaze3d.platform.Lighting;
import com.mojang.blaze3d.vertex.PoseStack;
import net.mcreator.wheat_death_of_the_universe.client.gui.UtilityinternetuiScreen;
import net.mcreator.wheat_death_of_the_universe.world.inventory.UtilityinternetuiMenu;
import net.minecraft.client.gui.GuiGraphics;
import net.minecraft.client.gui.components.Button;
import net.minecraft.client.gui.screens.inventory.AbstractContainerScreen;
import net.minecraft.client.renderer.texture.OverlayTexture;
import net.minecraft.network.chat.Component;
import net.minecraft.resources.ResourceLocation;
import net.minecraft.sounds.SoundSource;
import net.minecraft.world.entity.player.Inventory;
import net.minecraft.world.item.ItemDisplayContext;
import net.minecraft.world.item.ItemStack;
import org.jetbrains.annotations.NotNull;
import org.joml.Matrix4f;
import org.spongepowered.asm.mixin.*;
import org.spongepowered.asm.mixin.injection.*;
import org.spongepowered.asm.mixin.injection.callback.CallbackInfo;
import polyskull.wrotu.Wrotu;
import polyskull.wrotu.init.ModShops;
import polyskull.wrotu.init.ModSounds;
import polyskull.wrotu.network.PacketHandler;
import polyskull.wrotu.network.protocol.ShopPurchaseItemPacket;
import polyskull.wrotu.shop.Shop;
import polyskull.wrotu.util.HoverButton;
import polyskull.wrotu.shop_legacy.ShopManager;

@SuppressWarnings("AddedMixinMembersNamePattern")
@Mixin(UtilityinternetuiScreen.class)
public abstract class MixinInternetScreen extends AbstractContainerScreen<UtilityinternetuiMenu> {
    @Unique
    private static final ResourceLocation BG_TEXTURE =
            new ResourceLocation(Wrotu.MOD_ID, "textures/screens/internet_screen.png");
    @Unique
    private static final int SPEED = 1000;
    @Unique
    private static final float SCALE = 3.0F;

    // Dummy constructor
    private MixinInternetScreen(UtilityinternetuiMenu p_97741_, Inventory p_97742_, Component p_97743_) {
        super(p_97741_, p_97742_, p_97743_);
    }

    @Inject(method = "renderLabels", at = @At("HEAD"), cancellable = true)
    private void wrotu$internetUiScreen$renderLabels(GuiGraphics guiGraphics, int mouseX, int mouseY, CallbackInfo ci) {
        ci.cancel();

        final Shop.Entry shopEntry = ModShops.PERSONAL_COMPUTER
                .getClientManager().getEntry();
        guiGraphics.drawString(
                this.font,
                shopEntry.stack().getHoverName(),
                this.titleLabelX + 60,
                this.titleLabelY + 10,
                -1,
                true
        );
        guiGraphics.drawString(
                this.font,
                String.format("%,d", shopEntry.cost()),
                this.titleLabelX + 76,
                this.titleLabelY + 25,
                -1,
                true
        );
    }

    @Redirect(
            method = "renderBg",
            at = @At(
                    value = "INVOKE",
                    target = "Lnet/minecraft/client/gui/GuiGraphics;blit(Lnet/minecraft/resources/ResourceLocation;IIFFIIII)V"
            )
    )
    private void wrotu$internetUiScreen$disableBg(
            GuiGraphics instance,
            ResourceLocation p_283272_,
            int p_283605_,
            int p_281879_,
            float p_282809_,
            float p_282update942_,
            int p_281922_,
            int p_282385_,
            int p_282596_,
            int p_281699_
    ) {
        // This disables rendering most of the shop elements, including the background
    }

    @Inject(method = "renderBg", at = @At(value = "INVOKE", target = "Lcom/mojang/blaze3d/systems/RenderSystem;defaultBlendFunc()V", shift = At.Shift.AFTER))
    private void wrotu$internetUiScreen$renderBg(@NotNull GuiGraphics guiGraphics, float partialTicks, int gx, int gy, CallbackInfo ci) {
        // Gui background
        guiGraphics.blit(
                BG_TEXTURE,
                this.leftPos, this.topPos,
                0.0F, 0.0F,
                this.imageWidth, this.imageHeight,
                this.imageWidth, this.imageHeight
        );

        this.renderSpinningItem(
                ModShops.PERSONAL_COMPUTER.getClientManager().getEntry().stack(),
                guiGraphics,
                this.leftPos + 25,
                this.topPos + 30
        );
    }

    @Inject(method = "init", at = @At("HEAD"), cancellable = true)
    private void wrotu$internetUiScreen$rewriteInit(CallbackInfo ci) {
        ci.cancel();
        super.init();

        this.addRenderableWidget(new HoverButton(Button.builder(
                Component.literal("<"), button ->
                        ModShops.PERSONAL_COMPUTER.getClientManager().prevItem()).bounds(
                this.leftPos + 64, this.topPos + 46,
                12, 14)
        ));
        this.addRenderableWidget(new HoverButton(Button.builder(
                Component.literal(">"), button ->
                        ModShops.PERSONAL_COMPUTER.getClientManager().nextItem()).bounds(
                this.leftPos + 80, this.topPos + 46,
                12, 14)
        ));
        this.addRenderableWidget(new HoverButton(Button.builder(
                Component.translatable("gui.wrotu.internetui.purchase_button"), button -> {
                    if(this.minecraft != null && this.minecraft.player != null && this.minecraft.level != null) {
                        final Shop pc = ModShops.PERSONAL_COMPUTER;
                        final Shop.ClientManager clientManager = pc.getClientManager();
                        if(ShopManager.playerHasEnoughMoney(this.minecraft.player, clientManager.getEntry().cost())) {
                            PacketHandler.INSTANCE.sendToServer(
                                    new ShopPurchaseItemPacket(pc.getClientManager().getId(), clientManager.getShopIndex()));
                            this.minecraft.level.playLocalSound(
                                    this.minecraft.player.blockPosition(),
                                    ModSounds.BUY_ITEM_SUCCESS.get(),
                                    SoundSource.PLAYERS,
                                    1.0F,
                                    1.0F,
                                    false
                            );
                        }
                    }
                }).bounds(
                this.leftPos + 96, this.topPos + 46,
                36, 14)
        ));
    }

    @Unique
    private void renderSpinningItem(final ItemStack stack, GuiGraphics guiGraphics, double x, double y) {
        if(this.minecraft != null) {
            final PoseStack poseStack = guiGraphics.pose();
            final float angle = (float) (System.currentTimeMillis() % (360 * SPEED)) / SPEED;

            try {
                poseStack.pushPose();
                poseStack.translate((float) (x + 8), (float) (y + 8), 150.0F);
                poseStack.mulPoseMatrix(
                        new Matrix4f()
                                .scaling(SCALE, -SCALE, SCALE)
                                .rotateLocalY(angle)
                );
                poseStack.scale(16.0F, 16.0F, 16.0F);
                Lighting.setupFor3DItems();
                this.minecraft.getItemRenderer().render(
                        stack,
                        ItemDisplayContext.GUI,
                        false,
                        poseStack,
                        guiGraphics.bufferSource(),
                        15728880,
                        OverlayTexture.NO_OVERLAY,
                        // ClientShopManager.getCachedModel()
                        this.minecraft.getItemRenderer().getModel(
                                stack,
                                this.minecraft.level,
                                this.minecraft.player,
                                0
                        )
                );
                guiGraphics.flush();
            }
            catch(Exception ex) {
                Wrotu.LOGGER.error("Error rendering shop entry preview: {}", ex.getMessage());
            }

            poseStack.popPose();
        }
    }
}
