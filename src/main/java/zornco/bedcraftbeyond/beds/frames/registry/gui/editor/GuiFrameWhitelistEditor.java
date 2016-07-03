package zornco.bedcraftbeyond.beds.frames.registry.gui.editor;

import net.minecraft.client.Minecraft;
import net.minecraft.client.gui.GuiButton;
import net.minecraft.client.gui.GuiScreen;
import net.minecraft.client.gui.inventory.GuiContainer;
import net.minecraft.client.renderer.GlStateManager;
import net.minecraft.inventory.Slot;
import net.minecraft.item.ItemStack;
import net.minecraft.util.text.translation.I18n;
import org.lwjgl.input.Mouse;
import org.w3c.dom.css.Rect;
import zornco.bedcraftbeyond.beds.frames.registry.FrameRegistry;
import zornco.bedcraftbeyond.beds.frames.registry.FrameWhitelistEntry;
import zornco.bedcraftbeyond.core.gui.GuiUtils;

import java.awt.*;
import java.io.IOException;

// TODO: Implement list and make buttons do stuff
public class GuiFrameWhitelistEditor extends GuiContainer {

    static final Dimension SIZE = new Dimension(460, 300);
    static Rectangle SCROLL_BOUNDS;
    static Rectangle SELECT_BOUNDS;
    static Rectangle INVENT_BOUNDS;

    private GuiScreen lastScreen;
    private EditorList list;

    GuiButton blacklist;
    GuiButton whitelist;
    GuiButton clear;
    GuiButton reset;

    private FrameWhitelistEntry entry;
    public GuiFrameWhitelistEditor(GuiScreen last, FrameWhitelistEntry entry, FrameRegistry.EnumFrameType type){
        super(new ContainerFrameWhitelistEditor(type, entry.getID(), Minecraft.getMinecraft().thePlayer));

        this.lastScreen = last;
        this.entry = entry;

        this.xSize = SIZE.width;
        this.ySize = SIZE.height;
    }

    @Override
    public void initGui() {
        super.initGui();

        SCROLL_BOUNDS = new Rectangle(SIZE.width / 2, 0, SIZE.width / 2, SIZE.height - 24);
        SELECT_BOUNDS = new Rectangle(0, 0, (SIZE.width / 2) - 4, 98);

        INVENT_BOUNDS = GuiUtils.getInventoryAreaCentered(new Dimension(SELECT_BOUNDS.width, 120), SELECT_BOUNDS.y + SELECT_BOUNDS.height + 12);

        int buttonSize = (((SIZE.width / 2) - 4) / 2) - 1;

        Rectangle buttonRow1 = new Rectangle(guiLeft, guiTop + SELECT_BOUNDS.height - 20, (SIZE.width / 2) - 4, 20);
        whitelist = new GuiButton(0,buttonRow1.x, buttonRow1.y, buttonSize, buttonRow1.height, "Whitelist");
        blacklist = new GuiButton(1, buttonRow1.x + buttonSize + 2, buttonRow1.y, buttonSize, buttonRow1.height, "Blacklist");

        buttonList.add(whitelist);
        buttonList.add(blacklist);

        Rectangle translatedScrollArea = new Rectangle(SCROLL_BOUNDS);
        translatedScrollArea.translate(guiLeft, guiTop);
        list = new EditorList(entry, translatedScrollArea, width, height);

        Rectangle scrollAreaActions = new Rectangle(guiLeft + SCROLL_BOUNDS.x, guiTop + SCROLL_BOUNDS.y + SCROLL_BOUNDS.height + 4, SCROLL_BOUNDS.width, 20);

        clear = new GuiButton(2, scrollAreaActions.x, scrollAreaActions.y, (scrollAreaActions.width / 2) - 2, scrollAreaActions.height, "Clear");
        reset = new GuiButton(3, scrollAreaActions.x + (scrollAreaActions.width / 2) + 3, scrollAreaActions.y, (scrollAreaActions.width / 2) - 2, scrollAreaActions.height, "Reset");
        buttonList.add(clear);
        buttonList.add(reset);

        GuiButton cancel = new GuiButton(-1, guiLeft, guiTop + SIZE.height - 20, 100, 20, I18n.translateToFallback("gui.cancel"));
        buttonList.add(cancel);
    }

