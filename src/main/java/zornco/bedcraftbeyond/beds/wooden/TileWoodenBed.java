package zornco.bedcraftbeyond.beds.wooden;

import net.minecraft.item.ItemStack;
import net.minecraft.nbt.NBTTagCompound;
import net.minecraft.network.NetworkManager;
import net.minecraft.network.play.server.SPacketUpdateTileEntity;
import net.minecraft.util.ResourceLocation;
import net.minecraft.world.World;
import net.minecraftforge.fml.common.network.NetworkRegistry;
import zornco.bedcraftbeyond.beds.base.TileGenericBed;
import zornco.bedcraftbeyond.beds.frames.BedFrameUpdate;
import zornco.bedcraftbeyond.beds.frames.registry.FrameException;
import zornco.bedcraftbeyond.beds.frames.registry.FrameHelper;
import zornco.bedcraftbeyond.beds.frames.registry.FrameRegistry;
import zornco.bedcraftbeyond.beds.frames.registry.FrameWhitelistEntry;
import zornco.bedcraftbeyond.beds.parts.EnumBedPart;
import zornco.bedcraftbeyond.beds.parts.drawer.DrawerHandler;
import zornco.bedcraftbeyond.beds.parts.linens.BedLinenUpdate;
import zornco.bedcraftbeyond.beds.parts.linens.LinenHandler;
import zornco.bedcraftbeyond.core.BedCraftBeyond;

import java.awt.*;

// This tile is only to be used ONCE on beds!
// Place it on the head of the bed. Use BlockWoodenBed.getTileEntity anywhere on a bed to fetch this instance.
public class TileWoodenBed extends TileGenericBed {

    private Color plankColor;
    public ResourceLocation plankType;
    protected int plankMeta;

    protected LinenHandler linens;
    protected DrawerHandler drawers;

    @SuppressWarnings("unused")
    public TileWoodenBed() {
        drawers = new DrawerHandler(2);
        linens = new LinenHandler();
    }

    public TileWoodenBed(World w) {
        super(w);
        drawers = new DrawerHandler(2);
        linens = new LinenHandler();
    }

    @Override
    public NBTTagCompound getUpdateTag() {
        return this.writeToNBT(new NBTTagCompound());
    }

    @Override
    public NBTTagCompound writeToNBT(NBTTagCompound tags) {
        super.writeToNBT(tags);
        if (linens != null) tags.setTag("linens", linens.serializeNBT());
        if (plankColor != null) tags.setInteger("plankColor", plankColor.getRGB());

        if (plankType != null) tags.setString("plankType", plankType.toString());
        tags.setInteger("plankMeta", plankMeta);
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

        if (tags.hasKey("linens"))
            linens.deserializeNBT(tags.getCompoundTag("linens"));
        if (tags.hasKey("blankets"))
            linens.setBlankets(ItemStack.loadItemStackFromNBT(tags.getCompoundTag("blankets")));
        if (tags.hasKey("sheets"))
            linens.setSheets(ItemStack.loadItemStackFromNBT(tags.getCompoundTag("sheets")));

        if (tags.hasKey("plankColor")) this.plankColor = new Color(tags.getInteger("plankColor"));
        this.plankType = new ResourceLocation(tags.getString("plankType"));
        this.plankMeta = tags.getInteger("plankMeta");

        updateClients(EnumBedPart.BLANKETS);
        updateClients(EnumBedPart.SHEETS);
    }

    public DrawerHandler getDrawerHandler() {
        return this.drawers;
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

    public LinenHandler getLinenHandler(){ return linens; }

    @Override
    public SPacketUpdateTileEntity getUpdatePacket() {
        SPacketUpdateTileEntity pack = super.getUpdatePacket();
        return pack;
    }

    public final void updateClients(EnumBedPart part) {
        if (worldObj == null || worldObj.isRemote) return;
        markDirty();
        if(part.isLinenPart()) {
            BedLinenUpdate update = new BedLinenUpdate(pos, part.toLinenPart(), linens.getLinenPart(part.toLinenPart(), false));
            BedCraftBeyond.NETWORK.sendToAllAround(update, new NetworkRegistry.TargetPoint(worldObj.provider.getDimension(), pos.getX(), pos.getY(), pos.getZ(), 25));
        }
    }
}
