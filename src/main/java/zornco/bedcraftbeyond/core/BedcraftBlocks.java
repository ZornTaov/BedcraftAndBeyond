package zornco.bedcraftbeyond.core;

import net.minecraft.block.Block;
import net.minecraftforge.eventbus.api.IEventBus;
import net.minecraftforge.fml.RegistryObject;
import net.minecraftforge.registries.DeferredRegister;
import net.minecraftforge.registries.ForgeRegistries;
import zornco.bedcraftbeyond.blocks.RainbowBedBlock;

public final class BedcraftBlocks {

    private static final DeferredRegister<Block> BLOCKS = new DeferredRegister<>(ForgeRegistries.BLOCKS, BedCraftBeyond.MOD_ID);

    public static final RegistryObject<Block> DRAWER_KEY = BLOCKS.register("rainbow_bed", () -> new RainbowBedBlock());

    public static void registerBlocks(IEventBus bus) {
        BLOCKS.register(bus);
    }
}