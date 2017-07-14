package com.jaquadro.minecraft.gardenstuff.block;

import com.jaquadro.minecraft.chameleon.resources.IItemEnum;
import com.jaquadro.minecraft.gardenstuff.core.ModCreativeTabs;
import net.minecraft.block.Block;
import net.minecraft.block.SoundType;
import net.minecraft.block.material.MapColor;
import net.minecraft.block.material.Material;
import net.minecraft.block.properties.PropertyBool;
import net.minecraft.block.properties.PropertyEnum;
import net.minecraft.block.state.BlockStateContainer;
import net.minecraft.block.state.IBlockState;
import net.minecraft.creativetab.CreativeTabs;
import net.minecraft.entity.Entity;
import net.minecraft.init.Blocks;
import net.minecraft.item.ItemStack;
import net.minecraft.util.*;
import net.minecraft.util.math.AxisAlignedBB;
import net.minecraft.util.math.BlockPos;
import net.minecraft.world.IBlockAccess;
import net.minecraft.world.World;
import net.minecraftforge.fml.relauncher.Side;
import net.minecraftforge.fml.relauncher.SideOnly;

import javax.annotation.Nullable;
import java.util.List;

public class BlockFence extends Block
{
    public static final PropertyBool NORTH = PropertyBool.create("north");
    public static final PropertyBool EAST = PropertyBool.create("east");
    public static final PropertyBool SOUTH = PropertyBool.create("south");
    public static final PropertyBool WEST = PropertyBool.create("west");
    public static final PropertyBool UP = PropertyBool.create("up");
    public static final PropertyBool DOWN = PropertyBool.create("down");
    public static final PropertyEnum<Variant> VARIANT = PropertyEnum.create("variant", Variant.class);

    protected static final AxisAlignedBB[] BOUNDING_BOXES = new AxisAlignedBB[] {
        new AxisAlignedBB(0.375D, 0.0D, 0.375D, 0.625D, 1.0D, 0.625D),
        new AxisAlignedBB(0.375D, 0.0D, 0.375D, 0.625D, 1.0D, 1.0D),
        new AxisAlignedBB(0.0D, 0.0D, 0.375D, 0.625D, 1.0D, 0.625D),
        new AxisAlignedBB(0.0D, 0.0D, 0.375D, 0.625D, 1.0D, 1.0D),
        new AxisAlignedBB(0.375D, 0.0D, 0.0D, 0.625D, 1.0D, 0.625D),
        new AxisAlignedBB(0.375D, 0.0D, 0.0D, 0.625D, 1.0D, 1.0D),
        new AxisAlignedBB(0.0D, 0.0D, 0.0D, 0.625D, 1.0D, 0.625D),
        new AxisAlignedBB(0.0D, 0.0D, 0.0D, 0.625D, 1.0D, 1.0D),
        new AxisAlignedBB(0.375D, 0.0D, 0.375D, 1.0D, 1.0D, 0.625D),
        new AxisAlignedBB(0.375D, 0.0D, 0.375D, 1.0D, 1.0D, 1.0D),
        new AxisAlignedBB(0.0D, 0.0D, 0.375D, 1.0D, 1.0D, 0.625D),
        new AxisAlignedBB(0.0D, 0.0D, 0.375D, 1.0D, 1.0D, 1.0D),
        new AxisAlignedBB(0.375D, 0.0D, 0.0D, 1.0D, 1.0D, 0.625D),
        new AxisAlignedBB(0.375D, 0.0D, 0.0D, 1.0D, 1.0D, 1.0D),
        new AxisAlignedBB(0.0D, 0.0D, 0.0D, 1.0D, 1.0D, 0.625D),
        new AxisAlignedBB(0.0D, 0.0D, 0.0D, 1.0D, 1.0D, 1.0D)
    };

    public static final AxisAlignedBB PILLAR_AABB = new AxisAlignedBB(0.375D, 0.0D, 0.375D, 0.625D, 1.5D, 0.625D);
    public static final AxisAlignedBB SOUTH_AABB = new AxisAlignedBB(0.375D, 0.0D, 0.625D, 0.625D, 1.5D, 1.0D);
    public static final AxisAlignedBB WEST_AABB = new AxisAlignedBB(0.0D, 0.0D, 0.375D, 0.375D, 1.5D, 0.625D);
    public static final AxisAlignedBB NORTH_AABB = new AxisAlignedBB(0.375D, 0.0D, 0.0D, 0.625D, 1.5D, 0.375D);
    public static final AxisAlignedBB EAST_AABB = new AxisAlignedBB(0.625D, 0.0D, 0.375D, 1.0D, 1.5D, 0.625D);

