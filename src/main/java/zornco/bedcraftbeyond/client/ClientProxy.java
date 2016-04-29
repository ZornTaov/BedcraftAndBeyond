package zornco.bedcraftbeyond.client;

import net.minecraft.block.BlockBed;
import net.minecraft.block.properties.IProperty;
import net.minecraft.client.Minecraft;
import net.minecraft.client.renderer.block.model.ModelBakery;
import net.minecraft.client.renderer.block.statemap.StateMap;
import net.minecraft.client.renderer.color.IItemColor;
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
import zornco.bedcraftbeyond.BedCraftBeyond;
import zornco.bedcraftbeyond.blocks.BlockColoredBed;
import zornco.bedcraftbeyond.client.colors.DyeColorSingleLayer;
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
    ModelLoader.setCustomStateMapper(BedCraftBeyond.stoneBedBlock, (new StateMap.Builder()).ignore(new IProperty[]{BlockBed.OCCUPIED}).ignore(new IProperty[]{BlockBed.PART}).ignore(new IProperty[]{BlockBed.FACING}).build());

    for (int i = 0; i < EnumDyeColor.values().length; i++) {
      RenderingHelper.registerBlockModelAsItem(BedCraftBeyond.rugBlock, i, "rugBlock");
      //ModelBakery.registerItemVariants(Item.getItemFromBlock(BedCraftBeyond.rugBlock), names);
    }

    RenderingHelper.registerItemModel(BedCraftBeyond.scissors);
    RenderingHelper.registerItemModel(BedCraftBeyond.rugItem);

    RenderingHelper.registerItemModel(BedCraftBeyond.drawerKey);
    RenderingHelper.registerItemModel(BedCraftBeyond.stoneBedItem);
  }

  public void init(){
    IItemColor dye = new DyeColorSingleLayer();
    Minecraft.getMinecraft().getItemColors().registerItemColorHandler(dye, BedCraftBeyond.rugItem);
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
