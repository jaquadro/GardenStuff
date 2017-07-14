package com.jaquadro.minecraft.gardenstuff.block.tile;

import com.jaquadro.minecraft.chameleon.block.ChamTileEntity;
import com.jaquadro.minecraft.gardenstuff.block.BlockCandelabra;
import net.minecraft.nbt.NBTTagCompound;
import net.minecraft.util.EnumFacing;

public class TileCandelabra extends ChamTileEntity
{
    private EnumFacing facing;
    private BlockCandelabra.EnumMount mount;

    public TileCandelabra initialize (EnumFacing facing, BlockCandelabra.EnumMount mount) {
        this.facing = facing;
        this.mount = mount;
        return this;
    }

    public EnumFacing getFacing () {
        return facing;
    }

    public void setFacing (EnumFacing facing) {
        this.facing = facing;
    }

    public BlockCandelabra.EnumMount getMount () {
        return mount;
    }

    public void setMount (BlockCandelabra.EnumMount mount) {
        this.mount = mount;
    }

    @Override
    public boolean dataPacketRequiresRenderUpdate () {
        return true;
    }

    @Override
    protected void readFromFixedNBT (NBTTagCompound tag) {
        super.readFromFixedNBT(tag);

        this.facing = EnumFacing.getFront(tag.getByte("dir"));
        this.mount = BlockCandelabra.EnumMount.byMetadata(tag.getByte("mnt"));
    }

    @Override
    protected NBTTagCompound writeToFixedNBT (NBTTagCompound tag) {
        tag = super.writeToFixedNBT(tag);

        tag.setByte("dir", (byte)facing.getIndex());
        tag.setByte("mnt", (byte)mount.getMetadata());

        return tag;
    }
}
