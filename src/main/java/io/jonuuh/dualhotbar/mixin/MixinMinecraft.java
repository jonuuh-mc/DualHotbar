package io.jonuuh.dualhotbar.mixin;

import io.jonuuh.dualhotbar.config.SharedMixinFields;
import net.minecraft.client.Minecraft;
import net.minecraft.client.entity.EntityPlayerSP;
import org.spongepowered.asm.mixin.Mixin;
import org.spongepowered.asm.mixin.Shadow;
import org.spongepowered.asm.mixin.injection.At;
import org.spongepowered.asm.mixin.injection.Inject;
import org.spongepowered.asm.mixin.injection.ModifyArg;
import org.spongepowered.asm.mixin.injection.callback.CallbackInfo;

@Mixin(Minecraft.class)
public abstract class MixinMinecraft
{
    @Shadow
    public EntityPlayerSP thePlayer;

    // Handle hotbar hotkeying
    @Inject(method = "runTick",
            at = @At(value = "FIELD", target = "Lnet/minecraft/entity/player/InventoryPlayer;currentItem:I", shift = At.Shift.AFTER)
    )
    private void dualhotbar$tryOverwriteHotbarHotkey(CallbackInfo ci)
    {
        if (!SharedMixinFields.isDualHotbarEnabled())
        {
            return;
        }

        if (SharedMixinFields.switchHotbarKeybinding.isKeyDown())
        {
            // Set the secondary hotbar index to the current index (was just changed after currentItem field write)
            SharedMixinFields.secondaryHotbarCurrentIndex = thePlayer.inventory.currentItem;
            // Overwrite the just changed currentItem index with recorded starting index
            thePlayer.inventory.currentItem = SharedMixinFields.mainHotbarStartingIndex;
        }
    }

    // Handle hotbar scrolling
    @ModifyArg(method = "runTick",
            at = @At(value = "INVOKE", target = "Lnet/minecraft/entity/player/InventoryPlayer;changeCurrentItem(I)V")
    )
    private int dualhotbar$interceptHotbarScroll(int direction)
    {
        if (!SharedMixinFields.isDualHotbarEnabled())
        {
            return direction;
        }

        if (SharedMixinFields.switchHotbarKeybinding.isKeyDown())
        {
            if (direction > 0)
            {
                direction = 1;
            }
            if (direction < 0)
            {
                direction = -1;
            }

            SharedMixinFields.secondaryHotbarCurrentIndex = (SharedMixinFields.secondaryHotbarCurrentIndex - direction + 9) % 9;
            return 0;
        }
        return direction;
    }
}
