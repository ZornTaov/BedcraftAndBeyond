package zornco.bedcraftbeyond.beds.parts.drawer;

import net.minecraft.entity.player.EntityPlayer;
import net.minecraft.item.ItemStack;
import net.minecraft.util.EnumHand;
import net.minecraft.util.math.BlockPos;
import net.minecraft.world.World;

public interface IDrawerHolder {

    /**
     * Tries to accept a drawer into a given block. Return null if the drawer is accepted.
     *
     * @param stack
     * @param player
     * @param hand
     * @param w
     * @param pos
     * @return
     */
    ItemStack acceptDrawer(ItemStack stack, EntityPlayer player, EnumHand hand, World w, BlockPos pos, boolean simulate);

}
