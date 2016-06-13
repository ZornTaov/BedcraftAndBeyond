package zornco.bedcraftbeyond.common.blocks.tiles;

import net.minecraft.item.ItemStack;
import net.minecraft.nbt.NBTTagCompound;
import net.minecraft.tileentity.TileEntity;
import net.minecraft.util.ResourceLocation;
import net.minecraft.world.World;
import net.minecraftforge.items.ItemStackHandler;
import zornco.bedcraftbeyond.items.IItemHandlerSized;
import zornco.bedcraftbeyond.common.crafting.carpenter.CarpenterRecipe;
import zornco.bedcraftbeyond.common.crafting.carpenter.CarpenterRecipes;

import java.awt.*;

public class TileCarpenter extends TileEntity {

    public CraftingHandler craftingInv = new CraftingHandler(this, 9);
    public CarpenterTemplateHandler template = new CarpenterTemplateHandler(this);
    public CarpenterOutputItemHandler outputs = new CarpenterOutputItemHandler(this, 1);

    private CarpenterRecipe currentRecipe;

    public TileCarpenter(){ }

    public TileCarpenter(World w){
        setWorldObj(w);
    }

    @Override
    public NBTTagCompound writeToNBT(NBTTagCompound compound) {
        compound.setTag("crafting", craftingInv.serializeNBT());
        compound.setTag("template", template.serializeNBT());
        return super.writeToNBT(compound);
    }

    @Override
    public void readFromNBT(NBTTagCompound compound) {
        template.deserializeNBT(compound.getCompoundTag("template"));
        craftingInv.deserializeNBT(compound.getCompoundTag("crafting"));

        super.readFromNBT(compound);
    }

    protected void checkRecipe(){
        ItemStack templateItem = template.getStackInSlot(0);
        if(templateItem == null){
            outputs.setStackInSlot(0, null);
            currentRecipe = null;
            return;
        }

        if(!templateItem.hasTagCompound() || !templateItem.getTagCompound().hasKey("recipe")) return;
        ResourceLocation recipe = new ResourceLocation(templateItem.getTagCompound().getString("recipe"));
        if(!CarpenterRecipes.recipes.containsKey(recipe)) return;
        currentRecipe = CarpenterRecipes.recipes.get(recipe);
        if(!currentRecipe.matches(craftingInv)) {
            outputs.setStackInSlot(0, null);
            return;
        }

        // Simulate a craft to show the crafter what to expect
        outputs.setStackInSlot(0, currentRecipe.doCraft(craftingInv, true));
    }

    /**
     * First slot is the template, the other slots are added as the template is
     * manipulated.
     */
    public static class CraftingHandler extends ItemStackHandler implements IItemHandlerSized {
        private TileCarpenter master;
        public CraftingHandler(TileCarpenter master, int slots){
            super(slots);
            this.master = master;
        }

        @Override
        protected void onContentsChanged(int slot) {
            master.checkRecipe();
        }

        @Override
        public Dimension getSize() {
            return new Dimension(3, 3);
        }
    }

    public static class CarpenterTemplateHandler extends ItemStackHandler {
        private TileCarpenter master;
        public CarpenterTemplateHandler(TileCarpenter master){
            super(1);
            this.master = master;
        }

        @Override
        protected void onContentsChanged(int slot) {
            master.checkRecipe();
        }
    }

    public static class CarpenterOutputItemHandler extends ItemStackHandler {

        private TileCarpenter master;
        public CarpenterOutputItemHandler(TileCarpenter master, int slots){
            super(slots);
            this.master = master;
        }

        // Don't allow anyone to put items in, only take them out
        @Override
        public ItemStack insertItem(int slot, ItemStack stack, boolean simulate) {
            return stack;
        }

        private boolean allClear(){
            for(int s = 0; s < getSlots(); ++s){
                ItemStack inSlot = getStackInSlot(s);
                if(inSlot != null) return false;
            }

            return true;
        }

        @Override
        public ItemStack extractItem(int slot, int amount, boolean simulate) {
            ItemStack extracted = super.extractItem(slot, amount, simulate);
            if(extracted != null && extracted.stackSize > 0 && allClear())
                master.currentRecipe.doCraft(master.craftingInv, false);
            return extracted;
        }
    }
}
