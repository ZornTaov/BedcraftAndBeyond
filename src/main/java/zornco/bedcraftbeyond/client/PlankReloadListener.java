package zornco.bedcraftbeyond.client;

import net.minecraft.client.renderer.texture.TextureUtil;
import net.minecraft.client.resources.IResourceManager;
import net.minecraft.client.resources.IResourceManagerReloadListener;
import net.minecraft.item.Item;
import net.minecraft.item.ItemStack;
import net.minecraft.util.ResourceLocation;
import zornco.bedcraftbeyond.BedCraftBeyond;
import zornco.bedcraftbeyond.common.frames.FrameHelper;
import zornco.bedcraftbeyond.util.PlankHelper;

public class PlankReloadListener implements IResourceManagerReloadListener{

	// TODO: Refactor to new system
	@Override
	public void onResourceManagerReload(IResourceManager resourceManager) {
		FrameHelper.compileFramesClient();
	}
}
