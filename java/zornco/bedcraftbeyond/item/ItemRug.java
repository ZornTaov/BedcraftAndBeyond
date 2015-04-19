package zornco.bedcraftbeyond.item;

import java.util.List;

import zornco.bedcraftbeyond.blocks.BlockRug;
import cpw.mods.fml.relauncher.Side;
import cpw.mods.fml.relauncher.SideOnly;
import net.minecraft.block.Block;
import net.minecraft.client.renderer.texture.IIconRegister;
import net.minecraft.creativetab.CreativeTabs;
import net.minecraft.entity.Entity;
import net.minecraft.entity.player.EntityPlayer;
import net.minecraft.init.Blocks;
import net.minecraft.item.Item;
import net.minecraft.item.ItemBlock;
import net.minecraft.item.ItemStack;
import net.minecraft.util.IIcon;
import net.minecraft.util.MathHelper;
import net.minecraft.world.World;

public class ItemRug extends ItemBlock {

	public static final String[] rugColorNames = new String[] {"Black", "Red", "Green", "Brown", "Blue", "Purple", "Cyan", "LightGray", "Gray", "Pink", "Lime", "Yellow", "LightBlue", "Magenta", "Orange", "White"};
	public static final String[] rugIconNames = new String[] {"rug_black", "rug_red", "rug_green", "rug_brown", "rug_blue", "rug_purple", "rug_cyan", "rug_silver", "rug_gray", "rug_pink", "rug_lime", "rug_yellow", "rug_lightBlue", "rug_magenta", "rug_orange", "rug_white"};

	@SideOnly(Side.CLIENT)
	private IIcon[] rugIconList;

	public ItemRug(Block par2) {
		super(par2);
		this.setMaxDamage(0);
		this.setHasSubtypes(true);
	}

	@Override
	@SideOnly(Side.CLIENT)

	/**
	 * Gets an icon index based on an item's damage value
	 */
	public IIcon getIconFromDamage(int par1)
	{
		int j = MathHelper.clamp_int(par1, 0, 15);
		j = 15-j;
		return this.rugIconList[j];
	}

