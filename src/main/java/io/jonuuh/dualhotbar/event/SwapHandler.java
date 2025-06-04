package io.jonuuh.dualhotbar.event;

import io.jonuuh.core.lib.util.ChatLogger;
import io.jonuuh.dualhotbar.config.SharedMixinFields;
import net.minecraft.client.Minecraft;
import net.minecraft.client.settings.KeyBinding;
import net.minecraftforge.common.MinecraftForge;
import net.minecraftforge.fml.common.eventhandler.SubscribeEvent;
import net.minecraftforge.fml.common.gameevent.InputEvent.KeyInputEvent;
import org.lwjgl.input.Keyboard;

/**
 * TODO: split into two classes? e.g. SwapKeyListener & SwapActionHandler
 */
public class SwapHandler
{
    private final Minecraft mc;
    private final KeyBinding switchHotbarKeybinding;
    private boolean isInitialKeyPress;

    public SwapHandler()
    {
        this.mc = Minecraft.getMinecraft();
        this.switchHotbarKeybinding = SharedMixinFields.switchHotbarKeybinding;
        this.isInitialKeyPress = true;
    }

    @SubscribeEvent
    public void onKeyInput(KeyInputEvent event)
    {
        if (mc.playerController.isSpectator() || mc.playerController.isInCreativeMode())
        {
            return;
        }

        // TODO: check if this event can fire while in a gui? should still keep check anyway just to be sure
        // currentSwapAction == null check is probably not necessary? better safe than sorry though

        if (Keyboard.getEventKey() == switchHotbarKeybinding.getKeyCode() && SharedMixinFields.currentSwapAction == null)
        {
            // KeyBinding.isPressed() continuously registers presses while the key is held down due to repeat
            // keyboard events being enabled (default while not in a GUI?). isInitialKeyPress is what solves this problem,
            // You could still just use if(isPressed()){} else{}, but I think this is more idiomatic to
            // the idea of detecting both key presses and releases
            boolean pressedDown = Keyboard.getEventKeyState();

            if (pressedDown)
            {
                if (isInitialKeyPress)
                {
//                    ChatLogger.INSTANCE.addSuccessLog("DOWN: " + Keyboard.getKeyName(switchHotbarKeybinding.getKeyCode()));
                    isInitialKeyPress = false;

                    SharedMixinFields.mainHotbarStartingIndex = mc.thePlayer.inventory.currentItem;
                    SharedMixinFields.secondaryHotbarCurrentIndex = mc.thePlayer.inventory.currentItem;
                }
            }
            else
            {
//                ChatLogger.INSTANCE.addSuccessLog("UP: " + Keyboard.getKeyName(switchHotbarKeybinding.getKeyCode()));
                isInitialKeyPress = true;
                // Necessary because of a weird workaround to using a @Redirect for hotbar hotkeying in MixinMinecraft
                mc.thePlayer.inventory.currentItem = SharedMixinFields.mainHotbarStartingIndex;

                SharedMixinFields.currentSwapAction = new SwapAction();
                MinecraftForge.EVENT_BUS.register(new SwapActionHandler());
            }
        }
    }
}
