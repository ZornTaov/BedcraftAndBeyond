package zornco.bedcraftbeyond.blocks;

import java.util.ArrayList;
import java.util.Random;

import net.minecraft.block.Block;
import net.minecraft.block.BlockBed;
import net.minecraft.block.ITileEntityProvider;
import net.minecraft.client.renderer.texture.IIconRegister;
import net.minecraft.entity.EntityLivingBase;
import net.minecraft.entity.item.EntityItem;
import net.minecraft.entity.player.EntityPlayer;
import net.minecraft.init.Blocks;
import net.minecraft.item.Item;
import net.minecraft.item.ItemBlock;
import net.minecraft.item.ItemDye;
import net.minecraft.item.ItemStack;
import net.minecraft.nbt.NBTTagCompound;
import net.minecraft.tileentity.TileEntity;
import net.minecraft.util.Direction;
import net.minecraft.util.IIcon;
import net.minecraft.util.MovingObjectPosition;
import net.minecraft.world.IBlockAccess;
import net.minecraft.world.World;
import zornco.bedcraftbeyond.BedCraftBeyond;
import zornco.bedcraftbeyond.util.PlankHelper;
import cpw.mods.fml.relauncher.Side;
import cpw.mods.fml.relauncher.SideOnly;

public class BlockColoredBed extends BlockBed implements ITileEntityProvider
{

	/** Maps the head-of-bed block to the foot-of-bed block. */
	/** Maps the foot-of-bed block to the head-of-bed block. */
	public static final int[][] footBlockToHeadBlockMap = new int[][] {{0, 1}, { -1, 0}, {0, -1}, {1, 0}};
	@SideOnly(Side.CLIENT)
	protected IIcon[][] bedEndIcons;
	@SideOnly(Side.CLIENT)
	protected IIcon[][] bedSideIcons;
	@SideOnly(Side.CLIENT)
	protected IIcon[][] bedTopIcons;

	private int colorCombo = 241;
	protected Random random;

	public BlockColoredBed()
	{
		// was 134
		super();
		random = new Random();
	}
	@Override
	public boolean isBed(IBlockAccess world, int x, int y, int z, EntityLivingBase player)
	{
		return this instanceof BlockColoredBed;
	}
	@Override
	public int damageDropped(int par1)
	{
		return par1;
	}
	@Override 
	public boolean removedByPlayer(World world, EntityPlayer player, int x, int y, int z, boolean willHarvest)
	{

		TileColoredBed tile = (TileColoredBed)world.getTileEntity(x, y, z);
		if (tile != null)
		{
			colorCombo = tile.getColorCombo();
		}
		return world.setBlockToAir(x, y, z);
	}
	@Override
	/**
	 * This returns a complete list of items dropped from this block.
	 *
	 * @param world The current world
	 * @param x X Position
	 * @param y Y Position
	 * @param z Z Position
	 * @param metadata Current metadata
	 * @param fortune Breakers fortune level
	 * @return A ArrayList containing all items this block drops
	 */
	public ArrayList<ItemStack> getDrops(World world, int x, int y, int z, int metadata, int fortune)
	{
		ArrayList<ItemStack> ret = new ArrayList<ItemStack>();

		/*int count = quantityDropped(metadata, fortune, world.rand);
		//int combo = 241; //red blnket white sheets
		for(int i = 0; i < count; i++)
		{
			Item id = getItemDropped(metadata, world.rand, fortune);
			if (id != null)
			{
				Block block = id instanceof ItemBlock && !isFlowerPot() ? Block.getBlockFromItem(id) : this;
				ItemStack stack = new ItemStack(id, 1, block.getDamageValue(world, x, y, z));
				NBTTagCompound nbt = new NBTTagCompound();        

				TileColoredBed tile = this.gett(TileColoredBed)world.getTileEntity(x, y, z);
				PlankHelper.validatePlank(nbt, tile.getPlankType());
				stack.setTagCompound(nbt);
				ret.add(stack);
			}
		}*/
		return ret;
	}
	public void onBlockHarvested(World world, int x, int y, int z, int meta, EntityPlayer player)
	{
		if(!player.capabilities.isCreativeMode)
		{
			TileColoredBed tile = (TileColoredBed)world.getTileEntity(x, y, z);
			ItemStack itemstack = new ItemStack(this.getBedItem(), 1, tile.getColorCombo());
			ItemStack plank = tile.getPlankType();
			NBTTagCompound nbt = new NBTTagCompound();    
			PlankHelper.validatePlank(nbt, tile.getPlankType());
			itemstack.setTagCompound(nbt);    


			if (itemstack != null)
			{
				float f = random.nextFloat() * 0.8F + 0.1F;
				float f1 = random.nextFloat() * 0.8F + 0.1F;
				float f2 = random.nextFloat() * 0.8F + 0.1F;
				while (itemstack.stackSize > 0)
				{
					int i1 = random.nextInt(21) + 10;
					if (i1 > itemstack.stackSize)
					{
						i1 = itemstack.stackSize;
					}
					itemstack.stackSize -= i1;
					EntityItem entityitem = new EntityItem(world, x + f, (float) y + f1, z + f2,
							new ItemStack(itemstack.getItem(), i1, itemstack.getItemDamage()));
					float f3 = 0.05F;
					entityitem.motionX = (float) random.nextGaussian() * f3;
					entityitem.motionY = (float) random.nextGaussian() * f3 + 0.2F;
					entityitem.motionZ = (float) random.nextGaussian() * f3;
					if (itemstack.hasTagCompound())
					{
						entityitem.getEntityItem().setTagCompound((NBTTagCompound) itemstack.getTagCompound().copy());
					}
					world.spawnEntityInWorld(entityitem);
				}
			}
		}
		super.onBlockHarvested(world, x, y, z, meta, player);

	}

