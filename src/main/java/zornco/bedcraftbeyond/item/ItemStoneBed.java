package zornco.bedcraftbeyond.item;

import net.minecraft.block.BlockBed;
import net.minecraft.block.state.IBlockState;
import net.minecraft.creativetab.CreativeTabs;
import net.minecraft.entity.player.EntityPlayer;
import net.minecraft.item.Item;
import net.minecraft.item.ItemStack;
import net.minecraft.util.EnumActionResult;
import net.minecraft.util.EnumFacing;
import net.minecraft.util.EnumHand;
import net.minecraft.util.math.BlockPos;
import net.minecraft.world.World;
import net.minecraftforge.fml.relauncher.Side;
import net.minecraftforge.fml.relauncher.SideOnly;
import zornco.bedcraftbeyond.BedCraftBeyond;
import zornco.bedcraftbeyond.util.BedUtils;

import java.util.List;

public class ItemStoneBed extends Item {

  public ItemStoneBed() {
    setRegistryName(BedCraftBeyond.stoneBedBlock.getRegistryName());
    setUnlocalizedName("beds.stone");
    setCreativeTab(BedCraftBeyond.bedCraftBeyondTab);
    setMaxStackSize(1);
  }

  @Override
  public EnumActionResult onItemUse(ItemStack stack, EntityPlayer playerIn, World worldIn, BlockPos pos, EnumHand hand, EnumFacing side, float hitX, float hitY, float hitZ) {
    // pos is position block will be placed at (bottom half of bed)
    // If we're on the client or not trying to place on the top of a block, fail
    if (worldIn.isRemote)	return EnumActionResult.SUCCESS;
    if (side != EnumFacing.UP) return EnumActionResult.FAIL;

    boolean canPlaceBedHere = BedUtils.testBedPlacement(worldIn, playerIn, pos, stack);
    if(!canPlaceBedHere) return EnumActionResult.FAIL;

    BlockPos btmHalf = pos.up();
    BlockPos topHalf = btmHalf.offset(playerIn.getHorizontalFacing());

    IBlockState bedFootState = BedCraftBeyond.stoneBedBlock.getDefaultState()
            .withProperty(BlockBed.OCCUPIED, false)
            .withProperty(BlockBed.FACING, playerIn.getHorizontalFacing())
            .withProperty(BlockBed.PART, BlockBed.EnumPartType.FOOT);

    if (worldIn.setBlockState(btmHalf, bedFootState, 3)) {
      IBlockState bedHeadState = bedFootState.withProperty(BlockBed.PART, BlockBed.EnumPartType.HEAD).withProperty(BlockBed.FACING, playerIn.getHorizontalFacing().getOpposite());
      worldIn.setBlockState(topHalf, bedHeadState, 3);
    }

    // If not creative mode, remove placer item
    if(!playerIn.capabilities.isCreativeMode) --stack.stackSize;

    return EnumActionResult.SUCCESS;
  }
}
