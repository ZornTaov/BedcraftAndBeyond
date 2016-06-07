package zornco.bedcraftbeyond.common.item;

import net.minecraft.entity.player.EntityPlayer;
import net.minecraft.item.Item;
import net.minecraft.item.ItemStack;
import net.minecraft.util.ActionResult;
import net.minecraft.util.EnumHand;
import net.minecraft.world.World;
import net.minecraftforge.fml.common.registry.GameRegistry;
import zornco.bedcraftbeyond.BedCraftBeyond;

public class ItemEyedropper extends Item {

    public ItemEyedropper(){
        setCreativeTab(BedCraftBeyond.MAIN_TAB);
        setMaxStackSize(1);
        setUnlocalizedName(BedCraftBeyond.MOD_ID + ".eyedropper");
        setRegistryName(BedCraftBeyond.MOD_ID, "eyedropper");

        GameRegistry.register(this);
    }

    @Override
    public ActionResult<ItemStack> onItemRightClick(ItemStack itemStackIn, World worldIn, EntityPlayer playerIn, EnumHand hand) {
        return super.onItemRightClick(itemStackIn, worldIn, playerIn, hand);
    }
}
