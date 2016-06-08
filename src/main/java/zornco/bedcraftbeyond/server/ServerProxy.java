package zornco.bedcraftbeyond.server;

import zornco.bedcraftbeyond.common.CommonProxy;
import zornco.bedcraftbeyond.common.frames.FrameLoader;

public class ServerProxy extends CommonProxy {

   @Override
   public void compileFrames() {
      FrameLoader.compileFramesServer();
   }

}
