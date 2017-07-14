package com.jaquadro.minecraft.gardenstuff.core;

import com.jaquadro.minecraft.chameleon.Chameleon;
import com.jaquadro.minecraft.chameleon.item.ChamMultiItem;
import com.jaquadro.minecraft.chameleon.resources.ModelRegistry;
import com.jaquadro.minecraft.gardenstuff.GardenStuff;
import com.jaquadro.minecraft.gardenstuff.item.EnumMaterial;
import com.jaquadro.minecraft.gardenstuff.item.ItemCandelillaSeeds;
import net.minecraft.item.Item;
import net.minecraft.item.crafting.IRecipe;
import net.minecraftforge.client.event.ModelRegistryEvent;
import net.minecraftforge.event.RegistryEvent;
import net.minecraftforge.fml.common.Mod;
import net.minecraftforge.fml.common.eventhandler.SubscribeEvent;
import net.minecraftforge.fml.common.registry.GameRegistry;
import net.minecraftforge.fml.common.registry.GameRegistry.ObjectHolder;
import net.minecraftforge.fml.relauncher.Side;
import net.minecraftforge.fml.relauncher.SideOnly;
import net.minecraftforge.oredict.OreDictionary;
import net.minecraftforge.registries.IForgeRegistry;

@ObjectHolder(GardenStuff.MOD_ID)
public class ModItems
{
    @ObjectHolder("material")
    public static Item material;
    @ObjectHolder("candelilla_seeds")
    public static Item candelilla_seeds;

    @Mod.EventBusSubscriber(modid = GardenStuff.MOD_ID)
    public static class Registration
    {
        @SubscribeEvent
        public static void registerItems (RegistryEvent.Register<Item> event) {
            IForgeRegistry<Item> registry = event.getRegistry();

            registry.registerAll(
                new ChamMultiItem<>(EnumMaterial.class, EnumMaterial::byMetadata)
                    .setRegistryName("material").setUnlocalizedName(makeName("material"))
                    .setCreativeTab(ModCreativeTabs.tabGardenStuff),
                new ItemCandelillaSeeds()
                    .setRegistryName("candelilla_seeds").setUnlocalizedName(makeName("candelilla_seeds"))
                    .setCreativeTab(ModCreativeTabs.tabGardenStuff)
            );
        }

        @SubscribeEvent
        public static void registerRecipes (RegistryEvent.Register<IRecipe> event) {
            IForgeRegistry<IRecipe> registry = event.getRegistry();

            OreDictionary.registerOre("ingotWroughtIron", EnumMaterial.WROUGHT_IRON_INGOT.getItemStack(material));
            OreDictionary.registerOre("nuggetWroughtIron", EnumMaterial.WROUGHT_IRON_NUGGET.getItemStack(material));
            OreDictionary.registerOre("materialWax", EnumMaterial.WAX.getItemStack(material));
            OreDictionary.registerOre("materialPressedWax", EnumMaterial.WAX.getItemStack(material));

            GameRegistry.addSmelting(EnumMaterial.CANDELILLA.getItemStack(material), EnumMaterial.WAX.getItemStack(material), 0);
        }

        @SubscribeEvent
        @SideOnly(Side.CLIENT)
        public static void registerModels (ModelRegistryEvent event) {
            ModelRegistry modelRegistry = Chameleon.instance.modelRegistry;

            modelRegistry.registerItemVariants(material);
            modelRegistry.registerItemVariants(candelilla_seeds);
        }
    }

    /*public void init () {
        material = new ChamMultiItem<>(EnumMaterial.class, EnumMaterial::byMetadata)
            .setRegistryName("material").setUnlocalizedName(makeName("material")).setCreativeTab(ModCreativeTabs.tabGardenStuff);

        GameRegistry.register(material);
    }

    @SideOnly(Side.CLIENT)
    public void initClient () {
        ModelRegistry modelRegistry = Chameleon.instance.modelRegistry;

        modelRegistry.registerItemVariants(material);
    }*/

    public static String makeName (String name) {
        return GardenStuff.MOD_ID + "." + name;
    }
}
