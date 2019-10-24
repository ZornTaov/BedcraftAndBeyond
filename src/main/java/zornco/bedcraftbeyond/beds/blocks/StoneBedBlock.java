package zornco.bedcraftbeyond.beds.blocks;

import net.minecraft.block.BedBlock;
import net.minecraft.block.Block;
import net.minecraft.block.BlockRenderType;
import net.minecraft.block.BlockState;
import net.minecraft.block.material.Material;
import net.minecraft.block.material.MaterialColor;
import net.minecraft.entity.Entity;
import net.minecraft.item.DyeColor;
import net.minecraft.world.IBlockReader;

public class StoneBedBlock extends BedBlock {

    private static Block.Properties BED_PROPS = Block.Properties.create(Material.ROCK, MaterialColor.WHITE_TERRACOTTA);

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
}