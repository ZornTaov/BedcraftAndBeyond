package zornco.bedcraftbeyond.item;

import net.minecraft.block.state.IBlockState;
import net.minecraft.entity.player.EntityPlayer;
import net.minecraft.item.ItemStack;
import net.minecraft.util.EnumActionResult;
import net.minecraft.util.EnumFacing;
import net.minecraft.util.EnumHand;
import net.minecraft.util.math.BlockPos;
import net.minecraft.world.World;
import zornco.bedcraftbeyond.BedCraftBeyond;
import zornco.bedcraftbeyond.blocks.BcbBlocks;
import zornco.bedcraftbeyond.blocks.BlockBedBase;

public class ItemStoneBed extends ItemBedPlacer {

  public ItemStoneBed() {
    super(BcbBlocks.stoneBed);
    setUnlocalizedName("beds.stone");
  }

  @Override
  public EnumActionResult onItemUse(ItemStack stack, EntityPlayer playerIn, World worldIn, BlockPos pos, EnumHand hand, EnumFacing side, float hitX, float hitY, float hitZ) {
    // pos is position block will be placed at (bottom half of bed)
    // If we're on the client or not trying to place on the top of a block, fail
    if (worldIn.isRemote)	return EnumActionResult.SUCCESS;
    if (side != EnumFacing.UP) return EnumActionResult.FAIL;

    boolean canPlaceBedHere = testSimpleBedPlacement(worldIn, playerIn, pos, stack);
    if(!canPlaceBedHere) return EnumActionResult.FAIL;

    try { placeSimpleBedBlocks(worldIn, playerIn, pos.up(), BcbBlocks.stoneBed, stack); }
    catch (Exception e) { return EnumActionResult.FAIL; }

    // If not creative mode, remove placer item
    if(!playerIn.capabilities.isCreativeMode) --stack.stackSize;
    if(stack.stackSize < 1) playerIn.setHeldItem(hand, null);

    return EnumActionResult.SUCCESS;
  }
}
