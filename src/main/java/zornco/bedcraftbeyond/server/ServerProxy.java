package zornco.bedcraftbeyond.server;

import zornco.bedcraftbeyond.common.frames.FrameHelper;
import zornco.bedcraftbeyond.common.CommonProxy;

public class ServerProxy extends CommonProxy {

   @Override
   public void compileFrames() {
      FrameHelper.compileFramesServer();
   }
}
