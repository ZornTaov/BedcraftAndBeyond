package zornco.bedcraftbeyond.blocks.tiles;

import net.minecraft.init.Blocks;
import net.minecraft.item.EnumDyeColor;
import net.minecraft.item.ItemStack;
import net.minecraft.nbt.NBTTagCompound;
import net.minecraft.network.NetworkManager;
import net.minecraft.network.Packet;
import net.minecraft.network.play.server.SPacketUpdateTileEntity;
import net.minecraft.tileentity.TileEntity;
import zornco.bedcraftbeyond.util.PlankHelper;

public class TileBedcraftBed extends TileEntity {
	private EnumDyeColor blankets;
	private EnumDyeColor sheets;
	private int plankColor;

	public ItemStack plankType;
	public String plankTypeNS;

	public TileBedcraftBed() {
		blankets = EnumDyeColor.WHITE;
		sheets = EnumDyeColor.WHITE;
	}

	@Override
	public void writeToNBT(NBTTagCompound nbttagcompound) {
		super.writeToNBT(nbttagcompound);
		nbttagcompound.setInteger("blankets", blankets.getMetadata());
		nbttagcompound.setInteger("sheets", sheets.getMetadata());
		nbttagcompound.setInteger("plankColor", plankColor);
		PlankHelper.validatePlank(nbttagcompound, getPlankType());
		if (plankTypeNS != null) {
			nbttagcompound.setString("plankNameSpace", plankTypeNS);
		}
	}

	@Override
	public void readFromNBT(NBTTagCompound nbttagcompound) {
		super.readFromNBT(nbttagcompound);
		this.blankets = EnumDyeColor.byMetadata(nbttagcompound.getInteger("blankets"));
		this.sheets = EnumDyeColor.byMetadata(nbttagcompound.getInteger("sheets"));
		this.plankColor = nbttagcompound.getInteger("plankColor");
		this.plankType = PlankHelper.validatePlank(nbttagcompound);
		this.plankTypeNS = nbttagcompound.getString("plankNameSpace");
	}

	public ItemStack getPlankType() {
		if (plankTypeNS != null) {
			return PlankHelper.plankItemStackfromString(plankTypeNS);
		}
		if (plankType != null) {
			plankTypeNS = PlankHelper.plankStringfromItemStack(plankType);
			return plankType;
		}
		return new ItemStack(Blocks.planks, 1, 0);
	}

	public void setPlankType(ItemStack plankType) {
		this.plankType = plankType;
		updateClients();
	}

	public String getPlankTypeNS() {
		return plankTypeNS;
	}

	public void setPlankTypeNS(String plank) {
		this.plankTypeNS = plank;
		updateClients();
	}

	public int getPlankColor() {
		return plankColor;
	}

	public void setPlankColor(int plankColor) {
		this.plankColor = plankColor;
		updateClients();
	}

	public String getPlankName() {
		return plankType.getDisplayName();
	}

	public void setColorCombo(EnumDyeColor sheets, EnumDyeColor blankets) {
		this.sheets = sheets;
		this.blankets = blankets;
		updateClients();
	}

	public void setSheetsColor(EnumDyeColor color){
		this.sheets = color;
		updateClients();
	}

	public void setBlanketsColor(EnumDyeColor color){
		this.blankets = color;
		updateClients();
	}

	public EnumDyeColor getSheetsColor(){ return this.sheets; }
	public EnumDyeColor getBlanketsColor(){ return this.blankets; }

	@Override
	public final Packet getDescriptionPacket() {
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

	public final void updateClients() {
		if (worldObj.isRemote)
			return;
		markDirty();
		/*
		 * S35PacketUpdateTileEntity packet = this.getDescriptionPacket();
		 * PacketDispatcher.sendPacketToAllInDimension(packet,
		 * this.worldObj.provider.dimensionId);
		 */
	}

}
