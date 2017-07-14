package com.jaquadro.minecraft.gardenstuff.item;

import com.jaquadro.minecraft.gardenstuff.core.ModBlocks;
import net.minecraft.advancements.CriteriaTriggers;
import net.minecraft.block.state.IBlockState;
import net.minecraft.entity.player.EntityPlayer;
import net.minecraft.entity.player.EntityPlayerMP;
import net.minecraft.item.Item;
import net.minecraft.item.ItemStack;
import net.minecraft.util.EnumActionResult;
import net.minecraft.util.EnumFacing;
import net.minecraft.util.EnumHand;
import net.minecraft.util.math.BlockPos;
import net.minecraft.world.IBlockAccess;
import net.minecraft.world.World;
import net.minecraftforge.common.EnumPlantType;
import net.minecraftforge.common.IPlantable;

public class ItemCandelillaSeeds extends Item implements IPlantable
{
    @Override
    public EnumActionResult onItemUse (EntityPlayer player, World worldIn, BlockPos pos, EnumHand hand, EnumFacing facing, float hitX, float hitY, float hitZ) {
        ItemStack stack = player.getHeldItem(hand);
        IBlockState state = worldIn.getBlockState(pos);

        if (facing != EnumFacing.UP)
            return EnumActionResult.FAIL;
        if (!player.canPlayerEdit(pos.offset(facing), facing, stack))
            return EnumActionResult.FAIL;
        if (!state.getBlock().canSustainPlant(state, worldIn, pos, EnumFacing.UP, this))
            return EnumActionResult.FAIL;
        if (!worldIn.isAirBlock(pos.up()))
            return EnumActionResult.FAIL;

        worldIn.setBlockState(pos.up(), ModBlocks.candelilla.getDefaultState());
        if (player instanceof EntityPlayerMP)
            CriteriaTriggers.PLACED_BLOCK.trigger((EntityPlayerMP)player, pos.up(), stack);

        stack.shrink(1);
        return EnumActionResult.SUCCESS;
    }

    @Override
    public EnumPlantType getPlantType (IBlockAccess world, BlockPos pos) {
        return EnumPlantType.Desert;
    }

    @Override
    public IBlockState getPlant (IBlockAccess world, BlockPos pos) {
        return ModBlocks.candelilla.getDefaultState();
    }
}
