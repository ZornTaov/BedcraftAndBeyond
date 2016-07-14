package zornco.bedcraftbeyond.suitcase;

import net.minecraft.entity.player.EntityPlayer;
import net.minecraft.item.Item;
import net.minecraft.item.ItemStack;
import net.minecraft.util.ActionResult;
import net.minecraft.util.EnumActionResult;
import net.minecraft.util.EnumFacing;
import net.minecraft.util.EnumHand;
import net.minecraft.util.math.BlockPos;
import net.minecraft.world.World;
import net.minecraftforge.fml.common.registry.GameRegistry;
import zornco.bedcraftbeyond.core.BedCraftBeyond;
import zornco.bedcraftbeyond.core.gui.GuiHandler;

public class ItemSuitcase extends Item {

    public ItemSuitcase() {
        setUnlocalizedName(BedCraftBeyond.MOD_ID + ".suitcase");
        setRegistryName(BedCraftBeyond.MOD_ID, "suitcase");
        setCreativeTab(BedCraftBeyond.MAIN_TAB);
        setMaxStackSize(1);

        GameRegistry.register(this);
    }

    @Override
    public EnumActionResult onItemUse(ItemStack stack, EntityPlayer playerIn, World worldIn, BlockPos pos, EnumHand hand, EnumFacing facing, float hitX, float hitY, float hitZ) {
        if (worldIn.isRemote)
            return EnumActionResult.SUCCESS;
        return EnumActionResult.SUCCESS;
    }

    @Override
    public ActionResult<ItemStack> onItemRightClick(ItemStack itemStackIn, World worldIn, EntityPlayer playerIn, EnumHand hand) {
        playerIn.setActiveHand(hand);
        playerIn.openGui(BedCraftBeyond.INSTANCE, GuiHandler.GUI_SUITCASE, worldIn, 0, 0, 0);

        return new ActionResult(EnumActionResult.SUCCESS, itemStackIn);
    }

    @Override
    public int getMaxItemUseDuration(ItemStack stack) {
        return 1;
    }
}
