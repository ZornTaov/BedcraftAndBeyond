package zornco.bedcraftbeyond.client;

import net.minecraft.client.resources.IResourceManager;
import net.minecraft.client.resources.IResourceManagerReloadListener;
import zornco.bedcraftbeyond.common.frames.FrameLoader;

public class PlankReloadListener implements IResourceManagerReloadListener{

	// TODO: Refactor to new system
	@Override
	public void onResourceManagerReload(IResourceManager resourceManager) {
		FrameLoader.compileFramesClient();
	}
}
