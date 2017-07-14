package com.jaquadro.minecraft.gardenstuff.config;

import com.jaquadro.minecraft.gardenstuff.GardenStuff;
import net.minecraft.client.gui.GuiScreen;
import net.minecraftforge.common.config.ConfigElement;
import net.minecraftforge.fml.client.config.GuiConfig;
import net.minecraftforge.fml.client.config.IConfigElement;

import java.util.ArrayList;
import java.util.List;

public class ModConfigGui extends GuiConfig
{
    public ModConfigGui (GuiScreen parent) {
        super(parent, getConfigElements(), GardenStuff.MOD_ID, false, false, "Garden Stuff Configuration");
    }

    private static List<IConfigElement> getConfigElements () {
        List<IConfigElement> list = new ArrayList<>();

        //for (ConfigManager.ConfigSection section : StorageDrawers.config.sections)
        //    list.add(new ConfigElement(section.getCategory()));

        return list;
    }
}
