package zornco.bedcraftbeyond.dyes.eyedropper;

import net.minecraft.client.gui.GuiPageButtonList;
import net.minecraft.client.gui.GuiTextField;
import net.minecraft.entity.player.EntityPlayer;
import net.minecraft.item.ItemStack;
import net.minecraft.nbt.NBTTagCompound;
import net.minecraft.util.EnumHand;
import net.minecraftforge.fml.client.config.GuiSlider;
import org.lwjgl.util.Dimension;
import org.lwjgl.util.Point;
import org.lwjgl.util.vector.Vector2f;
import zornco.bedcraftbeyond.core.BedCraftBeyond;
import zornco.bedcraftbeyond.core.gui.GuiSized;
import zornco.bedcraftbeyond.core.gui.input.GuiColorNumberEntry;
import zornco.bedcraftbeyond.core.gui.input.GuiColorSlider;
import zornco.bedcraftbeyond.core.util.ColorHelper;
import zornco.bedcraftbeyond.dyes.IColoredItem;

import java.awt.Color;
import java.io.IOException;

public class GuiEyedropper extends GuiSized {

    private EntityPlayer player;
    private EnumHand hand;
    private ItemStack eyedropper;

    private GuiColorSlider sliderRed;
    private GuiColorSlider sliderGreen;
    private GuiColorSlider sliderBlue;

    private ColorResponder red;
    private ColorResponder green;
    private ColorResponder blue;

    private ColorInputResponder colorBoxResponder;

    private GuiTextField hexBox;
    private GuiTextField redBox;
    private GuiTextField greenBox;
    private GuiTextField blueBox;

    public GuiEyedropper(EntityPlayer player, EnumHand hand, ItemStack eyedropper){
        this.player = player;
        this.eyedropper = eyedropper;
        this.hand = hand;

        this.red = new ColorResponder(this, "r");
        this.green = new ColorResponder(this, "g");
        this.blue = new ColorResponder(this, "b");

        this.guiSize = new Dimension(360, 150);

        this.colorBoxResponder = new ColorInputResponder(this);
    }

    @Override
    public void onGuiClosed() {
        super.onGuiClosed();
        MessageEyedropperUpdate meu = new MessageEyedropperUpdate(player, hand, ((IColoredItem) this.eyedropper.getItem()).getColorFromStack(eyedropper).getRGB());
        BedCraftBeyond.NETWORK.sendToServer(meu);
    }

    @Override
    public void initGui() {
        super.initGui();

        Color c = getCurrentColor();
        Vector2f sliderPos = new Vector2f(this.topLeft.getX(), this.topLeft.getY() + 35);
        Vector2f sliderSize = new Vector2f(280, 20);
        Dimension textSize = new Dimension(this.guiSize.getWidth() - 5 - (int) sliderSize.getX(), 18);

        this.sliderRed = new GuiColorSlider(1, sliderPos, sliderSize, "Red: ", c.getRed(), red);
        buttonList.add(sliderRed);
        this.redBox = new GuiColorNumberEntry(this, 5, GuiColorNumberEntry.ColorType.RED, fontRendererObj,
            new Point(topLeft.getX() + (int) sliderSize.getX() + 5, (int) sliderPos.getY() + 1), textSize);
        this.redBox.setTextColor(Color.RED.getRGB());
        sliderPos.y += 22;

        this.sliderGreen = new GuiColorSlider(2, sliderPos, sliderSize, "Green: ", c.getGreen(), green);
        buttonList.add(sliderGreen);
        this.greenBox = new GuiColorNumberEntry(this, 6, GuiColorNumberEntry.ColorType.GREEN, fontRendererObj,
            new Point(topLeft.getX() + (int) sliderSize.getX() + 5, (int) sliderPos.getY() + 1), textSize);
        this.greenBox.setTextColor(Color.GREEN.getRGB());
        sliderPos.y += 22;

        this.sliderBlue = new GuiColorSlider(3, sliderPos, sliderSize, "Blue: ", c.getBlue(), blue);
        this.blueBox = new GuiColorNumberEntry(this, 7, GuiColorNumberEntry.ColorType.BLUE, fontRendererObj,
            new Point(topLeft.getX() + (int) sliderSize.getX() + 5, (int) sliderPos.getY() + 1), textSize);
        this.blueBox.setTextColor(Color.BLUE.getRGB());
        buttonList.add(sliderBlue);

        this.hexBox = new GuiTextField(4, fontRendererObj, topLeft.getX() + 10, topLeft.getY() + 5, 60, 20);
        this.hexBox.setText(ColorHelper.getHexFromColor(c));
        this.hexBox.setMaxStringLength(6);
        this.hexBox.setGuiResponder(colorBoxResponder);

        this.setCurrentColor(getCurrentColor());
    }

