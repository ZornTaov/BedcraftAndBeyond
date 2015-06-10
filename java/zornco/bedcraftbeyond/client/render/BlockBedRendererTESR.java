package zornco.bedcraftbeyond.client.render;

import org.lwjgl.opengl.GL11;

import net.minecraft.block.Block;
import net.minecraft.client.renderer.tileentity.*;
import net.minecraft.client.renderer.OpenGlHelper;
import net.minecraft.client.renderer.RenderBlocks;
import net.minecraft.client.renderer.Tessellator;
import net.minecraft.entity.Entity;
import net.minecraft.tileentity.TileEntity;
import net.minecraft.util.Direction;
import net.minecraft.util.IIcon;
import net.minecraft.util.ResourceLocation;
import net.minecraft.world.IBlockAccess;
import zornco.bedcraftbeyond.BedCraftBeyond;
import zornco.bedcraftbeyond.blocks.BlockColoredBed;
import zornco.bedcraftbeyond.blocks.TileColoredBed;
import zornco.bedcraftbeyond.blocks.TileColoredChestBed;
import zornco.bedcraftbeyond.blocks.TileStoneBed;
import cpw.mods.fml.client.registry.ISimpleBlockRenderingHandler;

public class BlockBedRendererTESR extends TileEntitySpecialRenderer {

	/*@Override
	public void renderInventoryBlock(Block block, int metadata, int modelID,
			RenderBlocks renderer) {
	}*/

