package zornco.bedcraftbeyond.client;

import java.awt.Color;
import java.awt.image.BufferedImage;
import java.io.IOException;
import java.io.InputStream;
import java.util.HashMap;

import javax.imageio.ImageIO;

import net.minecraft.block.Block;
import net.minecraft.client.Minecraft;
import net.minecraft.client.renderer.texture.TextureAtlasSprite;
import net.minecraft.item.Item;
import net.minecraft.item.ItemStack;
import net.minecraft.util.ResourceLocation;

public class TextureUtils {

   /*
    * Thanks to BluSunrize/TTFTCUTS
    */
   static HashMap<String, ResourceLocation> textureMap = new HashMap<String, ResourceLocation>();

   public static ResourceLocation getResource(String path) {
      ResourceLocation rl = textureMap.containsKey(path) ? textureMap.get(path) : new ResourceLocation(path);
      if (!textureMap.containsKey(path))
         textureMap.put(path, rl);
      return rl;
   }

   public static BufferedImage getImageForResource(ResourceLocation resource) throws IOException {
      InputStream layer = Minecraft.getMinecraft().getResourceManager().getResource(resource).getInputStream();
      return ImageIO.read(layer);
   }

   // TODO: Change to throw errors instead, refactor so easier to understand
   public static int getAverageBlockColour(ItemStack block) throws Exception {
      int colour;
      ResourceLocation resource;
      BufferedImage buffered;
      //= item.getSpriteNumber()==1?TextureMap.locationItemsTexture:TextureMap.locationBlocksTexture;
      try {

         TextureAtlasSprite icon = Minecraft.getMinecraft().getBlockRendererDispatcher().getBlockModelShapes().getModelForState(Block.getBlockFromItem(block.getItem()).getStateFromMeta(block.getItemDamage())).getParticleTexture();//getTexture(block.getDefaultState());

         if (icon instanceof TextureAtlasSprite && !icon.getIconName().equals("missingno")) {
            String iconName = icon.getIconName();
            iconName = iconName.substring(0, Math.max(0, iconName.indexOf(":") + 1)) + "textures/" + iconName.substring(Math.max(0, iconName.indexOf(":") + 1)) + ".png";
            resource = getResource(iconName);
            buffered = getImageForResource(resource);

            colour = getAverageTextureColor(buffered, 0, 0, icon.getIconWidth(), icon.getIconHeight()).getRGB();
            colour = colour & 0xffffff;
         } else {
            return -3;
         }

      } catch (Exception e) {
         e.printStackTrace();
         throw e;
      }


      return colour;
   }

   /*
    * Where bi is your image, (x0,y0) is your upper left coordinate, and (w,h)
    * are your width and height respectively
    */
   public static Color getAverageTextureColor(BufferedImage bi, int x0, int y0, int w, int h) {
      int x1 = x0 + w;
      int y1 = y0 + h;
      long sumr = 0, sumg = 0, sumb = 0;
      for (int x = x0; x < x1; x++) {
         for (int y = y0; y < y1; y++) {
            Color pixel = new Color(bi.getRGB(x, y));
            sumr += pixel.getRed();
            sumg += pixel.getGreen();
            sumb += pixel.getBlue();
         }
      }
      int num = w * h;
      return new Color((int) (sumr / num), (int) (sumg / num), (int) (sumb / num));
   }
}
