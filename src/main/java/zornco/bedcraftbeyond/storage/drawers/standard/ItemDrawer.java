package zornco.bedcraftbeyond.storage.drawers.standard;

import net.minecraft.entity.player.EntityPlayer;
import net.minecraft.item.Item;
import net.minecraft.item.ItemStack;
import net.minecraft.nbt.NBTTagCompound;
import net.minecraft.world.World;
import net.minecraftforge.common.capabilities.ICapabilityProvider;
import net.minecraftforge.fml.common.registry.GameRegistry;
import zornco.bedcraftbeyond.core.BedCraftBeyond;
import zornco.bedcraftbeyond.core.ModContent;
import zornco.bedcraftbeyond.parts.IPart;
import zornco.bedcraftbeyond.parts.Part;
import zornco.bedcraftbeyond.storage.ItemInventory;

public class ItemDrawer extends Item implements IPart {

    public ItemDrawer() {
        setCreativeTab(BedCraftBeyond.BEDS_TAB);
        setMaxStackSize(4);
        setUnlocalizedName(BedCraftBeyond.MOD_ID + ".storage.drawers.standard");
        setRegistryName(BedCraftBeyond.MOD_ID, "drawer");

        GameRegistry.register(this);
    }

    @Override
    public void onCreated(ItemStack stack, World worldIn, EntityPlayer playerIn) {
        super.onCreated(stack, worldIn, playerIn);
        if(!stack.hasTagCompound()) stack.setTagCompound(new NBTTagCompound());
    }

    @Override
    public Part getPartReference() {
        return ModContent.BedParts.drawer;
    }

    @Override
    public ICapabilityProvider initCapabilities(ItemStack stack, NBTTagCompound oldCapNbt) {
        return new ItemInventory(9);
    }


}
