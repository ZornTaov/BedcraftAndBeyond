package zornco.bedcraftbeyond.common.blocks.tiles;

import net.minecraft.init.Blocks;
import net.minecraft.item.ItemStack;
import net.minecraft.nbt.NBTTagCompound;
import net.minecraft.network.NetworkManager;
import net.minecraft.network.play.server.SPacketUpdateTileEntity;
import net.minecraft.tileentity.TileEntity;
import net.minecraft.util.ResourceLocation;
import net.minecraftforge.fml.common.network.NetworkRegistry;
import zornco.bedcraftbeyond.BedCraftBeyond;
import zornco.bedcraftbeyond.common.blocks.BlockWoodenBed;
import zornco.bedcraftbeyond.common.item.linens.ILinenItem;
import zornco.bedcraftbeyond.common.blocks.properties.EnumBedFabricType;
import zornco.bedcraftbeyond.common.item.linens.ItemBlanket;
import zornco.bedcraftbeyond.common.item.linens.ItemSheets;
import zornco.bedcraftbeyond.network.BedPartUpdate;
import zornco.bedcraftbeyond.util.PlankHelper;

import javax.annotation.Nullable;
import java.awt.*;

// This tile is only to be used ONCE on beds!
// Place it on the head of the bed. Use BlockWoodenBed.getTileEntity anywhere on a bed to fetch this instance.
public class TileWoodenBed extends TileEntity {
	private ItemStack blankets;
	private ItemStack sheets;
	private int plankColor;
	public ResourceLocation plankType;

	public TileWoodenBed() { }


	@Override
	public NBTTagCompound writeToNBT(NBTTagCompound tags) {
		super.writeToNBT(tags);
		if(blankets != null) tags.setTag("blankets", blankets.writeToNBT(new NBTTagCompound()));
		if(sheets != null) tags.setTag("sheets", sheets.writeToNBT(new NBTTagCompound()));
		tags.setInteger("plankColor", plankColor);

		// TODO: Fix plank type
		PlankHelper.validatePlank(tags, getPlankType());
		if (plankType != null) tags.setString("plankType", plankType.toString());
		return tags;
	}

	@Override
	public void readFromNBT(NBTTagCompound tags) {
		super.readFromNBT(tags);
		if(tags.hasKey("blankets")) this.blankets = ItemStack.loadItemStackFromNBT(tags.getCompoundTag("blankets"));
		if(tags.hasKey("sheets")) this.sheets = ItemStack.loadItemStackFromNBT(tags.getCompoundTag("sheets"));
		this.plankColor = tags.getInteger("plankColor");
		// this.plankType = PlankHelper.validatePlank(tags.getTag("plankType"));
		// TODO: Fix plank type here
		this.plankType = new ResourceLocation("minecraft", "planks@0");
	}

	// TODO: Update plank stuff
	public ItemStack getPlankType(){
		return new ItemStack(Blocks.PLANKS, 1);
	}

	public NBTTagCompound getPlankData(){
		NBTTagCompound plankData = new NBTTagCompound();
		plankData.setInteger("color", plankColor);
		plankData.setString("type", plankType.toString());
		return plankData;
	}

	public void setPlankType(ItemStack plankType) {
		this.plankType = plankType.getItem().getRegistryName();
		// TODO: Set up the color hook here
		updateClients(BlockWoodenBed.EnumColoredPart.PLANKS);
	}

	public ItemStack getLinenPart(BlockWoodenBed.EnumColoredPart part, boolean extract){
		ItemStack partCopy;
		switch(part){
			case SHEETS:
				if(extract && sheets != null){ partCopy = sheets.copy(); sheets = null; return partCopy; }
				return sheets;

			case BLANKETS:
				if(extract && blankets != null){ partCopy = blankets.copy(); blankets = null; return partCopy; }
				return blankets;

			default:
				return null;
		}
	}

	public boolean setLinenPart(BlockWoodenBed.EnumColoredPart part, ItemStack linen){
		switch(part){
			case SHEETS:
				if(sheets == null && linen.getItem() instanceof ItemSheets){
					sheets = linen;
					updateClients(BlockWoodenBed.EnumColoredPart.SHEETS);
					return true;
				}

				return false;

			case BLANKETS:
				if(blankets == null && linen.getItem() instanceof ItemBlanket){
					blankets = linen;
					updateClients(BlockWoodenBed.EnumColoredPart.BLANKETS);
					return true;
				}

				return false;
		}

		return false;
	}

	public Color getPartColor(BlockWoodenBed.EnumColoredPart part){
		ItemStack i = getLinenPart(part, false);
		if(i == null) Color.WHITE.getRGB();
		if(getPartType(part) != EnumBedFabricType.SOLID_COLOR) return Color.WHITE;

		return ((ILinenItem) i.getItem()).getColor(i);
	}

	public EnumBedFabricType getPartType(BlockWoodenBed.EnumColoredPart type){
		ItemStack part = getLinenPart(type, false);
		if(part == null) return EnumBedFabricType.NONE;

		if(!part.hasTagCompound()){ part.setTagCompound(new NBTTagCompound()); }
		if(!part.getTagCompound().hasKey("type")) {
			part.getTagCompound().setString("type", EnumBedFabricType.NONE.name());
			return EnumBedFabricType.NONE;
		}

		try { return EnumBedFabricType.valueOf(part.getTagCompound().getString("type")); }
		catch(Exception e){ return EnumBedFabricType.NONE; }
	}

	@Nullable
	@Override
	public SPacketUpdateTileEntity getUpdatePacket() {
		NBTTagCompound nbt = new NBTTagCompound();
		this.writeToNBT(nbt);
		return new SPacketUpdateTileEntity(this.pos, -5, nbt);
	}

	@Override
	public final void onDataPacket(NetworkManager net,
			SPacketUpdateTileEntity packet) {
		NBTTagCompound nbt = packet.getNbtCompound();
		if (nbt != null) {
			this.readFromNBT(nbt);
		}
	}

	public final void updateClients(BlockWoodenBed.EnumColoredPart part) {
		if (worldObj.isRemote) return;
		markDirty();

		BedPartUpdate update = new BedPartUpdate(pos, part, getPartType(part), getPartColor(part).getRGB());
		BedCraftBeyond.network.sendToAllAround(update, new NetworkRegistry.TargetPoint(worldObj.provider.getDimension(), pos.getX(), pos.getY(), pos.getZ(), 25));
	}

}
