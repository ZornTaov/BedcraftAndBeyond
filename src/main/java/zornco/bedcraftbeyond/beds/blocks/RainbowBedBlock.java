package zornco.bedcraftbeyond.beds.blocks;

import net.minecraft.block.BedBlock;
import net.minecraft.block.Block;
import net.minecraft.block.BlockRenderType;
import net.minecraft.block.BlockState;
import net.minecraft.block.material.Material;
import net.minecraft.block.material.MaterialColor;
import net.minecraft.block.material.PushReaction;
import net.minecraft.item.DyeColor;

public class RainbowBedBlock extends BedBlock {

    private static Block.Properties BED_PROPS = Block.Properties.create(Material.WOOL, MaterialColor.WHITE_TERRACOTTA);

    public RainbowBedBlock() {
        super(DyeColor.WHITE, BED_PROPS);

    }

    @Override
    public BlockRenderType getRenderType(BlockState state) {
        return BlockRenderType.MODEL;
    }

    @Override
    public PushReaction getPushReaction(BlockState state) {
        return PushReaction.BLOCK;
    }
}