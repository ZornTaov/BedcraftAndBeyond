package zornco.bedcraftbeyond.common.item;

import net.minecraft.creativetab.CreativeTabs;
import net.minecraft.item.*;
import net.minecraftforge.fml.common.registry.GameRegistry;
import net.minecraftforge.fml.relauncher.Side;
import net.minecraftforge.fml.relauncher.SideOnly;
import zornco.bedcraftbeyond.common.blocks.BcbBlocks;
import zornco.bedcraftbeyond.BedCraftBeyond;
import zornco.bedcraftbeyond.common.blocks.BlockRug;

import java.util.List;

public class ItemRug extends ItemBlock {

    public static final String[] rugColorNames = new String[]{"Black", "Red", "Green", "Brown", "Blue", "Purple", "Cyan", "LightGray", "Gray", "Pink", "Lime", "Yellow", "LightBlue", "Magenta", "Orange", "White"};

    public ItemRug() {
        super(BcbBlocks.rug);
        this.setRegistryName(BcbBlocks.rug.getRegistryName());
        this.setUnlocalizedName(BedCraftBeyond.MOD_ID + ".rug");
        this.setMaxDamage(0);
        this.setHasSubtypes(true);

        GameRegistry.register(this);
    }

    /**
     * Returns the metadata of the block which this Item (ItemBlock) can place
     */
    @Override
    public int getMetadata(int par1) {
        return par1;
    }

    @Override
    public String getUnlocalizedName(ItemStack stack) {
        return super.getUnlocalizedName() + "." + ItemRug.rugColorNames[BlockRug.getBlockFromDye(stack.getItemDamage())];
    }

    @Override
    @SideOnly(Side.CLIENT)
    public void getSubItems(Item par1, CreativeTabs tab, List subItems) {
        for (int var4 = 0; var4 < 16; ++var4)
            subItems.add(new ItemStack(par1, 1, var4));
    }
}
