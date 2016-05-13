package zornco.bedcraftbeyond.common.item.frames;

import net.minecraft.block.Block;
import net.minecraft.client.renderer.color.IItemColor;
import net.minecraft.creativetab.CreativeTabs;
import net.minecraft.entity.player.EntityPlayer;
import net.minecraft.item.Item;
import net.minecraft.item.ItemStack;
import net.minecraft.nbt.NBTTagCompound;
import net.minecraft.util.EnumActionResult;
import net.minecraft.util.EnumFacing;
import net.minecraft.util.EnumHand;
import net.minecraft.util.ResourceLocation;
import net.minecraft.util.math.BlockPos;
import net.minecraft.util.text.translation.I18n;
import net.minecraft.world.World;
import net.minecraftforge.fml.relauncher.Side;
import net.minecraftforge.fml.relauncher.SideOnly;
import zornco.bedcraftbeyond.common.blocks.BcbBlocks;
import zornco.bedcraftbeyond.common.blocks.BlockWoodenBed;
import zornco.bedcraftbeyond.common.blocks.tiles.TileWoodenBed;
import zornco.bedcraftbeyond.client.tabs.TabBeds;
import zornco.bedcraftbeyond.common.frames.FrameHelper;
import zornco.bedcraftbeyond.common.frames.FrameRegistry;
import zornco.bedcraftbeyond.common.item.ItemBedPlacer;
import zornco.bedcraftbeyond.util.PlankHelper;

import java.util.List;

public class ItemWoodenFrame extends ItemBedPlacer implements IItemColor {

	public ItemWoodenFrame(Block b) {
		super(b);
		setUnlocalizedName("frames.wooden");
		this.setHasSubtypes(true);
	}

	@SuppressWarnings({ "rawtypes", "unchecked" })
	@Override
	@SideOnly(Side.CLIENT)
	/**
	 * returns a list of blocks with the same ID, but different meta (eg: wood returns 4 blocks)
	 */
	public void getSubItems(Item item, CreativeTabs tab, List subItems)
	{
		if(tab instanceof TabBeds) {
			for (ResourceLocation regName : FrameRegistry.getWoodFrameSet()) {
				ItemStack bed = new ItemStack(item, 1);
				NBTTagCompound tags = new NBTTagCompound();
				tags.setString("frameType", regName.toString());
				tags.setInteger("frameMeta", 0);
				bed.setTagCompound(tags);
				subItems.add(bed);
			}
		}

		if(subItems.size() == 0){
			ItemStack bed = new ItemStack(item, 1);
			NBTTagCompound tags = new NBTTagCompound();
			tags.setString("frameType", "minecraft:planks");
			tags.setInteger("frameMeta", 0);
			bed.setTagCompound(tags);
			subItems.add(bed);
		}

	}

	@SuppressWarnings({ "rawtypes", "unchecked" })
	@Override
	@SideOnly(Side.CLIENT)
	/**
	 * allows items to add custom lines of information to the mouseover description
	 */
	public void addInformation(ItemStack stack, EntityPlayer player, List<String> tags, boolean advanced) {
		// TODO: Fix frame type display (Oak Wood Planks, etc)
		if(!stack.hasTagCompound()) return;
		NBTTagCompound nbt = stack.getTagCompound();
		if(!nbt.hasKey("frameType")) return;
		String frameType = nbt.getString("frameType");
		Block b = Block.getBlockFromName(frameType + "@0");
		if(b != null) tags.add(I18n.translateToLocal(b.getUnlocalizedName()));
	}

	@Override
	public EnumActionResult onItemUse(ItemStack stack, EntityPlayer playerIn, World worldIn, BlockPos pos, EnumHand hand, EnumFacing side, float hitX, float hitY, float hitZ) {
		if (worldIn.isRemote)	return EnumActionResult.SUCCESS;
		if (side != EnumFacing.UP) return EnumActionResult.FAIL;

		boolean canPlaceBedHere = testSimpleBedPlacement(worldIn, playerIn, pos, stack);
		if(!canPlaceBedHere) return EnumActionResult.FAIL;

		pos = pos.up();
		try { placeSimpleBedBlocks(worldIn, playerIn, pos, BcbBlocks.woodenBed, stack); }
		catch(Exception e) { return EnumActionResult.FAIL; }

		TileWoodenBed tileTopHalf = BlockWoodenBed.getTileEntity(worldIn, pos);
		if (tileTopHalf != null) {
			// tileTopHalf.setBlanketsColor(BlockWoodenBed.getPartColorFromItem(stack, BlockWoodenBed.EnumColoredPart.BLANKETS));
			// tileTopHalf.setLinenPart(BlockWoodenBed.getPartColorFromItem(stack, BlockWoodenBed.EnumColoredPart.SHEETS));
			tileTopHalf.setPlankType(PlankHelper.validatePlank(stack));
		}

		// If not creative mode, remove placer item
		if(!playerIn.capabilities.isCreativeMode) --stack.stackSize;
		if(stack.stackSize < 1) playerIn.setHeldItem(hand, null);

		return EnumActionResult.SUCCESS;
	}

	@Override
	public int getColorFromItemstack(ItemStack stack, int tintIndex) {
		switch (tintIndex)
		{
			case 0:
				return 0; // ItemDye.dyeColors[BlockWoodenBed.getPartColorFromItem(stack, BlockWoodenBed.EnumColoredPart.BLANKETS).ordinal()];
			case 1:
				return 0; // ItemDye.dyeColors[BlockWoodenBed.getPartColorFromItem(stack, BlockWoodenBed.EnumColoredPart.SHEETS).ordinal()];
			case 2:
				// TODO: Fix plank colors
				// return ItemWoodenFrame.woodColors[ItemWoodenFrame.getColorFromInt(par1ItemStack.getItemDamage(), 0)];
				// return PlankHelper.getPlankColor(PlankHelper.plankStringfromItemStack(PlankHelper.validatePlank(par1ItemStack)));
				return PlankHelper.oakColor;
			default:
				return 0;
		}
	}
}