	/*@Override
	public boolean renderWorldBlock(IBlockAccess world, int x, int y, int z,
			Block block1, int modelId, RenderBlocks renderer) {

		Tessellator tessellator = Tessellator.instance;
		BlockColoredBed block = (BlockColoredBed)block1; 
		int i1 = block.getBedDirection(world, x, y, z);
		boolean flag = block.isBedFoot(world, x, y, z);
		float f = 0.5F;
		float f1 = 1.0F;
		float f2 = 0.8F;
		float f3 = 0.6F;
		//int red, green, blue;
		//Render Bottom
		int j1 = block.getMixedBrightnessForBlock(world, x, y, z);
		tessellator.setBrightness(j1);
		tessellator.setColorOpaque_F(f, f, f);
		IIcon icon = block.getIcon(0, world.getBlockMetadata(x, y, z), 0, world, x, y, z);
		if (renderer.hasOverrideBlockTexture()) icon = renderer.overrideBlockTexture; //BugFix Proper breaking texture on underside
		double d0 = icon.getMinU();
		double d1 = icon.getMaxU();
		double d2 = icon.getMinV();
		double d3 = icon.getMaxV();
		double d4 = x + renderer.renderMinX;
		double d5 = x + renderer.renderMaxX;
		double d6 = y + renderer.renderMinY + 0.1875D;
		double d7 = z + renderer.renderMinZ;
		double d8 = z + renderer.renderMaxZ;
		tessellator.addVertexWithUV(d4, d6, d8, d0, d3);
		tessellator.addVertexWithUV(d4, d6, d7, d0, d2);
		tessellator.addVertexWithUV(d5, d6, d7, d1, d2);
		tessellator.addVertexWithUV(d5, d6, d8, d1, d3);
		
		//Render Top
		tessellator.setBrightness(block.getMixedBrightnessForBlock(renderer.blockAccess, x, y + 1, z));
		tessellator.setColorOpaque_F(f1, f1, f1);
		for (int i = 0; i < 3; i++) {
			tessellator.setColorOpaque_I(BlockColoredBed.getColorFromTilePerPass(world, x, y, z, i));
			icon = block.getIcon(1, world.getBlockMetadata(x, y, z), i, world, x, y, z);
			if (renderer.hasOverrideBlockTexture()) icon = renderer.overrideBlockTexture; //BugFix Proper breaking texture on underside
			d0 = icon.getMinU();
			d1 = icon.getMaxU();
			d2 = icon.getMinV();
			d3 = icon.getMaxV();
			d4 = d0;
			d5 = d1;
			d6 = d2;
			d7 = d2;
			d8 = d0;
			double d9 = d1;
			double d10 = d3;
			double d11 = d3;

			if (i1 == 0)
			{
				d5 = d0;
				d6 = d3;
				d8 = d1;
				d11 = d2;
			}
			else if (i1 == 2)
			{
				d4 = d1;
				d7 = d3;
				d9 = d0;
				d10 = d2;
			}
			else if (i1 == 3)
			{
				d4 = d1;
				d7 = d3;
				d9 = d0;
				d10 = d2;
				d5 = d0;
				d6 = d3;
				d8 = d1;
				d11 = d2;
			}

			double d12 = x + renderer.renderMinX;
			double d13 = x + renderer.renderMaxX;
			double d14 = y + renderer.renderMaxY;
			double d15 = z + renderer.renderMinZ;
			double d16 = z + renderer.renderMaxZ;
			tessellator.addVertexWithUV(d13, d14, d16, d8, d10);
			tessellator.addVertexWithUV(d13, d14, d15, d4, d6);
			tessellator.addVertexWithUV(d12, d14, d15, d5, d7);
			tessellator.addVertexWithUV(d12, d14, d16, d9, d11);
		}
		//Render Sides
		int k1 = Direction.directionToFacing[i1];

		if (flag)
		{
			k1 = Direction.directionToFacing[Direction.rotateOpposite[i1]];
		}

		byte b0 = 4;

		switch (i1)
		{
		case 0:
			b0 = 5;
			break;
		case 1:
			b0 = 3;
		case 2:
		default:
			break;
		case 3:
			b0 = 2;
		}

		if (k1 != 2 && (renderer.renderAllFaces || block.shouldSideBeRendered(renderer.blockAccess, x, y, z - 1, 2)))
		{
			tessellator.setBrightness(renderer.renderMinZ > 0.0D ? j1 : block.getMixedBrightnessForBlock(renderer.blockAccess, x, y, z - 1));
			tessellator.setColorOpaque_F(f2, f2, f2);
			for (int i = 0; i < 3; i++) {
				tessellator.setColorOpaque_I(BlockColoredBed.getColorFromTilePerPass(world, x, y, z, i));
				renderer.flipTexture = b0 == 2;
				renderer.renderFaceZNeg(block, x, y, z, block.getIcon(2, world.getBlockMetadata(x, y, z), i, world, x, y, z));
			}
		}
		if (k1 != 3 && (renderer.renderAllFaces || block.shouldSideBeRendered(renderer.blockAccess, x, y, z + 1, 3)))
		{
			tessellator.setBrightness(renderer.renderMaxZ < 1.0D ? j1 : block.getMixedBrightnessForBlock(renderer.blockAccess, x, y, z + 1));
			tessellator.setColorOpaque_F(f2, f2, f2);
			for (int i = 0; i < 3; i++) {
				tessellator.setColorOpaque_I(BlockColoredBed.getColorFromTilePerPass(world, x, y, z, i));
				renderer.flipTexture = b0 == 3;
				renderer.renderFaceZPos(block, x, y, z, block.getIcon(3, world.getBlockMetadata(x, y, z), i, world, x, y, z));
			}
		}

		if (k1 != 4 && (renderer.renderAllFaces || block.shouldSideBeRendered(renderer.blockAccess, x - 1, y, z, 4)))
		{
			tessellator.setBrightness(renderer.renderMinZ > 0.0D ? j1 : block.getMixedBrightnessForBlock(renderer.blockAccess, x - 1, y, z));
			tessellator.setColorOpaque_F(f3, f3, f3);
			for (int i = 0; i < 3; i++) {
				tessellator.setColorOpaque_I(BlockColoredBed.getColorFromTilePerPass(world, x, y, z, i));
				renderer.flipTexture = b0 == 4;
				renderer.renderFaceXNeg(block, x, y, z, block.getIcon(4, world.getBlockMetadata(x, y, z), i, world, x, y, z));
			}
		}

		if (k1 != 5 && (renderer.renderAllFaces || block.shouldSideBeRendered(renderer.blockAccess, x + 1, y, z, 5)))
		{
			tessellator.setBrightness(renderer.renderMaxZ < 1.0D ? j1 : block.getMixedBrightnessForBlock(renderer.blockAccess, x + 1, y, z));
			tessellator.setColorOpaque_F(f3, f3, f3);
			for (int i = 0; i < 3; i++) {
				tessellator.setColorOpaque_I(BlockColoredBed.getColorFromTilePerPass(world, x, y, z, i));
				renderer.flipTexture = b0 == 5;
				renderer.renderFaceXPos(block, x, y, z, block.getIcon(5, world.getBlockMetadata(x, y, z), i, world, x, y, z));
			}
		}

		renderer.flipTexture = false;
		return true;
	}*/

