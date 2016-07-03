package zornco.bedcraftbeyond.rugs;

import net.minecraft.client.renderer.color.IItemColor;
import net.minecraft.item.EnumDyeColor;
import net.minecraft.item.ItemDye;
import net.minecraft.item.ItemStack;

public class RugColorer implements IItemColor {
    @Override
    public int getColorFromItemstack(ItemStack stack, int tintIndex) {
        return ItemDye.DYE_COLORS[EnumDyeColor.byMetadata(stack.getItemDamage()).getDyeDamage()];
    }

}
