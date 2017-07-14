package com.jaquadro.minecraft.gardenstuff;

import com.jaquadro.minecraft.gardenstuff.core.CommonProxy;
import com.jaquadro.minecraft.gardenstuff.core.ModBlocks;
import com.jaquadro.minecraft.gardenstuff.core.ModItems;
import com.jaquadro.minecraft.gardenstuff.core.handlers.GuiHandler;
import com.jaquadro.minecraft.gardenstuff.world.gen.WorldGenCandelilla;
import net.minecraftforge.fml.common.Mod;
import net.minecraftforge.fml.common.SidedProxy;
import net.minecraftforge.fml.common.event.FMLInitializationEvent;
import net.minecraftforge.fml.common.event.FMLPreInitializationEvent;
import net.minecraftforge.fml.common.network.NetworkRegistry;
import net.minecraftforge.fml.common.registry.GameRegistry;

@Mod(modid = GardenStuff.MOD_ID, name = GardenStuff.MOD_NAME, version = GardenStuff.MOD_VERSION,
    dependencies = "required-after:forge@14.21.0.2362,);required-after:chameleon;after:waila;",
    guiFactory = GardenStuff.SOURCE_PATH + "core.ModGuiFactory",
    acceptedMinecraftVersions = "[1.12,1.13)")
public class GardenStuff
{
    public static final String MOD_ID = "gardenstuff";
    public static final String MOD_NAME = "Garden Stuff";
    public static final String MOD_VERSION = "@VERSION@";
    public static final String SOURCE_PATH = "com.jaquadro.minecraft.gardenstuff.";

    @Mod.Instance(MOD_ID)
    public static GardenStuff instance;

    @SidedProxy(clientSide = SOURCE_PATH + "core.ClientProxy", serverSide = SOURCE_PATH + "core.CommonProxy")
    public static CommonProxy proxy;

    @Mod.EventHandler
    public void preInit (FMLPreInitializationEvent event) {
        proxy.registerRenderers();

        GameRegistry.registerWorldGenerator(new WorldGenCandelilla(), 10);
    }

    @Mod.EventHandler
    public void init (FMLInitializationEvent event) {
        NetworkRegistry.INSTANCE.registerGuiHandler(this, new GuiHandler());
    }
}
