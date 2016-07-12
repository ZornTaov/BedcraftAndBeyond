package zornco.bedcraftbeyond.storage.drawers.standard;

import net.minecraft.client.renderer.GlStateManager;
import net.minecraft.entity.player.EntityPlayer;
import net.minecraft.tileentity.TileEntity;
import net.minecraft.util.ResourceLocation;
import zornco.bedcraftbeyond.core.BedCraftBeyond;
import zornco.bedcraftbeyond.storage.handling.IStorageHandler;
import zornco.bedcraftbeyond.storage.gui.GuiStorage;

public class GuiDrawer extends GuiStorage {

    public GuiDrawer(EntityPlayer player, TileEntity tile, IStorageHandler handler, String id) {
        super(player, handler, id, new ContainerDrawer(player, tile, handler, id));
    }

    @Override
    protected void drawGuiContainerBackgroundLayer(float partialTicks, int mouseX, int mouseY) {
        drawDefaultBackground();
        mc.getTextureManager().bindTexture(new ResourceLocation(BedCraftBeyond.MOD_ID, "textures/gui/bed_drawer.png"));

        GlStateManager.pushMatrix();
        GlStateManager.translate(guiLeft, guiTop, 1);
        drawTexturedModalRect(0,0,0,0,xSize,ySize);
        GlStateManager.popMatrix();
    }
}
