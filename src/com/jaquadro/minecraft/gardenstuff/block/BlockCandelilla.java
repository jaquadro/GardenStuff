package com.jaquadro.minecraft.gardenstuff.block;

import com.jaquadro.minecraft.gardenstuff.core.ModCreativeTabs;
import com.jaquadro.minecraft.gardenstuff.core.ModItems;
import com.jaquadro.minecraft.gardenstuff.item.EnumMaterial;
import net.minecraft.block.Block;
import net.minecraft.block.BlockCrops;
import net.minecraft.block.state.IBlockState;
import net.minecraft.entity.player.EntityPlayer;
import net.minecraft.init.Blocks;
import net.minecraft.item.Item;
import net.minecraft.item.ItemStack;
import net.minecraft.util.math.AxisAlignedBB;
import net.minecraft.util.math.BlockPos;
import net.minecraft.world.IBlockAccess;
import net.minecraft.world.World;
import net.minecraftforge.common.EnumPlantType;
import net.minecraftforge.common.IShearable;

import javax.annotation.Nonnull;
import java.util.ArrayList;
import java.util.List;

public class BlockCandelilla extends BlockCrops implements IShearable
{
    private static final AxisAlignedBB[] AABB = new AxisAlignedBB[] {
        new AxisAlignedBB(0.0, 0.0, 0.0, 1.0, 0.1875, 1.0),
        new AxisAlignedBB(0.0, 0.0, 0.0, 1.0, 0.4375, 1.0),
        new AxisAlignedBB(0.0, 0.0, 0.0, 1.0, 0.8125, 1.0),
        new AxisAlignedBB(0.0, 0.0, 0.0, 1.0, 1.0, 1.0),
        new AxisAlignedBB(0.0, 0.0, 0.0, 1.0, 1.0, 1.0),
        new AxisAlignedBB(0.0, 0.0, 0.0, 1.0, 1.0, 1.0),
        new AxisAlignedBB(0.0, 0.0, 0.0, 1.0, 1.0, 1.0),
        new AxisAlignedBB(0.0, 0.0, 0.0, 1.0, 1.0, 1.0)
    };

    public BlockCandelilla (String registryName, String unlocalizedName) {
        setRegistryName(registryName);
        setUnlocalizedName(unlocalizedName);
        setCreativeTab(ModCreativeTabs.tabGardenStuff);

    }

    @Override
    protected Item getSeed () {
        return ModItems.candelilla_seeds;
    }

    @Override
    protected Item getCrop () {
        return ModItems.material;
    }

    @Override
    public int damageDropped (IBlockState state) {
        return isMaxAge(state) ? EnumMaterial.CANDELILLA.getMetadata() : 0;
    }

    @Override
    public AxisAlignedBB getBoundingBox (IBlockState state, IBlockAccess source, BlockPos pos) {
        return AABB[state.getValue(getAgeProperty())];
    }

    @Override
    public boolean isShearable (@Nonnull ItemStack item, IBlockAccess world, BlockPos pos) {
        return true;
    }

    @Override
    public List<ItemStack> onSheared (@Nonnull ItemStack item, IBlockAccess world, BlockPos pos, int fortune) {
        List<ItemStack> list = new ArrayList<>();
        list.add(new ItemStack(this, 1, getMetaFromState(world.getBlockState(pos))));
        return list;
    }

    @Override
    public boolean canHarvestBlock (IBlockAccess world, BlockPos pos, EntityPlayer player) {
        return super.canHarvestBlock(world, pos, player);
    }

    @Override
    public EnumPlantType getPlantType (IBlockAccess world, BlockPos pos) {
        return EnumPlantType.Desert;
    }

    @Override
    public boolean canBlockStay (World worldIn, BlockPos pos, IBlockState state) {
        if (super.canBlockStay(worldIn, pos, state))
            return true;

        return canSustainBush(worldIn.getBlockState(pos.down()));
    }

    @Override
    protected boolean canSustainBush (IBlockState state) {
        Block block = state.getBlock();
        return block == Blocks.FARMLAND || block == Blocks.GRASS || block == Blocks.SAND || block == Blocks.DIRT;
    }
}
