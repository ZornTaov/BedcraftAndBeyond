package zornco.bedcraftbeyond.common.item;

import net.minecraft.creativetab.CreativeTabs;
import net.minecraft.entity.player.EntityPlayer;
import net.minecraft.item.Item;
import net.minecraft.item.ItemStack;
import net.minecraft.nbt.NBTTagCompound;
import net.minecraftforge.fml.common.registry.GameRegistry;
import zornco.bedcraftbeyond.BedCraftBeyond;
import zornco.bedcraftbeyond.util.ColorHelper;

import java.awt.*;
import java.util.List;

public class ItemDyeBottle extends Item {

    public ItemDyeBottle() {
        setCreativeTab(BedCraftBeyond.bedsTab);
        setMaxStackSize(16);
        setHasSubtypes(true);
        setRegistryName(BedCraftBeyond.MOD_ID, "dye_bottle");
        setUnlocalizedName(BedCraftBeyond.MOD_ID + ".dyeBottle");

        GameRegistry.register(this);
    }

    @Override
    public void addInformation(ItemStack stack, EntityPlayer playerIn, List<String> tooltip, boolean advanced) {
        Color c = ColorHelper.getColorFromStack(stack);
        if (c != null) {
            String closest = ColorHelper.getColorNameFromColor(c);
            tooltip.add("Color: " + ColorHelper.getFormattedColorValues(c) + (closest != null ? " (~" + closest + ")" : ""));
        }
    }

    @Override
    public void getSubItems(Item itemIn, CreativeTabs tab, List<ItemStack> subItems) {
        for (Color c : ColorHelper.colorList.keySet()) {
            ItemStack dyeBottleStack = new ItemStack(this, 1);
            NBTTagCompound tags = new NBTTagCompound();
            tags.setTag("color", ColorHelper.getTagForColor(c));
            dyeBottleStack.setTagCompound(tags);
            subItems.add(dyeBottleStack);
        }
    }
}
