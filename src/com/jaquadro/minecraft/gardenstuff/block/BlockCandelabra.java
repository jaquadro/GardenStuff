package com.jaquadro.minecraft.gardenstuff.block;

import com.jaquadro.minecraft.chameleon.render.ChamRender;
import com.jaquadro.minecraft.chameleon.render.ChamRenderState;
import com.jaquadro.minecraft.chameleon.resources.IItemEnum;
import com.jaquadro.minecraft.gardenstuff.block.tile.TileCandelabra;
import com.jaquadro.minecraft.gardenstuff.core.ModCreativeTabs;
import net.minecraft.block.Block;
import net.minecraft.block.BlockHorizontal;
import net.minecraft.block.SoundType;
import net.minecraft.block.material.MapColor;
import net.minecraft.block.material.Material;
import net.minecraft.block.properties.PropertyDirection;
import net.minecraft.block.properties.PropertyEnum;
import net.minecraft.block.state.BlockStateContainer;
import net.minecraft.block.state.IBlockState;
import net.minecraft.creativetab.CreativeTabs;
import net.minecraft.entity.Entity;
import net.minecraft.entity.EntityLivingBase;
import net.minecraft.item.Item;
import net.minecraft.item.ItemStack;
import net.minecraft.tileentity.TileEntity;
import net.minecraft.util.*;
import net.minecraft.util.math.AxisAlignedBB;
import net.minecraft.util.math.BlockPos;
import net.minecraft.world.ChunkCache;
import net.minecraft.world.IBlockAccess;
import net.minecraft.world.World;
import net.minecraft.world.chunk.Chunk;
import net.minecraftforge.fml.relauncher.Side;
import net.minecraftforge.fml.relauncher.SideOnly;

import javax.annotation.Nullable;
import java.util.List;
import java.util.Random;

public class BlockCandelabra extends Block
{
    public static final PropertyDirection FACING = BlockHorizontal.FACING;
    public static final PropertyEnum<EnumMount> MOUNT = PropertyEnum.create("mount", EnumMount.class);
    public static final PropertyEnum<Variant> VARIANT = PropertyEnum.create("variant", Variant.class);

    // 123 - SWNE
    protected static final AxisAlignedBB[] WALL_BOUNDING_BOXES = new AxisAlignedBB[]{
        new AxisAlignedBB(.375, .0625, 0, .625, .875, .40625),
        new AxisAlignedBB(.59375, .0625, .375, 1, .875, .625),
        new AxisAlignedBB(.375, .0625, .59375, .625, .875, 1),
        new AxisAlignedBB(0, .0625, .375, .40625, .875, .625),
        new AxisAlignedBB(.109375, .0625, 0, 0.890625, .875, .40625),
        new AxisAlignedBB(.59375, .0625, .109375, 1, .875, 0.890625),
        new AxisAlignedBB(.109375, .0625, .59375, 0.890625, .875, 1),
        new AxisAlignedBB(0, .0625, .109375, .40625, .875, 0.890625),
        new AxisAlignedBB(.109375, .0625, 0, 0.890625, .875, .40625),
        new AxisAlignedBB(.59375, .0625, .109375, 1, .875, 0.890625),
        new AxisAlignedBB(.109375, .0625, .59375, 0.890625, .875, 1),
        new AxisAlignedBB(0, .0625, .109375, .40625, .875, 0.890625),
    };

    protected static final AxisAlignedBB LEVEL0_BOUNDING_BOX = new AxisAlignedBB(0.40625, 0, 0.40625, 0.59375, .5, 0.59375);
    protected static final AxisAlignedBB LEVEL1_BOUNDING_BOX = new AxisAlignedBB(0.359375, 0, 0.359375, 0.640625, 0.9375, 0.640625);
    protected static final AxisAlignedBB LEVEL3_BOUNDING_BOX = new AxisAlignedBB(0.03125, 0, 0.03125, 0.96875, 0.9375, 0.96875);

