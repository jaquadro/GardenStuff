package com.jaquadro.minecraft.gardenstuff.core;

import com.jaquadro.minecraft.chameleon.Chameleon;
import com.jaquadro.minecraft.chameleon.item.ChamMultiItemBlock;
import com.jaquadro.minecraft.chameleon.resources.ModelRegistry;
import com.jaquadro.minecraft.gardenstuff.GardenStuff;
import com.jaquadro.minecraft.gardenstuff.block.*;
import com.jaquadro.minecraft.gardenstuff.block.tile.TileBloomeryFurnace;
import com.jaquadro.minecraft.gardenstuff.block.tile.TileCandelabra;
import net.minecraft.block.Block;
import net.minecraft.client.renderer.block.model.ModelResourceLocation;
import net.minecraft.client.renderer.block.statemap.StateMap;
import net.minecraft.item.Item;
import net.minecraft.item.ItemBlock;
import net.minecraft.item.ItemStack;
import net.minecraft.item.crafting.IRecipe;
import net.minecraft.item.crafting.Ingredient;
import net.minecraft.item.crafting.ShapelessRecipes;
import net.minecraft.util.ResourceLocation;
import net.minecraftforge.client.event.ModelRegistryEvent;
import net.minecraftforge.client.model.ModelLoader;
import net.minecraftforge.event.RegistryEvent;
import net.minecraftforge.fml.common.Mod;
import net.minecraftforge.fml.common.eventhandler.SubscribeEvent;
import net.minecraftforge.fml.common.registry.ForgeRegistries;
import net.minecraftforge.fml.common.registry.GameRegistry;
import net.minecraftforge.fml.common.registry.GameRegistry.ObjectHolder;
import net.minecraftforge.fml.relauncher.Side;
import net.minecraftforge.fml.relauncher.SideOnly;
import net.minecraftforge.oredict.OreDictionary;
import net.minecraftforge.registries.IForgeRegistry;

public class ModBlocks
{
    @ObjectHolder(GardenStuff.MOD_ID + ":stone_block")
    public static BlockStoneMaterial stoneMaterial;
    @ObjectHolder(GardenStuff.MOD_ID + ":metal_block")
    public static BlockMetalMaterial metalMaterial;
    @ObjectHolder(GardenStuff.MOD_ID + ":bloomery_furnace")
    public static BlockBloomeryFurnace bloomeryFurnace;
    @ObjectHolder(GardenStuff.MOD_ID + ":metal_fence")
    public static BlockFence metalFence;
    @ObjectHolder(GardenStuff.MOD_ID + ":candelabra")
    public static BlockCandelabra candelabra;
    @ObjectHolder(GardenStuff.MOD_ID + ":candelilla")
    public static BlockCandelilla candelilla;

    @Mod.EventBusSubscriber(modid = GardenStuff.MOD_ID)
    public static class Registration
    {
        @SubscribeEvent
        public static void registerBlocks (RegistryEvent.Register<Block> event) {
            IForgeRegistry<Block> registry = event.getRegistry();

            registry.registerAll(
                new BlockStoneMaterial("stone_block", makeName("stone_block")),
                new BlockMetalMaterial("metal_block", makeName("metal_block")),
                new BlockBloomeryFurnace("bloomery_furnace", makeName("bloomery_furnace")),
                new BlockFence("metal_fence", makeName("metal_fence")),
                new BlockCandelabra("candelabra", makeName("candelabra")),
                new BlockCandelilla("candelilla", makeName("candelilla"))
            );

            GameRegistry.registerTileEntity(TileBloomeryFurnace.class, makeName("bloomery_furnace"));
            GameRegistry.registerTileEntity(TileCandelabra.class, makeName("candelabra"));
        }

