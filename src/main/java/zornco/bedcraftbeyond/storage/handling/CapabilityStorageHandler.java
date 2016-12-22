package zornco.bedcraftbeyond.storage.handling;

import com.google.common.collect.ImmutableList;
import net.minecraft.item.ItemStack;
import net.minecraft.nbt.NBTBase;
import net.minecraft.nbt.NBTTagCompound;
import net.minecraft.util.EnumFacing;
import net.minecraftforge.common.capabilities.Capability;
import net.minecraftforge.common.capabilities.CapabilityInject;
import net.minecraftforge.common.capabilities.CapabilityManager;
import zornco.bedcraftbeyond.storage.handling.impl.StorageHandler;

public class CapabilityStorageHandler {

    @CapabilityInject(IStorageHandler.class)
    public static Capability<IStorageHandler> INSTANCE = null;

    public static void register(){
        CapabilityManager.INSTANCE.register(IStorageHandler.class, new Capability.IStorage<IStorageHandler>() {
            @Override
            public NBTBase writeNBT(Capability<IStorageHandler> capability, IStorageHandler instance, EnumFacing side) {
                ImmutableList<String> slots = instance.getSlotNames();
                NBTTagCompound tags = new NBTTagCompound();
                slots.stream().filter(slotName -> instance.isSlotFilled(slotName)).forEach(slotName -> {
                    NBTTagCompound slotNBT = instance.getSlotItemstack(slotName, false).writeToNBT(new NBTTagCompound());
                    tags.setTag(slotName, slotNBT);
                });

                return tags;
            }

            @Override
            public void readNBT(Capability<IStorageHandler> capability, IStorageHandler instance, EnumFacing side, NBTBase nbt) {
                NBTTagCompound compound = (NBTTagCompound) nbt;
                instance.getSlotNames().stream().filter(slotName -> compound.hasKey(slotName)).forEach(slotName -> {
                    ItemStack stack = ItemStack.loadItemStackFromNBT(compound.getCompoundTag(slotName));
                    instance.setSlotItem(slotName, stack);
                });
            }
        }, () -> {
            return new StorageHandler();
        });
    }
}
