package io.jonuuh.dualhotbar.event;

import io.jonuuh.dualhotbar.config.SharedMixinFields;
import io.jonuuh.dualhotbar.mixin.MixinGuiContainer;
import io.jonuuh.dualhotbar.mixin.MixinKeyBinding;
import net.minecraft.client.Minecraft;
import net.minecraft.client.gui.inventory.GuiInventory;
import net.minecraft.client.settings.KeyBinding;
import org.lwjgl.input.Keyboard;

public class SwapAction
{
    private final Minecraft mc;
    private int phase;
    private boolean hasNextPhase;

    public SwapAction()
    {
        this.mc = Minecraft.getMinecraft();
        this.hasNextPhase = true;
    }

    public boolean hasNextPhase()
    {
        return hasNextPhase;
    }

    public void performNextPhase()
    {
        // Double check inventory is still open and wasn't manually closed; Inventory walk anticheat flagging precaution
        if (phase == 0 && mc.currentScreen instanceof GuiInventory)
        {
            int inventoryStackSlotID = (9 * SharedMixinFields.getSecondaryHotbarInventoryRow()) + SharedMixinFields.secondaryHotbarCurrentIndex;
            ((MixinGuiContainer) mc.currentScreen).dualhotbar$invokeHandleMouseClick(null, inventoryStackSlotID, SharedMixinFields.mainHotbarStartingIndex, 2);
        }
        else if (phase == 1)
        {
            // Only close gui screen if inventory is still open
            // (could have been manually cancelled with esc/inv key between phase 1 and now)
            if (mc.currentScreen instanceof GuiInventory)
            {
                mc.thePlayer.closeScreen();
            }
            // TODO: maaaaybe could flag inv walk due to not waiting a tick after gui close? probably not
            repressKeys();
            hasNextPhase = false;
            return;
        }
        phase++;
    }

    // TODO: don't do this if patcher is loaded in mod list? use onPostFMLInit method maybe?
    //  probably shouldn't do any harm either way. maybe more trouble than it's worth

    /**
     * This is necessary because of the call to unPressAllKeys in {@link Minecraft#setIngameNotInFocus()}.
     * <p>
     * This is a default feature in modern mc versions, and is a module called
     * "modern keybinding handling" or similar in lunar client, patcher mod, and more
     */
    private void repressKeys()
    {
        // Loop through all registered keybindings, re-pressing them if they are currently down.
        for (KeyBinding keyBinding : MixinKeyBinding.dualhotbar$accessKeyBindingArray())
        {
            int keyCode = keyBinding.getKeyCode();

            try
            {
                if (Keyboard.isKeyDown(keyCode))
                {
                    KeyBinding.setKeyBindState(keyCode, true);
                }
            }
            // Keybinding array usually contains negative keycodes (If any keybindings use mouse buttons). The keycodes for
            // these keybindings should be negative e.g. mouse button 1 = -100, mouse button 2 = -99, mouse button 3 = -98
            catch (IndexOutOfBoundsException ignored)
            {
            }
        }
    }
}
