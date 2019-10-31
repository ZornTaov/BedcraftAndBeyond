package zornco.bedcraftbeyond.core.util;

import java.awt.Color;
import java.util.ArrayList;
import java.util.stream.Stream;

import net.minecraft.client.Minecraft;
import net.minecraft.client.renderer.ItemRenderer;
import net.minecraft.client.renderer.texture.TextureAtlasSprite;
import net.minecraft.item.ItemStack;
import net.minecraftforge.api.distmarker.Dist;
import net.minecraftforge.api.distmarker.OnlyIn;
import net.minecraftforge.client.extensions.IForgeBakedModel;
import net.minecraftforge.client.model.data.EmptyModelData;
import zornco.bedcraftbeyond.core.BedCraftBeyond;

public class TextureHelper {

    /*
     * Thanks to BluSunrize/TTFTCUTS
     */
    @OnlyIn(Dist.CLIENT)
    public static Color getItemTextureColor(ItemStack item) throws Exception {
        if (item == null || item.getItem() == null)
            return Color.WHITE;

        try {

            ItemRenderer renderer = Minecraft.getInstance().getItemRenderer();

            IForgeBakedModel model = renderer.getModelWithOverrides(item, null, null);

            TextureAtlasSprite icon = model.getParticleTexture(EmptyModelData.INSTANCE);

            if (!(icon instanceof TextureAtlasSprite))
                throw new Exception("Icon for the itemstack is not a valid sprite.");

            if (icon.getName().getNamespace().equalsIgnoreCase("missingno"))
                throw new Exception("Icon does not have a valid sprite- seems to be missing.");

            Color[] colors = getColorsFromIcon(icon);
            return ColorHelper.getAverageColor(colors);

        } catch (Exception e) {
            BedCraftBeyond.LOGGER.error(e);
            throw e;
        }
    }

    public static Color[] getColorsFromIcon(TextureAtlasSprite icon) {
        ArrayList<Integer> colorList = new ArrayList<>();
        int pixelsX = icon.getWidth();
        int pixelsY = icon.getHeight();

        for (int x = 0; x < pixelsX; x++) {
            for (int y = 0; y < pixelsY; y++) {
                if (icon.isPixelTransparent(0, x, y))
                    continue;

                int pixelColor = icon.getPixelRGBA(0, x, y);
                colorList.add(pixelColor);
            }
        }

        Stream<Integer> uniqueColors = colorList.stream().distinct();
        Stream<Color> hsvColors = uniqueColors //
                .map(color -> Colors.RGB.fromRBGA(color)) //
                .map(colorRBGA -> {
                    try {
                        return Colors.RGB.toHSV(colorRBGA);
                    } catch (IllegalAccessException e) {
                        // shouldn't happen, this only happens if the above failed to create a correct
                        // array
                        // return black and print the error out just in case
                        e.printStackTrace();
                        return new float[] { 0, 0, 0 };
                    }
                }) //
                .map(colorHSV -> Color.getHSBColor(colorHSV[0], colorHSV[1], colorHSV[2]));

        return hsvColors.toArray(Color[]::new);
    }

}