	public Item getBedItem() {
		return BedCraftBeyond.bedItem;
	}
	@Override
	public ItemStack getPickBlock(MovingObjectPosition target, World world,
			int x, int y, int z, EntityPlayer player) {
		Item item = getItem(world, x, y, z);

		if (item == null)
		{
			return null;
		}

		Block block = item instanceof ItemBlock && !isFlowerPot() ? Block.getBlockFromItem(item) : this;
		ItemStack stack = new ItemStack(item, 1, block.getDamageValue(world, x, y, z));
		NBTTagCompound nbt = new NBTTagCompound();        

		TileColoredBed tile = (TileColoredBed)world.getTileEntity(x, y, z);
		PlankHelper.validatePlank(nbt, tile.getPlankType());
		stack.setTagCompound(nbt);

		return stack;
	}
	/**
	 * Sets whether or not the bed is occupied.
	 */
	public static void setBedOccupied(World par0World, int par1, int par2, int par3, boolean par4)
	{
		int l = par0World.getBlockMetadata(par1, par2, par3);

		if (par4)
		{
			l |= 4;
		}
		else
		{
			l &= -5;
		}

		par0World.setBlockMetadataWithNotify(par1, par2, par3, l, 4);
	}
	@Override
	public boolean onBlockActivated(World world, int par2, int par3, int par4, EntityPlayer player, int par6, float par7, float par8, float par9) {
		if (player.getHeldItem() == null || (player.getHeldItem() != null && player.getHeldItem().getItem() != null && !(player.getHeldItem().getItem() instanceof ItemDye)))
		{
			super.onBlockActivated(world, par2, par3, par4, player, par6, par7, par8, par9);
			world.markBlockForUpdate(par2, par3, par4);
			return true;
		}
		TileColoredBed tile = (TileColoredBed)world.getTileEntity(par2, par3, par4);

		if (tile == null)
		{
			return true;
		}

		if (world.isRemote)
		{
			return true;
		}
		boolean foot = this.isBedFoot(world, par2, par3, par4);
		ItemStack dye = player.getHeldItem();
		int color = dye.getItemDamage();
		int combo = tile.getColorCombo();
		int meta = world.getBlockMetadata(par2, par3, par4);

		tile.setColorCombo(BlockColoredBed.setColorToInt(combo, color, foot?1:2));
		world.markBlockForUpdate(par2, par3, par4);
		int i1 = getDirection(meta);
		if(isBlockHeadOfBed(meta))
		{
			par2 -= field_149981_a[i1][0];
			par4 -= field_149981_a[i1][1];
		}
		else
		{
			par2 += field_149981_a[i1][0];
			par4 += field_149981_a[i1][1];
		}
		tile = (TileColoredBed)world.getTileEntity(par2, par3, par4);
		tile.setColorCombo(BlockColoredBed.setColorToInt(combo, color, foot?1:2));


		world.markBlockForUpdate(par2, par3, par4);
		return true;
	}
	public static int getColorFromInt(int meta, int color)
	{
		switch (color)
		{
		case 0:
			return meta >> 8;
		case 1:
			return meta >> 4 & 0xF;
		case 2:
			return meta & 0xF;
		}
		return 0;
	}
	public static int setColorToInt(int combo, int newColor, int offset)
	{
		switch (offset)
		{
		case 0:
			return (combo & (0xFF)) | (newColor << 8); // will lose top 8 bits
		case 1:
			return (combo & ~(0xF << 4)) | (newColor << 4);
		case 2:
			return (combo & ~0xF) | newColor;
		}
		return combo;
	}

	@Override
	@SideOnly(Side.CLIENT)

	/**
	 * From the specified side and block metadata retrieves the blocks texture. Args: side, metadata
	 */
	public IIcon getIcon(int par1, int par2)
	{
		return Blocks.planks.getIcon(par1, 0);
	}

	public static int getColorFromTile(IBlockAccess par1World, int par2, int par3, int par4) {

		TileColoredBed tile = (TileColoredBed)par1World.getTileEntity(par2, par3, par4);
		if (tile != null)
		{
			return tile.getColorCombo();
		}
		return 241;
	}

	public static int getPlankColorFromTile(IBlockAccess par1World, int par2, int par3, int par4) {

		TileColoredBed tile = (TileColoredBed)par1World.getTileEntity(par2, par3, par4);
		if (tile != null && tile.getPlankType() != null)
		{
			return PlankHelper.getPlankColor(tile.getPlankType());
		}
		return PlankHelper.oakColor;
	}