    public BlockFence (String registryName, String unlocalizedName) {
        super(Material.IRON);

        setRegistryName(registryName);
        setUnlocalizedName(unlocalizedName);
        setCreativeTab(ModCreativeTabs.tabGardenStuff);
        setHardness(5);
        setResistance(10);
        setSoundType(SoundType.METAL);

        setDefaultState(blockState.getBaseState()
            .withProperty(NORTH, false)
            .withProperty(EAST, false)
            .withProperty(SOUTH, false)
            .withProperty(WEST, false)
            .withProperty(UP, false)
            .withProperty(DOWN, false)
            .withProperty(VARIANT, Variant.TYPE0));
    }

    @Override
    public int damageDropped (IBlockState state) {
        return state.getValue(VARIANT).getMetadata();
    }

    @Override
    @SideOnly(Side.CLIENT)
    public void getSubBlocks (CreativeTabs tab, NonNullList<ItemStack> list) {
        for (Variant type : Variant.values())
            list.add(new ItemStack(this, 1, type.getMetadata()));
    }

    @Override
    public MapColor getMapColor (IBlockState state, IBlockAccess worldIn, BlockPos pos) {
        return state.getValue(VARIANT).getColor();
    }

    @Override
    public BlockRenderLayer getBlockLayer () {
        return BlockRenderLayer.CUTOUT_MIPPED;
    }

    @Override
    public void addCollisionBoxToList (IBlockState state, World worldIn, BlockPos pos, AxisAlignedBB entityBox, List<AxisAlignedBB> collidingBoxes, @Nullable Entity entityIn, boolean p_185477_7_) {
        state = state.getActualState(worldIn, pos);
        addCollisionBoxToList(pos, entityBox, collidingBoxes, PILLAR_AABB);

        if (state.getValue(NORTH))
            addCollisionBoxToList(pos, entityBox, collidingBoxes, NORTH_AABB);
        if (state.getValue(EAST))
            addCollisionBoxToList(pos, entityBox, collidingBoxes, EAST_AABB);
        if (state.getValue(SOUTH))
            addCollisionBoxToList(pos, entityBox, collidingBoxes, SOUTH_AABB);
        if (state.getValue(WEST))
            addCollisionBoxToList(pos, entityBox, collidingBoxes, WEST_AABB);
    }

    @Override
    public AxisAlignedBB getBoundingBox (IBlockState state, IBlockAccess source, BlockPos pos) {
        state = getActualState(state, source, pos);
        return BOUNDING_BOXES[getBoundBoxIndex(state)];
    }

    private static int getBoundBoxIndex (IBlockState state) {
        int index = 0;
        if (state.getValue(NORTH))
            index |= 1 << EnumFacing.NORTH.getHorizontalIndex();
        if (state.getValue(EAST))
            index |= 1 << EnumFacing.EAST.getHorizontalIndex();
        if (state.getValue(WEST))
            index |= 1 << EnumFacing.WEST.getHorizontalIndex();
        if (state.getValue(SOUTH))
            index |= 1 << EnumFacing.SOUTH.getHorizontalIndex();

        return index;
    }

    @Override
    public boolean isOpaqueCube (IBlockState state) {
        return false;
    }

    @Override
    public boolean isFullCube (IBlockState state) {
        return false;
    }

    @Override
    public boolean isPassable (IBlockAccess world, BlockPos pos) {
        return false;
    }

    public boolean canConnectTo (IBlockAccess world, BlockPos pos) {
        IBlockState state = world.getBlockState(pos);
        Block block = state.getBlock();

        if (block instanceof BlockFence)
            return true;
        if (block == Blocks.BARRIER)
            return false;

        Material material = block.getMaterial(state);
        if (!material.isOpaque())
            return false;
        if (!state.isFullCube())
            return false;

        return material != Material.GOURD;
    }

