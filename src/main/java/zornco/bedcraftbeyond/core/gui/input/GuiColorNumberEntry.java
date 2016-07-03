package zornco.bedcraftbeyond.core.gui.input;

import net.minecraft.client.gui.FontRenderer;
import net.minecraft.client.gui.GuiPageButtonList;
import net.minecraft.client.gui.GuiTextField;
import org.lwjgl.util.Dimension;
import org.lwjgl.util.Point;
import zornco.bedcraftbeyond.dyes.eyedropper.GuiEyedropper;

import java.awt.*;

public class GuiColorNumberEntry extends GuiTextField {

    public enum ColorType {
        RED, GREEN, BLUE
    }

    private ColorType colorType;
    public GuiColorNumberEntry(GuiEyedropper master, int componentId, ColorType colorType, FontRenderer fontRenderer, Point pos, Dimension size) {
        super(componentId, fontRenderer, pos.getX(), pos.getY(), size.getWidth(), size.getHeight());
        this.colorType = colorType;
        this.setGuiResponder(new ColorInputResponder(master));
        this.setValidator(input -> input instanceof String &&
            (input.toString().isEmpty() ||
                (input.toString().matches("\\d+") && Integer.parseInt(input.toString()) >= 0 && Integer.parseInt(input.toString()) < 256)));
    }

    private class ColorInputResponder implements GuiPageButtonList.GuiResponder {

        private GuiEyedropper master;

        public ColorInputResponder(GuiEyedropper master) {
            this.master = master;
        }

        @Override
        public void setEntryValue(int id, boolean value) {
        }

        @Override
        public void setEntryValue(int id, float value) {
        }

        @Override
        public void setEntryValue(int id, String value) {

            Color original = master.getCurrentColor();
            Color newColor = Color.WHITE;
            try {
                int numValue;
                if (getText().isEmpty()) numValue = 0;
                else numValue = Integer.parseInt(getText());
                switch (colorType) {
                    case RED:
                        newColor = new Color(numValue, original.getGreen(), original.getBlue());
                        break;

                    case GREEN:
                        newColor = new Color(original.getRed(), numValue, original.getBlue());
                        break;

                    case BLUE:
                        newColor = new Color(original.getRed(), original.getGreen(), numValue);
                        break;
                }

                master.setCurrentColor(newColor);
            } catch (NumberFormatException nfe) {
                return;
            } catch (Exception e) {
                return;
            }
        }
    }
}
