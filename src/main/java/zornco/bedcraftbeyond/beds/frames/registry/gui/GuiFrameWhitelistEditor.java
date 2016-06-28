package zornco.bedcraftbeyond.beds.frames.registry.gui;

import com.google.common.collect.Range;
import net.minecraft.client.gui.inventory.GuiContainer;
import net.minecraft.client.renderer.GlStateManager;
import net.minecraft.entity.player.EntityPlayer;
import net.minecraft.item.Item;
import net.minecraft.item.ItemStack;
import net.minecraft.util.ResourceLocation;
import net.minecraftforge.fml.common.ModContainer;
import zornco.bedcraftbeyond.beds.frames.registry.FrameException;
import zornco.bedcraftbeyond.beds.frames.registry.FrameRegistry;
import zornco.bedcraftbeyond.beds.frames.registry.FrameWhitelist;
import zornco.bedcraftbeyond.core.gui.GuiUtils;
import zornco.bedcraftbeyond.core.util.ModUtils;

import java.awt.*;
import java.util.ArrayList;
import java.util.Set;

public class GuiFrameWhitelistEditor extends GuiContainer {

    private EntityPlayer player;

    static final Dimension SIZE = new Dimension(400, 300);

    static final Rectangle INV_AREA = new Rectangle(206, 204, 198, 80);
    static final Rectangle SLOT_AREA = new Rectangle(206, 0, 198, 60);

    static final Rectangle SCROLL_AREA = new Rectangle(0, 0, 200, SIZE.height);

    public GuiFrameWhitelistEditor(EntityPlayer player) {
        super(new ContainerFrameWhitelistEditor(player));
        this.player = player;
        this.xSize = SIZE.width;
        this.ySize = SIZE.height;
    }

    @Override
    public void initGui() {
        super.initGui();

    }

    @Override
    protected void drawGuiContainerForegroundLayer(int mouseX, int mouseY) {
        super.drawGuiContainerForegroundLayer(mouseX, mouseY);

        if (inventorySlots.getSlot(0).getHasStack()) {
            ItemStack editing = inventorySlots.getSlot(0).getStack();
            drawItemMetaInformation(editing);
        }

        GlStateManager.pushMatrix();
        GlStateManager.disableLighting();
        GlStateManager.translate(0,0,32);
        this.itemRender.zLevel = 200;
        this.zLevel = 200;
        FrameWhitelist wood = FrameRegistry.getFrameWhitelist(FrameRegistry.EnumFrameType.WOOD);
        Set<ResourceLocation> whitelisted = wood.getValidRegistryEntries();
        Point drawArea = new Point(SCROLL_AREA.getLocation());
        drawArea.translate(6, 6);
        for (ResourceLocation rl : whitelisted) {
            fontRendererObj.drawString(rl.toString(), drawArea.x, drawArea.y, Color.WHITE.getRGB());
            drawArea.translate(0, 10);

            Item i = Item.REGISTRY.getObject(rl);
            if (i == null) continue;

            try {
                for (Range<Integer> metas : wood.getValidMetaForEntry(rl)) {
                    if (metas.lowerEndpoint() == metas.upperEndpoint()) {
                        this.itemRender.renderItemIntoGUI(new ItemStack(i, 1, metas.lowerEndpoint()), drawArea.x, drawArea.y);
                        drawArea.translate(16, 0);
                        if(drawArea.x > SCROLL_AREA.x + SCROLL_AREA.width - 15)
                            drawArea.move(SCROLL_AREA.x + 6, drawArea.y + 16);
                    }

                    for (int lower = metas.lowerEndpoint(); lower < metas.upperEndpoint(); lower++) {
                        this.itemRender.renderItemIntoGUI(new ItemStack(i, 1, lower), drawArea.x, drawArea.y);
                        drawArea.translate(16, 0);
                        if(drawArea.x > SCROLL_AREA.x + SCROLL_AREA.width - 15)
                            drawArea.move(SCROLL_AREA.x + 6, drawArea.y + 16);
                    }
                }

                drawArea.move(SCROLL_AREA.x + 6, drawArea.y + 20);
            } catch (FrameException e) {
                e.printStackTrace();
            }


        }

        this.itemRender.zLevel = 0;
        this.zLevel = 0;
        GlStateManager.popMatrix();
    }

    /**
     * Draws the meta information of the item in the modifying slot.
     *
     * @param stack
     */
    private void drawItemMetaInformation(ItemStack stack) {
        ModContainer mod = ModUtils.getModForItem(stack.getItem());
        if (mod == null) return;

        Point meta_begin = new Point(SLOT_AREA.getLocation());
        meta_begin.translate(24, 6);

        fontRendererObj.drawString(stack.getDisplayName(), meta_begin.x, meta_begin.y, Color.RED.getRGB(), false);
        meta_begin.translate(0, 20);

        ArrayList<String> metadata = new ArrayList<>();
        metadata.add(stack.getItem().getRegistryName().toString());
        metadata.add("Meta " + stack.getMetadata());

        fontRendererObj.drawString(fontRendererObj.trimStringToWidth(mod.getName(), 100), meta_begin.x, meta_begin.y, Color.blue.getRGB());
        meta_begin.translate(0, 12);

        for (String s : metadata) {
            fontRendererObj.drawString(s, meta_begin.x, meta_begin.y, Color.black.getRGB(), false);
            meta_begin.translate(0, 10);
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