    public boolean canConnectToDown (IBlockAccess world, BlockPos pos) {
        IBlockState state = world.getBlockState(pos);
        Block block = state.getBlock();

        return block instanceof BlockFence;
    }

    @Override
    @SideOnly(Side.CLIENT)
    public boolean shouldSideBeRendered (IBlockState blockState, IBlockAccess blockAccess, BlockPos pos, EnumFacing side) {
        return true;
    }

    @Override
    public int getMetaFromState (IBlockState state) {
        return state.getValue(VARIANT).getMetadata();
    }

    @Override
    public IBlockState getStateFromMeta (int meta) {
        return getDefaultState().withProperty(VARIANT, Variant.byMetadata(meta));
    }

    @Override
    public IBlockState getActualState (IBlockState state, IBlockAccess world, BlockPos pos) {
        return state.withProperty(NORTH, canConnectTo(world, pos.north()))
            .withProperty(EAST, canConnectTo(world, pos.east()))
            .withProperty(SOUTH, canConnectTo(world, pos.south()))
            .withProperty(WEST, canConnectTo(world, pos.west()))
            .withProperty(UP, canConnectTo(world, pos.up()))
            .withProperty(DOWN, canConnectToDown(world, pos.down()));
    }

    @Override
    public IBlockState withRotation (IBlockState state, Rotation rot) {
        switch (rot) {
            case CLOCKWISE_180:
                return state.withProperty(NORTH, state.getValue(SOUTH))
                    .withProperty(EAST, state.getValue(WEST))
                    .withProperty(SOUTH, state.getValue(NORTH))
                    .withProperty(WEST, state.getValue(EAST));
            case COUNTERCLOCKWISE_90:
                return state.withProperty(NORTH, state.getValue(EAST))
                    .withProperty(EAST, state.getValue(SOUTH))
                    .withProperty(SOUTH, state.getValue(WEST))
                    .withProperty(WEST, state.getValue(NORTH));
            case CLOCKWISE_90:
                return state.withProperty(NORTH, state.getValue(WEST))
                    .withProperty(EAST, state.getValue(NORTH))
                    .withProperty(SOUTH, state.getValue(EAST))
                    .withProperty(WEST, state.getValue(SOUTH));
            default:
                return state;
        }
    }

    @Override
    public IBlockState withMirror (IBlockState state, Mirror mirror) {
        switch (mirror) {
            case LEFT_RIGHT:
                return state.withProperty(NORTH, state.getValue(SOUTH)).withProperty(SOUTH, state.getValue(NORTH));
            case FRONT_BACK:
                return state.withProperty(EAST, state.getValue(WEST)).withProperty(WEST, state.getValue(EAST));
            default:
                return super.withMirror(state, mirror);
        }
    }

    @Override
    protected BlockStateContainer createBlockState () {
        return new BlockStateContainer(this, NORTH, EAST, WEST, SOUTH, UP, DOWN, VARIANT);
    }

    public enum Variant implements IItemEnum
    {
        TYPE0(0, "0", "0", MapColor.BLACK),
        TYPE1(1, "1", "1", MapColor.BLACK),
        TYPE2(2, "2", "2", MapColor.BLACK),
        TYPE3(3, "3", "3", MapColor.BLACK)
        ;

        private static final Variant[] META_LOOKUP;

        private final int meta;
        private final String name;
        private final String unlocalizedName;
        private final MapColor color;

        Variant (int meta, String name, String unlocalizedName, MapColor color) {
            this.meta = meta;
            this.name = name;
            this.unlocalizedName = unlocalizedName;
            this.color = color;
        }

        public int getMetadata () {
            return meta;
        }

        public String getUnlocalizedName () {
            return unlocalizedName;
        }

        public MapColor getColor () {
            return color;
        }

        public static Variant byMetadata (int meta) {
            if (meta < 0 || meta >= META_LOOKUP.length)
                meta = 0;
            return META_LOOKUP[meta];
        }

        @Override
        public String toString () {
            return unlocalizedName;
        }

        @Override
        public String getName () {
            return name;
        }

        static {
            META_LOOKUP = new Variant[values().length];
            for (Variant upgrade : values()) {
                META_LOOKUP[upgrade.getMetadata()] = upgrade;
            }
        }
    }
}
