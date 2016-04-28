package zornco.bedcraftbeyond.client;

import net.minecraft.block.BlockBed;
import net.minecraft.block.properties.IProperty;
import net.minecraft.client.renderer.block.model.ModelBakery;
import net.minecraft.client.renderer.block.statemap.StateMap;
import net.minecraft.client.resources.IReloadableResourceManager;
import net.minecraft.entity.player.EntityPlayer;
import net.minecraft.item.EnumDyeColor;
import net.minecraft.item.Item;
import net.minecraft.item.ItemStack;
import net.minecraft.tileentity.TileEntity;
import net.minecraft.util.math.BlockPos;
import net.minecraft.util.ResourceLocation;
import net.minecraft.world.World;
import net.minecraftforge.client.model.ModelLoader;
import net.minecraftforge.fml.client.FMLClientHandler;
import net.minecraftforge.fml.client.registry.ClientRegistry;
import zornco.bedcraftbeyond.BedCraftBeyond;
import zornco.bedcraftbeyond.blocks.BlockBCBPlanks;
import zornco.bedcraftbeyond.blocks.BlockColoredBed;
import zornco.bedcraftbeyond.blocks.tiles.TileBedcraftBed;
import zornco.bedcraftbeyond.core.CommonProxy;
import zornco.bedcraftbeyond.util.ClientUtils;
import zornco.bedcraftbeyond.util.PlankHelperClient;
import zornco.bedcraftbeyond.util.RenderingHelper;

public class ClientProxy extends CommonProxy {

  @Override
  public void registerRenderInformationInit() {
    //RenderingRegistry.registerBlockHandler(new BlockRugRenderer());
  }

  @Override
  public void registerModels() {

    ModelLoader.setCustomStateMapper(BedCraftBeyond.coloredBedBlock, (new StateMap.Builder()).ignore(new IProperty[]{BlockBed.OCCUPIED, BlockColoredBed.SheetColor, BlockColoredBed.BlanketColor }).build());
    // ModelLoader.setCustomStateMapper(BedCraftBeyond.chestBedBlock, (new StateMap.Builder()).ignore(new IProperty[]{BlockBed.OCCUPIED}).ignore(new IProperty[]{BlockBed.PART}).ignore(new IProperty[]{BlockBed.FACING}).build());
    ModelLoader.setCustomStateMapper(BedCraftBeyond.stoneBedBlock, (new StateMap.Builder()).ignore(new IProperty[]{BlockBed.OCCUPIED}).ignore(new IProperty[]{BlockBed.PART}).ignore(new IProperty[]{BlockBed.FACING}).build());

    for (int i = 0; i < BlockBCBPlanks.EnumType.values().length; i++) {
      ModelBakery.registerItemVariants(Item.getItemFromBlock(BedCraftBeyond.plankBlock), new ResourceLocation(BedCraftBeyond.MOD_ID + ":plank/" + BlockBCBPlanks.EnumType.byMetadata(i)));
      RenderingHelper.registerBlockModelAsItem(BedCraftBeyond.plankBlock, i, "plank/" + BlockBCBPlanks.EnumType.byMetadata(i));
    }

    /*for (int i = 0; i < 255; i++) {
      RenderingHelper.registerItemModel(BedCraftBeyond.bedItem, i, ((ItemColoredBed) BedCraftBeyond.bedItem).getName(), "inventory");
      RenderingHelper.registerItemModel(BedCraftBeyond.chestBedItem, i, ((ItemColoredChestBed) BedCraftBeyond.chestBedItem).getName(), "inventory");
    }*/

    for (int i = 0; i < EnumDyeColor.values().length; i++) {
      RenderingHelper.registerBlockModelAsItem(BedCraftBeyond.rugBlock, i, "rugBlock");
      //ModelBakery.registerItemVariants(Item.getItemFromBlock(BedCraftBeyond.rugBlock), names);
    }

    RenderingHelper.registerItemModel(BedCraftBeyond.scissors);
    //registerItemModel(BedCraftBeyond.rugItem, ((IName) BedCraftBeyond.rugItem).getName());

    RenderingHelper.registerItemModel(BedCraftBeyond.drawerKey);
    RenderingHelper.registerItemModel(BedCraftBeyond.stoneBedItem);
  }

  @Override
  public void compilePlanks() {
    super.compilePlanks();

    ((IReloadableResourceManager) ClientUtils.mc().getResourceManager()).registerReloadListener(new PlankHelperClient());
  }

  @Override
  public int getAverageBlockColour(ItemStack stack2) {
    return ClientUtils.getAverageBlockColour(stack2);
  }

  @Override
  public World getClientWorld() {
    return FMLClientHandler.instance().getClient().theWorld;
  }

  @Override
  public Object getClientGuiElement(int ID, EntityPlayer player, World world, int x, int y, int z) {
    TileEntity te = world.getTileEntity(new BlockPos(x, y, z));
    // if (te != null && te instanceof TileBedcraftChestBed) return new GuiColoredChestBed(player.inventory, (TileBedcraftChestBed) te);

    return null;
  }
}
