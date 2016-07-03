package zornco.bedcraftbeyond.rugs;

import net.minecraft.item.Item;
import net.minecraftforge.fml.common.registry.GameRegistry;
import zornco.bedcraftbeyond.core.BedCraftBeyond;

public class ItemScissors extends Item {

    public ItemScissors() {
        setUnlocalizedName(BedCraftBeyond.MOD_ID + ".scissors");
        setRegistryName(BedCraftBeyond.MOD_ID, "scissors");
        setCreativeTab(BedCraftBeyond.MAIN_TAB);
        setMaxStackSize(1);

        GameRegistry.register(this);
    }
}
