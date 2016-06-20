package zornco.bedcraftbeyond.client.util;

import java.awt.*;
import java.awt.image.BufferedImage;
import java.io.IOException;
import java.io.InputStream;
import java.util.HashMap;

import javax.imageio.ImageIO;

import net.minecraft.block.Block;
import net.minecraft.client.Minecraft;
import net.minecraft.client.renderer.texture.TextureAtlasSprite;
import net.minecraft.item.ItemStack;
import net.minecraft.util.ResourceLocation;
import net.minecraftforge.fml.relauncher.Side;
import net.minecraftforge.fml.relauncher.SideOnly;
import zornco.bedcraftbeyond.BedCraftBeyond;
import zornco.bedcraftbeyond.common.util.ColorHelper;

public class TextureHelper {

    /*
     * Thanks to BluSunrize/TTFTCUTS
     */
    @SuppressWarnings("deprecation")
    @SideOnly(Side.CLIENT)
    public static Color getItemTextureColor(ItemStack item) throws Exception {
        //= item.getSpriteNumber()==1?TextureMap.locationItemsTexture:TextureMap.locationBlocksTexture;
        if(item == null || item.getItem() == null) return Color.WHITE;
        try {

            Block b = Block.getBlockFromItem(item.getItem());
            TextureAtlasSprite icon = Minecraft.getMinecraft().getBlockRendererDispatcher().getBlockModelShapes()
                .getModelForState(Block.getBlockFromItem(item.getItem())
                    .getStateFromMeta(item.getItemDamage()))
                        .getParticleTexture();

            if (!(icon instanceof TextureAtlasSprite))
                throw new Exception("Icon for the itemstack is not a valid sprite.");
            if (icon.getIconName().equalsIgnoreCase("missingno"))
                throw new Exception("Icon does not have a valid sprite- seems to be missing.");

            String iconName = icon.getIconName();
            String textureDomain = iconName.substring(0, Math.max(0, iconName.indexOf(':') + 1));
            String texturePath = "textures/" + iconName.substring(Math.max(0, iconName.indexOf(":") + 1)) + ".png";

            ResourceLocation resource = new ResourceLocation(textureDomain + texturePath);

            InputStream layer = Minecraft.getMinecraft().getResourceManager().getResource(resource).getInputStream();
            BufferedImage buffered = ImageIO.read(layer);

            return ColorHelper.getAverageColor(buffered, new Point(0,0), new Dimension(icon.getIconWidth(), icon.getIconHeight()));
        } catch (Exception e) {
            BedCraftBeyond.LOGGER.error(e);
            throw e;
        }
    }


}
