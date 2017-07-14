package com.jaquadro.minecraft.gardenstuff.client.gui;

import com.jaquadro.minecraft.gardenstuff.block.tile.TileBloomeryFurnace;
import com.jaquadro.minecraft.gardenstuff.inventory.ContainerBloomeryFurnace;
import net.minecraft.client.gui.inventory.GuiContainer;
import net.minecraft.client.renderer.GlStateManager;
import net.minecraft.entity.player.InventoryPlayer;
import net.minecraft.inventory.IInventory;
import net.minecraft.util.ResourceLocation;
import net.minecraftforge.fml.relauncher.Side;
import net.minecraftforge.fml.relauncher.SideOnly;

@SideOnly(Side.CLIENT)
public class GuiBloomeryFurnace extends GuiContainer
{
    private static final ResourceLocation FURNACE_GUI_TEXTURES = new ResourceLocation("gardenstuff", "textures/gui/bloomery_furnace.png");

    private final InventoryPlayer playerInventory;
    private final IInventory containerInventory;

    public GuiBloomeryFurnace (InventoryPlayer playerInventory, IInventory containerInventory) {
        super(new ContainerBloomeryFurnace(playerInventory, containerInventory));

        this.playerInventory = playerInventory;
        this.containerInventory = containerInventory;
    }

    @Override
    protected void drawGuiContainerForegroundLayer (int mouseX, int mouseY) {
        String name = containerInventory.getDisplayName().getUnformattedText();
        this.fontRenderer.drawString(name, xSize / 2 - fontRenderer.getStringWidth(name) / 2, 6, 4210752);
        this.fontRenderer.drawString(playerInventory.getDisplayName().getUnformattedText(), 8, ySize - 96 + 2, 4210752);
    }

    @Override
    protected void drawGuiContainerBackgroundLayer (float partialTicks, int mouseX, int mouseY) {
        GlStateManager.color(1, 1, 1, 1);
        mc.getTextureManager().bindTexture(FURNACE_GUI_TEXTURES);

        int halfW = (width - xSize) / 2;
        int halfH = (height - ySize) / 2;
        this.drawTexturedModalRect(halfW, halfH, 0, 0, xSize, ySize);

        if (TileBloomeryFurnace.isBurning(containerInventory)) {
            int burnLeft = getBurnLeftScaled(13);
            drawTexturedModalRect(halfW + 56, halfH + 36 + 12 - burnLeft, 176, 12 - burnLeft, 14, burnLeft + 1);
        }

        int cookProgress = getCookProgressScaled(24);
        this.drawTexturedModalRect(halfW + 79, halfH + 34, 176, 14, cookProgress + 1, 16);
    }

    private int getCookProgressScaled (int pixels) {
        int cookTime = containerInventory.getField(TileBloomeryFurnace.FIELD_COOK_TIME);
        int totalCookTime = containerInventory.getField(TileBloomeryFurnace.FIELD_TOTAL_COOK_TIME);

        if (totalCookTime != 0 && cookTime != 0)
            return cookTime * pixels / totalCookTime;

        return 0;
    }

    private int getBurnLeftScaled (int pixels) {
        int furnaceBurnTime = containerInventory.getField(TileBloomeryFurnace.FIELD_FURNACE_BURN_TIME);
        int currentItemBurnTime = containerInventory.getField(TileBloomeryFurnace.FIELD_CURRENT_ITEM_BURN_TIME);

        if (currentItemBurnTime == 0)
            currentItemBurnTime = 200;

        return furnaceBurnTime * pixels / currentItemBurnTime;
    }
}
