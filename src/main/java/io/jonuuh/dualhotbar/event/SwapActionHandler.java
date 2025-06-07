package io.jonuuh.dualhotbar.event;

import io.jonuuh.core.lib.config.setting.Settings;
import io.jonuuh.core.lib.config.setting.types.single.IntSetting;
import io.jonuuh.core.lib.util.logging.ChatLogger;
import io.jonuuh.dualhotbar.config.SettingKey;
import io.jonuuh.dualhotbar.config.SharedMixinFields;
import net.minecraft.client.Minecraft;
import net.minecraft.client.settings.KeyBinding;
import net.minecraftforge.common.MinecraftForge;
import net.minecraftforge.fml.common.eventhandler.SubscribeEvent;
import net.minecraftforge.fml.common.gameevent.TickEvent;

class SwapActionHandler
{
    private final ChatLogger chatLogger;
    private final IntSetting actionDelaySetting;
    private final IntSetting randomActionDelaySetting;
    private int ticks;

    public SwapActionHandler(ChatLogger chatLogger)
    {
        this.chatLogger = chatLogger;
        Settings defaultSettings = SharedMixinFields.getAdapter().getDefaultCategorySettings();
        this.actionDelaySetting = defaultSettings.getIntSetting(SettingKey.ACTION_DELAY);
        this.randomActionDelaySetting = defaultSettings.getIntSetting(SettingKey.MAX_RANDOM_ACTION_DELAY);
    }

    @SubscribeEvent
    public void onClientTick(TickEvent.ClientTickEvent event)
    {
        if (event.phase == TickEvent.Phase.END)
        {
            return;
        }

        if (ticks == 0)
        {
            chatLogger.addLog("Inventory keybinding ticked");
            KeyBinding.onTick(Minecraft.getMinecraft().gameSettings.keyBindInventory.getKeyCode());
        }

        ticks++;

        if (SharedMixinFields.currentSwapAction.hasNextPhase())
        {
            int tickDelay = actionDelaySetting.getCurrentValue();
            int additionalRandomTickDelay = (int) (Math.random() * (randomActionDelaySetting.getCurrentValue() + 1));

            if (ticks % (tickDelay + additionalRandomTickDelay) == 0)
            {
                SharedMixinFields.currentSwapAction.performNextPhase();
                chatLogger.addLog("Added random +" + additionalRandomTickDelay + " ticks");
            }
        }
        else
        {
            SharedMixinFields.currentSwapAction = null;
            MinecraftForge.EVENT_BUS.unregister(this);
        }
    }
}
