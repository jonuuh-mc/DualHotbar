package io.jonuuh.dualhotbar.mixin;

import io.jonuuh.core.lib.util.RenderUtils;
import io.jonuuh.dualhotbar.DualHotbar;
import io.jonuuh.dualhotbar.config.SharedMixinFields;
import net.minecraft.client.Minecraft;
import net.minecraft.client.gui.Gui;
import net.minecraft.client.gui.GuiIngame;
import net.minecraft.client.gui.ScaledResolution;
import net.minecraft.client.renderer.GlStateManager;
import net.minecraft.client.renderer.RenderHelper;
import net.minecraft.entity.player.EntityPlayer;
import net.minecraft.entity.player.InventoryPlayer;
import net.minecraft.item.ItemStack;
import net.minecraft.util.ResourceLocation;
import org.lwjgl.opengl.GL11;
import org.spongepowered.asm.mixin.Final;
import org.spongepowered.asm.mixin.Mixin;
import org.spongepowered.asm.mixin.Shadow;
import org.spongepowered.asm.mixin.injection.At;
import org.spongepowered.asm.mixin.injection.Inject;
import org.spongepowered.asm.mixin.injection.ModifyArg;
import org.spongepowered.asm.mixin.injection.Redirect;
import org.spongepowered.asm.mixin.injection.callback.CallbackInfo;

@Mixin(GuiIngame.class)
public abstract class MixinGuiIngame extends Gui
{
    @Final
    @Shadow
    protected Minecraft mc;

    @Final
    @Shadow
    protected static ResourceLocation widgetsTexPath;

    @Shadow
    protected abstract void renderHotbarItem(int index, int xPos, int yPos, float partialTicks, EntityPlayer player);

    @Inject(method = "renderTooltip", at = @At(value = "TAIL"))
    private void dualhotbar$renderSecondHotbar(ScaledResolution sr, float partialTicks, CallbackInfo ci)
    {
        if (mc.getRenderViewEntity() instanceof EntityPlayer)
        {
            GlStateManager.color(1.0F, 1.0F, 1.0F, 1.0F);
            mc.getTextureManager().bindTexture(widgetsTexPath);
            EntityPlayer entityplayer = (EntityPlayer) mc.getRenderViewEntity();

            int i = (sr.getScaledWidth() / 2);

            GL11.glPushMatrix();
            float scale = SharedMixinFields.getSecondaryHotbarScale().floatValue();

            // Center the hotbar before scaling
            GL11.glTranslatef((182 - (182 * scale)) / 2F, 0, 0);

            // Scale the hotbar in place (anchored at top left corner of hotbar)
            GL11.glTranslatef(i - 91, sr.getScaledHeight() - 22, 0);
            GL11.glScalef(scale, scale, 1);
            GL11.glTranslatef(-(i - 91), -(sr.getScaledHeight() - 22), 0);

            // might be necessary when hotbar is full 1.0 size
            // (hotbar selector texture bleeds over left/right edge)
//            RenderUtils.scissorFromTopLeft(i - 91,
//                    sr.getScaledHeight() - 22 + (22 - SharedMixinFields.getHudRaiseAmount()), 182, 22);

            GlStateManager.enableBlend();
            GlStateManager.tryBlendFuncSeparate(770, 771, 1, 0);

            float f = this.zLevel;
            zLevel = -200.0F;
            drawTexturedModalRect(i - 91, sr.getScaledHeight() - 22, 0, 0, 182, 22);
            drawTexturedModalRect(i - 91, sr.getScaledHeight() - 22, 0, 0, 182, 22);
            if (SharedMixinFields.switchHotbarKeybinding.isKeyDown())
            {
                drawTexturedModalRect(i - 91 - 1 + SharedMixinFields.secondaryHotbarCurrentIndex * 20, sr.getScaledHeight() - 22 - 1, 0, 22, 24, 22);
            }
            zLevel = f;

            GlStateManager.enableRescaleNormal();
            GlStateManager.enableBlend();
            GlStateManager.tryBlendFuncSeparate(770, 771, 1, 0);
            RenderHelper.enableGUIStandardItemLighting();

            GL11.glEnable(GL11.GL_SCISSOR_TEST);
            RenderUtils.scissorFromTopLeft(i - 91,
                    sr.getScaledHeight() - 22 + (22 - SharedMixinFields.getHudRaiseAmount()) + 1, 182, 22);

            for (int j = 0; j < 9; ++j)
            {
                int k = sr.getScaledWidth() / 2 - 90 + j * 20 + 2;
                int l = sr.getScaledHeight() - 16 - 3;

                renderHotbarItem(j + (9 * SharedMixinFields.getSecondaryHotbarInventoryRow()), k, l, partialTicks, entityplayer);
            }

            RenderHelper.disableStandardItemLighting();
            GlStateManager.disableRescaleNormal();
            GlStateManager.disableBlend();

            GL11.glDisable(GL11.GL_SCISSOR_TEST);
            GL11.glPopMatrix();
        }
    }

    @Redirect(method = "renderTooltip",
            at = @At(value = "INVOKE", target = "Lnet/minecraft/client/gui/GuiIngame;drawTexturedModalRect(IIIIII)V", ordinal = 1)
    )
    private void dualhotbar$makeRenderingSelectedHotbarSlotConditional(GuiIngame instance, int x, int y, int textureX, int textureY, int width, int height)
    {
        if (!SharedMixinFields.switchHotbarKeybinding.isKeyDown())
        {
            drawTexturedModalRect(x, y - SharedMixinFields.getHudRaiseAmount(), textureX, textureY, width, height);
        }
    }

    @Redirect(method = "updateTick",
            at = @At(value = "INVOKE", target = "Lnet/minecraft/entity/player/InventoryPlayer;getCurrentItem()Lnet/minecraft/item/ItemStack;")
    )
    private ItemStack dualhotbar$overrideSetHighlightingItemStack(InventoryPlayer instance)
    {
        if (SharedMixinFields.switchHotbarKeybinding.isKeyDown())
        {
            return mc.thePlayer.inventory.mainInventory[SharedMixinFields.secondaryHotbarCurrentIndex + (9 * SharedMixinFields.getSecondaryHotbarInventoryRow())];
        }
        return mc.thePlayer.inventory.getCurrentItem();
    }

    @ModifyArg(method = "renderTooltip",
            at = @At(value = "INVOKE", target = "Lnet/minecraft/client/gui/GuiIngame;drawTexturedModalRect(IIIIII)V", ordinal = 0),
            index = 1
    )
    private int dualhotbar$raiseHotbarBackground(int y)
    {
        return y - SharedMixinFields.getHudRaiseAmount();
    }

    @ModifyArg(method = "renderTooltip",
            at = @At(value = "INVOKE", target = "Lnet/minecraft/client/gui/GuiIngame;renderHotbarItem(IIIFLnet/minecraft/entity/player/EntityPlayer;)V"),
            index = 2
    )
    private int dualhotbar$raiseHotbarItems(int y)
    {
        return y - SharedMixinFields.getHudRaiseAmount();
    }
}
