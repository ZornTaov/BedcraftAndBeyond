package zornco.bedcraftbeyond.client;

import net.minecraft.block.BlockBed;
import net.minecraft.block.properties.IProperty;
import net.minecraft.client.Minecraft;
import net.minecraft.client.renderer.block.statemap.StateMap;
import net.minecraft.client.renderer.color.IBlockColor;
import net.minecraft.client.renderer.color.IItemColor;
import net.minecraft.client.resources.IReloadableResourceManager;
import net.minecraft.item.ItemStack;
import net.minecraft.world.World;
import net.minecraftforge.client.model.ModelLoader;
import net.minecraftforge.fml.client.FMLClientHandler;
import zornco.bedcraftbeyond.common.frames.FrameHelper;
import zornco.bedcraftbeyond.common.blocks.BcbBlocks;
import zornco.bedcraftbeyond.common.blocks.BlockWoodenBed;
import zornco.bedcraftbeyond.client.colors.BedFabricColorer;
import zornco.bedcraftbeyond.client.colors.DyeColorSingleLayer;
import zornco.bedcraftbeyond.common.CommonProxy;
import zornco.bedcraftbeyond.common.item.BcbItems;

public class ClientProxy extends CommonProxy {

   @Override
   public void registerModels() {

      ModelLoader.setCustomStateMapper(BcbBlocks.woodenBed, (new StateMap.Builder()).ignore(new IProperty[]{BlockBed.OCCUPIED, BlockWoodenBed.SHEETS, BlockWoodenBed.BLANKETS}).build());
      ModelLoader.setCustomStateMapper(BcbBlocks.stoneBed, (new StateMap.Builder()).ignore(new IProperty[]{BlockBed.OCCUPIED}).ignore(new IProperty[]{BlockBed.PART}).ignore(new IProperty[]{BlockBed.FACING}).build());

      RenderingHelper.registerItemModel(BcbItems.scissors);
      RenderingHelper.registerItemModel(BcbItems.rug);

      RenderingHelper.registerItemModel(BcbItems.drawerKey);
      RenderingHelper.registerItemModel(BcbItems.stoneBed);
   }

   public void init() {
      IItemColor dye = new DyeColorSingleLayer();
      Minecraft.getMinecraft().getItemColors().registerItemColorHandler(dye, BcbItems.rug);

      IBlockColor coloredBed = new BedFabricColorer();
      Minecraft.getMinecraft().getBlockColors().registerBlockColorHandler(coloredBed, BcbBlocks.woodenBed);
   }

   @Override
   public void compileFrames() {
      FrameHelper.compileFramesClient();
   }

   @Override
   public void compilePlanks() {
      super.compilePlanks();

      ((IReloadableResourceManager) Minecraft.getMinecraft().getResourceManager()).registerReloadListener(new PlankReloadListener());
   }

   @Override
   public int getAverageBlockColour(ItemStack stack2) {
      return ClientUtils.getAverageBlockColour(stack2);
   }

   @Override
   public World getClientWorld() {
      return FMLClientHandler.instance().getClient().theWorld;
   }
}
