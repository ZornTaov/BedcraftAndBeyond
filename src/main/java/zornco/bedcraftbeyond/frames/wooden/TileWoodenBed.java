package zornco.bedcraftbeyond.frames.wooden;

import com.google.common.collect.ImmutableList;
import net.minecraft.item.ItemStack;
import net.minecraft.nbt.NBTTagCompound;
import net.minecraft.nbt.NBTTagList;
import net.minecraft.network.NetworkManager;
import net.minecraft.network.play.server.SPacketUpdateTileEntity;
import net.minecraft.util.EnumFacing;
import net.minecraft.util.ResourceLocation;
import net.minecraft.world.World;
import net.minecraftforge.common.capabilities.Capability;
import net.minecraftforge.common.util.Constants;
import net.minecraftforge.fml.common.network.NetworkRegistry;
import zornco.bedcraftbeyond.core.BedCraftBeyond;
import zornco.bedcraftbeyond.frames.BedFrameUpdate;
import zornco.bedcraftbeyond.frames.base.TileGenericBed;
import zornco.bedcraftbeyond.frames.registry.FrameException;
import zornco.bedcraftbeyond.frames.registry.FrameHelper;
import zornco.bedcraftbeyond.frames.registry.FrameRegistry;
import zornco.bedcraftbeyond.frames.registry.FrameWhitelistEntry;
import zornco.bedcraftbeyond.linens.LinenType;
import zornco.bedcraftbeyond.linens.LinenUpdate;
import zornco.bedcraftbeyond.linens.cap.CapabilityLinenHandler;
import zornco.bedcraftbeyond.linens.cap.LinenHandler;
import zornco.bedcraftbeyond.storage.handling.CapabilityStorageHandler;
import zornco.bedcraftbeyond.storage.handling.MessageStorageUpdate;
import zornco.bedcraftbeyond.storage.handling.impl.StorageHandler;

import java.awt.*;

// This tile is only to be used ONCE on beds!
// Place it on the head of the bed. Use BlockWoodenBed.getTileEntity anywhere on a bed to fetch this instance.
public class TileWoodenBed extends TileGenericBed {

    private Color plankColor;
    public ResourceLocation plankType;
    protected int plankMeta;

    protected WoodenLinenStorage linens;
    protected StorageHandler storage;

    @SuppressWarnings("unused")
    public TileWoodenBed() {
        storage = new DrawerStorage();
        linens = new WoodenLinenStorage();
    }

    public TileWoodenBed(World w) {
        super(w);
        storage = new DrawerStorage();
        linens = new WoodenLinenStorage();
    }

    @Override
    public NBTTagCompound getUpdateTag() {
        return this.writeToNBT(new NBTTagCompound());
    }

    @Override
    public NBTTagCompound writeToNBT(NBTTagCompound tags) {
        super.writeToNBT(tags);

        if (plankColor != null) tags.setInteger("plankColor", plankColor.getRGB());
        if (plankType != null) tags.setString("plankType", plankType.toString());
        tags.setInteger("plankMeta", plankMeta);

        tags.setTag("linens", linens.serializeNBT());
        tags.setTag("storage", storage.serializeNBT());
        return tags;
    }

    @Override
    public void onDataPacket(NetworkManager net, SPacketUpdateTileEntity packet) {
        super.onDataPacket(net, packet);
        if (this.plankColor == null) recachePlankColor();
    }

    @Override
    public void readFromNBT(NBTTagCompound tags) {
        super.readFromNBT(tags);

        if (tags.hasKey("linens")) {
            NBTTagList linensTag = tags.getTagList("linens", Constants.NBT.TAG_COMPOUND);
            linens.deserializeNBT(linensTag);
        }

        if (tags.hasKey("plankColor")) this.plankColor = new Color(tags.getInteger("plankColor"));
        this.plankType = new ResourceLocation(tags.getString("plankType"));
        this.plankMeta = tags.getInteger("plankMeta");

        if(tags.hasKey("storage")) {
            NBTTagList storageTags = tags.getTagList("storage", Constants.NBT.TAG_COMPOUND);
            storage.deserializeNBT(storageTags);
        }

        updateClients();
    }

    //region Planks
    public NBTTagCompound getPlankData() {
        NBTTagCompound plankData = new NBTTagCompound();
        if (plankColor != null) plankData.setInteger("color", plankColor.getRGB());
        if (plankType != null) {
            plankData.setString("frameType", plankType.toString());
            plankData.setInteger("frameMeta", plankMeta);
        }

        return plankData;
    }

    @SuppressWarnings("unused")
    public ResourceLocation getPlankType() {
        return this.plankType;
    }

    @SuppressWarnings("unused")
    public int getPlankMeta() {
        return this.plankMeta;
    }

