package zornco.bedcraftbeyond.client.gui;

import net.minecraft.client.gui.inventory.GuiContainer;
import net.minecraft.entity.player.EntityPlayer;
import net.minecraft.util.ResourceLocation;
import zornco.bedcraftbeyond.BedCraftBeyond;
import zornco.bedcraftbeyond.common.blocks.tiles.TileCarpenter;
import zornco.bedcraftbeyond.common.gui.ContainerCarpenter;

public class GuiCarpenter extends GuiContainer {

    public GuiCarpenter(EntityPlayer player, TileCarpenter tile){
        super(new ContainerCarpenter(player, tile));

        this.xSize = 174;
        this.ySize = 173;
    }

    @Override
    protected void drawGuiContainerBackgroundLayer(float partialTicks, int mouseX, int mouseY) {
        mc.getTextureManager().bindTexture(new ResourceLocation(BedCraftBeyond.MOD_ID, "textures/gui/carpenter.png"));

        drawTexturedModalRect(this.guiLeft, this.guiTop, 0, 0, this.xSize, this.ySize);
    }
}
