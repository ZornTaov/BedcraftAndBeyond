package zornco.bedcraftbeyond.client.gui;

import net.minecraft.client.gui.GuiTextField;
import net.minecraft.client.gui.inventory.GuiContainer;
import net.minecraft.entity.player.EntityPlayer;
import net.minecraft.item.ItemStack;
import net.minecraft.nbt.NBTTagCompound;
import net.minecraft.util.EnumHand;
import net.minecraftforge.fml.client.config.GuiSlider;
import org.lwjgl.util.vector.Vector2f;
import zornco.bedcraftbeyond.BedCraftBeyond;
import zornco.bedcraftbeyond.common.gui.ContainerEyedropper;
import zornco.bedcraftbeyond.common.item.ItemEyedropper;
import zornco.bedcraftbeyond.network.MessageEyedropperUpdate;
import zornco.bedcraftbeyond.util.ColorHelper;

import java.awt.*;
import java.io.IOException;

public class GuiEyedropper extends GuiContainer {

    private EntityPlayer player;
    private EnumHand hand;
    private ItemStack eyedropper;

    private ColorResponder red;
    private ColorResponder green;
    private ColorResponder blue;

    private GuiTextField hexBox;

    public GuiEyedropper(EntityPlayer player, EnumHand hand, ItemStack eyedropper){
        super(new ContainerEyedropper(player, eyedropper));
        this.player = player;
        this.eyedropper = eyedropper;
        this.hand = hand;

        this.width = 400;
        this.height = 300;

        this.red = new ColorResponder(this, "r");
        this.green = new ColorResponder(this, "g");
        this.blue = new ColorResponder(this, "b");
    }

    @Override
    protected void drawGuiContainerBackgroundLayer(float partialTicks, int mouseX, int mouseY) {
        this.drawDefaultBackground();

        Vector2f lineStart = new Vector2f(guiLeft + 60 + 10, guiTop + 10);

        Color c = ItemEyedropper.getCurrentColor(this.eyedropper);
        int previewSize = 45;
        drawRect(guiLeft + 10, guiTop + 10, guiLeft + 10 + previewSize, guiTop + 10 + previewSize, c.getRGB());
    }

    @Override
    protected void drawGuiContainerForegroundLayer(int mouseX, int mouseY) {
        super.drawGuiContainerForegroundLayer(mouseX, mouseY);
    }

    @Override
    public void onGuiClosed() {
        super.onGuiClosed();
        MessageEyedropperUpdate meu = new MessageEyedropperUpdate(player, hand, ItemEyedropper.getCurrentColor(this.eyedropper).getRGB());
        BedCraftBeyond.NETWORK.sendToServer(meu);
    }

    @Override
    public void initGui() {
        super.initGui();
        Color c = ItemEyedropper.getCurrentColor(this.eyedropper);
        Vector2f startPos = new Vector2f(this.guiLeft + 65, this.guiTop + 10);

        buttonList.add(new GuiColorSlider(1, startPos, "Red: ", c.getRed(), red));
        startPos.y += 12;
        buttonList.add(new GuiColorSlider(2, startPos, "Green: ", c.getGreen(), green));
        startPos.y += 12;
        buttonList.add(new GuiColorSlider(3, startPos, "Blue: ", c.getBlue(), blue));

        this.hexBox = new GuiTextField(4, fontRendererObj, guiLeft + 10, guiTop + 70, 45, 20);
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
            ItemStack ed = master.eyedropper;
            Color original = ColorHelper.getColorFromStack(ed);
            Color newColor = new Color(0,0,0);
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

            NBTTagCompound newColorTag = ColorHelper.getTagForColor(newColor);
            if(!ed.hasTagCompound()) ed.setTagCompound(new NBTTagCompound());
            ed.getTagCompound().setTag("color", newColorTag);
        }
    }
}