    @SuppressWarnings("unused")
    public void setPlankType(NBTTagCompound nbt) throws FrameException {
        this.setPlankType(nbt, true);
    }

    public void setPlankType(NBTTagCompound nbt, boolean updateClients) throws FrameException {
        if (nbt == null) return;
        if (nbt.hasKey("color")) this.plankColor = new Color(nbt.getInteger("color"));
        if (!nbt.hasKey("frameType")) throw new FrameException("Need to have frame type set.");
        if (!nbt.hasKey("frameMeta")) throw new FrameException("Need to have frame meta set.");

        ResourceLocation rl = new ResourceLocation(nbt.getString("frameType"));
        int meta = nbt.getInteger("frameMeta");
        setPlankType(rl, meta, updateClients);
    }

    @SuppressWarnings("unused")
    public void setPlankType(ResourceLocation plankType, int plankMeta) throws FrameException {
        setPlankType(plankType, plankMeta, true);
    }

    public void setPlankType(ResourceLocation plankType, int plankMeta, boolean updateClients) throws FrameException {
        FrameWhitelistEntry entry = FrameRegistry.getFrameWhitelist(FrameRegistry.EnumFrameType.WOOD).getEntry(plankType);
        boolean valid = entry.isWhitelisted(plankMeta);
        if (!valid) throw new FrameException("Not a valid frame type");
        this.plankType = plankType;
        this.plankMeta = plankMeta;

        if (updateClients && (worldObj != null && !worldObj.isRemote)) {
            // Make sure we have the updated plank information on the server
            BedFrameUpdate update = new BedFrameUpdate(pos, this.plankType, this.plankMeta);
            BedCraftBeyond.NETWORK.sendToAllAround(update, new NetworkRegistry.TargetPoint(worldObj.provider.getDimension(), pos.getX(), pos.getY(), pos.getZ(), 25));
        }

        // If we're on the client and the frame color hasn't been set by something else (like an nbt tag from an item)
        if (worldObj.isRemote && this.plankColor == null)
            this.plankColor = FrameHelper.getColorFromPlankType(this.plankType, this.plankMeta);

        markDirty();
    }

    public Color getPlankColor(){
        if (worldObj.isRemote && plankColor == null) recachePlankColor();
        return this.plankColor != null ? plankColor : Color.WHITE;
    }

    public void recachePlankColor() {
        this.plankColor = FrameHelper.getColorFromPlankType(this.plankType, this.plankMeta);
    }
    //endregion

    @Override
    public SPacketUpdateTileEntity getUpdatePacket() {
        SPacketUpdateTileEntity pack = super.getUpdatePacket();
        return pack;
    }

    public final void updateClients() {
        if (worldObj == null || worldObj.isRemote) return;
        markDirty();
    }

    public void sendLinenUpdate(LinenType type) {
        LinenUpdate upd = new LinenUpdate(this.pos, type, linens.getSlotItem(type, false));
        BedCraftBeyond.NETWORK.sendToAllAround(upd, new NetworkRegistry.TargetPoint(
            worldObj.provider.getDimension(),
            pos.getX(), pos.getY(), pos.getZ(),
            25
        ));
    }

    public void sendStorageUpdate(String slot) {
        ItemStack stack = storage.getSlotItemstack(slot, false);
        MessageStorageUpdate msu = new MessageStorageUpdate(pos, slot, null, stack);
        BedCraftBeyond.NETWORK.sendToAllAround(msu, new NetworkRegistry.TargetPoint(
            worldObj.provider.getDimension(),
            pos.getX(), pos.getY(), pos.getZ(),
            10
        ));
    }

    @Override
    public boolean hasCapability(Capability<?> capability, EnumFacing facing) {
        if(capability == CapabilityStorageHandler.INSTANCE && facing != EnumFacing.UP)
            return true;

        if(capability == CapabilityLinenHandler.INSTANCE && facing == EnumFacing.UP)
            return true;

        return super.hasCapability(capability, facing);
    }

    @Override
    public <T> T getCapability(Capability<T> capability, EnumFacing facing) {
        if(capability == CapabilityStorageHandler.INSTANCE){
            if(facing == EnumFacing.UP) return null;
            return (T) storage;
        }

        if(capability == CapabilityLinenHandler.INSTANCE)
            return (T) linens;

        return super.getCapability(capability, facing);
    }

    private class DrawerStorage extends StorageHandler {
        public DrawerStorage() {
            this.availableSlots = ImmutableList.of("head", "foot");
        }
    }

    private class WoodenLinenStorage extends LinenHandler {
        public WoodenLinenStorage() {
            this.availableSlots = ImmutableList.of(LinenType.BLANKET, LinenType.SHEET);
        }
    }
}
