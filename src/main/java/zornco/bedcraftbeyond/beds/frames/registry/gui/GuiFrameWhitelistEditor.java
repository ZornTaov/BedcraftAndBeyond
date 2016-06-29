package zornco.bedcraftbeyond.beds.frames.registry.gui;

import com.google.common.collect.Range;
import net.minecraft.client.gui.GuiButton;
import net.minecraft.client.gui.inventory.GuiContainer;
import net.minecraft.client.renderer.GlStateManager;
import net.minecraft.client.renderer.vertex.DefaultVertexFormats;
import net.minecraft.entity.player.EntityPlayer;
import net.minecraft.item.Item;
import net.minecraft.item.ItemStack;
import net.minecraft.util.ResourceLocation;
import net.minecraft.util.math.MathHelper;
import net.minecraftforge.fml.common.ModContainer;
import zornco.bedcraftbeyond.beds.frames.registry.FrameException;
import zornco.bedcraftbeyond.beds.frames.registry.FrameRegistry;
import zornco.bedcraftbeyond.beds.frames.registry.FrameWhitelist;
import zornco.bedcraftbeyond.core.gui.GuiUtils;
import zornco.bedcraftbeyond.core.gui.container.SlotItemViewer;
import zornco.bedcraftbeyond.core.util.ModUtils;

import java.awt.*;
import java.io.IOException;
import java.util.ArrayList;
import java.util.Set;

public class GuiFrameWhitelistEditor extends GuiContainer {

    private EntityPlayer player;

    static final Dimension SIZE = new Dimension(400, 300);
    static Point OFFSET = new Point(0,0);

    static final Rectangle INV_AREA = new Rectangle(206, 204, 198, 80);
    static final Rectangle SLOT_AREA = new Rectangle(206, 0, 198, 60);

    static final Rectangle SCROLL_AREA = new Rectangle(0, 0, 200, SIZE.height);

    private GuiListWhitelist whitelist;

    public GuiFrameWhitelistEditor(EntityPlayer player) {
        super(new ContainerFrameWhitelistEditor(player));
        this.player = player;
        this.xSize = SIZE.width;
        this.ySize = SIZE.height;
    }

    @Override
    public void initGui() {
        super.initGui();
        OFFSET = new Point(guiLeft, guiTop);
        whitelist = new GuiListWhitelist(this, mc, FrameRegistry.EnumFrameType.WOOD);
    }

    @Override
    public void drawScreen(int mouseX, int mouseY, float partialTicks) {
        super.drawScreen(mouseX, mouseY, partialTicks);
        whitelist.drawScreen(mouseX, mouseY, partialTicks);
    }

    @Override
    public void handleMouseInput() throws IOException {
        super.handleMouseInput();
        whitelist.handleMouseInput();
    }

    @Override
    protected void actionPerformed(GuiButton button) throws IOException {
        super.actionPerformed(button);
        whitelist.actionPerformed(button);
    }

    @Override
    protected void mouseClicked(int mouseX, int mouseY, int mouseButton) throws IOException {
        super.mouseClicked(mouseX, mouseY, mouseButton);
        whitelist.mouseClicked(mouseX, mouseY, mouseButton);
    }

    @Override
    protected void mouseReleased(int mouseX, int mouseY, int state) {
        super.mouseReleased(mouseX, mouseY, state);
        whitelist.mouseReleased(mouseX, mouseY, state);
    }

    @Override
    protected void drawGuiContainerForegroundLayer(int mouseX, int mouseY) {
        super.drawGuiContainerForegroundLayer(mouseX, mouseY);

        if (inventorySlots.getSlot(0).getHasStack()) {
            ItemStack editing = inventorySlots.getSlot(0).getStack();
            drawItemMetaInformation(editing);
        }

//        FrameWhitelist wood = FrameRegistry.getFrameWhitelist(FrameRegistry.EnumFrameType.WOOD);
//        Set<ResourceLocation> whitelisted = wood.getValidRegistryEntries();
//
//        Point drawArea = new Point(GuiFrameWhitelistEditor.SCROLL_AREA.getLocation());
//        Rectangle scrollArea = GuiFrameWhitelistEditor.SCROLL_AREA;
//
//        drawArea.translate(6, 6);
//        for (ResourceLocation rl : whitelisted) {
//            drawArea.translate(0, 10);
//
//            Item i = Item.REGISTRY.getObject(rl);
//            if (i == null) continue;
//
//            try {
//                for (Range<Integer> metas : wood.getValidMetaForEntry(rl)) {
//                    if (metas.lowerEndpoint() == metas.upperEndpoint()) {
//                        SlotItemViewer slot = new SlotItemViewer(drawArea.x, drawArea.y);
//                        slot.setStack(new ItemStack(i, 1, metas.lowerEndpoint()));
//                        addSlotToContainer(slot);
//
//                        drawArea.translate(18, 0);
//                        if(drawArea.x > scrollArea.x + scrollArea.width - 17)
//                            drawArea.move(scrollArea.x + 6, drawArea.y + 18);
//                    }
//
//                    for (int meta = metas.lowerEndpoint(); meta < metas.upperEndpoint(); meta++) {
//                        SlotItemViewer slot = new SlotItemViewer(drawArea.x, drawArea.y);
//                        slot.setStack(new ItemStack(i, 1, meta));
//                        addSlotToContainer(slot);
//
//                        drawArea.translate(18, 0);
//                        if(drawArea.x > scrollArea.x + scrollArea.width - 17)
//                            drawArea.move(scrollArea.x + 6, drawArea.y + 18);
//                    }
//                }
//
//                drawArea.move(scrollArea.x + 6, drawArea.y + 20);
//            } catch (FrameException e) {
//                e.printStackTrace();
//            }
//
//
//        }
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
