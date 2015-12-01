package zornco.bedcraftbeyond.core;

import net.minecraft.block.Block;
import net.minecraft.entity.player.EntityPlayer;
import net.minecraft.tileentity.TileEntity;
import net.minecraft.world.World;
import zornco.bedcraftbeyond.BedCraftBeyond;
import zornco.bedcraftbeyond.blocks.ContainerColoredChestBed;
import zornco.bedcraftbeyond.blocks.TileColoredChestBed;
import zornco.bedcraftbeyond.util.PlankHelper;
import cpw.mods.fml.common.network.IGuiHandler;

public class CommonProxy implements IGuiHandler {

	public void registerRenderInformation () {
		// Nothing here as this is the server side proxy
	}

	
	@Override
    public Object getClientGuiElement(int ID, EntityPlayer player, World world, int x, int y, int z)
    {
        return null;
    }

    @Override
    public Object getServerGuiElement(int ID, EntityPlayer player, World world, int X, int Y, int Z)
    {
        TileEntity te = world.getTileEntity(X, Y, Z);
        if (te != null && te instanceof TileColoredChestBed)
        {
        	TileColoredChestBed teccb = (TileColoredChestBed) te;
            return new ContainerColoredChestBed(player.inventory, teccb);
        }
        else
        {
            return null;
        }
    }

    public World getClientWorld()
    {
        return null;
    }


	public void compilePlanks() {
		PlankHelper.compilePlanks();
		/*BedCraftBeyond.logger.info("SPAM TIME!");
		for (Object block : Block.blockRegistry.getKeys()) {
			String blockname = block.toString();
			BedCraftBeyond.logger.info(blockname);
		}*/
	}
}
