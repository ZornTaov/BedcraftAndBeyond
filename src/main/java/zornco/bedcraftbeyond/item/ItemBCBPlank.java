package zornco.bedcraftbeyond.item;

import net.minecraft.block.Block;
import net.minecraft.client.renderer.color.IItemColor;
import net.minecraft.item.ItemBlock;
import net.minecraft.item.ItemStack;
import zornco.bedcraftbeyond.blocks.BlockBCBPlanks;

public class ItemBCBPlank extends ItemBlock implements IItemColor
{
	public ItemBCBPlank(Block block)
	{
		super(block);
	}

	@Override
	public int getMetadata(int damage)
	{
		return damage;
	}

	@Override
	public String getUnlocalizedName(ItemStack stack)
	{
		return super.getUnlocalizedName() + "." + BlockBCBPlanks.EnumType.byMetadata(stack.getItemDamage()).getUnlocalizedName();
	}

	@Override
	public int getColorFromItemstack(ItemStack stack, int tintIndex) {
		// return this.block.getBlockColor();
		return 0;
	}
}
