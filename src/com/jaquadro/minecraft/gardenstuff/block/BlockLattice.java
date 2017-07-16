package com.jaquadro.minecraft.gardenstuff.block;

import com.jaquadro.minecraft.chameleon.resources.IItemEnum;
import com.jaquadro.minecraft.gardenstuff.block.tile.TileLattice;
import com.jaquadro.minecraft.gardenstuff.core.ModCreativeTabs;
import net.minecraft.block.Block;
import net.minecraft.block.SoundType;
import net.minecraft.block.material.MapColor;
import net.minecraft.block.material.Material;
import net.minecraft.block.properties.PropertyEnum;
import net.minecraft.block.state.BlockStateContainer;
import net.minecraft.block.state.IBlockState;
import net.minecraft.creativetab.CreativeTabs;
import net.minecraft.entity.Entity;
import net.minecraft.item.ItemStack;
import net.minecraft.tileentity.TileEntity;
import net.minecraft.util.EnumFacing;
import net.minecraft.util.IStringSerializable;
import net.minecraft.util.NonNullList;
import net.minecraft.util.math.AxisAlignedBB;
import net.minecraft.util.math.BlockPos;
import net.minecraft.world.IBlockAccess;
import net.minecraft.world.World;
import net.minecraftforge.fml.relauncher.Side;
import net.minecraftforge.fml.relauncher.SideOnly;

import javax.annotation.Nonnull;
import javax.annotation.Nullable;
import java.util.List;

public class BlockLattice extends Block
{
    public static final PropertyEnum<Connection> NORTH = PropertyEnum.create("north", Connection.class);
    public static final PropertyEnum<Connection> EAST = PropertyEnum.create("east", Connection.class);
    public static final PropertyEnum<Connection> SOUTH = PropertyEnum.create("south", Connection.class);
    public static final PropertyEnum<Connection> WEST = PropertyEnum.create("west", Connection.class);
    public static final PropertyEnum<Connection> UP = PropertyEnum.create("up", Connection.class);
    public static final PropertyEnum<Connection> DOWN = PropertyEnum.create("down", Connection.class);
    public static final PropertyEnum<Variant> VARIANT = PropertyEnum.create("variant", Variant.class);

    private static final AxisAlignedBB CENTER_AABB = new AxisAlignedBB(0.4375D, 0.4375D, 0.4375D, 0.5625D, 0.5625D, 0.5625D);
    private static final AxisAlignedBB NORTH_AABB = new AxisAlignedBB(0.4375D, 0.4375D, 0.0D, 0.5625D, 0.5625D, 0.5625D);
    private static final AxisAlignedBB EAST_AABB = new AxisAlignedBB(0.4375D, 0.4375D, 0.4375D, 1.0D, 0.5625D, 0.5625D);
    private static final AxisAlignedBB SOUTH_AABB = new AxisAlignedBB(0.4375D, 0.4375D, 0.4375D, 0.5625D, 0.5625D, 1.0D);
    private static final AxisAlignedBB WEST_AABB = new AxisAlignedBB(0.0D, 0.4375D, 0.4375D, 0.5625D, 0.5625D, 0.5625D);
    private static final AxisAlignedBB UP_AABB = new AxisAlignedBB(0.4375D, 0.4375D, 0.4375D, 0.5625D, 1.0D, 0.5625D);
    private static final AxisAlignedBB DOWN_AABB = new AxisAlignedBB(0.4375D, 0.0D, 0.4375D, 0.5625D, 0.5625D, 0.5625D);

    public BlockLattice (String registryName, String unlocalizedName) {
        super(Material.IRON);

        setRegistryName(registryName);
        setUnlocalizedName(unlocalizedName);
        setCreativeTab(ModCreativeTabs.tabGardenStuff);
        setHardness(2.5f);
        setResistance(5);
        setSoundType(SoundType.METAL);

        setDefaultState(blockState.getBaseState()
            .withProperty(NORTH, Connection.NONE)
            .withProperty(EAST, Connection.NONE)
            .withProperty(SOUTH, Connection.NONE)
            .withProperty(WEST, Connection.NONE)
            .withProperty(UP, Connection.NONE)
            .withProperty(DOWN, Connection.NONE)
            .withProperty(VARIANT, Variant.IRON)
        );
    }

