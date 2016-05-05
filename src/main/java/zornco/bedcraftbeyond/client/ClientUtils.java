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

public class ClientUtils {

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

   public static int getAverageBlockColour(ItemStack stack) {
      int colour = Color.WHITE.getRGB();
      Item item = stack.getItem();
      Block block = Block.getBlockFromItem(stack.getItem());

      ResourceLocation resource;
      BufferedImage buffered;
      //= item.getSpriteNumber()==1?TextureMap.locationItemsTexture:TextureMap.locationBlocksTexture;
      try {

         TextureAtlasSprite icon = Minecraft.getMinecraft().getBlockRendererDispatcher().getBlockModelShapes().getModelForState(block.getStateFromMeta(item.getDamage(stack))).getParticleTexture();//getTexture(block.getDefaultState());

         if (icon instanceof TextureAtlasSprite && !icon.getIconName().equals("missingno")) {
            String iconName = icon.getIconName();
            iconName = iconName.substring(0, Math.max(0, iconName.indexOf(":") + 1)) + "textures/" + iconName.substring(Math.max(0, iconName.indexOf(":") + 1)) + ".png";
            resource = getResource(iconName);
            buffered = getImageForResource(resource);

            colour = averageColor(buffered, 0, 0, icon.getIconWidth(), icon.getIconHeight()).getRGB();
            colour = colour & 0xffffff;
         } else {
            return -3;
         }

      } catch (Exception e) {
         e.printStackTrace();
         return -2;
      }


      return colour;
   }

   public static Color blendColours(Object o0, Object o1) {
      assert (o0 instanceof Color || o0 instanceof Integer);
      assert (o1 instanceof Color || o1 instanceof Integer);
      Color c0 = o0 instanceof Color ? (Color) o0 : new Color((Integer) o0);
      Color c1 = o1 instanceof Color ? (Color) o1 : new Color((Integer) o1);

      double totalAlpha = c0.getAlpha() + c1.getAlpha();
      double weight0 = c0.getAlpha() / totalAlpha;
      double weight1 = c1.getAlpha() / totalAlpha;

      double r = weight0 * c0.getRed() + weight1 * c1.getRed();
      double g = weight0 * c0.getGreen() + weight1 * c1.getGreen();
      double b = weight0 * c0.getBlue() + weight1 * c1.getBlue();
      double a = Math.max(c0.getAlpha(), c1.getAlpha());
      return new Color((int) r, (int) g, (int) b, (int) a);
   }

   public static int blendColoursToInt(BufferedImage bi, int x0, int y0, int w, int h) {
      return averageColor(bi, x0, y0, w, h).getRGB();
   }

   /*
    * Where bi is your image, (x0,y0) is your upper left coordinate, and (w,h)
    * are your width and height respectively
    */
   public static Color averageColor(BufferedImage bi, int x0, int y0, int w, int h) {
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
