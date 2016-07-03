package zornco.bedcraftbeyond.frames.registry.gui.editor;

import com.google.common.collect.Range;
import net.minecraft.client.Minecraft;
import net.minecraft.client.gui.FontRenderer;
import net.minecraft.client.renderer.RenderItem;
import net.minecraft.client.renderer.Tessellator;
import net.minecraft.item.Item;
import net.minecraft.item.ItemStack;
import net.minecraftforge.fml.client.GuiScrollingList;
import zornco.bedcraftbeyond.frames.registry.FrameWhitelistEntry;

import java.awt.*;
import java.util.ArrayList;

public class EditorList extends GuiScrollingList {

    private FrameWhitelistEntry entry;
    private ArrayList<Integer> entries;
    public EditorList(FrameWhitelistEntry entry, Rectangle bounds, int screenWidth, int screenHeight) {
        super(Minecraft.getMinecraft(), bounds.width, bounds.height, bounds.y, bounds.y + bounds.height, bounds.x, 36, screenWidth, screenHeight);
        this.entry = entry;
        entries = new ArrayList<>();
        for(Range<Integer> meta : entry.getValidMeta().asRanges()){
            if(meta.lowerEndpoint() == meta.upperEndpoint()){
                entries.add(meta.lowerEndpoint());
                continue;
            }

            for(int metaV = meta.lowerEndpoint(); metaV < meta.upperEndpoint() + 1; metaV++){
                entries.add(metaV);
            }
        }
    }

    @Override
    protected int getSize() {
        return entry.getNumberEntries();
    }

    @Override
    protected void elementClicked(int index, boolean doubleClick) {

    }

    @Override
    protected boolean isSelected(int index) {
        return false;
    }

    @Override
    protected void drawBackground() {

    }

    @Override
    protected void drawSlot(int slotIdx, int entryRight, int slotTop, int slotBuffer, Tessellator tess) {
        RenderItem ri = Minecraft.getMinecraft().getRenderItem();

        Integer i = entries.get(slotIdx);
        ItemStack stack = new ItemStack(Item.REGISTRY.getObject(entry.getID()), 1, i.intValue());
        ri.renderItemAndEffectIntoGUI(stack, this.left + 10, slotTop + 2);

        FontRenderer fr = Minecraft.getMinecraft().fontRendererObj;
        fr.drawString(stack.getDisplayName(), this.left + 10 + 24, slotTop + 5, Color.WHITE.getRGB(), true);
        fr.drawString("Meta " + stack.getMetadata(), this.left + 10 + 24, slotTop + 5 + fr.FONT_HEIGHT + 2, Color.lightGray.getRGB(), false);
    }
}
