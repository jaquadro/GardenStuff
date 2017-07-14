package com.jaquadro.minecraft.gardenstuff.block;

import com.jaquadro.minecraft.chameleon.resources.IItemEnum;
import com.jaquadro.minecraft.gardenstuff.core.ModCreativeTabs;
import net.minecraft.block.Block;
import net.minecraft.block.SoundType;
import net.minecraft.block.material.MapColor;
import net.minecraft.block.material.Material;
import net.minecraft.block.properties.PropertyEnum;
import net.minecraft.block.state.BlockStateContainer;
import net.minecraft.block.state.IBlockState;
import net.minecraft.creativetab.CreativeTabs;
import net.minecraft.item.ItemStack;
import net.minecraft.util.NonNullList;
import net.minecraft.util.math.BlockPos;
import net.minecraft.world.World;
import net.minecraftforge.fml.common.IFuelHandler;

import javax.annotation.Nonnull;

public class BlockMetalMaterial extends Block implements IFuelHandler
{
    public static final PropertyEnum<MetalType> VARIANT = PropertyEnum.create("variant", MetalType.class);

    public BlockMetalMaterial (String registryName, String unlocalizedName) {
        super(Material.IRON);

        setRegistryName(registryName);
        setUnlocalizedName(unlocalizedName);
        setCreativeTab(ModCreativeTabs.tabGardenStuff);
        setHardness(5);
        setResistance(10);
        setSoundType(SoundType.METAL);

        setDefaultState(blockState.getBaseState().withProperty(VARIANT, MetalType.WROUGHT_IRON_BLOCK));
    }

    public MapColor getMapColor (IBlockState state)
    {
        return (state.getValue(VARIANT)).getColor();
    }

    @Override
    public void getSubBlocks (CreativeTabs tab, NonNullList<ItemStack> list) {
        for (MetalType type : MetalType.values())
            list.add(new ItemStack(this, 1, type.getMetadata()));
    }

    @Nonnull
    @Override
    public ItemStack getItem (World worldIn, BlockPos pos, IBlockState state) {
        return new ItemStack(this, 1, state.getValue(VARIANT).getMetadata());
    }

    @Override
    public IBlockState getStateFromMeta (int meta) {
        return getDefaultState().withProperty(VARIANT, MetalType.byMetadata(meta));
    }

    @Override
    public int getMetaFromState (IBlockState state) {
        return state.getValue(VARIANT).getMetadata();
    }

    @Override
    protected BlockStateContainer createBlockState () {
        return new BlockStateContainer(this, VARIANT);
    }

    @Override
    public int damageDropped (IBlockState state) {
        return state.getValue(VARIANT).getMetadata();
    }

    @Override
    public int getBurnTime (@Nonnull ItemStack fuel) {
        if (!fuel.isEmpty() && Block.getBlockFromItem(fuel.getItem()) == this && MetalType.byMetadata(fuel.getItemDamage()) == MetalType.WROUGHT_IRON_BLOCK)
            return 16000;

        return 0;
    }

    public enum MetalType implements IItemEnum {
        WROUGHT_IRON_BLOCK(0, "wrought_iron", "wroughtIron", MapColor.BLACK)
        ;

        private static final MetalType[] META_LOOKUP;

        private final int meta;
        private final String name;
        private final String unlocalizedName;
        private final MapColor color;

        MetalType (int meta, String name, String unlocalizedName, MapColor color) {
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

        public static MetalType byMetadata (int meta) {
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
            META_LOOKUP = new MetalType[values().length];
            for (MetalType upgrade : values()) {
                META_LOOKUP[upgrade.getMetadata()] = upgrade;
            }
        }
    }
}
