package io.jonuuh.dualhotbar.config;

import io.jonuuh.core.lib.config.SettingsConfigurationAdapter;
import io.jonuuh.core.lib.config.setting.Settings;
import io.jonuuh.core.lib.util.ChatLogger;
import net.minecraft.client.entity.EntityPlayerSP;
import net.minecraft.command.CommandBase;
import net.minecraft.command.CommandException;
import net.minecraft.command.ICommandSender;
import net.minecraft.command.NumberInvalidException;
import net.minecraft.util.BlockPos;
import net.minecraft.util.ChatComponentText;
import net.minecraft.util.ChatStyle;
import net.minecraft.util.EnumChatFormatting;
import net.minecraft.util.IChatComponent;

import java.util.ArrayList;
import java.util.List;

public class CommandDualHotbar extends CommandBase
{
    private final ChatLogger chatLogger;
    private final Settings settings;

    public CommandDualHotbar()
    {
        this.chatLogger = ChatLogger.INSTANCE;
        this.settings = SettingsConfigurationAdapter.INSTANCE.getDefaultCategorySettings();
    }

    /**
     * Gets the name of the command
     */
    @Override
    public String getCommandName()
    {
        return "dualhotbar";
    }

    /**
     * Gets the usage string for the command.
     *
     * @param sender The command sender that executed the command
     */
    @Override
    public String getCommandUsage(ICommandSender sender)
    {
        return "";
    }

    /**
     * Gets the required permission level for this command.
     */
    @Override
    public int getRequiredPermissionLevel()
    {
        return 0;
    }

    /**
     * Callback when the command is invoked
     *
     * @param sender The command sender that executed the command
     * @param args The arguments that were passed
     */
    @Override
    public void processCommand(ICommandSender sender, String[] args) throws CommandException
    {
        if (!(sender.getCommandSenderEntity() instanceof EntityPlayerSP))
        {
            return;
        }

        if (args.length == 0)
        {
            displayCommandHelp();
            return;
        }

        SubCommand command;
        try
        {
            command = SubCommand.valueOf(args[0]);
        }
        catch (IllegalArgumentException e)
        {
            displayCommandHelp();
            return;
        }

        switch (command)
        {
            case displayConfig:
                if (isArgsLenUnexpected(args, 1))
                {
                    break;
                }
                displayConfig();
                break;

            case setEnabled:
                if (isArgsLenUnexpected(args, 2))
                {
                    break;
                }
                boolean parsedBool0 = parseBoolean(args[1]);
                settings.getBoolSetting(SettingKey.ENABLED).setCurrentValue(parsedBool0);
                chatLogger.addSuccessLog("Set DualHotbar enabled to: " + parsedBool0 + ".");
                settings.saveCurrentValues();
                break;

            case setSwapActionDelay:
                if (isArgsLenUnexpected(args, 2))
                {
                    break;
                }
                try
                {
                    int parsed = parseInt(args[1], 1, 3000);
                    settings.getIntSetting(SettingKey.ACTION_DELAY).setCurrentValue(parsed);
                    int msToTicks = (int) Math.ceil((20 * (parsed / 1000F)));

                    chatLogger.addSuccessLog("Set delay between phases of swap action to: " + parsed + "ms (" + msToTicks + " ticks).");
                    if (parsed <= 50)
                    {
                        // TODO: test different delays on diff anticheats?
                        chatLogger.addLog("USING A VERY LOW DELAY (depends on server) MAY CAUSE ANTICHEAT FLAGS AND/OR A BAN", EnumChatFormatting.DARK_RED);
                    }
                    settings.saveCurrentValues();
                }
                catch (NumberInvalidException e)
                {
                    chatLogger.addFailureLog("Invalid number. bounds = (1, 3000)");
                }
                break;

            case setHudRaiseAmount:
                if (isArgsLenUnexpected(args, 2))
                {
                    break;
                }
                try
                {
                    int parsed = parseInt(args[1], 0, 30);
                    settings.getIntSetting(SettingKey.MAIN_HUD_RAISE_AMOUNT).setCurrentValue(parsed);
                    chatLogger.addSuccessLog("Set main hud rasied amount to: " + parsed + "px.");
                    settings.saveCurrentValues();
                }
                catch (NumberInvalidException e)
                {
                    chatLogger.addFailureLog("Invalid number. bounds = (0, 30)");
                }
                break;

            case setSecondaryHotbarScale:
                if (isArgsLenUnexpected(args, 2))
                {
                    break;
                }
                try
                {
                    double parsed = parseDouble(args[1], 0, 100);
                    settings.getDoubleSetting(SettingKey.SECONDARY_HOTBAR_SCALE).setCurrentValue(parsed / 100);
                    chatLogger.addSuccessLog("Set secondary hotbar scale to: " + parsed + "%.");
                    settings.saveCurrentValues();
                }
                catch (NumberInvalidException e)
                {
                    chatLogger.addFailureLog("Invalid number. bounds = (0, 100)");
                }
                break;

            case setDrawInventoryDuringSwap:
                if (isArgsLenUnexpected(args, 2))
                {
                    break;
                }
                boolean parsedBool1 = parseBoolean(args[1]);
                settings.getBoolSetting(SettingKey.DRAW_INVENTORY_DURING_SWAP).setCurrentValue(parsedBool1);
                chatLogger.addSuccessLog("Set should draw inventory during swap to: " + parsedBool1 + ".");
                settings.saveCurrentValues();
                break;

            case setCombineSimilarItemStacks:
                if (isArgsLenUnexpected(args, 2))
                {
                    break;
                }
                boolean parsedBool2 = parseBoolean(args[1]);
                settings.getBoolSetting(SettingKey.COMBINE_SIMILAR_ITEMSTACKS).setCurrentValue(parsedBool2);
                chatLogger.addSuccessLog("Set should combine similar item stacks on swap to: " + parsedBool2 + ".");
                settings.saveCurrentValues();
                break;

            case setSecondaryHotbarInventoryRow:
                if (isArgsLenUnexpected(args, 2))
                {
                    break;
                }
                try
                {
                    int parsed = parseInt(args[1], 1, 3);
                    settings.getIntSetting(SettingKey.SECONDARY_HOTBAR_INVENTORY_ROW).setCurrentValue(parsed);
                    chatLogger.addSuccessLog("Set secondary hotbar inventory row to: " + parsed + ".");
                    settings.saveCurrentValues();
                }
                catch (NumberInvalidException e)
                {
                    chatLogger.addFailureLog("Invalid number. bounds = (1, 3)");
                }
                break;

            default:
                displayCommandHelp();
                break;
        }
    }

