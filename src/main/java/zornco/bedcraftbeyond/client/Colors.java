package zornco.bedcraftbeyond.client;

import net.minecraft.block.state.IBlockState;
import net.minecraft.client.renderer.color.IBlockColor;
import net.minecraft.client.renderer.color.IItemColor;
import net.minecraft.item.EnumDyeColor;
import net.minecraft.item.ItemDye;
import net.minecraft.item.ItemStack;
import net.minecraft.util.math.BlockPos;
import net.minecraft.world.IBlockAccess;
import zornco.bedcraftbeyond.common.blocks.BlockWoodenBed;
import zornco.bedcraftbeyond.common.blocks.properties.EnumBedFabricType;
import zornco.bedcraftbeyond.common.blocks.tiles.TileWoodenBed;
import zornco.bedcraftbeyond.util.ColorHelper;

import java.awt.*;

public class Colors {

    public static class DyeItemColorer implements IItemColor {

        @Override
        public int getColorFromItemstack(ItemStack stack, int tintIndex) {
            if (tintIndex == 1) return ColorHelper.getColorFromStack(stack).getRGB();
            return -1;
        }
    }

    public static class LinenColorer implements IItemColor {

        @Override
        public int getColorFromItemstack(ItemStack stack, int tintIndex) {
            Color c = ColorHelper.getColorFromStack(stack);
            return c.getRGB();
        }
    }

    public static class WoolDamageColorer implements IItemColor {

        @Override
        public int getColorFromItemstack(ItemStack stack, int tintIndex) {
            return ItemDye.DYE_COLORS[EnumDyeColor.byMetadata(stack.getItemDamage()).getDyeDamage()];
        }
    }

    public static class BedColorer implements IBlockColor {

        @Override
        public int colorMultiplier(IBlockState state, IBlockAccess world, BlockPos pos, int tintIndex) {
            state = state.getActualState(world, pos);
            TileWoodenBed bed = (TileWoodenBed) ((BlockWoodenBed) state.getBlock()).getTileForBed(world, state, pos);
            if(bed == null) return 0;
            switch (tintIndex) {
                case 0:
                    return bed.getPartColor(BlockWoodenBed.EnumColoredPart.PLANKS).getRGB();

                case 1:
                    return bed.getPartColor(BlockWoodenBed.EnumColoredPart.SHEETS).getRGB();

                case 2:
                    return bed.getPartColor(BlockWoodenBed.EnumColoredPart.BLANKETS).getRGB();

                default:
                    return 0;
            }
        }
    }

}
