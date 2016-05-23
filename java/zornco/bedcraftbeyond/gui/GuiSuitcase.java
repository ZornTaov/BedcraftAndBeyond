package zornco.bedcraftbeyond.gui;

import net.minecraft.client.gui.inventory.GuiContainer;
import net.minecraft.client.renderer.GlStateManager;
import net.minecraft.entity.player.EntityPlayer;
import net.minecraft.entity.player.InventoryPlayer;
import net.minecraft.util.BlockPos;
import net.minecraft.util.ResourceLocation;
import net.minecraft.util.StatCollector;
import net.minecraft.world.World;

public class GuiSuitcase extends GuiContainer
{

    private static final ResourceLocation CHEST_GUI_TEXTURE = new ResourceLocation("textures/gui/container/generic_54.png");
	private final InventorySuitcase inventory;

    public GuiSuitcase(ContainerSuitcase containerSuitcase) {

		super(containerSuitcase);
		this.inventory = containerSuitcase.inventory;
	}

	@Override
    protected void drawGuiContainerBackgroundLayer(float partialTicks, int mouseX, int mouseY)
    {
        GlStateManager.color(1, 1, 1, 1);
        this.mc.getTextureManager().bindTexture(CHEST_GUI_TEXTURE);

        this.drawTexturedModalRect(guiLeft, guiTop, 0, 0, xSize, 3 * 18 + 17);
        this.drawTexturedModalRect(guiLeft, guiTop + 3 * 18 + 17, 0, 126, xSize, 96);
    }

    @Override
    protected void drawGuiContainerForegroundLayer(int mouseX, int mouseY)
    {
    	String s = this.inventory.getDisplayName().getUnformattedText();
    	mc.fontRendererObj.drawString(s, this.xSize / 2 - mc.fontRendererObj.getStringWidth(s) / 2, 6, 4210752);
    	mc.fontRendererObj.drawString(StatCollector.translateToLocal("container.inventory"), 8, this.ySize - 96 + 4, 4210752);
    }
}
