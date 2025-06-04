package io.jonuuh.dualhotbar.event;

import io.jonuuh.core.lib.config.SettingsConfigurationAdapter;
import io.jonuuh.core.lib.config.setting.Settings;
import io.jonuuh.core.lib.config.setting.types.single.IntSetting;
import io.jonuuh.dualhotbar.config.SettingKey;
import io.jonuuh.dualhotbar.config.SharedMixinFields;
import net.minecraftforge.common.MinecraftForge;
import net.minecraftforge.fml.common.eventhandler.SubscribeEvent;
import net.minecraftforge.fml.common.gameevent.TickEvent;

public class SwapActionHandler
{
    private final IntSetting actionDelayMsSetting;
    private int ticks;

    public SwapActionHandler()
    {
        Settings defaultSettings = SettingsConfigurationAdapter.INSTANCE.getDefaultCategorySettings();
        this.actionDelayMsSetting = defaultSettings.getIntSetting(SettingKey.ACTION_DELAY);
    }

    @SubscribeEvent
    public void onClientTick(TickEvent.ClientTickEvent event)
    {
        if (event.phase == TickEvent.Phase.START)
        {
            return;
        }

        ticks++;

        if (SharedMixinFields.currentSwapAction != null)
        {
            if (!SharedMixinFields.currentSwapAction.hasNextPhase())
            {
                ticks = 0;
                SharedMixinFields.currentSwapAction = null;
                MinecraftForge.EVENT_BUS.unregister(this);
                return;
            }

            int msToTicks = (int) Math.ceil((20 * (actionDelayMsSetting.getCurrentValue() / 1000F)));

            if (ticks % msToTicks == 0)
            {
//                System.out.println("performing next phase");
                SharedMixinFields.currentSwapAction.performNextPhase();
            }
        }
    }
}
