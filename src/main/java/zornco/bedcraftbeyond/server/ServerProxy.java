package zornco.bedcraftbeyond.server;

import net.minecraft.world.World;
import zornco.bedcraftbeyond.common.frames.FrameLoader;
import zornco.bedcraftbeyond.common.CommonProxy;

public class ServerProxy extends CommonProxy {

   @Override
   public World getClientWorld() { return null; }

   @Override
   public void compileFrames() {
      FrameLoader.compileFramesServer();
   }

   @Override
   public void registerMessages() {
      zornco.bedcraftbeyond.network.Registration.registerMessagesServer();
   }
}
