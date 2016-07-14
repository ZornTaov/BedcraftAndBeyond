package zornco.bedcraftbeyond.frames.wooden;

import net.minecraft.block.state.IBlockState;
import net.minecraft.client.renderer.color.IBlockColor;
import net.minecraft.client.renderer.color.IItemColor;
import net.minecraft.item.Item;
import net.minecraft.item.ItemStack;
import net.minecraft.nbt.NBTTagCompound;
import net.minecraft.util.math.BlockPos;
import net.minecraft.world.IBlockAccess;
import zornco.bedcraftbeyond.parts.Part;
import zornco.bedcraftbeyond.core.BedCraftBeyond;

public class WoodenBedColorer implements IItemColor, IBlockColor {

    @Override
    public int colorMultiplier(IBlockState state, IBlockAccess world, BlockPos pos, int tintIndex) {
        state = state.getActualState(world, pos);
        TileWoodenBed bed = (TileWoodenBed) ((BlockWoodenBed) state.getBlock()).getTileForBed(world, state, pos);
        if(bed == null) return 0;
        switch (tintIndex) {
            case 0:
                return bed.getPlankColor().getRGB();

            case 1:
                return bed.getLinenHandler().getLinenColor(Part.Type.SHEETS).getRGB();

            case 2:
                return bed.getLinenHandler().getLinenColor(Part.Type.BLANKETS).getRGB();

            default:
                return 0;
        }
    }

    @Override
    public int getColorFromItemstack(ItemStack stack, int tintIndex) {
        if(tintIndex > 0) return -1;
        if(!stack.hasTagCompound() || !stack.getTagCompound().hasKey("frame")) return -1;
        NBTTagCompound frameTag = stack.getTagCompound().getCompoundTag("frame");
        if(!frameTag.hasKey("color")) {
            ItemStack frameItem = new ItemStack(Item.getByNameOrId(frameTag.getString("frameType")), 1, frameTag.getInteger("frameMeta"));
            if(frameItem == null) return -1;
            try {
                int color = BedCraftBeyond.PROXY.getColorFromTexture(frameItem).getRGB();
                frameTag.setInteger("color", color);
                stack.getTagCompound().setTag("frame", frameTag);
                return color;
            } catch (Exception e) {
                e.printStackTrace();
            }
        }

        return frameTag.getInteger("color");
    }
}
