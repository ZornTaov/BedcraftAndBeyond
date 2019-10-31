package zornco.bedcraftbeyond.beds.renderer;

import java.util.ArrayList;
import java.util.Arrays;
import java.util.Comparator;

import com.mojang.blaze3d.platform.GlStateManager;

import net.minecraft.block.BlockState;
import net.minecraft.client.renderer.Vector3f;
import net.minecraft.client.renderer.tileentity.TileEntityRenderer;
import net.minecraft.client.renderer.tileentity.model.BedModel;
import net.minecraft.item.DyeColor;
import net.minecraft.state.properties.BedPart;
import net.minecraft.util.Direction;
import net.minecraft.util.ResourceLocation;
import zornco.bedcraftbeyond.beds.blocks.RainbowBedBlock;
import zornco.bedcraftbeyond.beds.tileentity.RainbowBedTileEntity;

/**
 * RainbowBedTER
 */
public class RainbowBedTER extends TileEntityRenderer<RainbowBedTileEntity> {
   static ArrayList<Vector3f> colors = new ArrayList<Vector3f>();
	static
	{
		for (int r=0; r<100; r++) colors.add(new Vector3f(r*255/100,       255,         0));
		for (int g=100; g>0; g--) colors.add(new Vector3f(      255, g*255/100,         0));
		for (int b=0; b<100; b++) colors.add(new Vector3f(      255,         0, b*255/100));
		for (int r=100; r>0; r--) colors.add(new Vector3f(r*255/100,         0,       255));
		for (int g=0; g<100; g++) colors.add(new Vector3f(        0, g*255/100,       255));
		for (int b=100; b>0; b--) colors.add(new Vector3f(        0,       255, b*255/100));
		                          colors.add(new Vector3f(        0,       255,         0));
	}
   private static final ResourceLocation[] TEXTURES = Arrays.stream(DyeColor.values())
         .sorted(Comparator.comparingInt(DyeColor::getId)).map((p_199742_0_) -> {
            return new ResourceLocation("textures/entity/bed/" + p_199742_0_.getTranslationKey() + ".png");
         }).toArray((p_199743_0_) -> {
            return new ResourceLocation[p_199743_0_];
         });
   private final BedModel model = new BedModel();

   public void render(RainbowBedTileEntity tileEntityIn, double x, double y, double z, float partialTicks,
         int destroyStage) {
      if (destroyStage >= 0) {
         this.bindTexture(DESTROY_STAGES[destroyStage]);
         GlStateManager.matrixMode(5890);
         GlStateManager.pushMatrix();
         GlStateManager.scalef(4.0F, 4.0F, 1.0F);
         GlStateManager.translatef(0.0625F, 0.0625F, 0.0625F);
         GlStateManager.matrixMode(5888);
      } else {
         ResourceLocation resourcelocation = TEXTURES[tileEntityIn.getColor().getId()];
         if (resourcelocation != null) {
            this.bindTexture(resourcelocation);
         }
      }
      if (tileEntityIn.hasWorld()) {
         BlockState blockstate = tileEntityIn.getBlockState();
         boolean isHead = blockstate.get(RainbowBedBlock.PART) == BedPart.HEAD;
         this.func_199343_a(isHead, x, y, z,
               blockstate.get(RainbowBedBlock.HORIZONTAL_FACING), isHead ? colors.get(tileEntityIn.getRainbowColor1()): colors.get(tileEntityIn.getRainbowColor2()));
      } else {
         this.func_199343_a(true, x, y, z, Direction.SOUTH, colors.get(tileEntityIn.getRainbowColor1()));
         this.func_199343_a(false, x, y, z - 1.0D, Direction.SOUTH, colors.get(tileEntityIn.getRainbowColor2()));
      }

      GlStateManager.color3f(1, 1, 1);
      if (destroyStage >= 0) {
         GlStateManager.matrixMode(5890);
         GlStateManager.popMatrix();
         GlStateManager.matrixMode(5888);
      }

   }

   private void func_199343_a(boolean isHead, double x, double y, double z,
         Direction direction, Vector3f color) {
      this.model.preparePiece(isHead);
      GlStateManager.pushMatrix();
      GlStateManager.translatef((float) x, (float) y + 0.5625F, (float) z);
      GlStateManager.rotatef(90.0F, 1.0F, 0.0F, 0.0F);
      GlStateManager.translatef(0.5F, 0.5F, 0.5F);
      GlStateManager.rotatef(180.0F + direction.getHorizontalAngle(), 0.0F, 0.0F, 1.0F);
      GlStateManager.translatef(-0.5F, -0.5F, -0.5F);
      GlStateManager.enableRescaleNormal();
      
      GlStateManager.color3f(color.getX()/255.0F, color.getY()/255.0F, color.getZ()/255.0F);
      this.model.render();
      GlStateManager.color4f(1.0F, 1.0F, 1.0F, 1.0F);
      GlStateManager.popMatrix();
   }
}