	/*@Override
	public boolean shouldRender3DInInventory(int modelID) {
		return false;
	}

	@Override
	public int getRenderId() {
		return BedCraftBeyond.bedRI;
	}*/

	private static final ResourceLocation[] bedTextures = new ResourceLocation[] {
		new ResourceLocation("bedcraftbeyond","textures/blocks/bed0.png"),
		new ResourceLocation("bedcraftbeyond","textures/blocks/bed1.png"),
		new ResourceLocation("bedcraftbeyond","textures/blocks/bed2.png"),
		new ResourceLocation("bedcraftbeyond","textures/blocks/bed3.png")
		};

	private ModelColoredBed coloredBedModel = new ModelColoredBed();

	public void renderWorldBlock(TileEntity tile, IBlockAccess world, int i, int j, int k,
			Block block, double x, double y, double z) {

		Tessellator tessellator = Tessellator.instance;
		boolean flag = block.isBedFoot(world, i, j, k);


		/*This will rotate your model corresponding to player direction that was when you placed the block. If you want this to work,
        	add these lines to onBlockPlacedBy method in your block class.
        	int dir = MathHelper.floor_double((double)((player.rotationYaw * 4F) / 360F) + 0.5D) & 3;
        	world.setBlockMetadataWithNotify(x, y, z, dir, 0);*/

		int dir = block.getBedDirection(world, i, j, k);		

		GL11.glPushMatrix();
		switch(dir)
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
		GL11.glRotatef(dir * (-90F), 0F, 1F, 0F);
		//GL11.glTranslatef(-0.5F, 0, -0.5F);
		GL11.glDisable(GL11.GL_CULL_FACE);
		for (int m = 0; m < bedTextures.length; m++) {
			if (!(tile instanceof TileColoredChestBed) && m == 3) {
				break;
			}
			//This will make your block brightness dependent from surroundings lighting.
			float f = block.getMixedBrightnessForBlock(world, i, j, k);
			int l = world.getLightBrightnessForSkyBlocks(i, j, k, 0);
			int l1 = l % 65536;
			int l2 = l / 65536;
			tessellator.setColorOpaque_F(f, f, f);
			OpenGlHelper.setLightmapTextureCoords(OpenGlHelper.lightmapTexUnit, l1, l2);
			setColorOpaque_I(BlockColoredBed.getColorFromTilePerPass(world, i, j, k, m));
			//BedCraftBeyond.logger.info(BlockColoredBed.getColorFromTilePerPass(world, i, j, k, m));
			if (tile instanceof TileColoredChestBed && m == 3) {
				setColorOpaque_I(BlockColoredBed.getColorFromTilePerPass(world, i, j, k, m-1));
				this.bindTexture(bedTextures[m]);
				this.coloredBedModel.render((Entity)null, (flag?1:0), m, -0.1F, 0.0F, 0.0F, 0.0625F);
			}
			if (m == 2) {
				this.bindTexture(bedTextures[m]);
				this.coloredBedModel.renderPlank(0.0625F);
			}
			this.bindTexture(bedTextures[m]);
			this.coloredBedModel.render((Entity)null, (flag?1:0), m, -0.1F, 0.0F, 0.0F, 0.0625F);
		}
		GL11.glEnable(GL11.GL_CULL_FACE);
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
	public void renderTileEntityAt(TileEntity tileEntity, double d0, double d1,
			double d2, float f) {
		GL11.glPushMatrix();
		//This will move our renderer so that it will be on proper place in the world
		TileColoredBed tileEntityYour = (TileColoredBed)tileEntity;
		/*Note that true tile entity coordinates (tileEntity.xCoord, etc) do not match to render coordinates (d, etc) that are calculated as [true coordinates] - [player coordinates (camera coordinates)]*/
		renderWorldBlock(tileEntityYour, tileEntity.getWorldObj(), tileEntity.xCoord, tileEntity.yCoord, tileEntity.zCoord, BedCraftBeyond.stoneBedBlock, (float)d0, (float)d1, (float)d2);
		GL11.glPopMatrix();
	}
}
