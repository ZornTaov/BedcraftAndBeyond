package zornco.bedcraftbeyond.blocks;

import net.minecraft.block.BedBlock;
import net.minecraft.block.Block;
import net.minecraft.block.material.Material;
import net.minecraft.block.material.MaterialColor;
import net.minecraft.item.DyeColor;

public class RainbowBedBlock extends BedBlock {

    private static Block.Properties BED_PROPS = Block.Properties.create(Material.WOOL, MaterialColor.WHITE_TERRACOTTA);

    public RainbowBedBlock() {
        super(DyeColor.WHITE, BED_PROPS);

    }

}