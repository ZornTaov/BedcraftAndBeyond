package zornco.bedcraftbeyond.storage.gui;

import net.minecraft.client.gui.inventory.GuiContainer;
import net.minecraft.client.renderer.GlStateManager;
import net.minecraft.entity.player.EntityPlayer;
import net.minecraft.item.ItemStack;
import zornco.bedcraftbeyond.parts.IPart;
import zornco.bedcraftbeyond.parts.Part;
import zornco.bedcraftbeyond.storage.IStorageHandler;
import zornco.bedcraftbeyond.storage.StorageHandler;
import zornco.bedcraftbeyond.storage.StoragePart;

import java.awt.*;

public class GuiStorage extends GuiContainer {

    private String id;
    private StoragePart partRef;
    private IStorageHandler handler;

    public GuiStorage(EntityPlayer player, IStorageHandler handler, String storageID) {
        super(new ContainerStorage(player, handler, storageID));

        this.partRef = handler.getSlotPart(storageID);
        Dimension size = partRef.getGuiSize();
        this.xSize = size.width;
        this.ySize = size.height;

        this.id = storageID;
        this.handler = handler;
    }

    @Override
    public void initGui() {
        super.initGui();
    }

    @Override
    protected void drawGuiContainerForegroundLayer(int mouseX, int mouseY) {
        super.drawGuiContainerForegroundLayer(mouseX, mouseY);
    }

    @Override
    protected void drawGuiContainerBackgroundLayer(float partialTicks, int mouseX, int mouseY) {
        drawDefaultBackground();

        mc.getTextureManager().bindTexture(partRef.getGuiBackground());

        GlStateManager.pushMatrix();
        GlStateManager.translate(guiLeft, guiTop, 1);
        drawTexturedModalRect(0,0,0,0,xSize,ySize);

        GlStateManager.popMatrix();
    }
}
