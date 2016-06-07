package zornco.bedcraftbeyond.common.crafting.carpenter;

import net.minecraft.init.Blocks;
import net.minecraft.item.ItemStack;
import net.minecraft.nbt.NBTTagCompound;
import net.minecraftforge.oredict.OreDictionary;
import org.apache.commons.lang3.ArrayUtils;
import zornco.bedcraftbeyond.common.blocks.tiles.TileCarpenter;
import zornco.bedcraftbeyond.common.frames.FrameRegistry;
import zornco.bedcraftbeyond.common.item.BcbItems;

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
        ItemStack wood = inv.extractItem(0, 3, true);
        ItemStack slabs = inv.extractItem(1, 3, true);

        // If not enough wood
        if(wood == null || wood.stackSize != 3 || !FrameRegistry.isValidFrameMaterial(FrameRegistry.EnumFrameType.WOOD, wood))
            return false;

        // If not enough slabs
        if(slabs == null || slabs.stackSize != 3) return false;

        // If slabs (item 1) not a wooden slab
        if(!ArrayUtils.contains(OreDictionary.getOreIDs(slabs), woodSlabID)) return false;

        return true;
    }

    @Override
    public ItemStack doCraft(TileCarpenter.CraftingHandler inv, boolean simulate) {
        if(!matches(inv)) return null;

        ItemStack wood = inv.extractItem(0, 3, simulate);
        inv.extractItem(1, 3, simulate);

        if(wood == null) return null;

        ItemStack bedFrame = new ItemStack(BcbItems.woodenBed, 1);
        NBTTagCompound tags = new NBTTagCompound();
        tags.setString("frameType", wood.getItem().getRegistryName().toString());
        tags.setInteger("frameMeta", wood.getMetadata());
        bedFrame.setTagCompound(tags);

        return bedFrame;
    }
}
