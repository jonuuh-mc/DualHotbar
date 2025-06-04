package io.jonuuh.dualhotbar.gui;

import io.jonuuh.core.lib.config.SettingsConfigurationAdapter;
import io.jonuuh.core.lib.config.setting.Settings;
import io.jonuuh.core.lib.config.setting.types.single.BoolSetting;
import io.jonuuh.dualhotbar.config.SettingKey;
import io.jonuuh.dualhotbar.config.SharedMixinFields;
import net.minecraft.client.gui.inventory.GuiContainer;
import net.minecraft.client.gui.inventory.GuiInventory;
import net.minecraft.entity.player.EntityPlayer;
import net.minecraft.entity.player.InventoryPlayer;
import net.minecraft.inventory.Container;
import net.minecraft.item.ItemStack;

public class GuiInventoryExt extends GuiInventory
{
    private final BoolSetting drawInventoryDuringSwapSetting;

    public GuiInventoryExt(EntityPlayer player)
    {
        super(player);

        Settings settings = SettingsConfigurationAdapter.INSTANCE.getDefaultCategorySettings();
        this.drawInventoryDuringSwapSetting = settings.getBoolSetting(SettingKey.DRAW_INVENTORY_DURING_SWAP);
    }

    /**
     * NOTE that th slot id uses the layout of {@link Container#inventorySlots} (e.g. mc.thePlayer.openContainer.inventorySlots)
     * rather than {@link InventoryPlayer#mainInventory} (e.g. mc.thePlayer.inventory.mainInventory)
     * <p>
     * - clickType/mode of 2 is used for the shortcut to move items between hotbar and inventory in one click: {@link GuiContainer#checkHotbarKeys(int)}
     * - clickType/mode of 1 is used for shift click combine stacks shortcut
     */
    public void swapItemStacksBetweenHotbars()
    {
        int inventoryStackSlotID = (9 * SharedMixinFields.getSecondaryHotbarInventoryRow()) + SharedMixinFields.secondaryHotbarCurrentIndex;
//        System.out.println(inventoryStack + " -> " + hotbarStack + " " + inventoryStack.isItemEqual(hotbarStack));

        if (SharedMixinFields.getShouldCombineItemStacks())
        {
            ItemStack inventoryStack = mc.thePlayer.openContainer.inventorySlots.get(inventoryStackSlotID).getStack();
            ItemStack hotbarStack = mc.thePlayer.getHeldItem();

            if (inventoryStack != null && inventoryStack.isItemEqual(hotbarStack))
            {
                super.handleMouseClick(null, inventoryStackSlotID, 0, 1);
                return;
            }
        }

        super.handleMouseClick(null, inventoryStackSlotID, SharedMixinFields.mainHotbarStartingIndex, 2);
    }

    @Override
    public void drawScreen(int mouseX, int mouseY, float partialTicks)
    {
        if (drawInventoryDuringSwapSetting.getCurrentValue())
        {
            super.drawScreen(mouseX, mouseY, partialTicks);
        }
    }
}
