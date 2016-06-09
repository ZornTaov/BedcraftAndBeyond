package zornco.bedcraftbeyond.client.gui.input;


import net.minecraft.client.Minecraft;
import net.minecraft.client.gui.Gui;
import net.minecraft.client.renderer.GlStateManager;
import net.minecraftforge.fml.client.config.GuiSlider;
import org.lwjgl.util.vector.Vector2f;

import java.awt.*;

public class GuiColorSlider extends GuiSlider {

    public GuiColorSlider(int id, Vector2f startPos, Vector2f size, String prefix, ISlider responder){
        this(id, startPos, size, prefix, 0, responder);
    }

    public GuiColorSlider(int id, Vector2f startPos, String prefix, double startVal, ISlider responder){
        this(id, startPos, new Vector2f(100, 20), prefix, startVal, responder);
    }

    public GuiColorSlider(int id, Vector2f startPos, Vector2f size, String prefix, double startVal, ISlider responder){
        super(id, (int) startPos.x, (int) startPos.y, (int) size.getX(), (int) size.getY(), prefix, "", 0, 255, startVal, false, false, responder);
    }

    /**
     * Fired when the mouse button is dragged. Equivalent of MouseListener.mouseDragged(MouseEvent e).
     */
    /**
     * Fired when the mouse button is dragged. Equivalent of MouseListener.mouseDragged(MouseEvent e).
     */
    @Override
    protected void mouseDragged(Minecraft par1Minecraft, int par2, int par3)
    {
        if (this.visible)
        {
            if (this.dragging)
            {
                this.sliderValue = (par2 - (this.xPosition + 4)) / (float)(this.width - 8);
                updateSlider();
            }

            GlStateManager.color(1.0F, 1.0F, 1.0F, 1.0F);

            Vector2f sliderPos = new Vector2f(
                this.xPosition + (int) (this.sliderValue * (float) (this.width - 8)),
                this.yPosition
            );

            Vector2f sliderSize = new Vector2f(8, this.height);

            // Draw slider
            Gui.drawRect((int) sliderPos.getX(), (int) sliderPos.getY(), (int) sliderPos.getX() + (int) sliderSize.x, (int) sliderPos.getY() + (int) sliderSize.getY(), Color.BLACK.getRGB());

            // Draw slider inner
            Gui.drawRect((int) sliderPos.getX() + 2, (int) sliderPos.getY() + 2, (int) sliderPos.getX() + (int) sliderSize.x - 2, (int) sliderPos.getY() + (int) sliderSize.getY() - 2, Color.WHITE.getRGB());
        }
    }
}
