package zornco.bedcraftbeyond.beds.parts.drawer.gui;

import net.minecraft.client.gui.inventory.GuiContainer;
import net.minecraft.entity.player.EntityPlayer;
import net.minecraft.util.math.BlockPos;

public class GuiDrawer extends GuiContainer {

    public GuiDrawer(EntityPlayer player, BlockPos pos) {
        super(new ContainerDrawer(player, pos));
    }

    @Override
    protected void drawGuiContainerBackgroundLayer(float partialTicks, int mouseX, int mouseY) {

    }
}
