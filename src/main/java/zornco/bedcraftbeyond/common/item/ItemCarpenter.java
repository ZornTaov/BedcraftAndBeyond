package zornco.bedcraftbeyond.common.item;

import net.minecraft.block.Block;
import net.minecraft.item.ItemBlock;
import net.minecraftforge.fml.common.registry.GameRegistry;
import zornco.bedcraftbeyond.BedCraftBeyond;
import zornco.bedcraftbeyond.common.blocks.BcbBlocks;

public class ItemCarpenter extends ItemBlock {

    public ItemCarpenter() {
        super(BcbBlocks.carpenter);
        setRegistryName(BcbBlocks.carpenter.getRegistryName());
        setUnlocalizedName(BedCraftBeyond.MOD_ID + ".carpenter");

        setCreativeTab(BedCraftBeyond.MAIN_TAB);

        GameRegistry.register(this);
    }
}
