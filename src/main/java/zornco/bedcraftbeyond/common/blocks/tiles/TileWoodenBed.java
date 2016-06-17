package zornco.bedcraftbeyond.common.blocks.tiles;

import net.minecraft.item.ItemStack;
import net.minecraft.nbt.NBTTagCompound;
import net.minecraft.network.play.server.SPacketUpdateTileEntity;
import net.minecraft.util.ResourceLocation;
import net.minecraft.world.World;
import net.minecraftforge.fml.common.network.NetworkRegistry;
import net.minecraftforge.items.ItemHandlerHelper;
import zornco.bedcraftbeyond.BedCraftBeyond;
import zornco.bedcraftbeyond.common.blocks.BlockWoodenBed;
import zornco.bedcraftbeyond.common.blocks.properties.EnumBedFabricType;
import zornco.bedcraftbeyond.frames.FrameException;
import zornco.bedcraftbeyond.frames.FrameHelper;
import zornco.bedcraftbeyond.frames.FrameRegistry;
import zornco.bedcraftbeyond.common.item.linens.ILinenItem;
import zornco.bedcraftbeyond.common.item.linens.ItemBlanket;
import zornco.bedcraftbeyond.common.item.linens.ItemSheets;
import zornco.bedcraftbeyond.network.BedPartUpdate;

import java.awt.*;

// This tile is only to be used ONCE on beds!
// Place it on the head of the bed. Use BlockWoodenBed.getTileEntity anywhere on a bed to fetch this instance.
public class TileWoodenBed extends TileGenericBed {

    private ItemStack blankets;
    private ItemStack sheets;
    private Color plankColor;
    public ResourceLocation plankType;
    protected int plankMeta;

    @SuppressWarnings("unused")
    public TileWoodenBed() { }
    public TileWoodenBed(World w){ super(w); }

    @Override
    public NBTTagCompound getUpdateTag() {
        return this.writeToNBT(new NBTTagCompound());
    }

    @Override
    public NBTTagCompound writeToNBT(NBTTagCompound tags) {
        super.writeToNBT(tags);
        if (blankets != null) tags.setTag("blankets", blankets.writeToNBT(new NBTTagCompound()));
        if (sheets != null) tags.setTag("sheets", sheets.writeToNBT(new NBTTagCompound()));
        if(plankColor != null) tags.setInteger("plankColor", plankColor.getRGB());

        if (plankType != null) tags.setString("plankType", plankType.toString());
        tags.setInteger("plankMeta", plankMeta);
        return tags;
    }

    @Override
    public void readFromNBT(NBTTagCompound tags) {
        super.readFromNBT(tags);
        if (tags.hasKey("blankets")) this.blankets = ItemStack.loadItemStackFromNBT(tags.getCompoundTag("blankets"));
        if (tags.hasKey("sheets")) this.sheets = ItemStack.loadItemStackFromNBT(tags.getCompoundTag("sheets"));

        this.plankColor = new Color(tags.getInteger("plankColor"));
        this.plankType = new ResourceLocation(tags.getString("plankType"));
        this.plankMeta = tags.getInteger("plankMeta");

        updateClients(BlockWoodenBed.EnumColoredPart.BLANKETS);
        updateClients(BlockWoodenBed.EnumColoredPart.SHEETS);
    }

    public NBTTagCompound getPlankData() {
        NBTTagCompound plankData = new NBTTagCompound();
        if(plankColor != null) plankData.setInteger("color", plankColor.getRGB());
        plankData.setString("frameType", plankType.toString());
        plankData.setInteger("frameMeta", plankMeta);
        return plankData;
    }

    @SuppressWarnings("unused")
    public ResourceLocation getPlankType(){
        return this.plankType;
    }

    @SuppressWarnings("unused")
    public int getPlankMeta(){
        return this.plankMeta;
    }

    @SuppressWarnings("unused")
    public void setPlankType(NBTTagCompound nbt) throws FrameException {
        this.setPlankType(nbt, true);
    }

    public void setPlankType(NBTTagCompound nbt, boolean updateClients) throws FrameException {
        if(nbt == null) return;
        if(!nbt.hasKey("frameType")) throw new FrameException("Need to have frame type set.");
        if(!nbt.hasKey("frameMeta")) throw new FrameException("Need to have frame meta set.");

        ResourceLocation rl = new ResourceLocation(nbt.getString("frameType"));
        int meta = nbt.getInteger("frameMeta");
        if(!FrameRegistry.getFrameWhitelist(FrameRegistry.EnumFrameType.WOOD).metaIsWhitelisted(rl, meta))
            throw new FrameException("Not a valid frame type.");

        this.plankType = rl;
        this.plankMeta = meta;
        if(updateClients) updateClients(BlockWoodenBed.EnumColoredPart.PLANKS);
        if(worldObj.isRemote) updatePlankColor();
    }

