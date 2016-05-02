package zornco.bedcraftbeyond.util;

import net.minecraft.block.properties.IProperty;
import net.minecraft.block.state.IBlockState;
import net.minecraft.entity.player.EntityPlayer;
import net.minecraft.item.EnumDyeColor;
import net.minecraft.item.ItemStack;
import net.minecraft.nbt.NBTTagCompound;
import net.minecraft.util.EnumFacing;
import net.minecraft.util.math.BlockPos;
import net.minecraft.world.World;
import zornco.bedcraftbeyond.blocks.BlockColoredBed;
import zornco.bedcraftbeyond.blocks.tiles.TileColoredBed;

public class BedUtils {

  public static boolean testBedPlacement(World world, EntityPlayer player, BlockPos initialPosition, ItemStack placer){

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

  public enum EnumColoredBedPiece {
    BLANKETS,
    SHEETS,
    BASE
  }

  public static EnumDyeColor getColor(ItemStack stack, EnumColoredBedPiece type){
    if(!stack.hasTagCompound()) stack.setTagCompound(new NBTTagCompound());
    NBTTagCompound stackNBT = stack.getTagCompound();
    String key = type.toString().toLowerCase();

    if(!stackNBT.hasKey(key)){
      stackNBT.setInteger(key, EnumDyeColor.WHITE.getMetadata());
      stack.setTagCompound(stackNBT);
      return EnumDyeColor.WHITE;
    }

    return EnumDyeColor.byMetadata(stackNBT.getInteger(key));
  }

  public static void setColorOfBed(EnumColoredBedPiece piece, BlockPos pos, World w, EnumDyeColor color){
    IBlockState stateHead = w.getBlockState(pos);
    BlockPos footPos = pos.offset(stateHead.getValue(BlockColoredBed.FACING));
    IBlockState stateFoot = w.getBlockState(footPos);
    IProperty toChange = BlockColoredBed.BLANKETS;

    switch (piece){
      case BLANKETS:
        toChange = BlockColoredBed.BLANKETS;
        ((TileColoredBed) w.getTileEntity(pos)).setBlanketsColor(color);
        ((TileColoredBed) w.getTileEntity(footPos)).setBlanketsColor(color);
        break;

      case SHEETS:
        toChange = BlockColoredBed.SHEETS;
        ((TileColoredBed) w.getTileEntity(pos)).setSheetsColor(color);
        ((TileColoredBed) w.getTileEntity(footPos)).setSheetsColor(color);
        break;
    }

    stateHead = stateHead.withProperty(toChange, color);
    w.setBlockState(pos, stateHead, 3);

    stateFoot = stateFoot.withProperty(toChange, color);
    w.setBlockState(footPos, stateFoot, 3);
  }
}
