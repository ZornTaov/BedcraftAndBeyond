package zornco.bedcraftbeyond.util;

import net.minecraft.client.resources.IResourceManager;
import net.minecraft.client.resources.IResourceManagerReloadListener;
import net.minecraft.item.Item;
import net.minecraft.item.ItemStack;
import net.minecraft.util.ResourceLocation;
import zornco.bedcraftbeyond.BedCraftBeyond;

public class PlankReloadListener implements IResourceManagerReloadListener{
	
	@Override
	public void onResourceManagerReload(IResourceManager resourceManager) {
		if (PlankHelper.readyToColor) {
			for (String plank : PlankHelper.getPlankColorMap().keySet()) {
				String[] plankSplit = plank.split("@");
				ItemStack stack2 =  new ItemStack(Item.itemRegistry.getObject(new ResourceLocation(plankSplit[0])), 1, Integer.parseInt(plankSplit[1]));
				int color = BedCraftBeyond.instance.proxy.getAverageBlockColour(stack2);
				//getPlankColorMap().put(stack2, color);
				PlankHelper.addPlankToList(stack2, color);
			}
		}
	}
}
