package zornco.bedcraftbeyond.item;

import net.minecraft.block.state.IBlockState;
import net.minecraft.entity.player.EntityPlayer;
import net.minecraft.item.Item;
import net.minecraft.item.ItemStack;
import net.minecraft.tileentity.TileEntity;
import net.minecraft.tileentity.TileEntityChest;
import net.minecraft.util.BlockPos;
import net.minecraft.util.ChatComponentText;
import net.minecraft.util.EnumFacing;
import net.minecraft.world.World;
import net.minecraftforge.fml.common.FMLCommonHandler;
import net.minecraftforge.fml.common.registry.GameRegistry;
import net.minecraftforge.fml.relauncher.Side;
import net.minecraftforge.fml.relauncher.SideOnly;
import zornco.bedcraftbeyond.BedCraftBeyond;
import zornco.bedcraftbeyond.blocks.TileColoredBed;
import zornco.bedcraftbeyond.blocks.TileColoredChestBed;
import zornco.bedcraftbeyond.util.PlankHelper;

public class ItemScissors extends Item implements IName {

	private final String name = "scissors";
	public ItemScissors() {
		GameRegistry.registerItem(this, name);
		setUnlocalizedName(BedCraftBeyond.MOD_ID + "_" + name);
		setCreativeTab(BedCraftBeyond.bedCraftBeyondTab);
		this.maxStackSize = 1;
	}
	public String getName()
	{
		return name;
	}

	/**
	 * Callback for item usage. If the item does something special on right clicking, he will have one of those. Return
	 * True if something happen and false if it don't. This is for ITEMS, not BLOCKS
	 */
	@Override
	public boolean onItemUse(ItemStack stack, EntityPlayer playerIn, World worldIn, BlockPos pos, EnumFacing side, float hitX, float hitY, float hitZ)
	{
		/*BedCraftBeyond.logger.info(""+par4+" "+par5+" "+par6);
		BedCraftBeyond.logger.info(""+(worldIn.getClass().toString()));
		BedCraftBeyond.logger.info(""+worldIn.getBlockId(pos));*/
		IBlockState state = worldIn.getBlockState(pos);
		playerIn.addChatMessage(new ChatComponentText(FMLCommonHandler.instance().getEffectiveSide() + " " + worldIn.getBlockState(pos)));
		BedCraftBeyond.logger.info(""+state.getBlock().getActualState(state, worldIn, pos));
		TileEntity tile = worldIn.getTileEntity(pos);
		if (tile != null)
		{

			if(tile instanceof TileColoredBed)
			{
				TileColoredBed tilebed = (TileColoredBed)tile;
				playerIn.addChatMessage(new ChatComponentText(tilebed.getColorCombo()+" "+tilebed.getPlankType().toString()+" "+tilebed.getPlankTypeNS()));
			}

			if(tile instanceof TileColoredChestBed)
			{
				TileColoredChestBed tilebed = (TileColoredChestBed)tile;
				BedCraftBeyond.logger.info(tilebed.ownerName+"");
				playerIn.addChatMessage(new ChatComponentText(tilebed.ownerName+""));
			}
			if (tile instanceof TileEntityChest) {
				playerIn.addChatMessage(new ChatComponentText(PlankHelper.getPlankColorMap().keySet().toString()+" HELLO"));
			}
		}
		else
		{
			return false;
		}

		return true;

	}


	/*@Override
	@SideOnly(Side.CLIENT)
	public void registerIcons(IIconRegister par1IconRegister) {
		itemIcon = par1IconRegister.registerIcon("bedcraftbeyond:scissors");
	}*/
}
