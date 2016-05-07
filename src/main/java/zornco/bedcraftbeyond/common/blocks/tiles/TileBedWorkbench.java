package zornco.bedcraftbeyond.common.blocks.tiles;

import net.minecraft.tileentity.TileEntity;
import net.minecraftforge.items.ItemStackHandler;

public class TileBedWorkbench extends TileEntity {

   public ItemStackHandler craftingInventory;

   public TileBedWorkbench(){
      craftingInventory = new ItemStackHandler(9);
   }
}
