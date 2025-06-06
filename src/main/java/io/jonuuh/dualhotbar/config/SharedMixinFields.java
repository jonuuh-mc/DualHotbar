package io.jonuuh.dualhotbar.config;

import io.jonuuh.core.lib.config.SettingsConfigurationAdapter;
import io.jonuuh.core.lib.config.setting.Settings;
import io.jonuuh.dualhotbar.DualHotbar;
import io.jonuuh.dualhotbar.event.SwapAction;
import net.minecraft.client.Minecraft;
import net.minecraft.client.settings.KeyBinding;
import org.lwjgl.input.Keyboard;

/**
 * Helper class for getting Setting objects from within mixin classes, or storing shared vars between mixins.
 * <p>
 * Note that anything in a mixin constructor would (usually/always?) be run before any FML events.
 * <p>
 * The functions in here shouldn't ever be called before FMLPreInit is called for this mod or things might not work right (fail soft w/ return values)
 * (however mixin code in rendering class functions shouldn't possibly be run before FMLPreInit? at least HUD rendering surely not, right...?)
 * <p>
 * I don't know of a better way than using statics to share information between
 * mixins as you obviously can't pass anything through a mixin constructor, etc
 */
public final class SharedMixinFields
{
    // Never before created a keybinding at the class level, usually done in FMLInit but this should be fine?
    // It's how it's done with vanilla keybindings, and KeyBinding constructor only uses static fields
    public static KeyBinding switchHotbarKeybinding = new KeyBinding("Switch to secondary hotbar", Keyboard.KEY_LCONTROL, DualHotbar.modID);
    public static SwapAction currentSwapAction;
    public static int secondaryHotbarCurrentIndex;
    public static int mainHotbarStartingIndex;

    public static boolean isDualHotbarEnabled()
    {
        Minecraft mc = Minecraft.getMinecraft();

        if (mc.playerController.isSpectator() || mc.playerController.isInCreativeMode())
        {
            return false;
        }

        if (hasAdapter())
        {
            Settings defaultSettings = getAdapter().getDefaultCategorySettings();
            return defaultSettings.getBoolSetting(SettingKey.ENABLED).getCurrentValue();
        }
        return false;
    }

    public static int getHudRaiseAmount()
    {
        if (hasAdapter())
        {
            Settings defaultSettings = getAdapter().getDefaultCategorySettings();
            return defaultSettings.getIntSetting(SettingKey.MAIN_HUD_RAISE_AMOUNT).getCurrentValue();
        }
        return 0;
    }

    public static Double getSecondaryHotbarScale()
    {
        if (hasAdapter())
        {
            Settings defaultSettings = getAdapter().getDefaultCategorySettings();
            return defaultSettings.getDoubleSetting(SettingKey.SECONDARY_HOTBAR_SCALE).getCurrentValue();
        }
        return 0D;
    }

    public static int getSecondaryHotbarInventoryRow()
    {
        if (hasAdapter())
        {
            Settings defaultSettings = getAdapter().getDefaultCategorySettings();
            return defaultSettings.getIntSetting(SettingKey.SECONDARY_HOTBAR_INVENTORY_ROW).getCurrentValue();
        }
        return 1;
    }

    public static boolean getShouldCombineItemStacks()
    {
        if (hasAdapter())
        {
            Settings defaultSettings = getAdapter().getDefaultCategorySettings();
            return defaultSettings.getBoolSetting(SettingKey.COMBINE_SIMILAR_ITEMSTACKS).getCurrentValue();
        }
        return false;
    }

    private static boolean hasAdapter()
    {
        return SettingsConfigurationAdapter.hasAdapter(DualHotbar.modID);
    }

    private static SettingsConfigurationAdapter getAdapter()
    {
        return SettingsConfigurationAdapter.adapters.get(DualHotbar.modID);
    }
}
