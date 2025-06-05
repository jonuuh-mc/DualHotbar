package io.jonuuh.dualhotbar.event;

import io.jonuuh.core.lib.util.ChatLogger;
import io.jonuuh.dualhotbar.gui.GuiInventoryExt;
import io.jonuuh.dualhotbar.mixin.MixinKeyBinding;
import net.minecraft.client.Minecraft;
import net.minecraft.client.settings.KeyBinding;
import org.lwjgl.input.Keyboard;

public class SwapAction
{
    private final Minecraft mc;
    private final GuiInventoryExt inventory;
    private int phase;
    private boolean hasNextPhase;

    public SwapAction()
    {
        this.mc = Minecraft.getMinecraft();
        this.inventory = new GuiInventoryExt(mc.thePlayer);
        this.hasNextPhase = true;
    }

    public boolean hasNextPhase()
    {
        return hasNextPhase;
    }

    public void performNextPhase()
    {
        if (!hasNextPhase())
        {
            ChatLogger.INSTANCE.addFailureLog("Tried to perform next phase on finished SwapAction; This should never happen - please message @jonnuh on discord");
            return;
        }

        if (phase == 0)
        {
            mc.displayGuiScreen(inventory);
        }
        // Double check inventory is still open and wasn't manually closed; Inventory walk anticheat flagging precaution
        else if (phase == 1 && mc.currentScreen == inventory)
        {
            inventory.swapItemStacksBetweenHotbars();
        }
        else if (phase == 2)
        {
            // Only close gui screen if inventory is still open
            // (could have been manually cancelled with esc/inv key between phase 1 and now)
            if (mc.currentScreen == inventory)
            {
//                System.out.println("inventory");
                mc.thePlayer.closeScreen();
            }
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
