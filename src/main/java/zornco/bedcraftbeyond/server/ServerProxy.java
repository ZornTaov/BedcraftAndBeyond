package zornco.bedcraftbeyond.server;

import zornco.bedcraftbeyond.common.BedHelper;
import zornco.bedcraftbeyond.common.CommonProxy;

public class ServerProxy extends CommonProxy {

   @Override
   public void compileFrames() {
      BedHelper.compileWoodFrames();
   }
}
