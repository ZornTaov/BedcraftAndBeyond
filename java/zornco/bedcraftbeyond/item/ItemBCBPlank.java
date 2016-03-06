package zornco.bedcraftbeyond.item;

import net.minecraft.block.Block;
import net.minecraft.item.ItemBlock;
import net.minecraft.item.ItemStack;
import net.minecraftforge.fml.relauncher.Side;
import net.minecraftforge.fml.relauncher.SideOnly;
import zornco.bedcraftbeyond.blocks.BlockBCBPlanks;

public class ItemBCBPlank extends ItemBlock implements IName
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
	@SideOnly(Side.CLIENT)
	public int getColorFromItemStack(ItemStack stack, int renderPass)
	{
		return this.block.getBlockColor();
	}

	@Override
	public String getName() {
		// TODO Auto-generated method stub
		return null;
	}
}
