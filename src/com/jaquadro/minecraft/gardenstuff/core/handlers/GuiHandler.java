package com.jaquadro.minecraft.gardenstuff.core.handlers;

import com.jaquadro.minecraft.gardenstuff.block.tile.TileBloomeryFurnace;
import com.jaquadro.minecraft.gardenstuff.client.gui.GuiBloomeryFurnace;
import com.jaquadro.minecraft.gardenstuff.inventory.ContainerBloomeryFurnace;
import net.minecraft.entity.player.EntityPlayer;
import net.minecraft.tileentity.TileEntity;
import net.minecraft.util.math.BlockPos;
import net.minecraft.world.World;
import net.minecraftforge.fml.common.network.IGuiHandler;

public class GuiHandler implements IGuiHandler
{
    public static int BLOOMERY_FURNACE_ID = 0;

    @Override
    public Object getServerGuiElement (int ID, EntityPlayer player, World world, int x, int y, int z) {
        TileEntity tile = world.getTileEntity(new BlockPos(x, y, z));
        if (tile instanceof TileBloomeryFurnace)
            return new ContainerBloomeryFurnace(player.inventory, (TileBloomeryFurnace)tile);

        return null;
    }

    @Override
    public Object getClientGuiElement (int ID, EntityPlayer player, World world, int x, int y, int z) {
        TileEntity tile = world.getTileEntity(new BlockPos(x, y, z));
        if (tile instanceof TileBloomeryFurnace)
            return new GuiBloomeryFurnace(player.inventory, (TileBloomeryFurnace)tile);

        return null;
    }
}
