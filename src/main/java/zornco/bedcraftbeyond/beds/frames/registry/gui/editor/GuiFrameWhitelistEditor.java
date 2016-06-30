package zornco.bedcraftbeyond.beds.frames.registry.gui.editor;

import net.minecraft.client.gui.inventory.GuiContainer;
import net.minecraft.entity.Entity;
import net.minecraft.entity.player.EntityPlayer;
import zornco.bedcraftbeyond.beds.frames.registry.FrameRegistry;

public class GuiFrameWhitelistEditor extends GuiContainer {

    public GuiFrameWhitelistEditor(EntityPlayer player, FrameRegistry.EnumFrameType type){
        super(new ContainerFrameWhitelistEditor(type, player));
    }

    @Override
    protected void drawGuiContainerForegroundLayer(int mouseX, int mouseY) {
        super.drawGuiContainerForegroundLayer(mouseX, mouseY);
    }

    @Override
    protected void drawGuiContainerBackgroundLayer(float partialTicks, int mouseX, int mouseY) {

    }
}
