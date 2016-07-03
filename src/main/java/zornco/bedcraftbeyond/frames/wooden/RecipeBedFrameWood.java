package zornco.bedcraftbeyond.frames.wooden;

import net.minecraft.inventory.InventoryCrafting;
import net.minecraft.item.ItemStack;
import net.minecraft.item.crafting.IRecipe;
import net.minecraft.nbt.NBTTagCompound;
import net.minecraft.world.World;
import net.minecraftforge.common.ForgeHooks;
import net.minecraftforge.items.IItemHandler;
import net.minecraftforge.items.wrapper.InvWrapper;
import net.minecraftforge.oredict.OreDictionary;
import org.apache.commons.lang3.ArrayUtils;
import zornco.bedcraftbeyond.frames.registry.FrameHelper;
import zornco.bedcraftbeyond.frames.registry.FrameRegistry;
import zornco.bedcraftbeyond.core.ModContent;
import zornco.bedcraftbeyond.core.util.items.ItemHandlerGridHelper;
import zornco.bedcraftbeyond.core.util.items.ItemHelper;

import javax.annotation.Nullable;
import java.awt.*;

public class RecipeBedFrameWood implements IRecipe {

    private int woodSlabID = OreDictionary.getOreID("slabWood");

    @Override
    public boolean matches(InventoryCrafting crafting, World worldIn) {

        Dimension craftingSize = new Dimension(crafting.getWidth(), crafting.getHeight());

        InvWrapper inv = new InvWrapper(crafting);
        IItemHandler wood, slab;

        try {
            wood = ItemHandlerGridHelper.getItemsWrapped(inv, craftingSize, new Rectangle(0,2,3,1));
            slab = ItemHandlerGridHelper.getItemsWrapped(inv, craftingSize, new Rectangle(0,1,3,1));
        }

        catch (IndexOutOfBoundsException iob){
            return false;
        }


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

    @Nullable
    @Override
    public ItemStack getCraftingResult(InventoryCrafting inv) {
        InvWrapper wrapped = new InvWrapper(inv);
        ItemStack wood = wrapped.extractItem(6, 1, true);

        if(wood == null) return null;
        ItemStack bedFrame = new ItemStack(ModContent.Items.woodenBed, 1);
        NBTTagCompound tags = new NBTTagCompound();
        tags.setTag("frame", FrameHelper.getFrameTag(wood));
        bedFrame.setTagCompound(tags);

        return bedFrame;
    }

    @Override
    public int getRecipeSize() {
        return 9;
    }

    @Nullable
    @Override
    public ItemStack getRecipeOutput() {
        return new ItemStack(ModContent.Items.woodenBed, 1);
    }

    @Override
    public ItemStack[] getRemainingItems(InventoryCrafting inv) {
        return ForgeHooks.defaultRecipeGetRemainingItems(inv);
    }
}
