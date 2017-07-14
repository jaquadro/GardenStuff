package com.jaquadro.minecraft.gardenstuff.block.tile;

import com.jaquadro.minecraft.chameleon.block.ChamLockableTileEntity;
import com.jaquadro.minecraft.chameleon.block.tiledata.CustomNameData;
import com.jaquadro.minecraft.chameleon.block.tiledata.LockableData;
import com.jaquadro.minecraft.gardenstuff.GardenStuff;
import com.jaquadro.minecraft.gardenstuff.block.BlockBloomeryFurnace;
import com.jaquadro.minecraft.gardenstuff.block.BlockStoneMaterial;
import com.jaquadro.minecraft.gardenstuff.core.ModBlocks;
import com.jaquadro.minecraft.gardenstuff.core.ModItems;
import com.jaquadro.minecraft.gardenstuff.inventory.ContainerBloomeryFurnace;
import com.jaquadro.minecraft.gardenstuff.item.EnumMaterial;
import net.minecraft.block.Block;
import net.minecraft.block.state.IBlockState;
import net.minecraft.entity.player.EntityPlayer;
import net.minecraft.entity.player.InventoryPlayer;
import net.minecraft.init.Blocks;
import net.minecraft.init.Items;
import net.minecraft.inventory.Container;
import net.minecraft.inventory.IInventory;
import net.minecraft.inventory.ISidedInventory;
import net.minecraft.inventory.ItemStackHelper;
import net.minecraft.item.Item;
import net.minecraft.item.ItemBlock;
import net.minecraft.item.ItemStack;
import net.minecraft.nbt.NBTTagCompound;
import net.minecraft.util.EnumFacing;
import net.minecraft.util.ITickable;
import net.minecraft.util.NonNullList;
import net.minecraft.util.math.BlockPos;
import net.minecraft.util.math.MathHelper;
import net.minecraft.world.World;
import net.minecraftforge.common.capabilities.Capability;
import net.minecraftforge.common.capabilities.CapabilityInject;
import net.minecraftforge.fml.relauncher.Side;
import net.minecraftforge.fml.relauncher.SideOnly;
import net.minecraftforge.items.IItemHandler;
import net.minecraftforge.items.wrapper.SidedInvWrapper;

import javax.annotation.Nonnull;
import javax.annotation.Nullable;

public class TileBloomeryFurnace extends ChamLockableTileEntity implements ITickable, ISidedInventory
{
    public static final int FIELD_FURNACE_BURN_TIME = 0;
    public static final int FIELD_CURRENT_ITEM_BURN_TIME = 1;
    public static final int FIELD_COOK_TIME = 2;
    public static final int FIELD_TOTAL_COOK_TIME = 3;

    private static final int SLOT_PRIMARY = 0;
    private static final int SLOT_SECONDARY = 1;
    private static final int SLOT_FUEL = 2;
    private static final int SLOT_OUTPUT = 3;

    private static final int[] slots = new int[] { 0, 1, 2, 3 };

    private NonNullList<ItemStack> furnaceItemStacks = NonNullList.withSize(4, ItemStack.EMPTY);

    private int furnaceBurnTime;
    private int currentItemBurnTime;
    private int cookTime;
    private int totalCookTime;

    public TileBloomeryFurnace () {
        injectData(new LockableData());
        injectPortableData(new CustomNameData("container.gardenstuff.bloomery_furnace"));
    }

    public static boolean isItemFuel (@Nonnull ItemStack stack) {
        return getItemBurnTime(stack) > 0;
    }

    public static int getItemBurnTime (@Nonnull ItemStack stack) {
        if (stack.isEmpty())
            return 0;

        Item item = stack.getItem();

        if (item instanceof ItemBlock) {
            Block block = Block.getBlockFromItem(item);
            if (block == ModBlocks.stoneMaterial && BlockStoneMaterial.StoneType.byMetadata(stack.getItemDamage()) == BlockStoneMaterial.StoneType.CHARCOAL_BLOCK)
                return ModBlocks.stoneMaterial.getBurnTime(stack);
        }

        if (item == Items.COAL && stack.getItemDamage() == 1)
            return 1600;

        return 0;
    }

    public static boolean isItemPrimaryInput (@Nonnull ItemStack stack) {
        if (stack.isEmpty())
            return false;

        if (stack.getItem() == Items.IRON_INGOT)
            return true;
        if (stack.getItem() == Item.getItemFromBlock(Blocks.IRON_ORE))
            return true;

        return false;
    }

    public static boolean isItemSecondaryInput (@Nonnull ItemStack stack) {
        if (stack.isEmpty())
            return false;

        if (stack.getItem() == Item.getItemFromBlock(Blocks.SAND))
            return true;

        return false;
    }

    @SideOnly(Side.CLIENT)
    public static boolean isBurning (IInventory inventory)
    {
        return inventory.getField(0) > 0;
    }

    public int getCookTime (@Nonnull ItemStack stack)
    {
        return 200;
    }

    public boolean isBurning()
    {
        return furnaceBurnTime > 0;
    }

