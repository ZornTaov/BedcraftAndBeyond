package zornco.bedcraftbeyond.core;

import net.minecraft.item.Item;
import net.minecraftforge.eventbus.api.IEventBus;
import net.minecraftforge.fml.RegistryObject;
import net.minecraftforge.registries.DeferredRegister;
import net.minecraftforge.registries.ForgeRegistries;
import zornco.bedcraftbeyond.storage.ItemDrawerKey;

public final class BedcraftItems {

    private static final DeferredRegister<Item> ITEMS = new DeferredRegister<>(ForgeRegistries.ITEMS,
            BedCraftBeyond.MOD_ID);

    public static final RegistryObject<Item> DRAWER_KEY = ITEMS.register("drawer_key", () -> new ItemDrawerKey());

    public static void registerItems(IEventBus bus) {
        ITEMS.register(bus);
    }
}