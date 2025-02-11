package polyskull.wrotu.shop;

import net.minecraft.client.gui.components.Button;

public class ShopButton extends Button {
    public ShopButton(Builder builder) {
        super(builder);
    }

    @Override
    public boolean isHoveredOrFocused() {
        return this.isHovered();
        //return (!this.isFocused() || this.isHovered()) && (this.isHovered() || this.isFocused());
    }
}