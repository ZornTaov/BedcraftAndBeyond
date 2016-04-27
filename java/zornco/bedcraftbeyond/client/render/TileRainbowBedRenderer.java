package zornco.bedcraftbeyond.client.render;

import java.util.ArrayList;
import java.util.List;

import org.lwjgl.opengl.GL11;
import org.lwjgl.util.Color;

import net.minecraft.block.Block;
import net.minecraft.client.Minecraft;
import net.minecraft.client.renderer.OpenGlHelper;
import net.minecraft.client.renderer.Tessellator;
import net.minecraft.client.renderer.texture.TextureAtlasSprite;
import net.minecraft.client.renderer.tileentity.TileEntitySpecialRenderer;
import net.minecraft.entity.Entity;
import net.minecraft.item.Item;
import net.minecraft.item.ItemStack;
import net.minecraft.tileentity.TileEntity;
import net.minecraft.util.BlockPos;
import net.minecraft.util.EnumFacing;
import net.minecraft.util.ResourceLocation;
import net.minecraft.world.IBlockAccess;
import zornco.bedcraftbeyond.blocks.BlockColoredBed;
import zornco.bedcraftbeyond.blocks.BlockRainbowBed;
import zornco.bedcraftbeyond.blocks.TileColoredBed;
import zornco.bedcraftbeyond.blocks.TileColoredChestBed;
import zornco.bedcraftbeyond.blocks.TileRainbowBed;
import zornco.bedcraftbeyond.util.ClientUtils;

public class TileRainbowBedRenderer extends TileEntitySpecialRenderer {
	static ArrayList<Color> colors = new ArrayList<Color>();
	static
	{
		for (int r=0; r<100; r++) colors.add(new Color(r*255/100,       255,         0));
		for (int g=100; g>0; g--) colors.add(new Color(      255, g*255/100,         0));
		for (int b=0; b<100; b++) colors.add(new Color(      255,         0, b*255/100));
		for (int r=100; r>0; r--) colors.add(new Color(r*255/100,         0,       255));
		for (int g=0; g<100; g++) colors.add(new Color(        0, g*255/100,       255));
		for (int b=100; b>0; b--) colors.add(new Color(        0,       255, b*255/100));
		                          colors.add(new Color(        0,       255,         0));
	}
	private static final ResourceLocation[] bedTextures = new ResourceLocation[] {
			new ResourceLocation("bedcraftbeyond","textures/blocks/bed0.png"),
			new ResourceLocation("bedcraftbeyond","textures/blocks/bed1.png"),
			new ResourceLocation("bedcraftbeyond","textures/blocks/bed2.png"),
			new ResourceLocation("bedcraftbeyond","textures/blocks/bed3.png")
		};
		private static final ResourceLocation plankTextures = new ResourceLocation("bedcraftbeyond","textures/blocks/planks.png");

		private ModelColoredBed coloredBedModel = new ModelColoredBed();