    @SuppressWarnings("unused")
    public void setPlankType(ResourceLocation plankType, int plankMeta) throws FrameException {
        setPlankType(plankType, plankMeta, true);
    }

    public void setPlankType(ResourceLocation plankType, int plankMeta, boolean updateClients) throws FrameException {
        boolean valid = FrameRegistry.getFrameWhitelist(FrameRegistry.EnumFrameType.WOOD).metaIsWhitelisted(plankType, plankMeta);
        if(!valid) throw new FrameException("Not a valid frame type");
        this.plankType = plankType;
        this.plankMeta = plankMeta;

        if(updateClients) updateClients(BlockWoodenBed.EnumColoredPart.PLANKS);
        if(worldObj.isRemote) updatePlankColor();
    }

    private void updatePlankColor(){
        if(!worldObj.isRemote) return;
        this.plankColor = FrameHelper.getColorFromPlankType(this.plankType, this.plankMeta);
    }

    @SuppressWarnings("unused")
    public boolean hasLinenPart(BlockWoodenBed.EnumColoredPart part){
        switch (part) {
            case SHEETS:
                return sheets != null;

            case BLANKETS:
                return blankets != null;

            case PLANKS:
                return true;

            default:
                return false;
        }
    }

    public ItemStack getLinenPart(BlockWoodenBed.EnumColoredPart part, boolean extract) {
        ItemStack partCopy;
        switch (part) {
            case SHEETS:
                if (extract && sheets != null) {
                    partCopy = ItemHandlerHelper.copyStackWithSize(sheets, 1);
                    sheets = null;
                    return partCopy;
                }
                return sheets;

            case BLANKETS:
                if (extract && blankets != null) {
                    partCopy = ItemHandlerHelper.copyStackWithSize(blankets, 1);
                    blankets = null;
                    return partCopy;
                }
                return blankets;

            default:
                return null;
        }
    }

    public boolean setLinenPart(BlockWoodenBed.EnumColoredPart part, ItemStack linen) {
        if(linen == null || linen.getItem() == null) return false;
        ItemStack linenCopy = ItemHandlerHelper.copyStackWithSize(linen, 1);
        switch (part) {
            case SHEETS:
                if (sheets == null && linenCopy.getItem() instanceof ItemSheets) {
                    sheets = linenCopy;
                    updateClients(BlockWoodenBed.EnumColoredPart.SHEETS);
                    return true;
                }

                return false;

            case BLANKETS:
                if (blankets == null && linenCopy.getItem() instanceof ItemBlanket) {
                    blankets = linenCopy;
                    updateClients(BlockWoodenBed.EnumColoredPart.BLANKETS);
                    return true;
                }

                return false;
        }

        return false;
    }

    public Color getPartColor(BlockWoodenBed.EnumColoredPart part) {
        switch(part){
            case BLANKETS:
            case SHEETS:
                ItemStack i = getLinenPart(part, false);
                if (i == null) Color.WHITE.getRGB();
                if (getPartType(part) != EnumBedFabricType.SOLID_COLOR) return Color.WHITE;

                return ((ILinenItem) i.getItem()).getColor(i);
            case PLANKS:
                if(worldObj.isRemote && plankColor == null) updatePlankColor();
                return this.plankColor != null ? plankColor : Color.WHITE;
        }

        return Color.WHITE;
    }

    public EnumBedFabricType getPartType(BlockWoodenBed.EnumColoredPart type) {
        ItemStack part = getLinenPart(type, false);
        if (part == null) return EnumBedFabricType.NONE;

        if (!part.hasTagCompound()) {
            part.setTagCompound(new NBTTagCompound());
        }
        if (!part.getTagCompound().hasKey("type")) {
            part.getTagCompound().setString("type", EnumBedFabricType.NONE.name());
            return EnumBedFabricType.NONE;
        }

        try {
            return EnumBedFabricType.valueOf(part.getTagCompound().getString("type"));
        } catch (Exception e) {
            return EnumBedFabricType.NONE;
        }
    }

    @Override
    public SPacketUpdateTileEntity getUpdatePacket() {
        SPacketUpdateTileEntity pack = super.getUpdatePacket();
        return pack;
    }

    public final void updateClients(BlockWoodenBed.EnumColoredPart part) {
        if (worldObj == null || worldObj.isRemote) return;
        markDirty();
        BedPartUpdate update = new BedPartUpdate(pos, part, getLinenPart(part, false));
        BedCraftBeyond.NETWORK.sendToAllAround(update, new NetworkRegistry.TargetPoint(worldObj.provider.getDimension(), pos.getX(), pos.getY(), pos.getZ(), 25));
    }
}
