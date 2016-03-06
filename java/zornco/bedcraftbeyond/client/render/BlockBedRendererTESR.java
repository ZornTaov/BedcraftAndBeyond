package zornco.bedcraftbeyond.client.render;

import org.lwjgl.opengl.GL11;

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
import zornco.bedcraftbeyond.BedCraftBeyond;
import zornco.bedcraftbeyond.blocks.BlockColoredBed;
import zornco.bedcraftbeyond.blocks.TileColoredBed;
import zornco.bedcraftbeyond.blocks.TileColoredChestBed;
import zornco.bedcraftbeyond.util.ClientUtils;

public class BlockBedRendererTESR extends TileEntitySpecialRenderer {

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

		if (!(block instanceof BlockColoredBed)) {
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
			GL11.glDisable(GL11.GL_CULL_FACE);
			if (!(tile instanceof TileColoredChestBed) && m == 3) {
				break;
			}
			//This will make your block brightness dependent from surroundings lighting.
			float f = block.getMixedBrightnessForBlock(world, pos);
			int l = world.getCombinedLight(pos, 0);
			int l1 = l % 65536;
			int l2 = l / 65536;
			//tessellator.setColorOpaque_F(f, f, f);
			OpenGlHelper.setLightmapTextureCoords(OpenGlHelper.lightmapTexUnit, l1, l2);
			setColorOpaque_I(BlockColoredBed.getColorFromTilePerPass(world, pos, m));
			//BedCraftBeyond.logger.info(BlockColoredBed.getColorFromTilePerPass(world, pos, m));
			this.bindTexture(bedTextures[m]);
			this.coloredBedModel.render((Entity)null, (flag?1:0), m, 0.0F, 0.0F, 0.0F, 0.0625F);
			if (tile instanceof TileColoredChestBed && m == 3) {
				setColorOpaque_I(BlockColoredBed.getColorFromTilePerPass(world, pos, m-1));
				this.bindTexture(bedTextures[m]);
				this.coloredBedModel.render((Entity)null, (flag?1:0), m, 0.0F, 0.0F, 0.0F, 0.0625F);
			}
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
				if(tile instanceof TileColoredChestBed && m == 2) 
					GL11.glTranslatef(0, (3.0F/16.0F), 0);

				setColorOpaque_I(0xFFFFFF);
				this.coloredBedModel.renderPlank(0.0625F);
				setColorOpaque_I(BlockColoredBed.getColorFromTilePerPass(world, pos, m));
				if(tile instanceof TileColoredChestBed && m == 2) 
					GL11.glTranslatef(0, -(3.0F/16.0F), 0);
			}
		}
		setColorOpaque_I(0xFFFFFF);
		GL11.glPopMatrix();
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
