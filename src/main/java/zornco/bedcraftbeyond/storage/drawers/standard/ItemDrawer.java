package zornco.bedcraftbeyond.storage.drawers.standard;

import net.minecraft.entity.player.EntityPlayer;
import net.minecraft.inventory.Container;
import net.minecraft.item.Item;
import net.minecraft.item.ItemStack;
import net.minecraft.nbt.NBTTagCompound;
import net.minecraft.tileentity.TileEntity;
import net.minecraft.world.World;
import net.minecraftforge.common.capabilities.ICapabilityProvider;
import net.minecraftforge.fml.common.registry.GameRegistry;
import zornco.bedcraftbeyond.core.BedCraftBeyond;
import zornco.bedcraftbeyond.storage.IStorageItem;
import zornco.bedcraftbeyond.storage.ItemInventory;
import zornco.bedcraftbeyond.storage.handling.IStorageHandler;

import java.awt.*;

public class ItemDrawer extends Item implements IStorageItem {

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
    public ICapabilityProvider initCapabilities(ItemStack stack, NBTTagCompound oldCapNbt) {
        return new ItemInventory(9);
    }

    @Override
    public int getInventorySize() {
        return 9;
    }

    @Override
    public Dimension getGuiSize() {
        return new Dimension(176, 132);
    }

    @Override
    public Object createGUI(EntityPlayer player, TileEntity tile, IStorageHandler handler, String id) throws Exception {
        return new GuiDrawer(player, tile, handler, id);
    }

    @Override
    public Container createContainer(EntityPlayer player, TileEntity tile, IStorageHandler handler, String id) throws Exception {
        return new ContainerDrawer(player, tile, handler, id);
    }
}
