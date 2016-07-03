package zornco.bedcraftbeyond.frames.registry.gui.list;

import com.google.common.collect.Range;
import com.google.common.collect.RangeSet;
import net.minecraft.client.Minecraft;
import net.minecraft.client.gui.FontRenderer;
import net.minecraft.client.renderer.RenderItem;
import net.minecraft.item.Item;
import net.minecraft.item.ItemStack;
import net.minecraft.util.ResourceLocation;
import zornco.bedcraftbeyond.frames.registry.FrameException;
import zornco.bedcraftbeyond.frames.registry.FrameWhitelist;
import zornco.bedcraftbeyond.frames.registry.FrameWhitelistEntry;

import java.awt.*;

public class FrameWhitelistSection {

    static final int NUM_ACROSS = GuiRegistryList.SCROLL_AREA.width / 18;

    private FrameWhitelist whitelist;
    private FrameWhitelistEntry entry;
    private int num_slots;

    FrameWhitelistSection(ResourceLocation id, RegistryWhitelistComponent parent) throws FrameException {
        this.entry = parent.whitelist.getEntry(id);
        this.num_slots = 0;

        this.whitelist = parent.whitelist;
        RangeSet<Integer> metas = whitelist.getEntry(id).getValidMeta();
        for (Range<Integer> metaRange : metas.asRanges()) {
            if (metaRange.lowerEndpoint() == metaRange.upperEndpoint())
                num_slots++;
            else
                num_slots += metaRange.upperEndpoint() - metaRange.lowerEndpoint() + 1;
        }
    }

    public FrameWhitelistEntry getEntry(){ return this.entry; }

    public void draw(Rectangle slotArea) {
        FontRenderer fr = Minecraft.getMinecraft().fontRendererObj;
        Point drawPointer = new Point(slotArea.getLocation());
        drawPointer.translate(4, 4);
        fr.drawString(entry.getID().toString(), drawPointer.x, drawPointer.y, Color.CYAN.getRGB(), true);
        drawPointer.translate(0, fr.FONT_HEIGHT + 4);
        fr.drawString("Whitelisted: " + num_slots, drawPointer.x, drawPointer.y, Color.lightGray.getRGB(), false);

        drawPointer.translate(0, fr.FONT_HEIGHT + 4);
        Item i = Item.REGISTRY.getObject(entry.getID());
        RenderItem ri = Minecraft.getMinecraft().getRenderItem();

        if (i == null) return;
        try {
            for (Range<Integer> metaRange : whitelist.getEntry(entry.getID()).getValidMeta().asRanges()) {
                if (metaRange.lowerEndpoint() == metaRange.upperEndpoint()) {
                    ItemStack itemMeta = new ItemStack(i, 1, metaRange.lowerEndpoint());
                    ri.renderItemAndEffectIntoGUI(itemMeta, drawPointer.x, drawPointer.y);

                    drawPointer.translate(18, 0);
                    if (drawPointer.x > slotArea.x + slotArea.width - 17)
                        drawPointer.move(slotArea.x, slotArea.y + 18);

                    continue;
                }

                for (int meta = metaRange.lowerEndpoint(); meta < metaRange.upperEndpoint() + 1; meta++) {
                    ItemStack itemMeta = new ItemStack(i, 1, meta);
                    ri.renderItemAndEffectIntoGUI(itemMeta, drawPointer.x, drawPointer.y);

                    drawPointer.translate(18, 0);
                    if (drawPointer.x > slotArea.x + slotArea.width - 17)
                        drawPointer.move(slotArea.x, slotArea.y + 18);
                }
            }
        } catch (FrameException e) {
            e.printStackTrace();
        }
    }
}
