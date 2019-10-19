package zornco.bedcraftbeyond.core.util;

import net.minecraft.client.Minecraft;
import net.minecraftforge.fml.common.thread.SidedThreadGroups;

public abstract class SettingsHelper {

    public static boolean showingAdvancedTooltips(){
        if(Thread.currentThread().getThreadGroup() == SidedThreadGroups.SERVER)
            return false;
        return Minecraft.getInstance().gameSettings.advancedItemTooltips;
    }

}
