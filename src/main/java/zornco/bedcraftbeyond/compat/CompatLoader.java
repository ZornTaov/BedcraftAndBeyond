package zornco.bedcraftbeyond.compat;

import net.minecraftforge.fml.common.FMLModContainer;
import net.minecraftforge.fml.common.Loader;
import zornco.bedcraftbeyond.compat.jei.JeiCompat;

public abstract class CompatLoader {

    public static void loadCompat(){
        if(Loader.isModLoaded("JEI"))
            JeiCompat.load();
    }

}
