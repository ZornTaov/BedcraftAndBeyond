package zornco.bedcraftbeyond.common.crafting.carpenter;

import net.minecraft.inventory.InventoryCrafting;
import net.minecraft.item.ItemStack;
import net.minecraft.nbt.NBTTagCompound;
import net.minecraft.world.World;
import net.minecraftforge.items.ItemHandlerHelper;
import net.minecraftforge.oredict.OreDictionary;
import org.apache.commons.lang3.ArrayUtils;
import zornco.bedcraftbeyond.common.blocks.tiles.TileCarpenter;
import zornco.bedcraftbeyond.common.frames.FrameException;
import zornco.bedcraftbeyond.common.frames.FrameRegistry;
import zornco.bedcraftbeyond.common.item.BcbItems;

public class RecipeWoodFrame implements CarpenterRecipe {

    private int woodSlabID = OreDictionary.getOreID("slabWood");

    @Override
    public boolean matches(TileCarpenter.CarpenterCraftingItemHandler inv) {
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
    public CarpenterRecipeOutput doCraft(TileCarpenter.CarpenterCraftingItemHandler crafting, boolean simulate) {
        if(!matches(crafting)) return null;

        ItemStack wood = crafting.extractItem(0, 3, simulate);
        crafting.extractItem(1, 3, simulate);

        if(wood == null) return null;

        ItemStack bedFrame = new ItemStack(BcbItems.woodenBed, 1);
        NBTTagCompound tags = new NBTTagCompound();
        tags.setString("frameType", wood.getItem().getRegistryName().toString());
        tags.setInteger("frameMeta", wood.getMetadata());
        bedFrame.setTagCompound(tags);

        return new CarpenterRecipeOutput(bedFrame);
    }
}
