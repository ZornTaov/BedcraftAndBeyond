package zornco.bedcraftbeyond.client;

import net.minecraft.client.renderer.texture.TextureUtil;
import net.minecraft.client.resources.IResourceManager;
import net.minecraft.client.resources.IResourceManagerReloadListener;
import net.minecraft.item.Item;
import net.minecraft.item.ItemStack;
import net.minecraft.util.ResourceLocation;
import zornco.bedcraftbeyond.BedCraftBeyond;
import zornco.bedcraftbeyond.util.PlankHelper;

public class PlankReloadListener implements IResourceManagerReloadListener{

	// TODO: Refactor to new system
	@Override
	public void onResourceManagerReload(IResourceManager resourceManager) {
		if (PlankHelper.readyToColor) {
			for (String plank : PlankHelper.getPlankColorMap().keySet()) {
				String[] plankSplit = plank.split("@");
				ItemStack stack2 =  new ItemStack(Item.itemRegistry.getObject(new ResourceLocation(plankSplit[0])), 1, Integer.parseInt(plankSplit[1]));

				try {
					int color = TextureUtils.getAverageBlockColour(stack2);
					PlankHelper.addPlankToList(stack2, color);
				} catch (Exception e) {
					e.printStackTrace();
				}
				//getPlankColorMap().put(stack2, color);

			}
		}
	}
}
