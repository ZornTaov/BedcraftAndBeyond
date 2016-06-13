package zornco.bedcraftbeyond.common.crafting.recipes;

import net.minecraft.init.Blocks;
import net.minecraft.item.ItemStack;
import net.minecraft.nbt.NBTTagCompound;
import net.minecraftforge.items.IItemHandler;
import net.minecraftforge.oredict.OreDictionary;
import org.apache.commons.lang3.ArrayUtils;
import zornco.bedcraftbeyond.common.blocks.tiles.TileCarpenter;
import zornco.bedcraftbeyond.common.crafting.carpenter.CarpenterRecipe;
import zornco.bedcraftbeyond.common.frames.FrameRegistry;
import zornco.bedcraftbeyond.common.item.BcbItems;
import zornco.bedcraftbeyond.items.ItemHandlerGridHelper;
import zornco.bedcraftbeyond.items.ItemHelper;

import java.awt.*;

public class RecipeWoodFrame implements CarpenterRecipe {

    private int woodSlabID = OreDictionary.getOreID("slabWood");

    @Override
    public ItemStack getRecipeOutput() {
        ItemStack bed = new ItemStack(BcbItems.woodenBed, 1);
        NBTTagCompound compound = new NBTTagCompound();
        compound.setString("frameType", Blocks.PLANKS.getRegistryName().toString());
        compound.setInteger("frameMeta", 0);
        bed.setTagCompound(compound);
        return bed;
    }

    @Override
    public boolean matches(TileCarpenter.CraftingHandler inv) {
        // Wood should be in first slot, slabs in second
        IItemHandler wood = ItemHandlerGridHelper.getItemsWrapped(inv, new Rectangle(0,2,3,1));
        IItemHandler slab = ItemHandlerGridHelper.getItemsWrapped(inv, new Rectangle(0,1,3,1));

        if(!ItemHelper.areItemStacksEqual(wood)) return false;
        if(!ItemHelper.areItemStacksEqual(slab)) return false;

        // If not enough wood
        if(wood.getSlots() < 3 || !FrameRegistry.isValidFrameMaterial(FrameRegistry.EnumFrameType.WOOD, wood.getStackInSlot(0)))
            return false;

        // If not enough slabs
        if(slab.getSlots() < 3) return false;

        // If slabs (item 1) not a wooden slab
        if(!ArrayUtils.contains(OreDictionary.getOreIDs(slab.getStackInSlot(0)), woodSlabID)) return false;

        return true;
    }

    @Override
    public ItemStack doCraft(TileCarpenter.CraftingHandler inv, boolean simulate) {
        if(!matches(inv)) return null;

        ItemStack wood = inv.extractItem(6, 1, true);
        for(int y = 3; y < 9; y++) inv.extractItem(y, 1, simulate);

        if(wood == null) return null;

        ItemStack bedFrame = new ItemStack(BcbItems.woodenBed, 1);
        NBTTagCompound tags = new NBTTagCompound();
        tags.setString("frameType", wood.getItem().getRegistryName().toString());
        tags.setInteger("frameMeta", wood.getMetadata());
        bedFrame.setTagCompound(tags);

        return bedFrame;
    }
}