    protected static final AxisAlignedBB[] LEVEL2_BOUNDING_BOXES = new AxisAlignedBB[] {
        new AxisAlignedBB(0.03125, 0, 0.359375, 0.96875, 0.9375, 0.640625),
        new AxisAlignedBB(0.359375, 0, 0.03125, 0.640625, 0.9375, 0.96875),
        new AxisAlignedBB(0.03125, 0, 0.359375, 0.96875, 0.9375, 0.640625),
        new AxisAlignedBB(0.359375, 0, 0.03125, 0.640625, 0.9375, 0.96875),
    };

    public BlockCandelabra (String registryName, String unlocalizedName) {
        super(Material.IRON);

        setRegistryName(registryName);
        setUnlocalizedName(unlocalizedName);
        setCreativeTab(ModCreativeTabs.tabGardenStuff);
        setHardness(5);
        setResistance(10);
        setSoundType(SoundType.STONE);

        setDefaultState(blockState.getBaseState()
            .withProperty(FACING, EnumFacing.NORTH)
            .withProperty(MOUNT, EnumMount.FLOOR)
            .withProperty(VARIANT, Variant.LEVEL1));
    }

    @Override
    public int damageDropped (IBlockState state) {
        return state.getValue(VARIANT).getMetadata();
    }

    @Override
    @SideOnly(Side.CLIENT)
    public void getSubBlocks (CreativeTabs itemIn, NonNullList<ItemStack> items) {
        for (Variant type : Variant.values())
            items.add(new ItemStack(this, 1, type.getMetadata()));
    }

    @Override
    public BlockRenderLayer getBlockLayer () {
        return BlockRenderLayer.CUTOUT_MIPPED;
    }

    @Override
    public AxisAlignedBB getBoundingBox (IBlockState state, IBlockAccess source, BlockPos pos) {
        state = getActualState(state, source, pos);

        if (state.getValue(VARIANT) != Variant.LEVEL0 && state.getValue(MOUNT) == EnumMount.WALL)
            return WALL_BOUNDING_BOXES[getWallBoundingBoxIndex(state)];

        switch (state.getValue(VARIANT)) {
            case LEVEL0:
                return LEVEL0_BOUNDING_BOX;
            case LEVEL1:
                return LEVEL1_BOUNDING_BOX;
            case LEVEL2:
                return LEVEL2_BOUNDING_BOXES[state.getValue(FACING).getHorizontalIndex()];
            case LEVEL3:
                return LEVEL3_BOUNDING_BOX;
        }

        return LEVEL1_BOUNDING_BOX;
    }

