package zornco.bedcraftbeyond.common;

import net.minecraft.item.ItemStack;
import net.minecraft.world.World;
import net.minecraftforge.fml.common.registry.GameRegistry;
import zornco.bedcraftbeyond.common.blocks.tiles.TileBedWorkbench;
import zornco.bedcraftbeyond.common.blocks.tiles.TileWoodenBed;
import zornco.bedcraftbeyond.util.PlankHelper;

public abstract class CommonProxy {

	public void registerModels() { }

	public void init() {
		GameRegistry.registerTileEntity(TileWoodenBed.class, "wooden_bed");
		GameRegistry.registerTileEntity(TileBedWorkbench.class, "bed_workbench");
	}

	public World getClientWorld()
	{
		return null;
	}

	public int getAverageBlockColour(ItemStack stack2) {
		return -1;
	}

	public abstract void compileFrames();

	public void compilePlanks() {
		PlankHelper.compilePlanks();
		PlankHelper.compilePlanksClient();
	}
}
