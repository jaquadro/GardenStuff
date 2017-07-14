package com.jaquadro.minecraft.gardenstuff.block;

import com.jaquadro.minecraft.gardenstuff.GardenStuff;
import com.jaquadro.minecraft.gardenstuff.block.tile.TileBloomeryFurnace;
import com.jaquadro.minecraft.gardenstuff.core.ModBlocks;
import com.jaquadro.minecraft.gardenstuff.core.ModCreativeTabs;
import com.jaquadro.minecraft.gardenstuff.core.handlers.GuiHandler;
import net.minecraft.block.BlockContainer;
import net.minecraft.block.BlockHorizontal;
import net.minecraft.block.SoundType;
import net.minecraft.block.material.Material;
import net.minecraft.block.properties.PropertyBool;
import net.minecraft.block.properties.PropertyDirection;
import net.minecraft.block.state.BlockStateContainer;
import net.minecraft.block.state.IBlockState;
import net.minecraft.entity.EntityLivingBase;
import net.minecraft.entity.player.EntityPlayer;
import net.minecraft.init.SoundEvents;
import net.minecraft.inventory.Container;
import net.minecraft.inventory.InventoryHelper;
import net.minecraft.item.Item;
import net.minecraft.item.ItemStack;
import net.minecraft.tileentity.TileEntity;
import net.minecraft.util.*;
import net.minecraft.util.math.BlockPos;
import net.minecraft.world.IBlockAccess;
import net.minecraft.world.World;
import net.minecraftforge.fml.relauncher.Side;
import net.minecraftforge.fml.relauncher.SideOnly;

import javax.annotation.Nonnull;
import javax.annotation.Nullable;
import java.util.Random;

public class BlockBloomeryFurnace extends BlockContainer
{
    public static final PropertyDirection FACING = BlockHorizontal.FACING;
    public static final PropertyBool LIT = PropertyBool.create("lit");

    public BlockBloomeryFurnace (String registryName, String unlocalizedName) {
        super(Material.ROCK);

        setRegistryName(registryName);
        setUnlocalizedName(unlocalizedName);
        setHardness(3.5f);
        setSoundType(SoundType.STONE);
        setCreativeTab(ModCreativeTabs.tabGardenStuff);

        setDefaultState(blockState.getBaseState().withProperty(FACING, EnumFacing.NORTH).withProperty(LIT, false));
    }

    @Override
    public Item getItemDropped (IBlockState state, Random rand, int fortune) {
        return Item.getItemFromBlock(ModBlocks.bloomeryFurnace);
    }

    @Override
    public void onBlockAdded (World world, BlockPos pos, IBlockState state) {
        setDefaultFacing(world, pos, state);
    }

    private void setDefaultFacing (World world, BlockPos pos, IBlockState state) {
        if (!world.isRemote)
        {
            IBlockState bsNorth = world.getBlockState(pos.north());
            IBlockState bsSouth = world.getBlockState(pos.south());
            IBlockState bsWest = world.getBlockState(pos.west());
            IBlockState bsEast = world.getBlockState(pos.east());
            EnumFacing enumfacing = (EnumFacing)state.getValue(FACING);

            if (enumfacing == EnumFacing.NORTH && bsNorth.isFullBlock() && !bsSouth.isFullBlock())
            {
                enumfacing = EnumFacing.SOUTH;
            }
            else if (enumfacing == EnumFacing.SOUTH && bsSouth.isFullBlock() && !bsNorth.isFullBlock())
            {
                enumfacing = EnumFacing.NORTH;
            }
            else if (enumfacing == EnumFacing.WEST && bsWest.isFullBlock() && !bsEast.isFullBlock())
            {
                enumfacing = EnumFacing.EAST;
            }
            else if (enumfacing == EnumFacing.EAST && bsEast.isFullBlock() && !bsWest.isFullBlock())
            {
                enumfacing = EnumFacing.WEST;
            }

            world.setBlockState(pos, state.withProperty(FACING, enumfacing), 2);
        }
    }

    @Override
    public boolean onBlockActivated (World world, BlockPos pos, IBlockState state, EntityPlayer player, EnumHand hand, EnumFacing facing, float hitX, float hitY, float hitZ) {
        if (world.isRemote)
            return true;

        TileEntity tile = world.getTileEntity(pos);
        if (tile instanceof TileBloomeryFurnace)
            player.openGui(GardenStuff.instance, GuiHandler.BLOOMERY_FURNACE_ID, world, pos.getX(), pos.getY(), pos.getZ());

        return true;
    }

    public static void setState (boolean active, World world, BlockPos pos) {
        IBlockState state = world.getBlockState(pos);
        world.setBlockState(pos, state.withProperty(LIT, active), 3);
    }

    @Override
    public TileEntity createNewTileEntity (World world, int meta) {
        return new TileBloomeryFurnace();
    }

    @Override
    @SuppressWarnings("deprecation")
    public int getLightValue (IBlockState state) {
        return (state.getValue(LIT)) ? 14 : 0;
    }

    @Override
    public int getLightValue (IBlockState state, IBlockAccess world, BlockPos pos) {
        return super.getLightValue(state, world, pos);
    }