	@Override
	@SideOnly(Side.CLIENT)
	public void registerIcons(IIconRegister par1IconRegister)
	{
		this.rugIconList = new IIcon[rugIconNames.length];

		for (int i = 0; i < rugIconNames.length; ++i)
		{
			this.rugIconList[i] = par1IconRegister.registerIcon("bedcraftbeyond:"+rugIconNames[i]);
		}
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
	public String getUnlocalizedName(ItemStack par1ItemStack)
	{
		return super.getUnlocalizedName() + "." + ItemRug.rugColorNames[BlockRug.getBlockFromDye(par1ItemStack.getItemDamage())];
	}

	@SuppressWarnings({ "rawtypes", "unchecked" })
	@Override
	@SideOnly(Side.CLIENT)

	/**
	 * returns a list of blocks with the same ID, but different meta (eg: wood returns 4 blocks)
	 */
	public void getSubItems(Item par1, CreativeTabs par2CreativeTabs, List par3List)
	{
		for (int var4 = 0; var4 < 16; ++var4)
		{
			par3List.add(new ItemStack(par1, 1, var4));
		}
	}

	/**
	 * Callback for item usage. If the item does something special on right clicking, he will have one of those. Return
	 * True if something happen and false if it don't. This is for ITEMS, not BLOCKS
	 */
	@Override
	public boolean onItemUse(ItemStack par1ItemStack, EntityPlayer par2EntityPlayer, World par3World, int par4, int par5, int par6, int par7, float par8, float par9, float par10)
	{
		Block var11 = par3World.getBlock(par4, par5, par6);

		if (var11 == Blocks.snow)
		{
			par7 = 1;
		}
		else if (var11 != Blocks.vine && var11 != Blocks.tallgrass && var11 != Blocks.deadbush
				&& (var11 == null || !var11.isReplaceable(par3World, par4, par5, par6)))
		{
			if (par7 == 0)
			{
				--par5;
			}

			if (par7 == 1)
			{
				++par5;
			}

			if (par7 == 2)
			{
				--par6;
			}

			if (par7 == 3)
			{
				++par6;
			}

			if (par7 == 4)
			{
				--par4;
			}

			if (par7 == 5)
			{
				++par4;
			}
		}

		if (par1ItemStack.stackSize == 0)
		{
			return false;
		}
		else if (!par2EntityPlayer.canPlayerEdit(par4, par5, par6, par7, par1ItemStack))
		{
			return false;
		}
		else if (par5 == 255 && this.field_150939_a.getMaterial().isSolid())
		{
			return false;
		}
		else if (par3World.canPlaceEntityOnSide(this.field_150939_a, par4, par5, par6, false, par7, par2EntityPlayer, par1ItemStack))
		{
			Block var12 = this.field_150939_a;
			int var13 = this.getMetadata(par1ItemStack.getItemDamage());
			int var14 = this.field_150939_a.onBlockPlaced(par3World, par4, par5, par6, par7, par8, par9, par10, var13);

			if (placeBlockAt(par1ItemStack, par2EntityPlayer, par3World, par4, par5, par6, par7, par8, par9, par10, var14))
			{
				par3World.playSoundEffect(par4 + 0.5F, par5 + 0.5F, par6 + 0.5F, var12.stepSound.func_150496_b(), (var12.stepSound.getVolume() + 1.0F) / 2.0F, var12.stepSound.getPitch() * 0.8F);
				if(!par2EntityPlayer.capabilities.isCreativeMode)
	        		--par1ItemStack.stackSize;
			}

			return true;
		}
		else
		{
			return false;
		}
	}

	@SideOnly(Side.CLIENT)

	/**
	 * Returns true if the given ItemBlock can be placed on the given side of the given block position.
	 */
	public boolean canPlaceItemBlockOnSide(World par1World, int par2, int par3, int par4, int par5, EntityPlayer par6EntityPlayer, ItemStack par7ItemStack)
	{
		Block var8 = par1World.getBlock(par2, par3, par4);

		if (var8 == Blocks.snow)
		{
			par5 = 1;
		}
		else if (var8 != Blocks.vine && var8 != Blocks.tallgrass && var8 != Blocks.deadbush
				&& (var8 == null || !var8.isReplaceable(par1World, par2, par3, par4)))
		{
			if (par5 == 0)
			{
				--par3;
			}

			if (par5 == 1)
			{
				++par3;
			}

			if (par5 == 2)
			{
				--par4;
			}

			if (par5 == 3)
			{
				++par4;
			}

			if (par5 == 4)
			{
				--par2;
			}

			if (par5 == 5)
			{
				++par2;
			}
		}

		return par1World.canPlaceEntityOnSide(this.field_150939_a, par2, par3, par4, false, par5, (Entity)null, par7ItemStack);
	}
	@Override
	public String getUnlocalizedName()
	{
		return this.field_150939_a.getUnlocalizedName();
	}

	@Override
	@SideOnly(Side.CLIENT)

	/**
	 * gets the CreativeTab this item is displayed on
	 */
	public CreativeTabs getCreativeTab()
	{
		return this.field_150939_a.getCreativeTabToDisplayOn();
	}

	/**
	 * Called to actually place the block, after the location is determined
	 * and all permission checks have been made.
	 *
	 * @param stack The item stack that was used to place the block. This can be changed inside the method.
	 * @param player The player who is placing the block. Can be null if the block is not being placed by a player.
	 * @param side The side the player (or machine) right-clicked on.
	 */
	public boolean placeBlockAt(ItemStack stack, EntityPlayer player, World world, int x, int y, int z, int side, float hitX, float hitY, float hitZ, int metadata)
	{
		if (!world.setBlock(x, y, z, this.field_150939_a, metadata, 3))
		{
			return false;
		}

		if (world.getBlock(x, y, z) == this.field_150939_a)
		{
			this.field_150939_a.onBlockPlacedBy(world, x, y, z, player, stack);
			this.field_150939_a.onPostBlockPlaced(world, x, y, z, metadata);
		}

		return true;
	}
}
