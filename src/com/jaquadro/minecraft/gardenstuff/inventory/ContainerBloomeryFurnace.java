package com.jaquadro.minecraft.gardenstuff.inventory;

import com.jaquadro.minecraft.gardenstuff.block.tile.TileBloomeryFurnace;
import net.minecraft.entity.player.EntityPlayer;
import net.minecraft.entity.player.InventoryPlayer;
import net.minecraft.inventory.Container;
import net.minecraft.inventory.IContainerListener;
import net.minecraft.inventory.IInventory;
import net.minecraft.inventory.Slot;
import net.minecraft.item.ItemStack;
import net.minecraftforge.fml.relauncher.Side;
import net.minecraftforge.fml.relauncher.SideOnly;

import javax.annotation.Nonnull;
import java.util.ArrayList;
import java.util.List;

public class ContainerBloomeryFurnace extends Container
{
    private static final int InventoryX = 8;
    private static final int InventoryY = 84;
    private static final int HotbarY = 142;

    private final IInventory tile;
    private int cookTime;
    private int totalCookTime;
    private int furnaceBurnTime;
    private int currentItemBurnTime;

    private Slot primarySlot;
    private Slot secondarySlot;
    private Slot fuelSlot;
    private Slot outputSlot;
    private List<Slot> playerSlots;
    private List<Slot> hotbarSlots;

    public ContainerBloomeryFurnace (InventoryPlayer playerInventory, IInventory containerInventory) {
        this.tile = containerInventory;

        primarySlot = addSlotToContainer(new Slot(tile, 0, 56, 17));
        secondarySlot = addSlotToContainer(new Slot(tile, 1, 35, 17));
        fuelSlot = addSlotToContainer(new Slot(tile, 2, 56, 53));

        outputSlot = addSlotToContainer(new SlotBloomeryOutput(playerInventory.player, tile, 3, 116, 35));

        playerSlots = new ArrayList<>();
        for (int i = 0; i < 3; i++) {
            for (int j = 0; j < 9; j++)
                playerSlots.add(addSlotToContainer(new Slot(playerInventory, j + i * 9 + 9, InventoryX + j * 18, InventoryY + i * 18)));
        }

        hotbarSlots = new ArrayList<>();
        for (int i = 0; i < 9; i++)
            hotbarSlots.add(addSlotToContainer(new Slot(playerInventory, i, InventoryX + i * 18, HotbarY)));
    }

    public void addListener (IContainerListener listener) {
        super.addListener(listener);
        listener.sendAllWindowProperties(this, tile);
    }

    public void detectAndSendChanges () {
        super.detectAndSendChanges();

        for (IContainerListener listener : this.listeners) {
            if (cookTime != tile.getField(2))
                listener.sendWindowProperty(this, 2, tile.getField(2));
            if (furnaceBurnTime != tile.getField(0))
                listener.sendWindowProperty(this, 0, tile.getField(0));
            if (currentItemBurnTime != tile.getField(1))
                listener.sendWindowProperty(this, 1, tile.getField(1));
            if (totalCookTime != tile.getField(3))
                listener.sendWindowProperty(this, 3, tile.getField(3));
        }

        cookTime = tile.getField(2);
        furnaceBurnTime = tile.getField(0);
        currentItemBurnTime = tile.getField(1);
        totalCookTime = tile.getField(3);
    }

    @SideOnly(Side.CLIENT)
    public void updateProgressBar (int id, int data) {
        this.tile.setField(id, data);
    }

    @Override
    public boolean canInteractWith (EntityPlayer player) {
        return tile.isUsableByPlayer(player);
    }

    @Override
    @Nonnull
    public ItemStack transferStackInSlot (EntityPlayer player, int slotIndex) {
        @Nonnull ItemStack itemStack = ItemStack.EMPTY;
        Slot slot = inventorySlots.get(slotIndex);

        // Assume inventory and hotbar slot IDs are contiguous
        int inventoryStart = playerSlots.get(0).slotNumber;
        int hotbarStart = hotbarSlots.get(0).slotNumber;
        int hotbarEnd = hotbarSlots.get(hotbarSlots.size() - 1).slotNumber + 1;

        if (slot != null && slot.getHasStack()) {
            @Nonnull ItemStack slotStack = slot.getStack();
            itemStack = slotStack.copy();

            // Try merge output into inventory and signal change
            if (slotIndex == outputSlot.slotNumber) {
                if (!mergeItemStack(slotStack, inventoryStart, hotbarEnd, true))
                    return ItemStack.EMPTY;

                slot.onSlotChange(slotStack, itemStack);
            }

            // Try merge stacks within inventory and hotbar spaces
            else if (slotIndex >= inventoryStart && slotIndex < hotbarEnd) {
                boolean merged = false;
                if (TileBloomeryFurnace.isItemFuel(slotStack))
                    merged = mergeItemStack(slotStack, fuelSlot.slotNumber, fuelSlot.slotNumber + 1, false);
                else if (TileBloomeryFurnace.isItemPrimaryInput(slotStack))
                    merged = mergeItemStack(slotStack, primarySlot.slotNumber, primarySlot.slotNumber + 1, false);
                else if (TileBloomeryFurnace.isItemSecondaryInput(slotStack))
                    merged = mergeItemStack(slotStack, secondarySlot.slotNumber, secondarySlot.slotNumber + 1, false);

                if (!merged) {
                    if (slotIndex >= inventoryStart && slotIndex < hotbarStart) {
                        if (!mergeItemStack(slotStack, hotbarStart, hotbarEnd, false))
                            return ItemStack.EMPTY;
                    } else if (slotIndex >= hotbarStart && slotIndex < hotbarEnd && !this.mergeItemStack(slotStack, inventoryStart, hotbarStart, false))
                        return ItemStack.EMPTY;
                }
            }

            // Try merge stack into inventory
            else if (!mergeItemStack(slotStack, inventoryStart, hotbarEnd, false))
                return ItemStack.EMPTY;

            if (slotStack.isEmpty())
                slot.putStack(ItemStack.EMPTY);
            else
                slot.onSlotChanged();

            if (slotStack.getCount() == itemStack.getCount())
                return ItemStack.EMPTY;

            slot.onTake(player, slotStack);
        }

        return itemStack;
    }
}
