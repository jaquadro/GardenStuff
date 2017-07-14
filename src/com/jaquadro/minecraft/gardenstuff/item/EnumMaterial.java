package com.jaquadro.minecraft.gardenstuff.item;

import com.jaquadro.minecraft.chameleon.resources.IItemEnum;
import net.minecraft.item.Item;
import net.minecraft.item.ItemStack;

import javax.annotation.Nonnull;

public enum EnumMaterial implements IItemEnum
{
    IRON_NUGGET(0, "iron_nugget", "iron_nugget"),
    IRON_LINK(1, "iron_link", "iron_link"),
    GOLD_LINK(2, "gold_link", "gold_link"),
    WROUGHT_IRON_NUGGET(3, "wrought_iron_nugget", "wrought_iron_nugget"),
    WROUGHT_IRON_INGOT(4, "wrought_iron_ingot", "wrought_iron_ingot"),
    WROUGHT_IRON_LINK(5, "wrought_iron_link", "wrought_iron_link"),
    CANDELILLA(6, "candelilla", "candelilla"),
    WAX(7, "wax", "wax"),
    ;

    private static final EnumMaterial[] META_LOOKUP;

    private final int meta;
    private final String name;
    private final String unlocalizedName;

    EnumMaterial (int meta, String name, String unlocalizedName) {
        this.meta = meta;
        this.name = name;
        this.unlocalizedName = unlocalizedName;
    }

    public int getMetadata () {
        return meta;
    }

    public String getUnlocalizedName () {
        return unlocalizedName;
    }

    public static EnumMaterial byMetadata (int meta) {
        if (meta < 0 || meta >= META_LOOKUP.length)
            meta = 0;
        return META_LOOKUP[meta];
    }

    @Override
    public String toString () {
        return unlocalizedName;
    }

    @Override
    @Nonnull
    public String getName () {
        return name;
    }

    public ItemStack getItemStack (Item obj) {
        return new ItemStack(obj, 1, getMetadata());
    }

    static {
        META_LOOKUP = new EnumMaterial[values().length];
        for (EnumMaterial upgrade : values()) {
            META_LOOKUP[upgrade.getMetadata()] = upgrade;
        }
    }
}
