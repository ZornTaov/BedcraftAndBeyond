package zornco.bedcraftbeyond.storage.gui;

import net.minecraft.client.gui.inventory.GuiContainer;
import net.minecraft.client.renderer.GlStateManager;
import net.minecraft.entity.player.EntityPlayer;
import net.minecraft.inventory.Container;
import net.minecraft.item.ItemStack;
import zornco.bedcraftbeyond.storage.IStorageItem;
import zornco.bedcraftbeyond.storage.handling.IStorageHandler;

import java.awt.*;

public class GuiStorage extends GuiContainer {

    private String id;
    private IStorageItem storageItem;
    private IStorageHandler handler;
    public GuiStorage(EntityPlayer player, IStorageHandler handler, String storageID, Container container) throws Exception {
        super(container);

        this.storageItem = handler.getSlotItem(storageID);
        Dimension size = storageItem.getGuiSize();
        this.xSize = size.width;
        this.ySize = size.height;

        this.id = storageID;
        this.handler = handler;
    }

    @Override
    protected void drawGuiContainerForegroundLayer(int mouseX, int mouseY) {
        ItemStack storageItem = handler.getSlotItemstack(id, false);
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
