package zornco.bedcraftbeyond.common.item;

import net.minecraft.creativetab.CreativeTabs;
import net.minecraft.entity.player.EntityPlayer;
import net.minecraft.item.Item;
import net.minecraft.item.ItemStack;
import net.minecraft.nbt.NBTTagCompound;
import net.minecraft.util.ResourceLocation;
import net.minecraft.util.text.TextFormatting;
import net.minecraftforge.fml.common.registry.GameRegistry;
import zornco.bedcraftbeyond.BedCraftBeyond;
import zornco.bedcraftbeyond.common.crafting.carpenter.CarpenterRecipes;

import java.util.List;

public class ItemTemplate extends Item {

    public ItemTemplate(){
        setMaxStackSize(1);
        setUnlocalizedName(BedCraftBeyond.MOD_ID + ".template");
        setRegistryName(BedCraftBeyond.MOD_ID, "template");
        setHasSubtypes(true);

        GameRegistry.register(this);
    }

    @Override
    public void getSubItems(Item itemIn, CreativeTabs tab, List<ItemStack> subItems) {
        for(ResourceLocation rl : CarpenterRecipes.recipes.keySet()){
            ItemStack debug = new ItemStack(this, 1);
            debug.setTagCompound(new NBTTagCompound());
            debug.getTagCompound().setString("recipe", rl.toString());
            subItems.add(debug);
        }
    }

    @Override
    // TODO: Make a better way to show which recipe the template is for.
    public void addInformation(ItemStack stack, EntityPlayer playerIn, List<String> tooltip, boolean advanced) {
        if(stack.hasTagCompound() && stack.getTagCompound().hasKey("recipe"))
            tooltip.add(TextFormatting.AQUA + stack.getTagCompound().getString("recipe"));
    }
}
