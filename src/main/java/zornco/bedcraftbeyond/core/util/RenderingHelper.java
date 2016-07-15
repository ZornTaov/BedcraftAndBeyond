package zornco.bedcraftbeyond.core.util;

import net.minecraft.block.Block;
import net.minecraft.client.renderer.block.model.ModelResourceLocation;
import net.minecraft.item.Item;
import net.minecraftforge.client.model.ModelLoader;

public class RenderingHelper {

  public static void registerBlockModelAsItem(final Block block, int meta) {
    registerBlockModelAsItem(block, meta, "inventory");
  }
  public static void registerBlockModelAsItem(final Block block, int meta, final String variantName) {
    registerBlockModelAsItem(block, meta, variantName, "");
  }

  public static void registerBlockModelAsItem(final Block block, int meta, final String variantName, final String blockName) {
	  Item item = Item.getItemFromBlock(block);
	  ModelLoader.setCustomModelResourceLocation(item, meta, new ModelResourceLocation(item.getRegistryName() + (blockName.equals("") ? "" : ("_" + blockName)), variantName));
  }

  public static void registerItemModel(final Item item){
    registerItemModel(item, "inventory");
  }

  public static void registerItemModel(final Item item, final String variant){
    registerItemModel(item, variant, 0);
  }

  public static void registerItemModel(final Item item, final String variant, final int meta){
    ModelLoader.setCustomModelResourceLocation(item, meta, new ModelResourceLocation(item.getRegistryName(), variant));
  }
}