    @Override
    public List<String> addTabCompletionOptions(ICommandSender sender, String[] args, BlockPos pos)
    {
        if (args.length == 1)
        {
            return getListOfStringsMatchingLastWord(args, SubCommand.getNames());
        }
        return null;
    }

    private boolean isArgsLenUnexpected(String[] args, int expectedLen)
    {
        if (args.length != expectedLen)
        {
            chatLogger.addFailureLog("Command failed, wrong number of arguments.");
            return true;
        }
        return false;
    }

    private void displayCommandHelp()
    {
        chatLogger.addTitleLog(" DualHotbar Commands ", new ChatStyle().setColor(chatLogger.mainColor), chatLogger.accentColor);
        for (SubCommand command : SubCommand.values())
        {
            chatLogger.addCenteredLog(new ChatComponentText(chatLogger.accentColor + "/dualhotbar ")
                    .appendSibling(new ChatComponentText(chatLogger.mainColor + command.name() + " "))
                    .appendSibling(new ChatComponentText(EnumChatFormatting.YELLOW + command.argUsage)));
        }
        chatLogger.addBarLog(chatLogger.accentColor);
    }

    private void displayConfig()
    {
        List<IChatComponent> components = new ArrayList<>();

        components.add(new ChatComponentText(chatLogger.accentColor + "Is DualHotbar enabled:").appendSibling(
                new ChatComponentText(chatLogger.mainColor + " " + settings.getBoolSetting(SettingKey.ENABLED).getCurrentValue())));

        components.add(new ChatComponentText(chatLogger.accentColor + "Delay between phases of swap action:").appendSibling(
                new ChatComponentText(chatLogger.mainColor + " " + settings.getIntSetting(SettingKey.ACTION_DELAY).getCurrentValue() + "ms")));

        components.add(new ChatComponentText(chatLogger.accentColor + "Base HUD raised amount:").appendSibling(
                new ChatComponentText(chatLogger.mainColor + " " + settings.getIntSetting(SettingKey.MAIN_HUD_RAISE_AMOUNT).getCurrentValue() + "px")));

        components.add(new ChatComponentText(chatLogger.accentColor + "Secondary hotbar scale:").appendSibling(
                new ChatComponentText(chatLogger.mainColor + " " + settings.getDoubleSetting(SettingKey.SECONDARY_HOTBAR_SCALE).getCurrentValue() * 100 + "%")));

        components.add(new ChatComponentText(chatLogger.accentColor + "Should draw inventory during swap:").appendSibling(
                new ChatComponentText(chatLogger.mainColor + " " + settings.getBoolSetting(SettingKey.DRAW_INVENTORY_DURING_SWAP).getCurrentValue())));

        components.add(new ChatComponentText(chatLogger.accentColor + "Should combine similar item stacks on swap:").appendSibling(
                new ChatComponentText(chatLogger.mainColor + " " + settings.getBoolSetting(SettingKey.COMBINE_SIMILAR_ITEMSTACKS).getCurrentValue())));

        components.add(new ChatComponentText(chatLogger.accentColor + "Secondary hotbar inventory row:").appendSibling(
                new ChatComponentText(chatLogger.mainColor + " " + settings.getIntSetting(SettingKey.SECONDARY_HOTBAR_INVENTORY_ROW).getCurrentValue())));

        chatLogger.addFancyLogsBox(components, " DualHotbar Settings ");
    }

    private enum SubCommand
    {
        displayConfig,
        setEnabled("<boolean>"),
        setSwapActionDelay("<integer>ms"),
        setHudRaiseAmount("<integer>px"),
        setSecondaryHotbarScale("<double>%"),
        setDrawInventoryDuringSwap("<boolean>"),
        setCombineSimilarItemStacks("<boolean>"),
        setSecondaryHotbarInventoryRow("<integer>");

        private final String argUsage;

        SubCommand()
        {
            this.argUsage = "";
        }

        SubCommand(String argUsage)
        {
            this.argUsage = argUsage;
        }

        private static String[] getNames()
        {
            return new String[]{displayConfig.name(), setEnabled.name(), setSwapActionDelay.name(), setHudRaiseAmount.name(),
                    setSecondaryHotbarScale.name(), setDrawInventoryDuringSwap.name(), setCombineSimilarItemStacks.name(),
                    setSecondaryHotbarInventoryRow.name()};
        }
    }
}