    public void smeltItem () {
        if (!canSmelt())
            return;

        @Nonnull ItemStack itemOutput = new ItemStack(ModItems.material, 1, EnumMaterial.WROUGHT_IRON_INGOT.getMetadata());

        if (furnaceItemStacks.get(SLOT_OUTPUT).isEmpty())
            furnaceItemStacks.set(SLOT_OUTPUT, itemOutput.copy());
        else if (furnaceItemStacks.get(SLOT_OUTPUT).getItem() == itemOutput.getItem())
            furnaceItemStacks.get(SLOT_OUTPUT).shrink(itemOutput.getCount());

        furnaceItemStacks.get(SLOT_PRIMARY).shrink(1);
        furnaceItemStacks.get(SLOT_SECONDARY).shrink(1);

        if (furnaceItemStacks.get(SLOT_PRIMARY).getCount() <= 0)
            furnaceItemStacks.set(SLOT_PRIMARY, ItemStack.EMPTY);
        if (furnaceItemStacks.get(SLOT_SECONDARY).getCount() <= 0)
            furnaceItemStacks.set(SLOT_SECONDARY, ItemStack.EMPTY);
    }

    @Override
    public boolean shouldRefresh (World world, BlockPos pos, IBlockState oldState, IBlockState newSate) {
        return oldState.getBlock() != newSate.getBlock();
    }

    @Override
    public Container createContainer (InventoryPlayer playerInventory, EntityPlayer playerIn) {
        return new ContainerBloomeryFurnace(playerInventory, this);
    }

    @Override
    public String getGuiID () {
        return GardenStuff.MOD_ID + ":bloomery_furnace";
    }

    @Override
    public int[] getSlotsForFace (EnumFacing side) {
        return slots;
    }

    @Override
    public boolean canInsertItem (int index, @Nonnull ItemStack itemStackIn, EnumFacing direction) {
        return isItemValidForSlot(index, itemStackIn);
    }

    @Override
    public boolean canExtractItem (int index, @Nonnull ItemStack stack, EnumFacing direction) {
        return index == SLOT_OUTPUT;
    }

    @Override
    public int getSizeInventory () {
        return furnaceItemStacks.size();
    }

    @Override
    public boolean isEmpty () {
        for (ItemStack stack : furnaceItemStacks) {
            if (!stack.isEmpty())
                return false;
        }

        return true;
    }

    @Nonnull
    @Override
    public ItemStack getStackInSlot (int index) {
        return furnaceItemStacks.get(index);
    }

    @Nonnull
    @Override
    public ItemStack decrStackSize (int index, int count) {
        return ItemStackHelper.getAndSplit(furnaceItemStacks, index, count);
    }

    @Nonnull
    @Override
    public ItemStack removeStackFromSlot (int index) {
        return ItemStackHelper.getAndRemove(furnaceItemStacks, index);
    }

    @Override
    public void setInventorySlotContents (int index, @Nonnull ItemStack stack) {
        boolean stackEqual = !stack.isEmpty() && stack.isItemEqual(this.furnaceItemStacks.get(index)) && ItemStack.areItemStackTagsEqual(stack, this.furnaceItemStacks.get(index));
        furnaceItemStacks.set(index, stack);

        if (!stack.isEmpty() && stack.getCount() > getInventoryStackLimit())
            stack.setCount(getInventoryStackLimit());

        if (index == SLOT_PRIMARY && !stackEqual) {
            totalCookTime = getCookTime(stack);
            cookTime = 0;
            markDirty();
        }
    }

    @Override
    public int getInventoryStackLimit () {
        return 64;
    }

    @Override
    public boolean isUsableByPlayer (EntityPlayer player) {
        return getWorld().getTileEntity(pos) == this && player.getDistanceSq(pos.getX() + 0.5D, pos.getY() + 0.5D, pos.getZ() + 0.5D) <= 64.0D;
    }

    @Override
    public void openInventory (EntityPlayer player) {
    }

    @Override
    public void closeInventory (EntityPlayer player) {
    }

    @Override
    public boolean isItemValidForSlot (int index, @Nonnull ItemStack stack) {
        if (index == SLOT_OUTPUT)
            return false;
        if (index == SLOT_FUEL)
            return isItemFuel(stack);
        if (index == SLOT_PRIMARY)
            return isItemPrimaryInput(stack);
        if (index == SLOT_SECONDARY)
            return isItemSecondaryInput(stack);

        return false;
    }

    @Override
    public int getField (int id) {
        switch (id) {
            case FIELD_FURNACE_BURN_TIME:
                return furnaceBurnTime;
            case FIELD_CURRENT_ITEM_BURN_TIME:
                return currentItemBurnTime;
            case FIELD_COOK_TIME:
                return cookTime;
            case FIELD_TOTAL_COOK_TIME:
                return totalCookTime;
            default:
                return 0;
        }
    }

    @Override
    public void setField (int id, int value) {
        switch (id) {
            case FIELD_FURNACE_BURN_TIME:
                furnaceBurnTime = value;
                break;
            case FIELD_CURRENT_ITEM_BURN_TIME:
                currentItemBurnTime = value;
                break;
            case FIELD_COOK_TIME:
                cookTime = value;
                break;
            case FIELD_TOTAL_COOK_TIME:
                totalCookTime = value;
                break;
        }
    }

