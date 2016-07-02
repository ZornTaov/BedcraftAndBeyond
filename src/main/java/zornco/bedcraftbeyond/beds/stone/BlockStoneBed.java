package zornco.bedcraftbeyond.beds.stone;

import net.minecraft.block.state.IBlockState;
import net.minecraft.item.ItemStack;
import net.minecraft.util.EnumFacing;
import net.minecraft.util.math.BlockPos;
import net.minecraft.world.IBlockAccess;
import net.minecraftforge.fml.common.registry.GameRegistry;
import zornco.bedcraftbeyond.core.BedCraftBeyond;
import zornco.bedcraftbeyond.core.ModContent;

import java.util.ArrayList;
import java.util.List;

public class BlockStoneBed extends zornco.bedcraftbeyond.beds.base.BlockBedBase {

    public BlockStoneBed() {
        setCreativeTab(BedCraftBeyond.BEDS_TAB);
        setHardness(1.0f);
        setUnlocalizedName(BedCraftBeyond.MOD_ID + ".beds.stone");
        setRegistryName(BedCraftBeyond.MOD_ID, "stone_bed");
        setDefaultState(getDefaultState().withProperty(HEAD, false)
            .withProperty(FACING, EnumFacing.NORTH)
            .withProperty(OCCUPIED, false));

        GameRegistry.register(this);
    }

    @Override
    public List<ItemStack> getDrops(IBlockAccess world, BlockPos pos, IBlockState state, int fortune) {
        ArrayList<ItemStack> drops = new ArrayList<ItemStack>();
        if(state.getValue(HEAD))
            drops.add(new ItemStack(ModContent.Items.stoneBed));
        return drops;
    }
}
