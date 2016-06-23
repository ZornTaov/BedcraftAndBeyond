package zornco.bedcraftbeyond.core.gui;

import net.minecraft.client.gui.GuiScreen;
import org.lwjgl.util.Dimension;
import org.lwjgl.util.Point;

public abstract class GuiSized extends GuiScreen {

    protected Point topLeft;
    protected Dimension guiSize;

    @Override
    public void initGui() {
        this.topLeft = new Point((this.width - this.guiSize.getWidth()) / 2,
            (this.height - this.guiSize.getHeight()) / 2);
    }

    @Override
    public void drawScreen(int mouseX, int mouseY, float partialTicks) {
        super.drawScreen(mouseX, mouseY, partialTicks);
        drawSizedBackground();
        drawSizedForeground();
    }

    public abstract void drawSizedBackground();
    public abstract void drawSizedForeground();
}