    @Override
    public int damageDropped (IBlockState state) {
        return state.getValue(VARIANT).getMetadata();
    }

    @Override
    @SuppressWarnings("deprecation")
    public MapColor getMapColor (IBlockState state, IBlockAccess worldIn, BlockPos pos) {
        return state.getValue(VARIANT).getColor();
    }

    @Override
    @SideOnly(Side.CLIENT)
    public void getSubBlocks (CreativeTabs tab, NonNullList<ItemStack> list) {
        for (Variant type : Variant.values())
            list.add(new ItemStack(this, 1, type.getMetadata()));
    }

    @Override
    @SuppressWarnings("deprecation")
    public void addCollisionBoxToList (IBlockState state, World worldIn, BlockPos pos, AxisAlignedBB entityBox, List<AxisAlignedBB> collidingBoxes, @Nullable Entity entityIn, boolean p_185477_7_) {
        if (!p_185477_7_)
            state = getActualState(state, worldIn, pos);

        addCollisionBoxToList(pos, entityBox, collidingBoxes, CENTER_AABB);
        if (state.getValue(NORTH) != Connection.NONE)
            addCollisionBoxToList(pos, entityBox, collidingBoxes, NORTH_AABB);
        if (state.getValue(EAST) != Connection.NONE)
            addCollisionBoxToList(pos, entityBox, collidingBoxes, EAST_AABB);
        if (state.getValue(SOUTH) != Connection.NONE)
            addCollisionBoxToList(pos, entityBox, collidingBoxes, SOUTH_AABB);
        if (state.getValue(WEST) != Connection.NONE)
            addCollisionBoxToList(pos, entityBox, collidingBoxes, WEST_AABB);
        if (state.getValue(UP) != Connection.NONE)
            addCollisionBoxToList(pos, entityBox, collidingBoxes, UP_AABB);
        if (state.getValue(DOWN) != Connection.NONE)
            addCollisionBoxToList(pos, entityBox, collidingBoxes, DOWN_AABB);
    }

    @Override
    @SuppressWarnings("deprecation")
    public AxisAlignedBB getBoundingBox (IBlockState state, IBlockAccess source, BlockPos pos) {
        state = getActualState(state, source, pos);

        float xMin = boundingBoxExtant(state.getValue(WEST));
        float xMax = 1 - boundingBoxExtant(state.getValue(EAST));
        float yMin = boundingBoxExtant(state.getValue(DOWN));
        float yMax = 1 - boundingBoxExtant(state.getValue(UP));
        float zMin = boundingBoxExtant(state.getValue(NORTH));
        float zMax = 1 - boundingBoxExtant(state.getValue(SOUTH));

        return new AxisAlignedBB(xMin, yMin, zMin, xMax, yMax, zMax);
    }

    private static float boundingBoxExtant (Connection con) {
        switch (con) {
            case NONE:
                return 0.4375f;
            case SIDE:
            case EXT:
            case LATTICE:
            default:
                return 0;
        }
    }

    @Override
    @SuppressWarnings("deprecation")
    public boolean isOpaqueCube (IBlockState state) {
        return false;
    }

    @Override
    @SuppressWarnings("deprecation")
    public boolean isFullCube (IBlockState state) {
        return false;
    }

    @Override
    public boolean isPassable (IBlockAccess worldIn, BlockPos pos) {
        return false;
    }

    @Override
    @SideOnly(Side.CLIENT)
    @SuppressWarnings("deprecation")
    public boolean shouldSideBeRendered (IBlockState blockState, IBlockAccess blockAccess, BlockPos pos, EnumFacing side) {
        return true;
    }

