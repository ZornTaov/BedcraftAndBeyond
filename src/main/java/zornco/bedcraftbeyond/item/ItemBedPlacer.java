package zornco.bedcraftbeyond.item;

import net.minecraft.block.Block;
import net.minecraft.block.state.IBlockState;
import net.minecraft.entity.player.EntityPlayer;
import net.minecraft.item.ItemBlock;
import net.minecraft.item.ItemStack;
import net.minecraft.util.EnumActionResult;
import net.minecraft.util.EnumFacing;
import net.minecraft.util.math.BlockPos;
import net.minecraft.world.World;
import zornco.bedcraftbeyond.BedCraftBeyond;
import zornco.bedcraftbeyond.blocks.BlockBedBase;

public class ItemBedPlacer extends ItemBlock {


  public ItemBedPlacer(Block block) {
    super(block);
    setRegistryName(block.getRegistryName());
    setCreativeTab(BedCraftBeyond.bedCraftBeyondTab);
    setMaxStackSize(1);
  }

  protected void placeSimpleBedBlocks(World world, EntityPlayer player, BlockPos initPos, Block bedBlock, ItemStack placer) throws Exception {
    BlockPos btmHalf = initPos;
    BlockPos topHalf = btmHalf.offset(player.getHorizontalFacing());

    IBlockState bhead = bedBlock.getDefaultState().withProperty(BlockBedBase.FACING, player.getHorizontalFacing())
            .withProperty(BlockBedBase.HEAD, true);
    if (!world.setBlockState(btmHalf, bhead, 3)) throw new Exception("Failed to set head blockstate.");

    IBlockState bfoot = bedBlock.getDefaultState()
            .withProperty(BlockBedBase.HEAD, false)
            .withProperty(BlockBedBase.FACING, player.getHorizontalFacing().getOpposite());
    if(!world.setBlockState(topHalf, bfoot, 2)) throw new Exception("Failed to set foot blockstate.");
  }

  public static boolean testSimpleBedPlacement(World world, EntityPlayer player, BlockPos initialPosition, ItemStack placer){
    if(!world.getBlockState(initialPosition).getBlock().isReplaceable(world, initialPosition))
      initialPosition = initialPosition.offset(EnumFacing.UP);

    BlockPos btmPos = initialPosition;

    IBlockState stateInEditing = world.getBlockState(btmPos);
    if(!stateInEditing.getBlock().isReplaceable(world, btmPos) && !world.isAirBlock(btmPos)) return false;

    EnumFacing playerFacing = player.getHorizontalFacing();

    BlockPos topPos = initialPosition.offset(playerFacing);
    if (!world.getBlockState(topPos).getBlock().isReplaceable(world, topPos) && !world.isAirBlock(topPos)) return false;

    return true;
  }
}