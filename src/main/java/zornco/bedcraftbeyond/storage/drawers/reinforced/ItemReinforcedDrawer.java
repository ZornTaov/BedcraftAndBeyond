package zornco.bedcraftbeyond.storage.drawers.reinforced;

import net.minecraft.entity.player.EntityPlayer;
import net.minecraft.item.Item;
import net.minecraft.item.ItemStack;
import net.minecraft.nbt.NBTTagCompound;
import net.minecraft.tileentity.TileEntity;
import net.minecraft.world.World;
import net.minecraftforge.common.capabilities.ICapabilityProvider;
import net.minecraftforge.fml.common.registry.GameRegistry;
import zornco.bedcraftbeyond.core.BedCraftBeyond;
import zornco.bedcraftbeyond.core.ModContent;
import zornco.bedcraftbeyond.storage.IStorageItem;
import zornco.bedcraftbeyond.storage.ItemInventory;
import zornco.bedcraftbeyond.storage.handling.IStorageHandler;

import java.awt.*;

public class ItemReinforcedDrawer extends Item implements IStorageItem {

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
    public ICapabilityProvider initCapabilities(ItemStack stack, NBTTagCompound oldCapNbt) {
        return new ItemInventory(18);
    }

    @Override
    public int getInventorySize() {
        return 18;
    }

    @Override
    public Dimension getGuiSize() {
        return new Dimension(176, 153);
    }

    @Override
    public Object createGUI(EntityPlayer player, TileEntity tile, IStorageHandler handler, String id) throws Exception {
        return new GuiReinforcedDrawer(player, tile, handler, id);
    }

    @Override
    public net.minecraft.inventory.Container createContainer(EntityPlayer player, TileEntity tile, IStorageHandler handler, String id) throws Exception {
        return new ContainerReinforcedDrawer(player, tile, handler, id);
    }

}