		public void renderWorldBlock(TileEntity tile, IBlockAccess world, BlockPos pos,
				Block block, double x, double y, double z) {

			if (!(block instanceof BlockRainbowBed || !(world.getBlockState(pos).getBlock() instanceof BlockRainbowBed))) {
				return;
			}
			Tessellator tessellator = Tessellator.getInstance();
			boolean flag = !block.isBedFoot(world, pos);


			/*This will rotate your model corresponding to player direction that was when you placed the block. If you want this to work,
	        	add these lines to onBlockPlacedBy method in your block class.
	        	int dir = MathHelper.floor_double((double)((player.rotationYaw * 4F) / 360F) + 0.5D) & 3;
	        	world.setBlockMetadataWithNotify(x, y, z, dir, 0);*/

			EnumFacing dir = block.getBedDirection(world, pos);		

			GL11.glPushMatrix();
			switch(dir.getHorizontalIndex())
			{
			case 0:
			case 2:
				GL11.glTranslated(x+1, y, z);
				GL11.glScalef(-1.0F, -1.0F, 1.0F);
				break;
			case 1:
			case 3:
				GL11.glTranslated(x, y, z+1);
				GL11.glScalef(1.0F, -1.0F, -1.0F);
				break;

			}
			GL11.glTranslatef(0.5F, 0, 0.5F);
			//This line actually rotates the renderer.
			GL11.glRotatef(dir.getHorizontalIndex() * (-90F), 0F, 1F, 0F);
			//GL11.glTranslatef(-0.5F, 0, -0.5F);
			for (int m = 0; m < bedTextures.length; m++) {
				if (!(tile instanceof TileColoredChestBed) && m == 3) {
					break;
				}
				GL11.glDisable(GL11.GL_CULL_FACE);
				//This will make your block brightness dependent from surroundings lighting.
				float f = block.getMixedBrightnessForBlock(world, pos);
				int l = world.getCombinedLight(pos, 0);
				int l1 = l % 65536;
				int l2 = l / 65536;
				//tessellator.setColorOpaque_F(f, f, f);
				OpenGlHelper.setLightmapTextureCoords(OpenGlHelper.lightmapTexUnit, l1, l2);
				switch (m) {
				case 0:

					setColorOpaque_I(colors.get(((TileRainbowBed)tile).incColor1()));
					break;
				case 1:
					setColorOpaque_I(colors.get(((TileRainbowBed)tile).incColor2()));
					break;
				case 2:
					setColorOpaque_I(BlockColoredBed.getColorFromTilePerPass(world, pos, m));
					break;
				default:
					break;
				}
				//BedCraftBeyond.logger.info(BlockColoredBed.getColorFromTilePerPass(world, pos, m));
				this.bindTexture(bedTextures[m]);
				this.coloredBedModel.render((Entity)null, (flag?1:0), m, 0.0F, 0.0F, 0.0F, 0.0625F);
				GL11.glEnable(GL11.GL_CULL_FACE);
				if (m == 2) {
					ItemStack stack = ((TileColoredBed)tile).getPlankType(); 
					if (stack != null) {

						Item item = stack.getItem();
						TextureAtlasSprite icon = Minecraft.getMinecraft().getBlockRendererDispatcher().getBlockModelShapes().getTexture(Block.getBlockFromItem(item).getStateFromMeta(item.getDamage(stack)));

						ResourceLocation resource;
						if(icon instanceof TextureAtlasSprite)
						{
							TextureAtlasSprite tas = (TextureAtlasSprite) icon;
							String iconName = tas.getIconName();
							iconName = iconName.substring(0, Math.max(0,iconName.indexOf(":")+1)) + "textures/" + iconName.substring(Math.max(0,iconName.indexOf(":")+1)) + ".png";
							resource = ClientUtils.getResource(iconName);
							this.bindTexture(resource);
						}
					}
					else
					{
						this.bindTexture(plankTextures);
					}

					setColorOpaque_I(0xFFFFFF);
					this.coloredBedModel.renderPlank(0.0625F);
				}
			}
			setColorOpaque_I(0xFFFFFF);
			GL11.glPopMatrix();
		}
		private void setColorOpaque_I(Color color) {
			GL11.glColor3f(color.getRed() / 255.0F, color.getGreen() / 255.0F, color.getBlue() / 255.0F);
		}
		/**
		 * Sets the color to the given opaque value (stored as byte values packed in an integer).
		 */
		public void setColorOpaque_I(int j)
		{
			float f1 = (float)(j >> 16 & 255) / 255.0F;
			float f2 = (float)(j >> 8 & 255) / 255.0F;
			float f3 = (float)(j & 255) / 255.0F;
			GL11.glColor3f(f1, f2, f3);
		}
		@Override
		public void renderTileEntityAt(TileEntity tileEntity, double x, double y, double z, float partialTicks, int destroyStage) {
			GL11.glPushMatrix();
			//This will move our renderer so that it will be on proper place in the world
			TileColoredBed tileEntityYour = (TileColoredBed)tileEntity;
			/*Note that true tile entity coordinates (tileEntity.xCoord, etc) do not match to render coordinates (d, etc) that are calculated as [true coordinates] - [player coordinates (camera coordinates)]*/
			renderWorldBlock(tileEntityYour, tileEntity.getWorld(), tileEntity.getPos(), tileEntityYour.getBlockType(), (float)x, (float)y, (float)z);
			GL11.glPopMatrix();
		}
}
