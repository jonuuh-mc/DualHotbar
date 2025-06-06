package io.jonuuh.dualhotbar.mixin;

import net.minecraft.client.gui.inventory.GuiContainer;
import net.minecraft.inventory.Slot;
import org.spongepowered.asm.mixin.Mixin;
import org.spongepowered.asm.mixin.gen.Invoker;

@Mixin(GuiContainer.class)
public interface MixinGuiContainer
{
    @Invoker("handleMouseClick")
    void dualhotbar$invokeHandleMouseClick(Slot slotIn, int slotId, int clickedButton, int clickType);
}
