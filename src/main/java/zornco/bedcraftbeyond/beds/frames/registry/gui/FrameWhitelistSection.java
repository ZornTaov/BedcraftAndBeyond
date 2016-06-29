package zornco.bedcraftbeyond.beds.frames.registry.gui;

import com.google.common.collect.Range;
import net.minecraft.client.Minecraft;
import net.minecraft.client.gui.FontRenderer;
import net.minecraft.client.gui.Gui;
import net.minecraft.client.gui.GuiListExtended;
import net.minecraft.client.gui.GuiSlot;
import net.minecraft.util.ResourceLocation;
import zornco.bedcraftbeyond.beds.frames.registry.FrameException;
import zornco.bedcraftbeyond.beds.frames.registry.FrameRegistry;
import zornco.bedcraftbeyond.beds.frames.registry.FrameWhitelist;

import java.awt.*;
import java.util.Set;

public class FrameWhitelistSection implements GuiListExtended.IGuiListEntry {

    static final int NUM_ACROSS = GuiFrameWhitelistEditor.SCROLL_AREA.width / 18;

    private ResourceLocation id;
    private int num_slots;

    FrameWhitelistSection(ResourceLocation id, GuiListWhitelist parent) throws FrameException {
        this.id = id;
        this.num_slots = 0;

        Set<Range<Integer>> metas = parent.whitelist.getValidMetaForEntry(id);
        for(Range<Integer> metaRange : metas){
            if(metaRange.lowerEndpoint() == metaRange.upperEndpoint())
                num_slots++;
            else
                num_slots += metaRange.upperEndpoint() - metaRange.lowerEndpoint();
        }
    }

    @Override
    public void drawEntry(int slotIndex, int x, int y, int listWidth, int slotHeight, int mouseX, int mouseY, boolean isSelected) {
        FontRenderer fr = Minecraft.getMinecraft().fontRendererObj;
        fr.drawString(id.toString(), x, y, Color.WHITE.getRGB(), true);
        fr.drawString("Whitelisted: " + num_slots, x, y + fr.FONT_HEIGHT + 3, Color.lightGray.getRGB(), false);
    }

    @Override
    public boolean mousePressed(int slotIndex, int mouseX, int mouseY, int mouseEvent, int relativeX, int relativeY) {
        return false;
    }

    @Override
    public void mouseReleased(int slotIndex, int x, int y, int mouseEvent, int relativeX, int relativeY) {

    }

    @Override
    public void setSelected(int p_178011_1_, int p_178011_2_, int p_178011_3_) { }
}
