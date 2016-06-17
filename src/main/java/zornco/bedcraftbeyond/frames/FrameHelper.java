package zornco.bedcraftbeyond.frames;

import net.minecraft.block.Block;
import net.minecraft.block.state.IBlockState;
import net.minecraft.item.Item;
import net.minecraft.item.ItemStack;
import net.minecraft.nbt.NBTTagCompound;
import net.minecraft.util.ResourceLocation;
import net.minecraftforge.fml.relauncher.Side;
import net.minecraftforge.fml.relauncher.SideOnly;
import zornco.bedcraftbeyond.client.TextureUtils;

import java.awt.*;

public abstract class FrameHelper {

    @SideOnly(Side.CLIENT)
    public static Color getColorFromPlankType(ResourceLocation rl, int meta){
        ItemStack fakeItem = new ItemStack(Item.REGISTRY.getObject(rl), 1, meta);
        return getColorFromPlankType(fakeItem);
    }

    @SideOnly(Side.CLIENT)
    public static Color getColorFromPlankType(ItemStack stack){
        try {
            return TextureUtils.getItemTextureColor(stack);
        } catch (Exception e) {
            e.printStackTrace();
        }

        return Color.WHITE;
    }

    public static NBTTagCompound getFrameTag(ItemStack stack){
        NBTTagCompound frameTag = new NBTTagCompound();
        frameTag.setString("frameType", stack.getItem().getRegistryName().toString());
        frameTag.setInteger("frameMeta", stack.getMetadata());
        return frameTag;
    }

    // TODO: Stop depending on meta here in next major Minecraft version (because no meta?)
    public static ItemStack getItemFromFrameTag(NBTTagCompound compound){
        if(compound.hasKey("frame")) compound = compound.getCompoundTag("frame");

        Block b = Block.getBlockFromName(compound.getString("frameType"));
        if(b == null) return null;

        IBlockState state = b.getStateFromMeta(compound.hasKey("frameMeta") ? compound.getInteger("frameMeta") : 0);
        ItemStack frameStack = b.getPickBlock(state, null, null, null, null);
        return frameStack;
    }
}
