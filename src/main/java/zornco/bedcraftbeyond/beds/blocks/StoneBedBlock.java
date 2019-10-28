package zornco.bedcraftbeyond.beds.blocks;

import net.minecraft.block.BedBlock;
import net.minecraft.block.Block;
import net.minecraft.block.BlockRenderType;
import net.minecraft.block.BlockState;
import net.minecraft.block.material.Material;
import net.minecraft.block.material.MaterialColor;
import net.minecraft.entity.Entity;
import net.minecraft.item.DyeColor;
import net.minecraft.state.properties.BedPart;
import net.minecraft.util.Direction;
import net.minecraft.util.math.BlockPos;
import net.minecraft.util.math.shapes.ISelectionContext;
import net.minecraft.util.math.shapes.VoxelShape;
import net.minecraft.util.math.shapes.VoxelShapes;
import net.minecraft.world.IBlockReader;

public class StoneBedBlock extends BedBlock {
   protected static VoxelShape voxelTop = Block.makeCuboidShape(0.0D, 3.0D, 0.0D, 16.0D, 9.0D, 16.0D);
   protected static VoxelShape voxelUndersideWest = Block.makeCuboidShape(2.0D, 0.0D, 2.0D, 16.0D, 3.0D, 14.0D);
   protected static VoxelShape voxelUndersideSouth = Block.makeCuboidShape(2.0D, 0.0D, 0.0D, 14.0D, 3.0D, 14.0D);
   protected static VoxelShape voxelUndersideEast = Block.makeCuboidShape(0.0D, 0.0D, 2.0D, 14.0D, 3.0D, 14.0D);
   protected static VoxelShape voxelUndersideNorth = Block.makeCuboidShape(2.0D, 0.0D, 2.0D, 14.0D, 3.0D, 16.0D);
   protected static VoxelShape voxelNorth = VoxelShapes.or(voxelTop, voxelUndersideNorth);
   protected static VoxelShape voxelSouth = VoxelShapes.or(voxelTop, voxelUndersideSouth);
   protected static VoxelShape voxelWest = VoxelShapes.or(voxelTop, voxelUndersideWest);
   protected static VoxelShape voxelEast = VoxelShapes.or(voxelTop, voxelUndersideEast);

    private static Block.Properties BED_PROPS = Block.Properties.create(Material.ROCK, MaterialColor.STONE);

    public StoneBedBlock() {
        super(DyeColor.GRAY, BED_PROPS);

    }
    @Override
    public BlockRenderType getRenderType(BlockState state) {
      return BlockRenderType.MODEL;
   }
   @Override
   public void onLanded(IBlockReader worldIn, Entity entityIn) {
      entityIn.setMotion(entityIn.getMotion().mul(1.0D, 0.0D, 1.0D));
   }
   
   public VoxelShape getShape(BlockState state, IBlockReader worldIn, BlockPos pos, ISelectionContext context) {
      Direction direction = state.get(HORIZONTAL_FACING);
      Direction direction1 = state.get(PART) == BedPart.HEAD ? direction : direction.getOpposite();
      switch(direction1) {
      case NORTH:
         return voxelNorth;
      case SOUTH:
         return voxelSouth;
      case WEST:
         return voxelWest;
      default:
         return voxelEast;
      }
   }
}