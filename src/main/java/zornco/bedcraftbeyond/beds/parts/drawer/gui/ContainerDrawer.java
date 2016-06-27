package zornco.bedcraftbeyond.beds.parts.drawer.gui;

import net.minecraft.entity.player.EntityPlayer;
import net.minecraft.inventory.Container;
import net.minecraft.util.math.BlockPos;

public class ContainerDrawer extends Container {

    public ContainerDrawer(EntityPlayer player, BlockPos pos){

    }

    @Override
    public boolean canInteractWith(EntityPlayer playerIn) {
        return true;
    }
}