	public static int getColorFromTilePerPass(IBlockAccess par1World, int par2, int par3, int par4, int pass)
	{
		int combo = getColorFromTile(par1World, par2, par3, par4);
		switch(pass)
		{
		case 0:
			return ItemDye.field_150922_c[getColorFromInt(combo, 2)];
		case 1:
			return ItemDye.field_150922_c[getColorFromInt(combo, 1)];
		case 2:
			return getPlankColorFromTile(par1World, par2, par3, par4);
		}
		return 0;
	}

	@SideOnly(Side.CLIENT)

	/**
	 * From the specified side and block metadata retrieves the blocks texture. Args: side, metadata
	 */
	public IIcon getIcon(int side, int meta, int pass, IBlockAccess par1World, int par2, int par3, int par4)
	{
		if (side == 0)
		{
			//return Blocks.planks.getIcon(side, getColorFromInt(getColorFromTile(par1World, par2, par3, par4), 0));
			return Block.getBlockFromName("plank").getIcon(0, 0);
		}
		else
		{
			int k = getDirection(meta);
			int l = Direction.bedDirection[k][side];
			int i1 = isBlockHeadOfBed(meta) ? 1 : 0;
			return (i1 != 1 || l != 2) && (i1 != 0 || l != 3) ? (l != 5 && l != 4 ? bedTopIcons[pass][i1] : bedSideIcons[pass][i1]) : bedEndIcons[pass][i1];
		}
	}

	@Override
	@SideOnly(Side.CLIENT)

	/**
	 * When this method is called, your block should register all the icons it needs with the given IconRegister. This
	 * is the only chance you get to register icons.
	 */
	public void registerBlockIcons(IIconRegister par1IconRegister)
	{
		this.bedTopIcons = new IIcon[][] {{par1IconRegister.registerIcon("bedcraftbeyond:bed_feet_top_0"), par1IconRegister.registerIcon("bedcraftbeyond:bed_head_top_0")},
				{par1IconRegister.registerIcon("bedcraftbeyond:bed_feet_top_1"), par1IconRegister.registerIcon("bedcraftbeyond:bed_head_top_1")},
				{par1IconRegister.registerIcon("bedcraftbeyond:bed_feet_top_2"), par1IconRegister.registerIcon("bedcraftbeyond:bed_head_top_2")}};
		this.bedEndIcons = new IIcon[][] {{par1IconRegister.registerIcon("bedcraftbeyond:bed_feet_end_0"), par1IconRegister.registerIcon("bedcraftbeyond:bed_head_end_0")},
				{par1IconRegister.registerIcon("bedcraftbeyond:bed_feet_end_1"), par1IconRegister.registerIcon("bedcraftbeyond:bed_head_end_1")},
				{par1IconRegister.registerIcon("bedcraftbeyond:bed_feet_end_2"), par1IconRegister.registerIcon("bedcraftbeyond:bed_head_end_2")}};
		this.bedSideIcons = new IIcon[][] {{par1IconRegister.registerIcon("bedcraftbeyond:bed_feet_side_0"), par1IconRegister.registerIcon("bedcraftbeyond:bed_head_side_0")},
				{par1IconRegister.registerIcon("bedcraftbeyond:bed_feet_side_1"), par1IconRegister.registerIcon("bedcraftbeyond:bed_head_side_1")},
				{par1IconRegister.registerIcon("bedcraftbeyond:bed_feet_side_2"), par1IconRegister.registerIcon("bedcraftbeyond:bed_head_side_2")}};
	}

	/**
	 * The type of render function that is called for this block
	 */
	@Override
	public int getRenderType()
	{
		return BedCraftBeyond.bedRI;
	}

	/**
	 * Returns the ID of the items to drop on destruction.
	 */
	@Override
	public Item getItemDropped(int par1, Random par2Random, int par3)
	{
		return isBlockHeadOfBed(par1) ? Item.getItemById(0) : BedCraftBeyond.bedItem;
	}

	@Override
	@SideOnly(Side.CLIENT)

	/**
	 * only called by clickMiddleMouseButton , and passed to inventory.setCurrentItem (along with isCreative)
	 */
	public Item getItem(World par1World, int par2, int par3, int par4)
	{
		return BedCraftBeyond.bedItem;
	}

	/**
	 * Get the block's damage value (for use with pick block).
	 */
	@Override
	public int getDamageValue(World par1World, int i, int j, int k)
	{
		TileColoredBed tile = (TileColoredBed)par1World.getTileEntity(i, j, k);
		if (tile != null)
		{
			return tile.getColorCombo();
		}
		return 241;
	}
	@Override
	public TileEntity createNewTileEntity(World world, int metadata)
	{
		return new TileColoredBed();
	}

	/**
	 * ejects contained items into the world, and notifies neighbours of an update, as appropriate
	 */
	@Override
	public void breakBlock(World world, int x, int y, int z, Block block, int meta)
	{
		super.breakBlock(world, x, y, z, block, meta);

		world.removeTileEntity(x, y, z);
	}
}
