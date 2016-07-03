package zornco.bedcraftbeyond.core.gui.container;

import net.minecraft.entity.player.EntityPlayer;
import net.minecraft.inventory.Container;

public class ContainerBlank extends Container {

    public ContainerBlank(){ }

    @Override
    public boolean canInteractWith(EntityPlayer playerIn) {
        return true;
    }
}
