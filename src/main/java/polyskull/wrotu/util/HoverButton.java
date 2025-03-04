package polyskull.wrotu.util;

import net.minecraft.client.gui.components.Button;

public class HoverButton extends Button {
    public HoverButton(Builder builder) {
        super(builder);
    }

    @Override
    public boolean isHoveredOrFocused() {
        return this.isHovered();
    }
}