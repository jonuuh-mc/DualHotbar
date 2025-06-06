//package io.jonuuh.dualhotbar.gui;
//
//import io.jonuuh.core.lib.config.SettingsConfigurationAdapter;
//import io.jonuuh.core.lib.config.setting.Settings;
//import io.jonuuh.core.lib.config.setting.types.single.BoolSetting;
//import io.jonuuh.core.lib.util.ChatLogger;
//import io.jonuuh.dualhotbar.config.SettingKey;
//import io.jonuuh.dualhotbar.config.SharedMixinFields;
//import net.minecraft.client.gui.ScaledResolution;
//import net.minecraft.client.gui.inventory.GuiContainer;
//import net.minecraft.client.gui.inventory.GuiInventory;
//import net.minecraft.entity.player.EntityPlayer;
//import net.minecraft.entity.player.InventoryPlayer;
//import net.minecraft.inventory.Container;
//import net.minecraft.inventory.Slot;
//import net.minecraft.item.ItemStack;
//import org.lwjgl.input.Keyboard;
//import org.lwjgl.input.Mouse;
//
//import java.io.IOException;
//
//public class GuiInventoryExt extends GuiInventory
//{
//    private final BoolSetting drawInventoryDuringSwapSetting;
//
//    public GuiInventoryExt(EntityPlayer player)
//    {
//        super(player);
//
//        Settings settings = SettingsConfigurationAdapter.INSTANCE.getDefaultCategorySettings();
//        this.drawInventoryDuringSwapSetting = settings.getBoolSetting(SettingKey.DRAW_INVENTORY_DURING_SWAP);
//    }
//
//    public void moveMouseToSlot()
//    {
//        int inventoryStackSlotID = (9 * SharedMixinFields.getSecondaryHotbarInventoryRow()) + SharedMixinFields.secondaryHotbarCurrentIndex;
//        Slot inventorySlot = mc.thePlayer.openContainer.inventorySlots.get(inventoryStackSlotID);
//
//        ScaledResolution sr = new ScaledResolution(mc);
//
//        System.out.println(this.getSlotUnderMouse());
//        Mouse.setCursorPosition(
//                (guiLeft + inventorySlot.xDisplayPosition + 8) * sr.getScaleFactor(),
//                (guiTop + ySize - inventorySlot.yDisplayPosition - 8) * sr.getScaleFactor());
//        System.out.println(this.getSlotUnderMouse());
//    }
//
//    /**
//     * NOTE that th slot id uses the layout of {@link Container#inventorySlots} (e.g. mc.thePlayer.openContainer.inventorySlots)
//     * rather than {@link InventoryPlayer#mainInventory} (e.g. mc.thePlayer.inventory.mainInventory)
//     * <p>
//     * - clickType/mode of 2 is used for the shortcut to move items between hotbar and inventory in one click: {@link GuiContainer#checkHotbarKeys(int)}
//     * - clickType/mode of 1 is used for shift click combine stacks shortcut
//     */
//    public void swapItemStacksBetweenHotbars()
//    {
//        int inventoryStackSlotID = (9 * SharedMixinFields.getSecondaryHotbarInventoryRow()) + SharedMixinFields.secondaryHotbarCurrentIndex;
////        System.out.println(inventoryStack + " -> " + hotbarStack + " " + inventoryStack.isItemEqual(hotbarStack));
//
////        if (SharedMixinFields.getShouldCombineItemStacks())
////        {
//        Slot inventorySlot = mc.thePlayer.openContainer.inventorySlots.get(inventoryStackSlotID);
//
////        ItemStack inventoryStack = mc.thePlayer.inventory.mainInventory[inventoryStackSlotID];
////        ItemStack hotbarStack = mc.thePlayer.getHeldItem();
//
////        ItemStack inventoryStack = mc.thePlayer.openContainer.inventorySlots.get(inventoryStackSlotID).getStack();
////            ItemStack hotbarStack = mc.thePlayer.getHeldItem();
////
////            if (inventoryStack != null && inventoryStack.isItemEqual(hotbarStack))
////            {
////                super.handleMouseClick(null, inventoryStackSlotID, 0, 1);
////                return;
////            }
////        }
//
//        ChatLogger.INSTANCE.addLog("Slot under mouse: " + getSlotUnderMouse()
//                + " invSlot == slotUnder: " + (inventorySlot == getSlotUnderMouse()));
//
//
//        if (inventorySlot != getSlotUnderMouse())
//        {
//            ChatLogger.INSTANCE.addLog("slots not equal, cancelling swap");
//            return;
//        }
//
////        KeyBinding.setKeyBindState(mc.gameSettings.keyBindsHotbar[SharedMixinFields.mainHotbarStartingIndex].getKeyCode(), true);
//
////        KeyBinding.onTick(mc.gameSettings.keyBindsHotbar[SharedMixinFields.mainHotbarStartingIndex].getKeyCode());
////        mc.gameSettings.keyBindsHotbar[SharedMixinFields.mainHotbarStartingIndex]
//
////        Keyboard.isKeyDown()
//
////        System.out.println(Keyboard.getEventCharacter());
//
////        try
////        {
////            this.handleInput();
////        }
////        catch (IOException e)
////        {
////            System.out.println("exception");
////            e.printStackTrace();
////        }
//
////        System.out.println(mc.gameSettings.keyBindInventory.getKeyCode() + " " + Keyboard.getKeyName(mc.gameSettings.keyBindInventory.getKeyCode()));
//
////        System.out.println((char) ('0' + SharedMixinFields.mainHotbarStartingIndex));
//
////        Keyboard.getEventKey()
//
////        try
////        {
////            keyTyped((char) ('0' + SharedMixinFields.mainHotbarStartingIndex), mc.gameSettings.keyBindsHotbar[SharedMixinFields.mainHotbarStartingIndex].getKeyCode());
////        }
////        catch (IOException e)
////        {
////            System.out.println("fucked");
////        }
//
//        this.checkHotbarKeys(mc.gameSettings.keyBindsHotbar[SharedMixinFields.mainHotbarStartingIndex].getKeyCode());
////        System.out.println(isMouseOnSlot(inventorySlot, Mouse.getEventX(), Mouse.getEventY()));
//
////        super.handleMouseClick(null, inventoryStackSlotID, SharedMixinFields.mainHotbarStartingIndex, 2);
//    }
//
////    private boolean isMouseOnSlot(Slot slotIn, int mouseX, int mouseY)
////    {
////        return this.isPointInRegion(slotIn.xDisplayPosition, slotIn.yDisplayPosition, 16, 16, mouseX, mouseY);
////    }
//
//    @Override
//    public void drawScreen(int mouseX, int mouseY, float partialTicks)
//    {
//        if (drawInventoryDuringSwapSetting.getCurrentValue())
//        {
//            super.drawScreen(mouseX, mouseY, partialTicks);
//        }
//    }
//}