    private boolean isNeighborHardConnection (IBlockState state, IBlockAccess world, BlockPos pos, EnumFacing facing) {
        if (state.getMaterial().isOpaque() && state.isFullCube())
            return true;

        if (state.isSideSolid(world, pos, facing.getOpposite()))
            return true;

        if (facing == EnumFacing.DOWN) {
            Block block = state.getBlock();
            if (block instanceof BlockFence || block instanceof net.minecraft.block.BlockFence)
                return true;
        }

        return false;
    }

    private boolean isNeighborExtConnection (IBlockState state, IBlockAccess world, BlockPos pos, EnumFacing facing) {
        return false;
    }

    private boolean isNeighborSelfConnection (IBlockState state) {
        return state.getBlock() instanceof BlockLattice;
    }

    private Connection getNeighborConnection (IBlockAccess world, BlockPos pos, EnumFacing facing) {
        pos = pos.offset(facing);
        IBlockState state = world.getBlockState(pos);

        if (isNeighborSelfConnection(state))
            return Connection.LATTICE;
        if (isNeighborHardConnection(state, world, pos, facing))
            return Connection.SIDE;
        if (isNeighborExtConnection(state, world, pos, facing))
            return Connection.EXT;
        return Connection.NONE;
    }

    @Override
    @SuppressWarnings("deprecation")
    public IBlockState getActualState (IBlockState state, IBlockAccess world, BlockPos pos) {
        return state
            .withProperty(NORTH, getNeighborConnection(world, pos, EnumFacing.NORTH))
            .withProperty(EAST, getNeighborConnection(world, pos, EnumFacing.EAST))
            .withProperty(SOUTH, getNeighborConnection(world, pos, EnumFacing.SOUTH))
            .withProperty(WEST, getNeighborConnection(world, pos, EnumFacing.WEST))
            .withProperty(UP, getNeighborConnection(world, pos, EnumFacing.UP))
            .withProperty(DOWN, getNeighborConnection(world, pos, EnumFacing.DOWN));
    }

    @Nullable
    @Override
    public TileEntity createTileEntity (World world, IBlockState state) {
        return new TileLattice();
    }

    @Override
    public boolean hasTileEntity (IBlockState state) {
        return true;
    }

    @Override
    public int getMetaFromState (IBlockState state) {
        return state.getValue(VARIANT).getMetadata();
    }

    @Override
    @SuppressWarnings("deprecation")
    public IBlockState getStateFromMeta (int meta) {
        return getDefaultState().withProperty(VARIANT, Variant.byMetadata(meta));
    }

    @Override
    protected BlockStateContainer createBlockState () {
        return new BlockStateContainer(this, NORTH, EAST, WEST, SOUTH, UP, DOWN, VARIANT);
    }

    public enum Connection implements IStringSerializable
    {
        NONE("none"),
        SIDE("side"),
        EXT("ext"),
        LATTICE("lattice");

        private final String name;

        Connection (String name) {
            this.name = name;
        }

        public String getName () {
            return name;
        }
    }

    public enum Variant implements IItemEnum
    {
        IRON(0, "iron", "iron", MapColor.GRAY),
        WROUGHT_IRON(1, "wrought_iron", "wrought_iron", MapColor.BLACK),
        RUST(2, "rust", "rust", MapColor.ORANGE_STAINED_HARDENED_CLAY),
        AGED(3, "aged", "aged", MapColor.BROWN),
        MOSS(4, "moss", "moss", MapColor.GREEN_STAINED_HARDENED_CLAY),
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

        @Override
        public String getUnlocalizedName () {
            return unlocalizedName;
        }

        @Override
        public int getMetadata () {
            return meta;
        }

        @Override
        @Nonnull
        public String getName () {
            return name;
        }

        public MapColor getColor () {
            return color;
        }

        public static Variant byMetadata (int meta) {
            if (meta < 0 || meta >= META_LOOKUP.length)
                meta = 0;
            return META_LOOKUP[meta];
        }

        static {
            META_LOOKUP = new Variant[values().length];
            for (Variant upgrade : values()) {
                META_LOOKUP[upgrade.getMetadata()] = upgrade;
            }
        }
    }
}