        @SubscribeEvent
        public static void registerItems (RegistryEvent.Register<Item> event) {
            IForgeRegistry<Item> registry = event.getRegistry();

            registry.registerAll(
                new ChamMultiItemBlock(stoneMaterial, BlockStoneMaterial.VARIANT, BlockStoneMaterial.StoneType.class, BlockStoneMaterial.StoneType::byMetadata),
                new ChamMultiItemBlock(metalMaterial, BlockMetalMaterial.VARIANT, BlockMetalMaterial.MetalType.class, BlockMetalMaterial.MetalType::byMetadata),
                new ItemBlock(bloomeryFurnace).setRegistryName(bloomeryFurnace.getRegistryName()),
                new ChamMultiItemBlock(metalFence, BlockFence.VARIANT, BlockFence.Variant.class, BlockFence.Variant::byMetadata),
                new ChamMultiItemBlock(candelabra, BlockCandelabra.VARIANT, BlockCandelabra.Variant.class, BlockCandelabra.Variant::byMetadata)
            );

            OreDictionary.registerOre("blockCharcoal", new ItemStack(stoneMaterial, 1, BlockStoneMaterial.StoneType.CHARCOAL_BLOCK.getMetadata()));
            OreDictionary.registerOre("blockWroughtIron", new ItemStack(metalMaterial, 1, BlockMetalMaterial.MetalType.WROUGHT_IRON_BLOCK.getMetadata()));
        }

        private static final ResourceLocation EMPTY_GROUP = new ResourceLocation("", "");

        @SubscribeEvent
        public static void registerRecipes (RegistryEvent.Register<IRecipe> event) {
            IForgeRegistry<IRecipe> registry = event.getRegistry();
        }

        @SubscribeEvent
        @SideOnly(Side.CLIENT)
        public static void registerModels (ModelRegistryEvent event) {
            ModelRegistry modelRegistry = Chameleon.instance.modelRegistry;

            modelRegistry.registerItemVariants(stoneMaterial);
            modelRegistry.registerItemVariants(metalMaterial);
            modelRegistry.registerItemVariants(bloomeryFurnace);
            modelRegistry.registerItemVariants(metalFence);
            modelRegistry.registerItemVariants(candelabra);
            modelRegistry.registerItemVariants(candelilla);

            ModelLoader.setCustomStateMapper(metalFence, (new StateMap.Builder()).withName(BlockFence.VARIANT).withSuffix("_wrought_iron_fence").build());
            ModelLoader.setCustomStateMapper(candelabra, (new StateMap.Builder()).withName(BlockCandelabra.VARIANT).withSuffix("_candelabra").build());

            ModelLoader.setCustomModelResourceLocation(Item.getItemFromBlock(metalFence), 0, new ModelResourceLocation("gardenstuff:metal_fence_0", "inventory"));
            ModelLoader.setCustomModelResourceLocation(Item.getItemFromBlock(metalFence), 1, new ModelResourceLocation("gardenstuff:metal_fence_1", "inventory"));
            ModelLoader.setCustomModelResourceLocation(Item.getItemFromBlock(metalFence), 2, new ModelResourceLocation("gardenstuff:metal_fence_2", "inventory"));
            ModelLoader.setCustomModelResourceLocation(Item.getItemFromBlock(metalFence), 3, new ModelResourceLocation("gardenstuff:metal_fence_3", "inventory"));

            ModelLoader.setCustomModelResourceLocation(Item.getItemFromBlock(candelabra), 0, new ModelResourceLocation("gardenstuff:candelabra_level0", "inventory"));
            ModelLoader.setCustomModelResourceLocation(Item.getItemFromBlock(candelabra), 1, new ModelResourceLocation("gardenstuff:candelabra_level1", "inventory"));
            ModelLoader.setCustomModelResourceLocation(Item.getItemFromBlock(candelabra), 2, new ModelResourceLocation("gardenstuff:candelabra_level2", "inventory"));
            ModelLoader.setCustomModelResourceLocation(Item.getItemFromBlock(candelabra), 3, new ModelResourceLocation("gardenstuff:candelabra_level3", "inventory"));
        }
    }

