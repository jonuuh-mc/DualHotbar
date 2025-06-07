package io.jonuuh.dualhotbar.mixin.gui;

import io.jonuuh.dualhotbar.config.SharedMixinFields;
import net.minecraft.client.gui.inventory.GuiInventory;
import org.spongepowered.asm.mixin.Mixin;
import org.spongepowered.asm.mixin.injection.At;
import org.spongepowered.asm.mixin.injection.Inject;
import org.spongepowered.asm.mixin.injection.callback.CallbackInfo;

@Mixin(GuiInventory.class)
public class MixinGuiInventory
{
    @Inject(method = "drawScreen", at = @At(value = "HEAD"), cancellable = true)
    private void dualhotbar$tryOverwriteHotbarHotkey(CallbackInfo ci)
    {
        if (!SharedMixinFields.getShouldDrawInventory())
        {
            ci.cancel();
        }
    }
}
