package io.jonuuh.dualhotbar.mixin;

import io.jonuuh.dualhotbar.config.SharedMixinFields;
import net.minecraft.client.Minecraft;
import net.minecraft.client.gui.GuiIngame;
import net.minecraftforge.client.GuiIngameForge;
import org.spongepowered.asm.mixin.Mixin;
import org.spongepowered.asm.mixin.injection.At;
import org.spongepowered.asm.mixin.injection.ModifyArg;

@Mixin(GuiIngameForge.class)
public abstract class MixinGuiIngameForge extends GuiIngame
{
    private MixinGuiIngameForge(Minecraft mc)
    {
        super(mc);
    }

    @ModifyArg(method = "renderExperience",
            at = @At(value = "INVOKE", target = "Lnet/minecraftforge/client/GuiIngameForge;drawTexturedModalRect(IIIIII)V"),
            index = 1
    )
    private int dualhotbar$raiseExpBar(int y)
    {
        return SharedMixinFields.isDualHotbarEnabled() ? y - SharedMixinFields.getHudRaiseAmount() : y;
    }

    @ModifyArg(
            method = "renderExperience",
            at = @At(value = "INVOKE", target = "Lnet/minecraft/client/gui/FontRenderer;drawString(Ljava/lang/String;III)I"),
            index = 2
    )
    private int dualhotbar$raiseExpString(int y)
    {
        return SharedMixinFields.isDualHotbarEnabled() ? y - SharedMixinFields.getHudRaiseAmount() : y;
    }

    @ModifyArg(method = "renderToolHightlight",
            at = @At(value = "INVOKE", target = "Lnet/minecraft/client/gui/FontRenderer;drawStringWithShadow(Ljava/lang/String;FFI)I"),
            index = 2
    )
    private float dualhotbar$raiseHeldItemText(float y)
    {
        return SharedMixinFields.isDualHotbarEnabled() ? y - SharedMixinFields.getHudRaiseAmount() : y;
    }

    @ModifyArg(method = "renderRecordOverlay",
            at = @At(value = "INVOKE", target = "Lnet/minecraft/client/gui/FontRenderer;drawString(Ljava/lang/String;III)I"),
            index = 2
    )
    private int dualhotbar$raiseRecordOverlayText(int y)
    {
        return SharedMixinFields.isDualHotbarEnabled() ? y - SharedMixinFields.getHudRaiseAmount() : y;
    }
}
