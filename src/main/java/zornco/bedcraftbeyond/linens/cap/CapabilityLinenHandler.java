package zornco.bedcraftbeyond.linens.cap;

import net.minecraft.nbt.NBTBase;
import net.minecraft.nbt.NBTTagCompound;
import net.minecraft.util.EnumFacing;
import net.minecraftforge.common.capabilities.Capability;
import net.minecraftforge.common.capabilities.CapabilityInject;
import net.minecraftforge.common.capabilities.CapabilityManager;

public class CapabilityLinenHandler {

    @CapabilityInject(ILinenHandler.class)
    public static Capability<ILinenHandler> INSTANCE = null;

    public static void register() {
        CapabilityManager.INSTANCE.register(ILinenHandler.class, new Capability.IStorage<ILinenHandler>() {
            @Override
            public NBTBase writeNBT(Capability<ILinenHandler> capability, ILinenHandler instance, EnumFacing side) {
                return new NBTTagCompound();
            }

            @Override
            public void readNBT(Capability<ILinenHandler> capability, ILinenHandler instance, EnumFacing side, NBTBase nbt) {

            }
        }, () -> {
            return new LinenHandler();
        });
    }
}