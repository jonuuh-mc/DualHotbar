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
        if (event.type == RenderGameOverlayEvent.ElementType.ALL)
        {
//            if (SharedMixinFields.switchHotbarKeybinding.isKeyDown())
//            {
//                System.out.println("right clicking");
//                KeyBinding.onTick(Minecraft.getMinecraft().gameSettings.keyBindUseItem.getKeyCode());
//            }

            GuiIngameForge.left_height += SharedMixinFields.getHudRaiseAmount();
            GuiIngameForge.right_height += SharedMixinFields.getHudRaiseAmount();
        }
    }

//    @SubscribeEvent
//    public void onClientTick(TickEvent.ClientTickEvent event)
//    {
//        if (event.phase == TickEvent.Phase.START)
//        {
//            return;
//        }
//
//        if (SharedMixinFields.switchHotbarKeybinding.isKeyDown())
//        {
//            System.out.println("right clicking");
//            KeyBinding.onTick(Minecraft.getMinecraft().gameSettings.keyBindUseItem.getKeyCode());
//        }
//    }
}
