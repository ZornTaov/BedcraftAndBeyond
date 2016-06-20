package zornco.bedcraftbeyond.common.item;

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
import zornco.bedcraftbeyond.BedCraftBeyond;
import zornco.bedcraftbeyond.client.gui.GuiEyedropper;
import zornco.bedcraftbeyond.client.gui.IClientGui;
import zornco.bedcraftbeyond.common.gui.GuiHandler;
import zornco.bedcraftbeyond.common.util.ColorHelper;

import java.awt.*;
import java.util.List;

public class ItemEyedropper extends Item implements IClientGui {

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
                GuiHandler.ITEM_ID,
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
        Color c = ColorHelper.getColorFromStack(stack);
        tooltip.add("Color: " + ColorHelper.getFormattedColorValues(c));
    }

    @Override
    public Object getClientGui(World w, BlockPos pos, EntityPlayer player) {
        EnumHand hand = pos.getX() == 1 ? EnumHand.MAIN_HAND : EnumHand.OFF_HAND;
        return new GuiEyedropper(player, hand, player.getHeldItem(hand));
    }

    public static Color getCurrentColor(ItemStack stack){
        return ColorHelper.getColorFromStack(stack);
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
