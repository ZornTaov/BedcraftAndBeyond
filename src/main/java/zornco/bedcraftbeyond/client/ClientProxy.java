package zornco.bedcraftbeyond.client;

import net.minecraft.block.properties.IProperty;
import net.minecraft.client.Minecraft;
import net.minecraft.client.renderer.block.statemap.StateMap;
import net.minecraft.client.renderer.color.IBlockColor;
import net.minecraft.client.renderer.color.IItemColor;
import net.minecraft.client.renderer.color.ItemColors;
import net.minecraft.client.resources.IReloadableResourceManager;
import net.minecraft.world.World;
import net.minecraftforge.client.model.ModelLoader;
import net.minecraftforge.client.model.b3d.B3DLoader;
import net.minecraftforge.fml.client.FMLClientHandler;
import zornco.bedcraftbeyond.BedCraftBeyond;
import zornco.bedcraftbeyond.common.blocks.BlockBedBase;
import zornco.bedcraftbeyond.common.frames.FrameHelper;
import zornco.bedcraftbeyond.common.blocks.BcbBlocks;
import zornco.bedcraftbeyond.common.blocks.BlockWoodenBed;
import zornco.bedcraftbeyond.common.CommonProxy;
import zornco.bedcraftbeyond.common.item.BcbItems;
import zornco.bedcraftbeyond.network.Registration;

public class ClientProxy extends CommonProxy {

   @Override
   public void registerModels() {

      ModelLoader.setCustomStateMapper(BcbBlocks.woodenBed, (new StateMap.Builder()).ignore(new IProperty[]{BlockBedBase.OCCUPIED, BlockWoodenBed.SHEETS, BlockWoodenBed.BLANKETS, BlockWoodenBed.HEAD }).build());
      ModelLoader.setCustomStateMapper(BcbBlocks.stoneBed, (new StateMap.Builder()).ignore(new IProperty[]{BlockBedBase.OCCUPIED }).build());

      RenderingHelper.registerItemModel(BcbItems.scissors);
      for(int i = 0; i < 16; ++i)
         RenderingHelper.registerItemModel(BcbItems.rug, "inventory", i);

      RenderingHelper.registerItemModel(BcbItems.blanket);
      RenderingHelper.registerItemModel(BcbItems.sheets);

      RenderingHelper.registerItemModel(BcbItems.drawerKey);
      RenderingHelper.registerItemModel(BcbItems.stoneBed);

      RenderingHelper.registerItemModel(BcbItems.dyeBottle);

      B3DLoader.INSTANCE.addDomain(BedCraftBeyond.MOD_ID);
   }

   public void init() {
      super.init();

      ItemColors c = Minecraft.getMinecraft().getItemColors();
      IItemColor dyeBottleColorer = new Colors.DyeBottleColorer();
      IItemColor linenColorer = new Colors.LinenColorer();
      IItemColor woolDamageColorer = new Colors.WoolDamageColorer();

      c.registerItemColorHandler(dyeBottleColorer, BcbItems.dyeBottle );
      c.registerItemColorHandler(woolDamageColorer, BcbItems.rug);
      c.registerItemColorHandler(linenColorer, BcbItems.blanket, BcbItems.sheets );

      IBlockColor coloredBed = new Colors.BedColorer();
      Minecraft.getMinecraft().getBlockColors().registerBlockColorHandler(coloredBed, BcbBlocks.woodenBed);
   }

   @Override
   public void compileFrames() {
      FrameHelper.compileFramesClient();
   }

   @Override
   public void registerMessages() {
      Registration.registerMessagesClient();
   }

   @Override
   public World getClientWorld() {
      return FMLClientHandler.instance().getClient().theWorld;
   }
}
