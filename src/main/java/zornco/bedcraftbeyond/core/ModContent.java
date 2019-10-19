package zornco.bedcraftbeyond.core;

import java.util.ArrayList;
import java.util.List;

import net.minecraft.block.Block;
import net.minecraft.item.Item;
import net.minecraft.item.Item.Properties;
import net.minecraftforge.event.RegistryEvent;
import net.minecraftforge.eventbus.api.SubscribeEvent;
import net.minecraftforge.fml.common.Mod;
import net.minecraftforge.fml.common.registry.GameRegistry;
import zornco.bedcraftbeyond.storage.ItemDrawerKey;

public class ModContent {

    public static class Blocks {

        //public static BlockWoodenBed woodenBed = new zornco.bedcraftbeyond.frames.wooden.BlockWoodenBed();
        //public static BlockStoneBed stoneBed = new zornco.bedcraftbeyond.frames.stone.BlockStoneBed();
        //
        //public static BlockRug rug = new BlockRug();
    }

    public static class Items {
        public static List<Item> itemList = new ArrayList<Item>();

        //// Bed stuff
        //public static ItemWoodenFrame woodenBed = new ItemWoodenFrame(Blocks.woodenBed);
        public static ItemDrawerKey drawerKey = new ItemDrawerKey(new Properties());
        //
        //// Other stuff
        //public static ItemStoneBed stoneBed = new ItemStoneBed(Blocks.stoneBed);
        //public static ItemRug rug = new ItemRug();
        //public static ItemScissors scissors = new ItemScissors();
        //
        //// Suitcase stuff
        //public static ItemSuitcase suitcase = new ItemSuitcase();
        //
        //// Dying
        //public static ItemDyeBottle dyeBottle = new ItemDyeBottle();
        //public static ItemEyedropper eyedropper = new ItemEyedropper();
        //
        //// Storage
        //public static ItemDrawer drawer = new ItemDrawer();
        //public static ItemReinforcedDrawer reinforcedDrawer = new ItemReinforcedDrawer();
        //
        //// Linens - Handled by the parts
        //public static ItemBlanket blanket;
        //public static ItemSheets sheets;
    }

    public static class BedParts {
        //public static DrawerPart drawer = new DrawerPart();
        //public static BlanketPart blanket = new BlanketPart();
        //public static SheetPart sheet = new SheetPart();
        //public static ReinforcedDrawerPart drawerReinforced = new ReinforcedDrawerPart();
    }

    public static class TileEntities {
        public static void registerTiles() {
            //GameRegistry.registerTileEntity(TileGenericBed.class, "generic_bed");
            //GameRegistry.registerTileEntity(TileWoodenBed.class, "wooden_bed");
        }
    }
    // You can use EventBusSubscriber to automatically subscribe events on the contained class (this is subscribing to the MOD
    // Event bus for receiving Registry Events)
    @Mod.EventBusSubscriber(modid=BedCraftBeyond.MOD_ID, bus=Mod.EventBusSubscriber.Bus.MOD)
    public static class RegistryEvents {
        @SubscribeEvent
        public static void onBlocksRegistry(final RegistryEvent.Register<Block> blockRegistryEvent) {
            // register a new block here
            BedCraftBeyond.LOGGER.info("HELLO from Register Block");
        }
        @SubscribeEvent
        public static void onItemsRegistry(final RegistryEvent.Register<Item> itemRegistryEvent) {
            // register a new block here
        	BedCraftBeyond.LOGGER.info("HELLO from Register Item");
        	for (Item item : ModContent.Items.itemList) {
				itemRegistryEvent.getRegistry().register(item);
			}
        }
    }
}
