package io.jonuuh.dualhotbar.event;

import io.jonuuh.dualhotbar.config.SharedMixinFields;
import net.minecraftforge.client.GuiIngameForge;
import net.minecraftforge.client.event.RenderGameOverlayEvent;
import net.minecraftforge.fml.common.eventhandler.SubscribeEvent;

public class GameOverlayStatusBarsModifier
{
    /**
     * Raise health, armor, food, mount health, and air bars
     */
    @SubscribeEvent
    public void onRenderGameOverlay(RenderGameOverlayEvent.Pre event)
    {
        if (!SharedMixinFields.isDualHotbarEnabled())
        {
            return;
        }

        if (event.type == RenderGameOverlayEvent.ElementType.ALL)
        {
            GuiIngameForge.left_height += SharedMixinFields.getHudRaiseAmount();
            GuiIngameForge.right_height += SharedMixinFields.getHudRaiseAmount();
        }
    }
}
