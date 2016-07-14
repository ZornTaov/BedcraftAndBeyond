package zornco.bedcraftbeyond.storage.drawers.reinforced;

import net.minecraft.client.renderer.GlStateManager;
import net.minecraft.entity.player.EntityPlayer;
import net.minecraft.tileentity.TileEntity;
import net.minecraft.util.ResourceLocation;
import zornco.bedcraftbeyond.core.BedCraftBeyond;
import zornco.bedcraftbeyond.storage.handling.IStorageHandler;
import zornco.bedcraftbeyond.storage.gui.GuiStorage;

public class GuiReinforcedDrawer extends GuiStorage {

    public GuiReinforcedDrawer(EntityPlayer player, TileEntity tile, IStorageHandler handler, String id) {
        super(player, handler, id, new ContainerReinforcedDrawer(player, tile, handler, id));
    }

    @Override
    protected void drawGuiContainerBackgroundLayer(float partialTicks, int mouseX, int mouseY) {
        drawDefaultBackground();
        mc.getTextureManager().bindTexture(new ResourceLocation(BedCraftBeyond.MOD_ID, "textures/gui/reinforced_drawer.png"));

        GlStateManager.pushMatrix();
        GlStateManager.translate(guiLeft, guiTop, 1);
        drawTexturedModalRect(0,0,0,0,xSize,ySize);
        GlStateManager.popMatrix();
    }
}
