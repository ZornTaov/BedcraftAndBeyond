package zornco.bedcraftbeyond.storage.gui;

import net.minecraft.client.gui.inventory.GuiContainer;
import net.minecraft.client.renderer.GlStateManager;
import net.minecraft.entity.player.EntityPlayer;
import net.minecraft.inventory.Container;
import net.minecraft.inventory.ItemStackHelper;
import net.minecraft.item.ItemStack;
import zornco.bedcraftbeyond.storage.handling.IStorageHandler;
import zornco.bedcraftbeyond.storage.StoragePart;

import java.awt.*;

public class GuiStorage extends GuiContainer {

    private String id;
    private StoragePart partRef;
    private IStorageHandler handler;
    public GuiStorage(EntityPlayer player, IStorageHandler handler, String storageID, Container container) {
        super(container);

        this.partRef = handler.getSlotPart(storageID);
        Dimension size = partRef.getGuiSize();
        this.xSize = size.width;
        this.ySize = size.height;

        this.id = storageID;
        this.handler = handler;
    }

    @Override
    protected void drawGuiContainerForegroundLayer(int mouseX, int mouseY) {
        ItemStack storageItem = handler.getSlotItemStack(id, false);
        String name = storageItem.getDisplayName();

        GlStateManager.translate(0,0,100);
        fontRendererObj.drawString(name, 8, 6, Color.darkGray.getRGB(), false);
        GlStateManager.translate(0,0,-100);
    }

    @Override
    protected void drawGuiContainerBackgroundLayer(float partialTicks, int mouseX, int mouseY) {
        drawDefaultBackground();
    }
}
