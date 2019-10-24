package zornco.bedcraftbeyond.core;

import java.util.Objects;
import java.util.function.Supplier;

import net.minecraft.block.Block;
import net.minecraft.item.BlockItem;
import net.minecraft.item.Item;
import net.minecraft.tileentity.TileEntity;
import net.minecraft.tileentity.TileEntityType;
import net.minecraftforge.eventbus.api.IEventBus;
import net.minecraftforge.fml.RegistryObject;
import net.minecraftforge.registries.DeferredRegister;
import net.minecraftforge.registries.ForgeRegistries;
import zornco.bedcraftbeyond.beds.blocks.StoneBedBlock;
import zornco.bedcraftbeyond.beds.tileentity.RainbowBedTileEntity;
import zornco.bedcraftbeyond.beds.blocks.RainbowBedBlock;

public final class BedcraftBlocks {

    private static final DeferredRegister<Block> BLOCKS 
        = new DeferredRegister<>(ForgeRegistries.BLOCKS, BedCraftBeyond.MOD_ID);
    private static final DeferredRegister<TileEntityType<?>> TILES 
        = new DeferredRegister<>(ForgeRegistries.TILE_ENTITIES, BedCraftBeyond.MOD_ID);

    //public static final RegistryObject<Block> RAINBOW_BED = registerBlock("rainbow_bed", () -> new RainbowBedBlock());
    public static final RegistryObject<Block> STONE_BED = registerBlock("stone_bed", () -> new StoneBedBlock());

    public static final RegistryObject<Block> rainbow_BED = registerBlock("rainbow_bed", () -> new RainbowBedBlock());

    public static final RegistryObject<TileEntityType<?>> Rainbowbedte = withItemAndTileEntity("rainbow_bed",
    BedcraftBlocks.rainbow_BED.get(), () -> new RainbowBedTileEntity());

    public static void registerBlocks(IEventBus bus) {
        BLOCKS.register(bus);
        TILES.register(bus);
    }
    
    private static RegistryObject<Block> registerBlock(String name, Supplier<Block> supplier) {
		RegistryObject<Block> block = BLOCKS.register(name, supplier);
        BedcraftItems.ITEMS.register(name, () -> new BlockItem(Objects.requireNonNull(block.get()), 
            new Item.Properties().setNoRepair().group(BedCraftBeyond.MAIN_GROUP)));
		return block;
	}
    
    private static RegistryObject<TileEntityType<?>> withItemAndTileEntity(String name, 
        Block block, Supplier<TileEntity> supplier) 
    {
        
        RegistryObject<TileEntityType<?>> tile = TILES.register(name, 
            () -> TileEntityType.Builder.create(supplier, block).build(null));
        BedCraftBeyond.LOGGER.info("registered " + name);
		return tile;
	}
}