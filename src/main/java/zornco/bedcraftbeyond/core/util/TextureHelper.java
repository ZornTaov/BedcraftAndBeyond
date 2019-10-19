package zornco.bedcraftbeyond.core.util;

import java.awt.*;
import java.awt.image.BufferedImage;
import java.io.InputStream;

import javax.imageio.ImageIO;

import net.minecraft.block.Block;
import net.minecraft.client.Minecraft;
import net.minecraft.client.renderer.texture.TextureAtlasSprite;
import net.minecraft.item.BlockItem;
import net.minecraft.item.ItemStack;
import net.minecraft.util.ResourceLocation;
import net.minecraftforge.api.distmarker.Dist;
import net.minecraftforge.api.distmarker.OnlyIn;
import zornco.bedcraftbeyond.core.BedCraftBeyond;

public class TextureHelper {

    /*
     * Thanks to BluSunrize/TTFTCUTS
     */
    @SuppressWarnings("deprecation")
    @OnlyIn(Dist.CLIENT)
    public static Color getItemTextureColor(ItemStack item) throws Exception {
        //= item.getSpriteNumber()==1?TextureMap.locationItemsTexture:TextureMap.locationBlocksTexture;
        if(item == null || item.getItem() == null) return Color.WHITE;
        try {

            Block b = Block.getBlockFromItem(item.getItem());
            TextureAtlasSprite icon = Minecraft.getInstance().getItemRenderer().getModelWithOverrides(item, null, null).getParticleTexture();
                //.getModel(item.get)
                //.getParticleTexture();
         //       		Block.getBlockFromItem(item.getItem())
         //           .getStateById(item.getDamage()))

            if (!(icon instanceof TextureAtlasSprite))
                throw new Exception("Icon for the itemstack is not a valid sprite.");
            if (icon.getName().getNamespace().equalsIgnoreCase("missingno"))
                throw new Exception("Icon does not have a valid sprite- seems to be missing.");

            String iconName = icon.getName().getNamespace();
            String textureDomain = iconName.substring(0, Math.max(0, iconName.indexOf(':') + 1));
            String texturePath = "textures/" + iconName.substring(Math.max(0, iconName.indexOf(":") + 1)) + ".png";

            ResourceLocation resource = new ResourceLocation(textureDomain + texturePath);

            InputStream layer = Minecraft.getInstance().getResourceManager().getResource(resource).getInputStream();
            BufferedImage buffered = ImageIO.read(layer);

            return ColorHelper.getAverageColor(buffered, new Point(0,0), new Dimension(icon.getWidth(), icon.getHeight()));
        } catch (Exception e) {
            BedCraftBeyond.LOGGER.error(e);
            throw e;
        }
    }


}