    private static int getWallBoundingBoxIndex (IBlockState state) {
        return (state.getValue(VARIANT).getMetadata() - 1) << 2 | state.getValue(FACING).getHorizontalIndex();
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
    public boolean isPassable (IBlockAccess worldIn, BlockPos pos) {
        return false;
    }

    @Override
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
    public IBlockState getActualState (IBlockState state, IBlockAccess worldIn, BlockPos pos) {
        TileEntity tile = getTileEntityWeak(worldIn, pos);
        if (tile instanceof TileCandelabra) {
            TileCandelabra tileCandelabra = (TileCandelabra) tile;

            return state.withProperty(FACING, tileCandelabra.getFacing())
                .withProperty(MOUNT, tileCandelabra.getMount());
        }

        return state;
    }

    @Override
    protected BlockStateContainer createBlockState () {
        return new BlockStateContainer(this, FACING, MOUNT, VARIANT);
    }

    @Override
    public TileEntity createTileEntity (World world, IBlockState state) {
        return new TileCandelabra().initialize(EnumFacing.NORTH, EnumMount.FLOOR);
    }

    @Override
    public boolean hasTileEntity (IBlockState state) {
        return true;
    }

    @Override
    public int getLightValue (IBlockState state) {
        switch (state.getValue(VARIANT)) {
            case LEVEL0:
            case LEVEL1:
                return 13;
            case LEVEL2:
                return 14;
            case LEVEL3:
                return 15;
        }

        return 0;
    }

    private boolean canPlaceOn (World world, BlockPos pos) {
        IBlockState state = world.getBlockState(pos);
        if (state.isSideSolid(world, pos, EnumFacing.UP))
            return true;

        return state.getBlock().canPlaceTorchOnTop(state, world, pos);
    }

    private boolean canPlaceAt (World world, BlockPos pos, EnumFacing facing) {
        BlockPos blockPos = pos.offset(facing.getOpposite());
        if (facing.getAxis().isHorizontal())
            return world.isSideSolid(blockPos, facing, true);
        if (facing == EnumFacing.UP)
            return canPlaceOn(world, blockPos);
        if (facing == EnumFacing.DOWN)
            return true;

        return false;
    }

    @Override
    public boolean canPlaceBlockAt (World worldIn, BlockPos pos) {
        for (EnumFacing enumfacing : FACING.getAllowedValues()) {
            if (this.canPlaceAt(worldIn, pos, enumfacing))
                return true;
        }

        return true;
    }


    @Override
    public IBlockState getStateForPlacement (World world, BlockPos pos, EnumFacing facing, float hitX, float hitY, float hitZ, int meta, EntityLivingBase placer, EnumHand hand) {
        IBlockState state = getStateFromMeta(meta);

        if (canPlaceAt(world, pos, facing)) {
            if (facing.getAxis().isHorizontal()) {
                state = state.withProperty(MOUNT, EnumMount.WALL).withProperty(FACING, facing);
            }
            else {
                state = state.withProperty(FACING, placer.getHorizontalFacing().getOpposite());
                if (facing == EnumFacing.UP)
                    state = state.withProperty(MOUNT, EnumMount.FLOOR);
                else if (facing == EnumFacing.DOWN)
                    state = state.withProperty(MOUNT, EnumMount.CEILING);
            }
        }

        if (Variant.byMetadata(meta) == Variant.LEVEL0)
            state = state.withProperty(MOUNT, EnumMount.FLOOR);

        return state;
    }

    @Override
    public void onBlockPlacedBy (World worldIn, BlockPos pos, IBlockState state, EntityLivingBase placer, ItemStack stack) {
        worldIn.setBlockState(pos, state, 2);

        TileCandelabra tile = getTileEntity(worldIn, pos);
        if (tile != null) {
            tile.setFacing(state.getValue(FACING));
            tile.setMount(state.getValue(MOUNT));
        }
    }

    protected TileCandelabra getTileEntity (IBlockAccess blockAccess, BlockPos pos) {
        TileEntity tile = blockAccess.getTileEntity(pos);
        return (tile instanceof TileCandelabra) ? (TileCandelabra) tile : null;
    }

    protected TileEntity getTileEntityWeak (IBlockAccess blockAccess, BlockPos pos) {
        if (blockAccess instanceof ChunkCache)
            return ((ChunkCache) blockAccess).getTileEntity(pos, Chunk.EnumCreateEntityType.CHECK);
        else
            return blockAccess.getTileEntity(pos);
    }

    @Override
    @SideOnly(Side.CLIENT)
    public void randomDisplayTick (IBlockState state, World world, BlockPos pos, Random rand) {
        TileCandelabra tile = getTileEntity(world, pos);
        if (tile == null)
            return;

        state = getActualState(state, world, pos);
        Variant level = state.getValue(VARIANT);
        EnumFacing dir = state.getValue(FACING);
        EnumMount mount = state.getValue(MOUNT);

        float flameDepth = 0.96875f;

        if (mount == EnumMount.WALL) {
            switch (level) {
                case LEVEL1:
                    renderParticleAt(world, pos, dir, .5, flameDepth, .75);
                    break;
                case LEVEL2:
                    renderParticleAt(world, pos, dir, .25, flameDepth, .75);
                    renderParticleAt(world, pos, dir, .75, flameDepth, .75);
                    break;
                case LEVEL3:
                    renderParticleAt(world, pos, dir, .25, flameDepth, .75);
                    renderParticleAt(world, pos, dir, .75, flameDepth, .75);
                    renderParticleAt(world, pos, dir, .5, flameDepth, .71875);
            }
        }
        else if (mount == EnumMount.CEILING) {
            switch (level) {
                case LEVEL1:
                    renderParticleAt(world, pos, dir, .5, flameDepth - .3125, .5);
                    break;
                case LEVEL2:
                    renderParticleAt(world, pos, dir, .15625f, flameDepth, .5);
                    renderParticleAt(world, pos, dir, .84375, flameDepth, .5);
                    break;
                case LEVEL3:
                    renderParticleAt(world, pos, dir, .15625f, flameDepth, .5);
                    renderParticleAt(world, pos, dir, .84375, flameDepth, .5);
                    renderParticleAt(world, pos, dir, .5, flameDepth, .15625f);
                    renderParticleAt(world, pos, dir, .5, flameDepth, .84375);
                    break;
            }
        }
        else {
            switch (level) {
                case LEVEL0:
                    renderParticleAt(world, pos, dir, .5, .5, .5);
                    break;
                case LEVEL1:
                    renderParticleAt(world, pos, dir, .5, flameDepth + .0625, .5);
                    break;
                case LEVEL2:
                    renderParticleAt(world, pos, dir, .5, flameDepth + .0625, .5);
                    renderParticleAt(world, pos, dir, .15625f, flameDepth, .5);
                    renderParticleAt(world, pos, dir, .84375, flameDepth, .5);
                    break;
                case LEVEL3:
                    renderParticleAt(world, pos, dir, .5, flameDepth + .0625, .5);
                    renderParticleAt(world, pos, dir, .15625f, flameDepth, .5);
                    renderParticleAt(world, pos, dir, .84375, flameDepth, .5);
                    renderParticleAt(world, pos, dir, .5, flameDepth, .15625f);
                    renderParticleAt(world, pos, dir, .5, flameDepth, .84375);
                    break;
            }
        }
    }

    private void renderParticleAt (World world, BlockPos pos, EnumFacing dir, double x, double y, double z) {
        double[] out = new double[3];
        double[] in = new double[] { x, y, z };

        ChamRender.instance.state.transformCoord(in, out, ChamRenderState.ROTATION_BY_FACE_FACE[2][dir.getIndex()]);
        world.spawnParticle(EnumParticleTypes.FLAME, pos.getX() + out[0], pos.getY() + out[1], pos.getZ() + out[2], 0.0D, 0.0D, 0.0D);
    }

    public enum Variant implements IItemEnum
    {
        LEVEL0(0, "level0"),
        LEVEL1(1, "level1"),
        LEVEL2(2, "level2"),
        LEVEL3(3, "level3");

        private static final Variant[] META_LOOKUP;

        private final int meta;
        private final String name;
        private final String unlocalizedName;

        Variant (int meta, String name) {
            this.meta = meta;
            this.name = name;
            this.unlocalizedName = name;
        }

        public int getMetadata () {
            return meta;
        }

        public String getUnlocalizedName () {
            return unlocalizedName;
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

    public enum EnumMount implements IStringSerializable {
        FLOOR(0, "floor"),
        WALL(1, "wall"),
        CEILING(2, "ceiling"),
        PIKE(3, "pike");

        private static final EnumMount[] META_LOOKUP;

        private final int meta;
        private final String name;

        EnumMount (int meta, String name)
        {
            this.meta = meta;
            this.name = name;
        }

        public int getMetadata () {
            return meta;
        }

        public String toString()
        {
            return this.name;
        }

        public String getName()
        {
            return this.name;
        }

        public static EnumMount byMetadata (int meta) {
            if (meta < 0 || meta >= META_LOOKUP.length)
                meta = 0;
            return META_LOOKUP[meta];
        }

        static {
            META_LOOKUP = new EnumMount[values().length];
            for (EnumMount upgrade : values()) {
                META_LOOKUP[upgrade.getMetadata()] = upgrade;
            }
        }
    }
}
