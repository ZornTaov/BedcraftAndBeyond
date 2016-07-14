package zornco.bedcraftbeyond.linens.impl;

import net.minecraft.creativetab.CreativeTabs;
import net.minecraft.entity.player.EntityPlayer;
import net.minecraft.item.Item;
import net.minecraft.item.ItemStack;
import net.minecraft.nbt.NBTTagCompound;
import net.minecraft.world.World;
import net.minecraftforge.fml.common.registry.GameRegistry;
import zornco.bedcraftbeyond.parts.Part;
import zornco.bedcraftbeyond.parts.IPart;
import zornco.bedcraftbeyond.linens.PropertyFabricType;
import zornco.bedcraftbeyond.core.BedCraftBeyond;
import zornco.bedcraftbeyond.core.ModContent;
import zornco.bedcraftbeyond.core.util.ColorHelper;

import java.awt.*;
import java.util.List;

public class ItemBlanket extends Item implements IPart {

    public ItemBlanket() {
        setCreativeTab(BedCraftBeyond.BEDS_TAB);
        setMaxStackSize(16);
        setUnlocalizedName(BedCraftBeyond.MOD_ID + ".linens.blanket");
        setRegistryName(BedCraftBeyond.MOD_ID, "blanket");
        setHasSubtypes(true);

        GameRegistry.register(this);
    }

    @Override
    public void getSubItems(Item itemIn, CreativeTabs tab, List<ItemStack> subItems) {
        ItemStack stack = new ItemStack(this);
        NBTTagCompound tags = new NBTTagCompound();
        tags.setTag("color", ColorHelper.getTagForColor(Color.WHITE));
        tags.setString("type", PropertyFabricType.SOLID_COLOR.name());
        stack.setTagCompound(tags);
        subItems.add(stack);
    }

    @Override
    public void onCreated(ItemStack stack, World worldIn, EntityPlayer playerIn) {

    }

    @Override
    public void addInformation(ItemStack stack, EntityPlayer playerIn, List<String> tooltip, boolean advanced) {
        // TODO: Oh, the woes of Mojang and their privates.
        // TextFormatting.valueOf(getColor(stack).toDye().getUnlocalizedName().toUpperCase())
        // getColor(stack).toDye().getTextColor()
        Color c = ColorHelper.getColorFromStack(stack);
        if (c != null) {
            String closest = ColorHelper.getColorNameFromColor(c);
            tooltip.add("Color: " + ColorHelper.getFormattedColorValues(c) + (closest != null ? " (~" + closest + ")" : ""));
        }
    }

    @Override
    public Part getPartReference() {
        return ModContent.BedParts.blanket;
    }
}
