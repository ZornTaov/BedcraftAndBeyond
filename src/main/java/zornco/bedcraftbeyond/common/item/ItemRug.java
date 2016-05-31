package zornco.bedcraftbeyond.common.item;

import net.minecraft.block.Block;
import net.minecraft.block.state.IBlockState;
import net.minecraft.creativetab.CreativeTabs;
import net.minecraft.entity.player.EntityPlayer;
import net.minecraft.item.*;
import net.minecraft.util.EnumActionResult;
import net.minecraft.util.EnumFacing;
import net.minecraft.util.EnumHand;
import net.minecraft.util.math.BlockPos;
import net.minecraft.world.World;
import net.minecraftforge.fml.relauncher.Side;
import net.minecraftforge.fml.relauncher.SideOnly;
import zornco.bedcraftbeyond.BedCraftBeyond;
import zornco.bedcraftbeyond.common.blocks.BlockRug;

import java.util.List;

public class ItemRug extends ItemBlock {

	public static final String[] rugColorNames = new String[] {"Black", "Red", "Green", "Brown", "Blue", "Purple", "Cyan", "LightGray", "Gray", "Pink", "Lime", "Yellow", "LightBlue", "Magenta", "Orange", "White"};

	public ItemRug(Block par2) {
		super(par2);
		this.setRegistryName(par2.getRegistryName());
		this.setUnlocalizedName(BedCraftBeyond.MOD_ID + ".rug");
		this.setMaxDamage(0);
		this.setHasSubtypes(true);
	}
	
	/**
	 * Returns the metadata of the block which this Item (ItemBlock) can place
	 */
	@Override
	public int getMetadata(int par1)
	{
		return par1;
	}

	@Override
	public String getUnlocalizedName(ItemStack stack) {
		return super.getUnlocalizedName() + "." + ItemRug.rugColorNames[BlockRug.getBlockFromDye(stack.getItemDamage())];
	}

	@Override
	@SideOnly(Side.CLIENT)
	public void getSubItems(Item par1, CreativeTabs tab, List subItems) {
		for (int var4 = 0; var4 < 16; ++var4)
			subItems.add(new ItemStack(par1, 1, var4));
	}
}
