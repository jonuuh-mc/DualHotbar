package io.jonuuh.dualhotbar;

import io.jonuuh.core.lib.config.SettingsConfigurationAdapter;
import io.jonuuh.core.lib.config.setting.Settings;
import io.jonuuh.core.lib.config.setting.types.single.BoolSetting;
import io.jonuuh.core.lib.config.setting.types.single.DoubleSetting;
import io.jonuuh.core.lib.config.setting.types.single.IntSetting;
import io.jonuuh.core.lib.util.logging.ChatLogger;
import io.jonuuh.dualhotbar.config.CommandDualHotbar;
import io.jonuuh.dualhotbar.config.SettingKey;
import io.jonuuh.dualhotbar.config.SharedMixinFields;
import io.jonuuh.dualhotbar.event.GameOverlayStatusBarsModifier;
import io.jonuuh.dualhotbar.event.SwapKeyListener;
import net.minecraft.util.EnumChatFormatting;
import net.minecraftforge.client.ClientCommandHandler;
import net.minecraftforge.common.MinecraftForge;
import net.minecraftforge.fml.client.registry.ClientRegistry;
import net.minecraftforge.fml.common.Mod;
import net.minecraftforge.fml.common.event.FMLInitializationEvent;
import net.minecraftforge.fml.common.event.FMLPreInitializationEvent;

@Mod(modid = DualHotbar.modID, version = DualHotbar.version, acceptedMinecraftVersions = "[1.8.9]")
public class DualHotbar
{
    public static final String modID = "dualhotbar";
    public static final String modName = "DualHotbar";
    public static final String version = "1.0.0";
    private final ChatLogger chatLogger;

    public DualHotbar()
    {
        this.chatLogger = new ChatLogger(modName, EnumChatFormatting.GOLD, EnumChatFormatting.GRAY);
    }

    @Mod.EventHandler
    public void FMLPreInit(FMLPreInitializationEvent event)
    {
        SettingsConfigurationAdapter.addAdapter(modID, event.getSuggestedConfigurationFile(), initMasterSettings());
    }

    @Mod.EventHandler
    public void FMLInit(FMLInitializationEvent event)
    {
        ClientRegistry.registerKeyBinding(SharedMixinFields.switchHotbarKeybinding);
        ClientCommandHandler.instance.registerCommand(new CommandDualHotbar(chatLogger));

        MinecraftForge.EVENT_BUS.register(new GameOverlayStatusBarsModifier());
        MinecraftForge.EVENT_BUS.register(new SwapKeyListener(chatLogger));
    }

    private Settings initMasterSettings()
    {
        Settings settings = new Settings(modID);
        settings.put(SettingKey.ENABLED, new BoolSetting(true));
        settings.put(SettingKey.ACTION_DELAY, new IntSetting(2));
        settings.put(SettingKey.MAX_RANDOM_ACTION_DELAY, new IntSetting(2));
        settings.put(SettingKey.MAIN_HUD_RAISE_AMOUNT, new IntSetting(11));
        settings.put(SettingKey.SECONDARY_HOTBAR_SCALE, new DoubleSetting(0.98));
        settings.put(SettingKey.DRAW_INVENTORY_DURING_SWAP, new BoolSetting(true));
        settings.put(SettingKey.COMBINE_SIMILAR_ITEMSTACKS, new BoolSetting(true));
        settings.put(SettingKey.SECONDARY_HOTBAR_INVENTORY_ROW, new IntSetting(3));
        return settings;
    }
}
