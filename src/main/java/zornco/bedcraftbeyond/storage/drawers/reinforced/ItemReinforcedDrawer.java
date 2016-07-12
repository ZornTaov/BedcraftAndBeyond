package zornco.bedcraftbeyond.storage.drawers.reinforced;

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

public class ItemReinforcedDrawer extends Item implements IPart {

    public ItemReinforcedDrawer() {
        setCreativeTab(BedCraftBeyond.BEDS_TAB);
        setMaxStackSize(4);
        setUnlocalizedName(BedCraftBeyond.MOD_ID + ".storage.drawers.reinforced");
        setRegistryName(BedCraftBeyond.MOD_ID, "drawerReinforced");

        GameRegistry.register(this);
    }

    @Override
    public void onCreated(ItemStack stack, World worldIn, EntityPlayer playerIn) {
        super.onCreated(stack, worldIn, playerIn);
        if(!stack.hasTagCompound()) stack.setTagCompound(new NBTTagCompound());
    }

    @Override
    public Part getPartReference() {
        return ModContent.BedParts.drawerReinforced;
    }

    @Override
    public ICapabilityProvider initCapabilities(ItemStack stack, NBTTagCompound oldCapNbt) {
        return new ItemInventory(ModContent.BedParts.drawerReinforced.getInventorySize());
    }


}
