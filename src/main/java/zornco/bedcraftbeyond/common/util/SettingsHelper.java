package zornco.bedcraftbeyond.common.util;

import net.minecraft.client.Minecraft;
import net.minecraftforge.fml.common.FMLCommonHandler;
import net.minecraftforge.fml.relauncher.Side;

public abstract class SettingsHelper {

    public static boolean showingAdvancedTooltips(){
        if(FMLCommonHandler.instance().getEffectiveSide() == Side.SERVER)
            return false;
        return Minecraft.getMinecraft().gameSettings.advancedItemTooltips;
    }

}
