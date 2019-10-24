package zornco.bedcraftbeyond.core;

import java.util.Objects;
import java.util.function.Supplier;

import net.minecraft.block.Block;
import net.minecraft.item.BlockItem;
import net.minecraft.item.Item;
import net.minecraftforge.eventbus.api.IEventBus;
import net.minecraftforge.fml.RegistryObject;
import net.minecraftforge.registries.DeferredRegister;
import net.minecraftforge.registries.ForgeRegistries;
import zornco.bedcraftbeyond.beds.blocks.StoneBedBlock;
import zornco.bedcraftbeyond.beds.blocks.RainbowBedBlock;

public final class BedcraftBlocks {

    private static final DeferredRegister<Block> BLOCKS = new DeferredRegister<>(ForgeRegistries.BLOCKS, BedCraftBeyond.MOD_ID);

    public static final RegistryObject<Block> RAINBOW_BED = withItemBlock("rainbow_bed", () -> new RainbowBedBlock());
    public static final RegistryObject<Block> STONE_BED = withItemBlock("stone_bed", () -> new StoneBedBlock());

    public static void registerBlocks(IEventBus bus) {
        BLOCKS.register(bus);
    }
    
    private static RegistryObject<Block> withItemBlock(String name, Supplier<Block> supplier) {
		RegistryObject<Block> block = BLOCKS.register(name, supplier);
		BedcraftItems.ITEMS.register(name, () -> new BlockItem(Objects.requireNonNull(block.get()), new Item.Properties().setNoRepair().group(BedCraftBeyond.MAIN_GROUP)));
		return block;
	}
}