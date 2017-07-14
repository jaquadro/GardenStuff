package com.jaquadro.minecraft.gardenstuff.core;

import net.minecraft.creativetab.CreativeTabs;
import net.minecraft.item.ItemStack;
import net.minecraftforge.fml.relauncher.Side;
import net.minecraftforge.fml.relauncher.SideOnly;

import javax.annotation.Nonnull;

public class ModCreativeTabs
{
    private ModCreativeTabs () { }

    public static final CreativeTabs tabGardenStuff = new CreativeTabs("gardenstuff")
    {
        @Override
        @Nonnull
        @SideOnly(Side.CLIENT)
        public ItemStack getTabIconItem () {
            return new ItemStack(ModItems.material, 1, 4);
        }
    };
}
