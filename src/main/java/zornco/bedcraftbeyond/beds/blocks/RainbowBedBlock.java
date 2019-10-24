package zornco.bedcraftbeyond.beds.blocks;

import net.minecraft.block.BedBlock;
import net.minecraft.block.Block;
import net.minecraft.block.BlockRenderType;
import net.minecraft.block.BlockState;
import net.minecraft.block.material.Material;
import net.minecraft.block.material.MaterialColor;
import net.minecraft.block.material.PushReaction;
import net.minecraft.item.DyeColor;
import net.minecraft.tileentity.TileEntity;
import net.minecraft.world.IBlockReader;
import zornco.bedcraftbeyond.beds.tileentity.RainbowBedTileEntity;

public class RainbowBedBlock extends BedBlock {
    private final DyeColor color;
    private final DyeColor color2;

    private static Block.Properties BED_PROPS = Block.Properties.create(Material.WOOL, MaterialColor.WHITE_TERRACOTTA);

    public RainbowBedBlock() {
        super(DyeColor.WHITE, BED_PROPS);
        this.color = DyeColor.WHITE;
        this.color2 = DyeColor.WHITE;

    }
    @Override
    public TileEntity createNewTileEntity(IBlockReader worldIn) {
        return new RainbowBedTileEntity(this.color, this.color2);
    }

    @Override
    public BlockRenderType getRenderType(BlockState state) {
        return BlockRenderType.ENTITYBLOCK_ANIMATED;
    }
    
    @Override
    public PushReaction getPushReaction(BlockState state) {
        return PushReaction.BLOCK;
    }
}