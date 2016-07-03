package zornco.bedcraftbeyond.frames.registry.gui.list;

import net.minecraft.client.gui.GuiButton;
import net.minecraft.client.gui.GuiScreen;
import net.minecraft.client.gui.GuiYesNo;
import net.minecraft.client.resources.I18n;
import net.minecraft.entity.player.EntityPlayer;
import net.minecraft.util.text.TextComponentString;
import net.minecraftforge.fml.client.config.GuiMessageDialog;
import org.lwjgl.input.Mouse;
import zornco.bedcraftbeyond.frames.registry.FrameRegistry;
import zornco.bedcraftbeyond.frames.registry.FrameWhitelistEntry;
import zornco.bedcraftbeyond.frames.registry.gui.editor.GuiFrameWhitelistEditor;
import zornco.bedcraftbeyond.frames.registry.messages.MessageOpenWhitelistEditor;
import zornco.bedcraftbeyond.core.BedCraftBeyond;

import java.awt.*;
import java.io.IOException;

public class GuiRegistryList extends GuiScreen {

    private EntityPlayer player;
    private FrameRegistry.EnumFrameType TYPE;

    static Rectangle SCROLL_AREA;

    GuiButton add;
    GuiButton edit;
    GuiButton delete;
    GuiButton reset;

    private RegistryWhitelistComponent whitelist;

    public GuiRegistryList(EntityPlayer player, FrameRegistry.EnumFrameType TYPE) {
        this.player = player;
        this.TYPE = TYPE;
        SCROLL_AREA = new Rectangle(20, 40, 400, 1);
    }

    @Override
    public void initGui() {
        super.initGui();
        SCROLL_AREA.x = (this.width / 2) - (SCROLL_AREA.width / 2);
        SCROLL_AREA.height = height - 120;

        Point scrollBottom = new Point(SCROLL_AREA.x, SCROLL_AREA.y + SCROLL_AREA.height);

        whitelist = new RegistryWhitelistComponent(this, mc, TYPE);

        // ADD DELETE
        // EDIT RESET
        add = new GuiButton(0, scrollBottom.x, scrollBottom.y + 4, 199, 20, I18n.format(BedCraftBeyond.MOD_ID + ".frames.gui.add_entry"));
        edit = new GuiButton(1, scrollBottom.x, scrollBottom.y + 28, 199, 20, I18n.format(BedCraftBeyond.MOD_ID + ".frames.gui.edit_entry"));

        delete = new GuiButton(2, scrollBottom.x + 201, scrollBottom.y + 4, 199, 20, I18n.format(BedCraftBeyond.MOD_ID + ".frames.gui.delete_entry"));
        reset = new GuiButton(3, scrollBottom.x + 201, scrollBottom.y + 28, 199, 20, I18n.format(BedCraftBeyond.MOD_ID + ".frames.gui.reset_entry"));

        buttonList.add(add);
        buttonList.add(edit);
        buttonList.add(delete);
        buttonList.add(reset);
    }

    @Override
    public void drawScreen(int mouseX, int mouseY, float partialTicks) {
        drawBackground(partialTicks, mouseX, mouseY);

        super.drawScreen(mouseX, mouseY, partialTicks);
        whitelist.drawScreen(mouseX, mouseY, partialTicks);

        drawForeground(mouseX, mouseY);
    }

    @Override
    public void handleMouseInput() throws IOException {
        super.handleMouseInput();

        int mouseX = Mouse.getEventX() * this.width / this.mc.displayWidth;
        int mouseY = this.height - Mouse.getEventY() * this.height / this.mc.displayHeight - 1;
        whitelist.handleMouseInput(mouseX, mouseY);
    }

    @Override
    protected void actionPerformed(GuiButton button) throws IOException {
        switch(button.id){
            case 0:
                mc.displayGuiScreen(new GuiMessageDialog(
                    this, I18n.format(BedCraftBeyond.MOD_ID + ".frames.gui.not_yet_implemented"),
                    new TextComponentString("Coming soon!"), I18n.format("gui.back")
                ));

                // ADD
                break;

            case 1:
                // Edit
                MessageOpenWhitelistEditor owe = new MessageOpenWhitelistEditor(whitelist.getSelectedEntry(), TYPE, player.getUniqueID());
                BedCraftBeyond.NETWORK.sendToServer(owe);

                mc.displayGuiScreen(new GuiFrameWhitelistEditor(this, whitelist.getSelectedEntry(), TYPE));
                break;

            case 2:
                // Delete
                mc.displayGuiScreen(new GuiYesNo(
                    (result, id) -> {
                        if(result){
                            FrameWhitelistEntry rl = whitelist.getSelectedEntry();
                            FrameRegistry.getFrameWhitelist(TYPE).removeEntry(rl.getID());
                        }

                        mc.displayGuiScreen(new GuiRegistryList(player, TYPE));
                    },
                    I18n.format(BedCraftBeyond.MOD_ID + ".frames.gui.delete_confirm"), "",
                    I18n.format("gui.yes"),
                    I18n.format("gui.cancel"), 4));
                break;

            case 3:
                // Reset
                mc.displayGuiScreen(new GuiYesNo(
                    (result, id) -> {
                        if(result){
                            FrameWhitelistEntry entry = whitelist.getSelectedEntry();
                            entry.reset();
                        }

                        mc.displayGuiScreen(new GuiRegistryList(player, TYPE));
                    },
                    I18n.format(BedCraftBeyond.MOD_ID + ".frames.gui.reset_confirm"), "",
                    I18n.format("gui.yes"),
                    I18n.format("gui.cancel"), 4));
                break;

            case 4:
                // DELETE and RESET confirmations
                break;

            default:
                whitelist.actionPerformed(button);
                break;
        }
    }

    protected void drawForeground(int mouseX, int mouseY) {
        drawString(fontRendererObj, "Choose an entry to modify:", SCROLL_AREA.x, SCROLL_AREA.y - 10, Color.WHITE.getRGB());
    }

    protected void drawBackground(float partialTicks, int mouseX, int mouseY) {
        drawDefaultBackground();
    }
}
