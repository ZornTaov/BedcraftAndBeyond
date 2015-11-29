package zornco.bedcraftbeyond.util;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;

import javafx.scene.chart.StackedAreaChart;
import net.minecraft.block.Block;
import net.minecraft.init.Blocks;
import net.minecraft.item.Item;
import net.minecraft.item.ItemBlock;
import net.minecraft.item.ItemStack;
import net.minecraft.nbt.NBTTagCompound;
import net.minecraft.nbt.NBTTagList;
import net.minecraftforge.oredict.OreDictionary;
import zornco.bedcraftbeyond.BedCraftBeyond;
import cpw.mods.fml.relauncher.Side;
import cpw.mods.fml.relauncher.SideOnly;

public class PlankHelper {

	public static ArrayList<ItemStack> planks;
	public static HashMap<ItemStack, Integer> plankColorMap = new HashMap<ItemStack, Integer>();
	private static final int oakColor = 0xaf8f58;

	public static void compilePlanks()
	{
		planks = OreDictionary.getOres("plankWood");
	}

	static int prevcolor = 0;
	@SideOnly(Side.CLIENT)
	public static void compilePlanksClient() {

		for(ItemStack stack : planks)
		{ 
			//not bothering with items that are not blocks
			if (stack.getItem() instanceof ItemBlock) {
				if(stack.getItemDamage()==OreDictionary.WILDCARD_VALUE)
				{ 
					//iterate over sub items
					List<ItemStack> list = new ArrayList<ItemStack>();
					stack.getItem().getSubItems(stack.getItem(), null, list);
					for (ItemStack itemStack : list) {
						addPlankToList(itemStack);
					}

				}else
				{ 
					//just grab texture for the given meta 
					addPlankToList(stack);
				}
			}
		}
	}

	public static int getPlankColor(ItemStack plank) {
		if (getPlankColorMap().containsKey(plank)) {
			return getPlankColorMap().get(plank);
		}
		else return PlankHelper.oakColor;
	}

	public static HashMap<ItemStack, Integer> getPlankColorMap() {
		HashMap<ItemStack, Integer> plankColorMap = PlankHelper.plankColorMap;
		return plankColorMap;
	}

	public static void addPlankToList(ItemStack itemStack) {
		ItemStack stack2 = new ItemStack(itemStack.getItem(), 1, itemStack.getItemDamage());

		String result = "Block: " + stack2.getDisplayName() + " meta: " + stack2.getItemDamage();
		try {
			//color1 = ClientUtils.getAverageItemColour(stack2);
			//color2 = ClientUtils.getItemColours2(stack2);

			int color = ClientUtils.getAverageBlockColour(stack2);
			getPlankColorMap().put(stack2, color);
			result += " Color: " + color;
			if (color == prevcolor) {
				result += "SAME AS LAST COLOR! WTF?!";
			}
			prevcolor = color;
		} catch (Exception e) {
			result += " THIS TEXTURE IS MISSING OR BROKEN! WON'T BE ADDED!";
		}
		BedCraftBeyond.logger.info(result);
	}

	public static ItemStack validatePlank(ItemStack bed) {
		if (bed.getTagCompound() == null) {
			bed.setTagCompound(new NBTTagCompound());
		}
		return validatePlank(bed.getTagCompound());
	}

	public static ItemStack validatePlank(NBTTagCompound bedTags) {
		if (!bedTags.hasKey("plank")) {
			int frameNum = 0;
			if (bedTags.hasKey("Damage")) {
				frameNum = bedTags.getShort("Damage") >> 8;
			}
			if (bedTags.hasKey("colorCombo"))
			{
				frameNum = bedTags.getShort("colorCombo") >> 8;
			}
			return PlankHelper.addPlankInfo(bedTags, new ItemStack(Blocks.planks, 1, frameNum));
		}
		else
		{
			NBTTagList list = bedTags.getTagList("plank", 10);
			NBTTagCompound plank = list.getCompoundTagAt(0);
			return ItemStack.loadItemStackFromNBT(plank);
			/*bedTags.removeTag("plank");
			return new ItemStack(Blocks.planks, 1, 0);*/
		}
	}

	public static ItemStack addPlankInfo(NBTTagCompound bedTags, ItemStack plank) {
		NBTTagList nbttaglist = new NBTTagList();
		NBTTagCompound plankTag = new NBTTagCompound();
		plank.writeToNBT(plankTag);
		nbttaglist.appendTag(plankTag);
		bedTags.setTag("plank", nbttaglist);
		return plank;
	}
}
