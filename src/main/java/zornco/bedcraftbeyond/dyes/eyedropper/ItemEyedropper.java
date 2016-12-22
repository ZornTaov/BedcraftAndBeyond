package zornco.bedcraftbeyond.dyes.eyedropper;

import net.minecraft.entity.player.EntityPlayer;
import net.minecraft.item.Item;
import net.minecraft.item.ItemStack;
import net.minecraft.nbt.NBTTagCompound;
import net.minecraft.util.ActionResult;
import net.minecraft.util.EnumActionResult;
import net.minecraft.util.EnumHand;
import net.minecraft.util.math.BlockPos;
import net.minecraft.world.World;
import net.minecraftforge.fml.common.registry.GameRegistry;
import zornco.bedcraftbeyond.core.BedCraftBeyond;
import zornco.bedcraftbeyond.core.gui.GuiHandler;
import zornco.bedcraftbeyond.core.util.ColorHelper;
import zornco.bedcraftbeyond.dyes.IColoredItem;

import java.awt.*;
import java.util.List;

public class ItemEyedropper extends Item implements IColoredItem {

    public ItemEyedropper(){
        setCreativeTab(BedCraftBeyond.MAIN_TAB);
        setMaxStackSize(1);
        setUnlocalizedName(BedCraftBeyond.MOD_ID + ".eyedropper");
        setRegistryName(BedCraftBeyond.MOD_ID, "eyedropper");

        GameRegistry.register(this);
    }

    @Override
    public ActionResult<ItemStack> onItemRightClick(ItemStack stack, World worldIn, EntityPlayer playerIn, EnumHand hand) {
        if(playerIn.isSneaking()){
            playerIn.openGui(BedCraftBeyond.INSTANCE,
                GuiHandler.ID_EYEDROPPER,
                worldIn,
                hand == EnumHand.MAIN_HAND ? 1 : 0,
                0, 0);

            return ActionResult.newResult(EnumActionResult.SUCCESS, stack);
        }

        return ActionResult.newResult(EnumActionResult.PASS, stack);
    }

    @Override
    public void addInformation(ItemStack stack, EntityPlayer playerIn, List<String> tooltip, boolean advanced) {
        if(!stack.hasTagCompound()) stack.setTagCompound(new NBTTagCompound());
        if(!stack.getTagCompound().hasKey("color")) return;
        Color c = getColorFromStack(stack);
        tooltip.add("Color: " + ColorHelper.getFormattedColorValues(c));
    }

    @Override
    public boolean hasContainerItem(ItemStack stack) {
        return true;
    }

    @Override
    public ItemStack getContainerItem(ItemStack itemStack) {
        return itemStack.copy();
    }
}
