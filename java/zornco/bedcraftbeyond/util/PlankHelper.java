package zornco.bedcraftbeyond.util;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;

import net.minecraft.block.Block;
import net.minecraft.init.Blocks;
import net.minecraft.item.Item;
import net.minecraft.item.ItemBlock;
import net.minecraft.item.ItemStack;
import net.minecraft.nbt.NBTTagCompound;
import net.minecraftforge.oredict.OreDictionary;
import zornco.bedcraftbeyond.BedCraftBeyond;
import cpw.mods.fml.relauncher.Side;
import cpw.mods.fml.relauncher.SideOnly;

public class PlankHelper {

	public static ArrayList<ItemStack> planks;
	public static HashMap<ItemStack, Integer> plankColorMap = new HashMap<ItemStack, Integer>();
	
	public static void compilePlanks()
	{
		planks = OreDictionary.getOres("plankWood");
	}

	@SideOnly(Side.CLIENT)
	public static void compilePlanksClient() {
		
		for(ItemStack stack : planks)
		{ 
			if(stack.getItemDamage()==OreDictionary.WILDCARD_VALUE)
			{ 
				//iterate over sub items
				List<ItemStack> list = new ArrayList<ItemStack>();
				stack.getItem().getSubItems(stack.getItem(), null, list);
				for (ItemStack itemStack : list) {
					ItemStack stack2 = new ItemStack(stack.getItem(), 1, itemStack.getItemDamage());
					BedCraftBeyond.logger.info("Block: " + stack2.getDisplayName() + " meta: " + stack2.getItemDamage() + " Color: " + ClientUtils.getAverageBlockColour(stack2));
					plankColorMap.put(stack2, ClientUtils.getAverageItemColour(stack2));
				}
				
			}else
			{ //just grab texture for the given meta 
				BedCraftBeyond.logger.info("Block: " + stack.getDisplayName() + " meta: " + stack.getItemDamage());
				ItemStack stack2 = new ItemStack(stack.getItem(), 1, stack.getItemDamage());
				plankColorMap.put(stack2, ClientUtils.getAverageItemColour(stack));
			}
		}
		/*
			namespace = Item.itemRegistry.getNameForObject(itemStack.getItem());
			BedCraftBeyond.logger.info("ItemGroup: " + namespace);
				
			//plankColorMap.put(namespace, ClientUtils.getAverageItemColour(itemStack));*/
	}
	
	public static void validatePlank(NBTTagCompound bedTags) {
		if (!bedTags.hasKey("planks")) {
			int frameNum = 0;
			if (bedTags.hasKey("Damage")) {
				frameNum = bedTags.getShort("Damage") >> 8;
			}
			if (bedTags.hasKey("colorCombo"))
			{
				frameNum = bedTags.getShort("colorCombo") >> 8;
			}
			PlankHelper.addPlankInfo(bedTags, new ItemStack(Blocks.planks, 1, frameNum));
		}
	}

	public static void addPlankInfo(NBTTagCompound bedTags, ItemStack plank) {
		String namespace;
		if(plank.getItem() instanceof ItemBlock)
		{
			namespace = Block.blockRegistry.getNameForObject(Block.getBlockFromItem(plank.getItem()));
		}
		else
		{
			//namespace = Item.itemRegistry.getNameForObject(plank.getItem());
			return;
		}
		if (namespace == null) {
			BedCraftBeyond.logger.debug("Plank Broke: " + plank.getDisplayName());
			return;
		}
		bedTags.setString("plank", namespace);
		bedTags.setInteger("plankMeta", plank.getItemDamage());
	}
}
