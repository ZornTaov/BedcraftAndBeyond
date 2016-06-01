package zornco.bedcraftbeyond.common.crafting.carpenter;

import net.minecraft.item.ItemStack;

import java.util.Arrays;

public class CarpenterRecipeOutput {

    public static final int MAX_OUTPUTS = 3;

    public ItemStack[] items;

    public CarpenterRecipeOutput(ItemStack... outputs) {
        outputs = Arrays.copyOf(outputs, MAX_OUTPUTS);
        this.items = outputs;
    }

}
