package com.jaquadro.minecraft.gardenstuff.inventory;

import net.minecraft.entity.player.EntityPlayer;
import net.minecraft.inventory.IInventory;
import net.minecraft.inventory.Slot;
import net.minecraft.item.ItemStack;

import javax.annotation.Nonnull;

public class SlotBloomeryOutput extends Slot
{
    private EntityPlayer player;
    private int amountCrafted;

    public SlotBloomeryOutput (EntityPlayer player, IInventory inputInventory, int slotIndex, int xPos, int yPos) {
        super(inputInventory, slotIndex, xPos, yPos);
        this.player = player;
    }

    @Override
    public boolean isItemValid (@Nonnull ItemStack itemStack) {
        return false;
    }

    @Override
    @Nonnull
    public ItemStack decrStackSize (int count) {
        if (getHasStack())
            amountCrafted += Math.min(count, getStack().getCount());

        return super.decrStackSize(count);
    }

    @Override
    @Nonnull
    public ItemStack onTake (EntityPlayer thePlayer, @Nonnull ItemStack itemStack) {
        onCrafting(itemStack);
        return super.onTake(player, itemStack);
    }

    @Override
    protected void onCrafting (@Nonnull ItemStack itemStack, int count) {
        amountCrafted += count;
        super.onCrafting(itemStack, count);
    }

    @Override
    protected void onCrafting (@Nonnull ItemStack itemStack) {
        itemStack.onCrafting(player.world, player, amountCrafted);
        amountCrafted = 0;
    }
}