    @Override
    public int getFieldCount () {
        return 4;
    }

    @Override
    public void clear () {
        for (int i = 0; i < this.furnaceItemStacks.size(); ++i)
            furnaceItemStacks.set(i, ItemStack.EMPTY);
    }

    @Override
    public void update () {
        boolean prevBurning = isBurning();
        boolean isDirty = false;

        if (isBurning())
            furnaceBurnTime--;

        if (!getWorld().isRemote) {
            @Nonnull ItemStack fuelItem = furnaceItemStacks.get(SLOT_FUEL);

            if (isBurning() || (!fuelItem.isEmpty() && !furnaceItemStacks.get(SLOT_PRIMARY).isEmpty() && !furnaceItemStacks.get(SLOT_SECONDARY).isEmpty())) {
                if (!isBurning() && canSmelt()) {
                    currentItemBurnTime = furnaceBurnTime = getItemBurnTime(fuelItem);

                    if (isBurning()) {
                        isDirty = true;

                        if (!fuelItem.isEmpty()) {
                            fuelItem.shrink(1);
                            if (fuelItem.isEmpty())
                                furnaceItemStacks.set(SLOT_FUEL, fuelItem.getItem().getContainerItem(furnaceItemStacks.get(SLOT_FUEL)));
                        }
                    }
                }

                if (isBurning() && canSmelt()) {
                    cookTime++;

                    if (cookTime == totalCookTime) {
                        cookTime = 0;
                        totalCookTime = getCookTime(furnaceItemStacks.get(SLOT_PRIMARY));
                        smeltItem();
                        isDirty = true;
                    }
                }
                else
                    cookTime = 0;
            }
            else if (!isBurning() && cookTime > 0)
                cookTime = MathHelper.clamp(cookTime - 2, 0, totalCookTime);

            if (prevBurning != isBurning()) {
                isDirty = true;
                BlockBloomeryFurnace.setState(isBurning(), getWorld(), pos);
            }
        }

        if (isDirty)
            markDirty();
    }

    @Override
    protected void readFromFixedNBT (NBTTagCompound tag) {
        super.readFromFixedNBT(tag);

        furnaceItemStacks = NonNullList.withSize(getSizeInventory(), ItemStack.EMPTY);
        ItemStackHelper.loadAllItems(tag, furnaceItemStacks);

        furnaceBurnTime = tag.getShort("BurnTime");
        cookTime = tag.getShort("CookTime");
        totalCookTime = tag.getShort("CookTimeTotal");
        currentItemBurnTime = getItemBurnTime(furnaceItemStacks.get(SLOT_FUEL));
    }

    @Override
    protected NBTTagCompound writeToFixedNBT (NBTTagCompound tag) {
        tag = super.writeToFixedNBT(tag);

        tag.setShort("BurnTime", (short)furnaceBurnTime);
        tag.setShort("CookTime", (short)cookTime);
        tag.setShort("CookTimeTotal", (short)totalCookTime);

        ItemStackHelper.saveAllItems(tag, furnaceItemStacks);

        return tag;
    }

    private boolean canSmelt () {
        if (furnaceItemStacks.get(SLOT_PRIMARY).isEmpty() || furnaceItemStacks.get(SLOT_SECONDARY).isEmpty())
            return false;

        if (!isItemPrimaryInput(furnaceItemStacks.get(SLOT_PRIMARY)))
            return false;
        if (!isItemSecondaryInput(furnaceItemStacks.get(SLOT_SECONDARY)))
            return false;

        @Nonnull ItemStack itemOutput = new ItemStack(ModItems.material, 1, EnumMaterial.WROUGHT_IRON_INGOT.getMetadata());
        if (furnaceItemStacks.get(SLOT_OUTPUT).isEmpty())
            return true;
        if (!furnaceItemStacks.get(SLOT_OUTPUT).isItemEqual(itemOutput))
            return false;

        int result = furnaceItemStacks.get(SLOT_OUTPUT).getCount() + itemOutput.getCount();
        return result <= getInventoryStackLimit() && result <= furnaceItemStacks.get(SLOT_OUTPUT).getMaxStackSize();
    }

    @CapabilityInject(IItemHandler.class)
    public static Capability<IItemHandler> ITEM_HANDLER_CAPABILITY = null;

    IItemHandler itemHandler = new SidedInvWrapper(this, EnumFacing.UP);

    @Override
    public boolean hasCapability (Capability<?> capability, @Nullable EnumFacing facing) {
        return capability == ITEM_HANDLER_CAPABILITY
            || super.hasCapability(capability, facing);
    }

    @SuppressWarnings("unchecked")
    @Override
    public <T> T getCapability (Capability<T> capability, @Nullable  EnumFacing facing) {
        if (facing != null && capability == ITEM_HANDLER_CAPABILITY)
            return (T)itemHandler;

        return super.getCapability(capability, facing);
    }
}