    /*public void init () {
        stoneMaterial = new BlockStoneMaterial("stone_block", makeName("stoneBlock"));
        metalMaterial = new BlockMetalMaterial("metal_block", makeName("metalBlock"));
        bloomeryFurnace = new BlockBloomeryFurnace("bloomery_furnace", makeName("bloomeryFurnace"));
        metalFence = new BlockFence("metal_fence", makeName("metalFence"));
        candelabra = new BlockCandelabra("candelabra", makeName("candelabra"));

        GameRegistry.register(stoneMaterial);
        GameRegistry.register(new ChamMultiItemBlock(stoneMaterial, BlockStoneMaterial.VARIANT, BlockStoneMaterial.StoneType.class, BlockStoneMaterial.StoneType::byMetadata)
            .setRegistryName(stoneMaterial.getRegistryName()));

        GameRegistry.register(metalMaterial);
        GameRegistry.register(new ChamMultiItemBlock(metalMaterial, BlockMetalMaterial.VARIANT, BlockMetalMaterial.MetalType.class, BlockMetalMaterial.MetalType::byMetadata)
            .setRegistryName(metalMaterial.getRegistryName()));

        GameRegistry.register(bloomeryFurnace);
        GameRegistry.register(new ItemBlock(bloomeryFurnace).setRegistryName(bloomeryFurnace.getRegistryName()));
        GameRegistry.registerTileEntity(TileBloomeryFurnace.class, bloomeryFurnace.getRegistryName().toString());

        GameRegistry.register(metalFence);
        GameRegistry.register(new ChamMultiItemBlock(metalFence, BlockFence.VARIANT, BlockFence.Variant.class, BlockFence.Variant::byMetadata)
            .setRegistryName(metalFence.getRegistryName()));

        GameRegistry.register(candelabra);
        GameRegistry.register(new ChamMultiItemBlock(candelabra, BlockCandelabra.VARIANT, BlockCandelabra.Variant.class, BlockCandelabra.Variant::byMetadata)
            .setRegistryName(candelabra.getRegistryName()));
        GameRegistry.registerTileEntity(TileCandelabra.class, candelabra.getRegistryName().toString());

        OreDictionary.registerOre("blockCharcoal", new ItemStack(stoneMaterial, 1, BlockStoneMaterial.StoneType.CHARCOAL_BLOCK.getMetadata()));
        OreDictionary.registerOre("blockWroughtIron", new ItemStack(metalMaterial, 1, BlockMetalMaterial.MetalType.WROUGHT_IRON_BLOCK.getMetadata()));
    }

    @SideOnly(Side.CLIENT)
    public void initClient () {
        ModelRegistry modelRegistry = Chameleon.instance.modelRegistry;

        modelRegistry.registerItemVariants(stoneMaterial);
        modelRegistry.registerItemVariants(metalMaterial);
        modelRegistry.registerItemVariants(bloomeryFurnace);
        modelRegistry.registerItemVariants(metalFence);
        modelRegistry.registerItemVariants(candelabra);

        ModelLoader.setCustomStateMapper(metalFence, (new StateMap.Builder()).withName(BlockFence.VARIANT).withSuffix("_wrought_iron_fence").build());
        ModelLoader.setCustomStateMapper(candelabra, (new StateMap.Builder()).withName(BlockCandelabra.VARIANT).withSuffix("_candelabra").build());

        ModelLoader.setCustomModelResourceLocation(Item.getItemFromBlock(metalFence), 0, new ModelResourceLocation("gardenstuff:wrought_iron_fence_0", "inventory"));
        ModelLoader.setCustomModelResourceLocation(Item.getItemFromBlock(metalFence), 1, new ModelResourceLocation("gardenstuff:wrought_iron_fence_1", "inventory"));
        ModelLoader.setCustomModelResourceLocation(Item.getItemFromBlock(metalFence), 2, new ModelResourceLocation("gardenstuff:wrought_iron_fence_2", "inventory"));
        ModelLoader.setCustomModelResourceLocation(Item.getItemFromBlock(metalFence), 3, new ModelResourceLocation("gardenstuff:wrought_iron_fence_3", "inventory"));

        ModelLoader.setCustomModelResourceLocation(Item.getItemFromBlock(candelabra), 0, new ModelResourceLocation("gardenstuff:level1_candelabra", "inventory"));
    }*/

    public static String makeName (String name) {
        return GardenStuff.MOD_ID + "." + name;
    }
}
