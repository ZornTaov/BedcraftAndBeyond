package zornco.bedcraftbeyond.util;

import java.awt.Color;
import java.awt.image.BufferedImage;
import java.io.IOException;
import java.io.InputStream;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;

import javax.imageio.ImageIO;

import net.minecraft.block.Block;
import net.minecraft.client.Minecraft;
import net.minecraft.client.renderer.texture.TextureAtlasSprite;
import net.minecraft.item.Item;
import net.minecraft.item.ItemStack;
import net.minecraft.util.IIcon;
import net.minecraft.util.ResourceLocation;

public class ClientUtils {
	
	/*
	 * Thanks to BluSunrize/TTFTCUTS
	 */
	static HashMap<String,ResourceLocation> textureMap = new HashMap<String,ResourceLocation>();
	public static ResourceLocation getResource(String path)
	{
		ResourceLocation rl = textureMap.containsKey(path) ? textureMap.get(path) : new ResourceLocation(path);
		if(!textureMap.containsKey(path))
			textureMap.put(path, rl);
		return rl;
	}

	public static BufferedImage getImageForResource(ResourceLocation resource) throws IOException
	{
		InputStream layer = Minecraft.getMinecraft().getResourceManager().getResource(resource).getInputStream();
		return ImageIO.read(layer);
	}
	
	public static List<Integer> getItemColours(ItemStack stack)
	{
		List<Integer> colourSet = new ArrayList();
		Item item = stack.getItem();

		ResourceLocation resource;
		BufferedImage buffered;
		//= item.getSpriteNumber()==1?TextureMap.locationItemsTexture:TextureMap.locationBlocksTexture;
		try{

			for(int pass=0; pass<item.getRenderPasses(stack.getItemDamage()); pass++)
			{
				IIcon icon = item.getIcon(stack, pass);

				if(icon instanceof TextureAtlasSprite)
				{
					TextureAtlasSprite tas = (TextureAtlasSprite) icon;
					String iconName = tas.getIconName();
					iconName = iconName.substring(0, Math.max(0,iconName.indexOf(":")+1)) + (item.getSpriteNumber()==0?"textures/blocks/":"textures/items/") + iconName.substring(Math.max(0,iconName.indexOf(":")+1)) + ".png";
					resource = getResource(iconName);
					buffered = getImageForResource(resource);
					int passColour = item.getColorFromItemStack(stack, pass);
					
					int[] data = new int[buffered.getWidth()*buffered.getHeight()];
					buffered.getRGB(0,0, buffered.getWidth(),buffered.getHeight(), data, 0,tas.getIconWidth());
					//buffered.getRGB(tas.getOriginX(),tas.getOriginY(), tas.getIconWidth(),tas.getIconHeight(), data, 0,tas.getIconWidth());
					for(int rgb : data)
						if(rgb!=0)
						{
							int coloured = blendColoursToInt(rgb,passColour)&0xffffff;
							if(coloured>0 /*&& !colourSet.contains(coloured)*/)
								colourSet.add(coloured);
						}
				}

			}
		}catch(Exception e)
		{
			e.printStackTrace();
		}


		return colourSet;
	}
	public static int getAverageItemColour(ItemStack stack)
	{
		List<Integer> col = ClientUtils.getItemColours(stack);
		if(col!=null && col.size()>0)
		{
			int rgb = 0xffffff;
			for(int rgb2 : col)
				rgb = ClientUtils.blendColoursToInt(rgb, rgb2);
			return rgb&0xffffff;
		}
		return 0xffffff;
	}
	public static List<Integer> getBlockColours(ItemStack stack)
	{
		List<Integer> colourSet = new ArrayList();
		Item item = stack.getItem();
		Block block = Block.getBlockFromItem(stack.getItem());

		ResourceLocation resource;
		BufferedImage buffered;
		//= item.getSpriteNumber()==1?TextureMap.locationItemsTexture:TextureMap.locationBlocksTexture;
		try{

			IIcon icon = block.getIcon(0, stack.getItemDamage());

			if(icon instanceof TextureAtlasSprite)
			{
				TextureAtlasSprite tas = (TextureAtlasSprite) icon;
				String iconName = tas.getIconName();
				iconName = iconName.substring(0, Math.max(0,iconName.indexOf(":")+1)) + (item.getSpriteNumber()==0?"textures/blocks/":"textures/items/") + iconName.substring(Math.max(0,iconName.indexOf(":")+1)) + ".png";
				resource = getResource(iconName);
				buffered = getImageForResource(resource);
				int passColour = block.getRenderColor(stack.getItemDamage());
				
				int[] data = new int[buffered.getWidth()*buffered.getHeight()];
				buffered.getRGB(0,0, buffered.getWidth(),buffered.getHeight(), data, 0,tas.getIconWidth());
				//buffered.getRGB(tas.getOriginX(),tas.getOriginY(), tas.getIconWidth(),tas.getIconHeight(), data, 0,tas.getIconWidth());
				for(int rgb : data)
					if(rgb!=0)
					{
						int coloured = blendColoursToInt(rgb,passColour)&0xffffff;
						if(coloured>0 /*&& !colourSet.contains(coloured)*/)
							colourSet.add(coloured);
					}
			}

		}catch(Exception e)
		{
			e.printStackTrace();
		}


		return colourSet;
	}
	public static int getAverageBlockColour(ItemStack stack)
	{
		List<Integer> col = ClientUtils.getBlockColours(stack);
		if(col!=null && col.size()>0)
		{
			int rgb = 0xffffff;
			for(int rgb2 : col)
				rgb = ClientUtils.blendColoursToInt(rgb, rgb2);
			return rgb&0xffffff;
		}
		return 0xffffff;
	}

	public static int blendColoursToInt(Object o0, Object o1)
	{
		return blendColours(o0,o1).getRGB();
	}
	public static Color blendColours(Object o0, Object o1)
	{
		assert(o0 instanceof Color || o0 instanceof Integer);
		assert(o1 instanceof Color || o1 instanceof Integer);
		Color c0 = o0 instanceof Color?(Color)o0:new Color((Integer)o0);
		Color c1 = o1 instanceof Color?(Color)o1:new Color((Integer)o1);

		double totalAlpha = c0.getAlpha() + c1.getAlpha();
		double weight0 = c0.getAlpha() / totalAlpha;
		double weight1 = c1.getAlpha() / totalAlpha;

		double r = weight0 * c0.getRed() + weight1 * c1.getRed();
		double g = weight0 * c0.getGreen() + weight1 * c1.getGreen();
		double b = weight0 * c0.getBlue() + weight1 * c1.getBlue();
		double a = Math.max(c0.getAlpha(), c1.getAlpha());
		return new Color((int) r, (int) g, (int) b, (int) a);
	}
}