    @Override
    protected void keyTyped(char typedChar, int keyCode) throws IOException {
        if(keyCode == 1) this.mc.displayGuiScreen(null);
        hexBox.textboxKeyTyped(typedChar, keyCode);
        redBox.textboxKeyTyped(typedChar, keyCode);
        greenBox.textboxKeyTyped(typedChar, keyCode);
        blueBox.textboxKeyTyped(typedChar, keyCode);
    }

    @Override
    protected void mouseClicked(int mouseX, int mouseY, int mouseButton) throws IOException {
        super.mouseClicked(mouseX, mouseY, mouseButton);
        hexBox.mouseClicked(mouseX, mouseY, mouseButton);
        redBox.mouseClicked(mouseX, mouseY, mouseButton);
        greenBox.mouseClicked(mouseX, mouseY, mouseButton);
        blueBox.mouseClicked(mouseX, mouseY, mouseButton);
    }

    @Override
    public void drawSizedBackground() {
        Color c = getCurrentColor();
        drawRect(this.topLeft.getX(),
            this.topLeft.getY(),
            this.topLeft.getX() + this.guiSize.getWidth(),
            this.topLeft.getY() + 30, c.getRGB());
    }

    @Override
    public void drawSizedForeground() {
        hexBox.drawTextBox();
        redBox.drawTextBox();
        greenBox.drawTextBox();
        blueBox.drawTextBox();
    }

    public Color getCurrentColor(){
        ItemStack ed = eyedropper;
        return ((IColoredItem) ed.getItem()).getColorFromStack(ed);
    }

    public void setCurrentColor(Color c){
        NBTTagCompound newColorTag = ColorHelper.getTagForColor(c);
        if(!eyedropper.hasTagCompound()) eyedropper.setTagCompound(new NBTTagCompound());
        eyedropper.getTagCompound().setTag("color", newColorTag);

        if(hexBox.getText().length() == 6)
            hexBox.setText(ColorHelper.getHexFromColor(c));

        sliderRed.setValue(c.getRed());
        sliderGreen.setValue(c.getGreen());
        sliderBlue.setValue(c.getBlue());

        redBox.setText("" + c.getRed());
        greenBox.setText("" + c.getGreen());
        blueBox.setText("" + c.getBlue());
    }

    private class ColorResponder implements GuiSlider.ISlider {

        private GuiEyedropper master;
        private String key;
        public ColorResponder(GuiEyedropper master, String key){
            this.master = master;
            this.key = key;
        }

        @Override
        public void onChangeSliderValue(GuiSlider slider) {
            Color original = master.getCurrentColor();
            Color newColor = Color.WHITE;

            switch (key){
                case "r":
                    newColor = new Color(slider.getValueInt(), original.getGreen(), original.getBlue());
                    break;
                case "g":
                    newColor = new Color(original.getRed(), slider.getValueInt(), original.getBlue());
                    break;
                case "b":
                    newColor = new Color(original.getRed(), original.getGreen(), slider.getValueInt());
                    break;
            }

            master.setCurrentColor(newColor);
        }
    }

    /**
     * Used by the hex entry box to set the color manually.
     */
    private class ColorInputResponder implements GuiPageButtonList.GuiResponder {

        private GuiEyedropper master;
        public ColorInputResponder(GuiEyedropper master){
            this.master = master;
        }

        @Override
        public void setEntryValue(int id, boolean value) { }

        @Override
        public void setEntryValue(int id, float value) { }

        @Override
        public void setEntryValue(int id, String value) {
            // If the hex box
            boolean valid = value.length() == 6 || value.length() == 3;
            if(valid){
                master.hexBox.setTextColor(Color.WHITE.getRGB());
                try {
                    if(value.length() == 3)
                        value = String.format("%1$s%1$s%2$s%2$s%3$s%3$s",
                            value.substring(0,1), value.substring(1,2), value.substring(2,3));

                    Color c = Color.decode("#" + value);
                    master.setCurrentColor(c);
                }

                catch (Exception nfe){
                    BedCraftBeyond.LOGGER.error(nfe);
                }
            } else {
                master.hexBox.setTextColor(Color.RED.getRGB());
            }
        }
    }
}
