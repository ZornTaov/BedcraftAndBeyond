package zornco.bedcraftbeyond.frames.registry.gui.list;

import net.minecraft.client.Minecraft;
import net.minecraft.client.gui.FontRenderer;
import net.minecraft.client.renderer.Tessellator;
import net.minecraft.util.ResourceLocation;
import net.minecraftforge.fml.client.GuiScrollingList;
import zornco.bedcraftbeyond.frames.registry.FrameException;
import zornco.bedcraftbeyond.frames.registry.FrameRegistry;
import zornco.bedcraftbeyond.frames.registry.FrameWhitelist;
import zornco.bedcraftbeyond.frames.registry.FrameWhitelistEntry;

import java.awt.*;
import java.util.ArrayList;

public class RegistryWhitelistComponent extends GuiScrollingList {

    private GuiRegistryList editor;
    FrameWhitelist whitelist;
    private ArrayList<FrameWhitelistSection> sections;

    RegistryWhitelistComponent(GuiRegistryList editor, Minecraft mc, FrameRegistry.EnumFrameType type){
        super(mc,
            editor.SCROLL_AREA.width, editor.SCROLL_AREA.height,
            editor.SCROLL_AREA.y, editor.SCROLL_AREA.y + editor.SCROLL_AREA.height,
            editor.SCROLL_AREA.x, 60,
            editor.width, editor.height);
        this.editor = editor;
        this.sections = new ArrayList<>();

        whitelist = FrameRegistry.getFrameWhitelist(type);
        for(ResourceLocation entry : whitelist.getValidRegistryEntries()){
            try {
                FrameWhitelistSection section = new FrameWhitelistSection(entry, this);
                this.sections.add(section);
            } catch (FrameException e) {
                e.printStackTrace();
            }
        }
    }

    @Override
    protected void drawBackground() {

    }

    @Override
    protected void drawSlot(int slotIdx, int entryRight, int slotTop, int slotBuffer, Tessellator tess) {
        FontRenderer fr = Minecraft.getMinecraft().fontRendererObj;
        FrameWhitelistSection section = sections.get(slotIdx);
        section.draw(new Rectangle(this.left, slotTop, this.listWidth - 7, this.slotHeight));
    }

    @Override
    protected int getSize() {
        return whitelist.getValidRegistryEntries().size();
    }

    @Override
    protected void elementClicked(int index, boolean doubleClick) {
        this.selectedIndex = index;
    }

    @Override
    protected boolean isSelected(int index) {
        return index == selectedIndex;
    }

    public FrameWhitelistEntry getSelectedEntry() {
        return sections.get(selectedIndex).getEntry();
    }
}