    @Override
    public void handleMouseInput() throws IOException {
        super.handleMouseInput();

        int mouseX = Mouse.getEventX() * this.width / this.mc.displayWidth;
        int mouseY = this.height - Mouse.getEventY() * this.height / this.mc.displayHeight - 1;
        list.handleMouseInput(mouseX, mouseY);
    }

    @Override
    protected void drawGuiContainerForegroundLayer(int mouseX, int mouseY) {
        super.drawGuiContainerForegroundLayer(mouseX, mouseY);

        Point bottomLeft = new Point(0, SIZE.height - 24 - fontRendererObj.FONT_HEIGHT);
        fontRendererObj.drawString("Editing:", bottomLeft.x, bottomLeft.y, Color.lightGray.getRGB(), true);
        bottomLeft.translate(fontRendererObj.getStringWidth("Editing: "), 0);
        fontRendererObj.drawString(entry.getID().toString(), bottomLeft.x, bottomLeft.y, Color.ORANGE.getRGB(), true);

        ItemStack item = inventorySlots.getSlot(0).getStack();
        if(item == null) return;

        Point drawingArea = new Point(SELECT_BOUNDS.getLocation());
        drawingArea.translate(10 + 24, 10);
        fontRendererObj.drawString(item.getDisplayName(), drawingArea.x, drawingArea.y, Color.CYAN.getRGB(), true);

        drawingArea.translate(0, fontRendererObj.FONT_HEIGHT + 4);
        fontRendererObj.drawString("Metadata: " + item.getMetadata(), drawingArea.x, drawingArea.y, Color.lightGray.getRGB(), false);

        drawingArea.translate(0, fontRendererObj.FONT_HEIGHT + 2);
        if(!item.getItem().getRegistryName().equals(entry.getID()))
            fontRendererObj.drawString("INVALID ITEM", drawingArea.x, drawingArea.y, Color.red.getRGB());

        if(entry.isWhitelisted(item.getMetadata()))
            fontRendererObj.drawString("Already whitelisted.", drawingArea.x, drawingArea.y, Color.green.getRGB());


    }

    @Override
    protected void drawGuiContainerBackgroundLayer(float partialTicks, int mouseX, int mouseY) {
        drawDefaultBackground();

        GlStateManager.pushMatrix();
        GlStateManager.translate(guiLeft, guiTop, 0);

        Rectangle selectDataArea = new Rectangle(SELECT_BOUNDS);
        selectDataArea.height -= 22;

        GuiUtils.drawRectangle(selectDataArea, new Color(0,0,0,100));
        GuiUtils.drawRectangle(new Rectangle(10,10,16,16), new Color(255, 255, 255, 50));
        GuiUtils.drawRectangle(SCROLL_BOUNDS, Color.darkGray);

        for(Slot s : inventorySlots.inventorySlots){
            if(!s.getHasStack()) continue;
            ItemStack stack = s.getStack();
            if(!stack.getItem().getRegistryName().equals(this.entry.getID())) {
                Rectangle slotArea = new Rectangle(s.xDisplayPosition, s.yDisplayPosition, 16, 16);

                Color invalidColor = Color.red;
                invalidColor = new Color(invalidColor.getRed(), invalidColor.getGreen(), invalidColor.getBlue(), 100);

                GuiUtils.drawRectangle(slotArea, invalidColor);
            }
        }

        GlStateManager.popMatrix();
    }

    @Override
    protected void actionPerformed(GuiButton button) throws IOException {
        if(!button.enabled) return;

        switch (button.id){
            case -1:
                mc.displayGuiScreen(lastScreen);
                break;

            default:
                break;
        }

        list.actionPerformed(button);
    }

    @Override
    public void drawScreen(int mouseX, int mouseY, float partialTicks) {
        super.drawScreen(mouseX, mouseY, partialTicks);
        list.drawScreen(mouseX, mouseY, partialTicks);
    }
}
