package zornco.bedcraftbeyond.beds.frames.registry.gui;

import net.minecraft.client.gui.FontRenderer;
import net.minecraft.client.gui.GuiUtilRenderComponents;
import net.minecraft.client.gui.inventory.GuiContainer;
import net.minecraft.client.renderer.GlStateManager;
import net.minecraft.entity.player.EntityPlayer;
import net.minecraft.item.ItemStack;
import net.minecraftforge.fml.common.Loader;
import net.minecraftforge.fml.common.ModContainer;
import zornco.bedcraftbeyond.core.BedCraftBeyond;
import zornco.bedcraftbeyond.core.gui.GuiUtils;
import zornco.bedcraftbeyond.core.util.ModUtils;

import java.awt.*;
import java.util.ArrayList;

public class GuiFrameWhitelistEditor extends GuiContainer {

    static final Dimension SIZE = new Dimension(400, 300);

    static final Rectangle INV_AREA = new Rectangle(206, 204, 198, 80);
    static final Rectangle SLOT_AREA = new Rectangle(206, 0, 198, 60);

    static final Rectangle SCROLL_AREA = new Rectangle(0, 0, 200, SIZE.height);

    public GuiFrameWhitelistEditor(EntityPlayer player){
        super(new ContainerFrameWhitelistEditor(player));
        this.xSize = SIZE.width;
        this.ySize = SIZE.height;
    }

    @Override
    protected void drawGuiContainerForegroundLayer(int mouseX, int mouseY) {
        super.drawGuiContainerForegroundLayer(mouseX, mouseY);

        if(inventorySlots.getSlot(0).getHasStack()){
            ItemStack editing = inventorySlots.getSlot(0).getStack();

            ModContainer mod = ModUtils.getModForItem(editing.getItem());
            if(mod == null) return;

            Point meta_begin = new Point(SLOT_AREA.getLocation());
            meta_begin.translate(24, 6);

            fontRendererObj.drawString(editing.getDisplayName(), meta_begin.x, meta_begin.y, Color.RED.getRGB(), false);
            meta_begin.translate(0, 20);

            ArrayList<String> metadata = new ArrayList<>();
            metadata.add(editing.getItem().getRegistryName().toString());
            metadata.add("Meta " + editing.getMetadata());

            fontRendererObj.drawString(fontRendererObj.trimStringToWidth(mod.getName(), 100), meta_begin.x, meta_begin.y, Color.blue.getRGB());
            meta_begin.translate(0, 12);

            for(String s : metadata) {
                fontRendererObj.drawString(s, meta_begin.x, meta_begin.y, Color.black.getRGB(), false);
                meta_begin.translate(0, 10);
            }
        }
    }

    @Override
    protected void drawGuiContainerBackgroundLayer(float partialTicks, int mouseX, int mouseY) {
        GlStateManager.pushMatrix();
        GlStateManager.translate(guiLeft, guiTop, 0);

        GuiUtils.drawRectangle(SLOT_AREA, Color.lightGray);
        GuiUtils.drawRectangle(new Rectangle(SLOT_AREA.x + 3, SLOT_AREA.y + 5, 18, 18), Color.darkGray);

        GuiUtils.drawRectangle(SCROLL_AREA, Color.darkGray);
        GuiUtils.drawRectangle(INV_AREA, Color.BLACK);
        // drawRect(300, 0, 200, 200, Color.lightGray.getRGB());
        GlStateManager.popMatrix();
    }
}
