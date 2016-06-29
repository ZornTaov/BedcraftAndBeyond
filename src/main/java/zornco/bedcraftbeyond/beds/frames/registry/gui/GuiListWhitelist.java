package zornco.bedcraftbeyond.beds.frames.registry.gui;

import net.minecraft.client.Minecraft;
import net.minecraft.client.gui.GuiListExtended;
import net.minecraft.client.renderer.GlStateManager;
import net.minecraft.client.renderer.Tessellator;
import net.minecraft.client.renderer.VertexBuffer;
import net.minecraft.client.renderer.vertex.DefaultVertexFormats;
import net.minecraft.util.ResourceLocation;
import net.minecraft.util.math.MathHelper;
import zornco.bedcraftbeyond.beds.frames.registry.FrameException;
import zornco.bedcraftbeyond.beds.frames.registry.FrameRegistry;
import zornco.bedcraftbeyond.beds.frames.registry.FrameWhitelist;

import java.awt.*;
import java.util.ArrayList;

public class GuiListWhitelist extends GuiListExtended {

    private GuiFrameWhitelistEditor editor;
    FrameWhitelist whitelist;
    private ArrayList<FrameWhitelistSection> sections;

    GuiListWhitelist(GuiFrameWhitelistEditor editor, Minecraft mc, FrameRegistry.EnumFrameType type){
        super(mc, editor.SCROLL_AREA.width, editor.SCROLL_AREA.height, editor.SCROLL_AREA.y, editor.SCROLL_AREA.y + editor.SCROLL_AREA.height, 100);
        this.editor = editor;
        this.left = editor.SCROLL_AREA.x + editor.OFFSET.x;
        this.right = editor.SCROLL_AREA.width + editor.SCROLL_AREA.x + editor.OFFSET.x;

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
    public void drawScreen(int mouseXIn, int mouseYIn, float partialTicks) {
        super.drawScreen(mouseXIn, mouseYIn, partialTicks);
        drawScrollbar(mouseXIn, mouseYIn, partialTicks);
    }

    private void drawScrollbar(int mouseXIn, int mouseYIn, float partialTicks){
        if (this.visible)
        {
            this.mouseX = mouseXIn;
            this.mouseY = mouseYIn;

            this.drawBackground();
            int scrollBarLeft = this.right + 10;
            int scrollBarRight = scrollBarLeft + 6;
            this.bindAmountScrolled();

            GlStateManager.disableLighting();
            GlStateManager.disableFog();
            Tessellator tessellator = Tessellator.getInstance();
            VertexBuffer vertexbuffer = tessellator.getBuffer();
            // Forge: background rendering moved into separate method.
            // this.drawContainerBackground(tessellator);

//            // Selection width and height
//            int k = this.left + this.width / 2 - this.getListWidth() / 2 + 2;
//            int l = this.top + 4 - (int)this.amountScrolled;
//            this.drawSelectionBox(k, l, mouseXIn, mouseYIn);

//            if (this.hasListHeader) this.drawListHeader(k, l, tessellator);


            GlStateManager.disableDepth();
            int padding = 4;

            // this.overlayBackground(0, this.top, 255, 255);
            // this.overlayBackground(this.bottom, this.height, 255, 255);
            GlStateManager.enableBlend();
            GlStateManager.tryBlendFuncSeparate(GlStateManager.SourceFactor.SRC_ALPHA, GlStateManager.DestFactor.ONE_MINUS_SRC_ALPHA, GlStateManager.SourceFactor.ZERO, GlStateManager.DestFactor.ONE);
            GlStateManager.disableAlpha();
            GlStateManager.shadeModel(7425);
            GlStateManager.disableTexture2D();

            int maxScroll = this.getMaxScroll();

            // Scrolling available
            if (maxScroll > 0)
            {
                int k1 = (this.bottom - this.top) * (this.bottom - this.top) / this.getContentHeight();
                k1 = MathHelper.clamp_int(k1, 32, this.bottom - this.top - 8);
                int l1 = (int)this.amountScrolled * (this.bottom - this.top - k1) / maxScroll + this.top;

                if (l1 < this.top) l1 = this.top;

                vertexbuffer.begin(7, DefaultVertexFormats.POSITION_TEX_COLOR);
                vertexbuffer.pos((double)scrollBarLeft, (double)this.bottom, 0.0D).tex(0.0D, 1.0D).color(0, 0, 0, 255).endVertex();
                vertexbuffer.pos((double)scrollBarRight, (double)this.bottom, 0.0D).tex(1.0D, 1.0D).color(0, 0, 0, 255).endVertex();
                vertexbuffer.pos((double)scrollBarRight, (double)this.top, 0.0D).tex(1.0D, 0.0D).color(0, 0, 0, 255).endVertex();
                vertexbuffer.pos((double)scrollBarLeft, (double)this.top, 0.0D).tex(0.0D, 0.0D).color(0, 0, 0, 255).endVertex();
                tessellator.draw();
                vertexbuffer.begin(7, DefaultVertexFormats.POSITION_TEX_COLOR);
                vertexbuffer.pos((double)scrollBarLeft, (double)(l1 + k1), 0.0D).tex(0.0D, 1.0D).color(128, 128, 128, 255).endVertex();
                vertexbuffer.pos((double)scrollBarRight, (double)(l1 + k1), 0.0D).tex(1.0D, 1.0D).color(128, 128, 128, 255).endVertex();
                vertexbuffer.pos((double)scrollBarRight, (double)l1, 0.0D).tex(1.0D, 0.0D).color(128, 128, 128, 255).endVertex();
                vertexbuffer.pos((double)scrollBarLeft, (double)l1, 0.0D).tex(0.0D, 0.0D).color(128, 128, 128, 255).endVertex();
                tessellator.draw();
                vertexbuffer.begin(7, DefaultVertexFormats.POSITION_TEX_COLOR);
                vertexbuffer.pos((double)scrollBarLeft, (double)(l1 + k1 - 1), 0.0D).tex(0.0D, 1.0D).color(192, 192, 192, 255).endVertex();
                vertexbuffer.pos((double)(scrollBarRight - 1), (double)(l1 + k1 - 1), 0.0D).tex(1.0D, 1.0D).color(192, 192, 192, 255).endVertex();
                vertexbuffer.pos((double)(scrollBarRight - 1), (double)l1, 0.0D).tex(1.0D, 0.0D).color(192, 192, 192, 255).endVertex();
                vertexbuffer.pos((double)scrollBarLeft, (double)l1, 0.0D).tex(0.0D, 0.0D).color(192, 192, 192, 255).endVertex();
                tessellator.draw();
            }

            this.renderDecorations(mouseXIn, mouseYIn);
            GlStateManager.enableTexture2D();
            GlStateManager.shadeModel(7424);
            GlStateManager.enableAlpha();
            GlStateManager.disableBlend();
        }
    }

    @Override
    protected void drawBackground() {

    }

    @Override
    protected void drawContainerBackground(Tessellator tessellator) {

    }

    @Override
    public IGuiListEntry getListEntry(int index) {
        if(index > getSize() || index < 0) return null;
        return sections.get(index);
    }

    @Override
    protected int getSize() {
        return whitelist.getValidRegistryEntries().size();
    }
}
