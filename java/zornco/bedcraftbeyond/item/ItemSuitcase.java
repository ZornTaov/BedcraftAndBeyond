package zornco.bedcraftbeyond.item;

import net.minecraft.entity.player.EntityPlayer;
import net.minecraft.item.Item;
import net.minecraft.item.ItemStack;
import net.minecraft.util.BlockPos;
import net.minecraft.util.EnumFacing;
import net.minecraft.world.World;
import zornco.bedcraftbeyond.BedCraftBeyond;

public class ItemSuitcase extends Item implements IName
{

	public ItemSuitcase() {
		super();
	}
	@Override
	public boolean onItemUse(ItemStack stack, EntityPlayer playerIn, World worldIn, BlockPos pos, EnumFacing side,
			float hitX, float hitY, float hitZ) {
        if (worldIn.isRemote)
            return true;
        return true;
	}
	@Override
	public ItemStack onItemRightClick(ItemStack itemStackIn, World worldIn, EntityPlayer playerIn) {
        if (worldIn.isRemote)
            return itemStackIn;
        playerIn.openGui(BedCraftBeyond.instance, BedCraftBeyond.proxy.GUI_SUITCASE, worldIn, 0, 0, 0);
        
        return itemStackIn;
	}
	@Override
	public String getName() {
		return "suitcase";
	}

	@Override
	public int getMaxItemUseDuration(ItemStack stack) {
		return 1;
	}
}
