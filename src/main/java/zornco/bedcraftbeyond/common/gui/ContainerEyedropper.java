package zornco.bedcraftbeyond.common.gui;

import net.minecraft.entity.player.EntityPlayer;
import net.minecraft.inventory.Container;
import net.minecraft.item.ItemStack;

public class ContainerEyedropper extends Container {

    public ContainerEyedropper(EntityPlayer player, ItemStack stack){

    }

    @Override
    public boolean canInteractWith(EntityPlayer playerIn) {
        return true;
    }
}
