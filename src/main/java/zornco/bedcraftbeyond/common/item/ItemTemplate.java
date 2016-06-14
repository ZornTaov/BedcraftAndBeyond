package zornco.bedcraftbeyond.common.item;

import net.minecraft.creativetab.CreativeTabs;
import net.minecraft.entity.player.EntityPlayer;
import net.minecraft.init.Items;
import net.minecraft.item.Item;
import net.minecraft.item.ItemStack;
import net.minecraft.nbt.NBTTagCompound;
import net.minecraft.util.ActionResult;
import net.minecraft.util.EnumActionResult;
import net.minecraft.util.EnumHand;
import net.minecraft.util.ResourceLocation;
import net.minecraft.util.math.BlockPos;
import net.minecraft.util.text.TextComponentString;
import net.minecraft.util.text.TextFormatting;
import net.minecraft.util.text.translation.I18n;
import net.minecraft.world.World;
import net.minecraftforge.fml.common.registry.GameRegistry;
import net.minecraftforge.items.wrapper.PlayerOffhandInvWrapper;
import zornco.bedcraftbeyond.BedCraftBeyond;
import zornco.bedcraftbeyond.client.gui.IClientGui;
import zornco.bedcraftbeyond.common.crafting.carpenter.Templates;
import zornco.bedcraftbeyond.common.gui.GuiHandler;

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
        for(ResourceLocation rl : Templates.getAllTemplateKeys()){
            ItemStack debug = new ItemStack(this, 1);
            debug.setTagCompound(new NBTTagCompound());
            debug.getTagCompound().setString("recipe", rl.toString());
            subItems.add(debug);
        }

        subItems.add(new ItemStack(this, 1));
    }

    @Override
    // TODO: Make a better way to show which recipe the template is for.
    public void addInformation(ItemStack stack, EntityPlayer playerIn, List<String> tooltip, boolean advanced) {
        if(stack.hasTagCompound() && stack.getTagCompound().hasKey("recipe"))
            tooltip.add(TextFormatting.AQUA + stack.getTagCompound().getString("recipe"));
        else {
            tooltip.set(0, I18n.translateToLocal(this.getUnlocalizedName() + ".blank"));
        }
    }

    public static ItemStack generateItemWithRecipe(ResourceLocation recipe, int stackSize){
        ItemStack stack = new ItemStack(BcbItems.template, stackSize);
        NBTTagCompound tags = new NBTTagCompound();
        tags.setString("recipe", recipe.toString());
        stack.setTagCompound(tags);
        return stack;
    }
}
