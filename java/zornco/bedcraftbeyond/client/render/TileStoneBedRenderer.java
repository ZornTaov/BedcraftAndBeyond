package zornco.bedcraftbeyond.client.render;

import org.lwjgl.opengl.GL11;

import net.minecraft.block.Block;
import net.minecraft.client.renderer.OpenGlHelper;
import net.minecraft.client.renderer.Tessellator;
import net.minecraft.client.renderer.tileentity.TileEntitySpecialRenderer;
import net.minecraft.entity.Entity;
import net.minecraft.tileentity.TileEntity;
import net.minecraft.util.BlockPos;
import net.minecraft.util.EnumFacing;
import net.minecraft.util.ResourceLocation;
import net.minecraft.world.IBlockAccess;
import zornco.bedcraftbeyond.BedCraftBeyond;
import zornco.bedcraftbeyond.blocks.TileStoneBed;

public class TileStoneBedRenderer extends TileEntitySpecialRenderer {

	private static final ResourceLocation stoneBedTexture = new ResourceLocation("bedcraftbeyond","textures/blocks/stoneBed.png");

	private ModelStoneBed stoneBedModel = new ModelStoneBed();

	public void renderWorldBlock(TileEntity tile, IBlockAccess world, int i, int j, int k,
			Block block, double x, double y, double z) {

		Tessellator tessellator = Tessellator.getInstance();
		boolean flag = !block.isBedFoot(world, new BlockPos(i, j, k));

		//This will make your block brightness dependent from surroundings lighting.
		int f = block.getMixedBrightnessForBlock(world, new BlockPos(i, j, k));
		int l = world.getCombinedLight(new BlockPos(i, j, k), 0);
		int l1 = l % 65536;
		int l2 = l / 65536;
		//tessellator.getWorldRenderer().putBrightness4(f, f, f, f);
		OpenGlHelper.setLightmapTextureCoords(OpenGlHelper.lightmapTexUnit, l1, l2);

		/*This will rotate your model corresponding to player direction that was when you placed the block. If you want this to work,
        	add these lines to onBlockPlacedBy method in your block class.
        	int dir = MathHelper.floor_double((double)((player.rotationYaw * 4F) / 360F) + 0.5D) & 3;
        	world.setBlockMetadataWithNotify(x, y, z, dir, 0);*/

		EnumFacing dir = block.getBedDirection(world, new BlockPos(i, j, k));		

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
		this.bindTexture(stoneBedTexture);
		/*
         		Place your rendering code here.
		 */
		this.stoneBedModel.render((Entity)null, (flag?1:0), 0.0F, -0.1F, 0.0F, 0.0F, 0.0625F);

		GL11.glPopMatrix();
	}

	@Override
	public void renderTileEntityAt(TileEntity tileEntity, double x, double y, double z, float partialTicks, int destroyStage) {
		GL11.glPushMatrix();
		//This will move our renderer so that it will be on proper place in the world
		TileStoneBed tileEntityYour = (TileStoneBed)tileEntity;
		/*Note that true tile entity coordinates (tileEntity.xCoord, etc) do not match to render coordinates (d, etc) that are calculated as [true coordinates] - [player coordinates (camera coordinates)]*/
		renderWorldBlock(tileEntityYour, tileEntity.getWorld(), tileEntity.getPos().getX(), tileEntity.getPos().getY(), tileEntity.getPos().getZ(), BedCraftBeyond.stoneBedBlock, (float)x, (float)y, (float)z);
		GL11.glPopMatrix();
	}

}
