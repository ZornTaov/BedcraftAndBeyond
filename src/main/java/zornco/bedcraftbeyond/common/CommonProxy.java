package zornco.bedcraftbeyond.common;

import net.minecraft.entity.player.EntityPlayer;
import net.minecraft.item.ItemStack;
import net.minecraft.tileentity.TileEntity;
import net.minecraft.util.math.BlockPos;
import net.minecraft.world.World;
import net.minecraftforge.fml.common.network.IGuiHandler;
import net.minecraftforge.fml.common.registry.GameRegistry;
import zornco.bedcraftbeyond.common.blocks.tiles.TileColoredBed;
import zornco.bedcraftbeyond.util.PlankHelper;

public abstract class CommonProxy {

	public void registerModels() { }

	public void init() {
		GameRegistry.registerTileEntity(TileColoredBed.class, "CbedTile");
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
