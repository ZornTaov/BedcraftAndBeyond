package zornco.bedcraftbeyond.core;

import net.minecraft.item.Item;
import net.minecraftforge.eventbus.api.IEventBus;
import net.minecraftforge.fml.RegistryObject;
import net.minecraftforge.registries.DeferredRegister;
import net.minecraftforge.registries.ForgeRegistries;
import zornco.bedcraftbeyond.storage.items.ItemDrawerKey;
import zornco.bedcraftbeyond.core.BedCraftBeyond;

public final class BedcraftItems {

    protected static final DeferredRegister<Item> ITEMS = new DeferredRegister<>(ForgeRegistries.ITEMS,
            BedCraftBeyond.MOD_ID);

    private static Item.Properties DEFAULT_PROPS = new Item.Properties().group(BedCraftBeyond.MAIN_GROUP);

    public static final RegistryObject<Item> DRAWER_KEY = ITEMS.register("drawer_key",
            () -> new ItemDrawerKey(DEFAULT_PROPS.maxStackSize(1)));

    public static void registerItems(IEventBus bus) {
        ITEMS.register(bus);
    }
}