    @Override
    public void onBlockPlacedBy (World world, BlockPos pos, IBlockState state, EntityLivingBase placer, @Nonnull ItemStack stack) {
        world.setBlockState(pos, state.withProperty(FACING, placer.getHorizontalFacing().getOpposite()), 2);

        if (stack.hasDisplayName()) {
            TileEntity tile = world.getTileEntity(pos);
            if (tile instanceof TileBloomeryFurnace)
                ((TileBloomeryFurnace)tile).setInventoryName(stack.getDisplayName());
        }
    }

    @Override
    public void breakBlock (World world, BlockPos pos, IBlockState state) {
        TileEntity tile = world.getTileEntity(pos);
        if (tile instanceof TileBloomeryFurnace) {
            InventoryHelper.dropInventoryItems(world, pos, (TileBloomeryFurnace)tile);
            world.updateComparatorOutputLevel(pos, this);
        }

        super.breakBlock(world, pos, state);
    }

    @Override
    @SuppressWarnings("deprecation")
    public boolean hasComparatorInputOverride (IBlockState state) {
        return true;
    }

    @Override
    @SuppressWarnings("deprecation")
    public int getComparatorInputOverride (IBlockState blockState, World world, BlockPos pos) {
        return Container.calcRedstone(world.getTileEntity(pos));
    }

    @Nullable
    @Override
    @SuppressWarnings("deprecation")
    public @Nonnull ItemStack getItem (World worldIn, BlockPos pos, IBlockState state) {
        return new ItemStack(ModBlocks.bloomeryFurnace);
    }

    @Override
    public EnumBlockRenderType getRenderType (IBlockState state) {
        return EnumBlockRenderType.MODEL;
    }

    @Override
    @SuppressWarnings("deprecation")
    public IBlockState getStateFromMeta (int meta) {
        boolean lit = (meta & 0x8) > 0;
        EnumFacing facing = EnumFacing.getFront(meta & 0x7);
        if (facing.getAxis() == EnumFacing.Axis.Y)
            facing = EnumFacing.NORTH;

        return getDefaultState().withProperty(FACING, facing).withProperty(LIT, lit);
    }

    @Override
    public int getMetaFromState (IBlockState state) {
        int litBit = state.getValue(LIT) ? 0x8 : 0;
        return state.getValue(FACING).getIndex() | litBit;
    }

    @Override
    @SuppressWarnings("deprecation")
    public IBlockState withRotation (IBlockState state, Rotation rot) {
        return state.withProperty(FACING, rot.rotate(state.getValue(FACING)));
    }

    @Override
    @SuppressWarnings("deprecation")
    public IBlockState withMirror (IBlockState state, Mirror mirrorIn) {
        return state.withRotation(mirrorIn.toRotation(state.getValue(FACING)));
    }

    @Override
    protected BlockStateContainer createBlockState () {
        return new BlockStateContainer(this, FACING, LIT);
    }

    @Override
    @SideOnly(Side.CLIENT)
    public void randomDisplayTick (IBlockState state, World world, BlockPos pos, Random rand) {
        if (!state.getValue(LIT))
            return;

        float fx = pos.getX() + .5f;
        float fy = pos.getY() + rand.nextFloat() * 6f / 16f;
        float fz = pos.getZ() + .5f;
        float depth = .52f;
        float adjust = rand.nextFloat() * .6f - .3f;

        if (rand.nextDouble() < .1)
            world.playSound(pos.getX() + .5, pos.getY(), pos.getZ() + .5, SoundEvents.BLOCK_FURNACE_FIRE_CRACKLE, SoundCategory.BLOCKS, 1, 1, false);

        switch (state.getValue(FACING)) {
            case WEST:
                world.spawnParticle(EnumParticleTypes.SMOKE_NORMAL, fx - depth, fy, fz + adjust, 0, 0, 0);
                world.spawnParticle(EnumParticleTypes.FLAME, fx - depth, fy, fz + adjust, 0, 0, 0);
                break;
            case EAST:
                world.spawnParticle(EnumParticleTypes.SMOKE_NORMAL, fx + depth, fy, fz + adjust, 0, 0, 0);
                world.spawnParticle(EnumParticleTypes.FLAME, fx + depth, fy, fz + adjust, 0, 0, 0);
                break;
            case NORTH:
                world.spawnParticle(EnumParticleTypes.SMOKE_NORMAL, fx + adjust, fy, fz - depth, 0, 0, 0);
                world.spawnParticle(EnumParticleTypes.FLAME, fx + adjust, fy, fz - depth, 0, 0, 0);
                break;
            case SOUTH:
                world.spawnParticle(EnumParticleTypes.SMOKE_NORMAL, fx + adjust, fy, fz + depth, 0, 0, 0);
                world.spawnParticle(EnumParticleTypes.FLAME, fx + adjust, fy, fz + depth, 0, 0, 0);
                break;
        }

        if (!world.getBlockState(pos.up()).isOpaqueCube()) {
            world.spawnParticle(EnumParticleTypes.SMOKE_NORMAL, fx, fy + .5f, fz, 0, 0, 0);
            world.spawnParticle(EnumParticleTypes.SMOKE_NORMAL, fx, fy + .5f, fz, 0, 0, 0);
            world.spawnParticle(EnumParticleTypes.SMOKE_NORMAL, fx, fy + .5f, fz, 0, 0, 0);
        }
    }
}
