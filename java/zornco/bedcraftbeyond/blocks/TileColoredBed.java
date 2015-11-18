package zornco.bedcraftbeyond.blocks;

import net.minecraft.nbt.NBTTagCompound;
import net.minecraft.network.NetworkManager;
import net.minecraft.network.Packet;
import net.minecraft.network.play.server.S35PacketUpdateTileEntity;
import net.minecraft.tileentity.TileEntity;

public class TileColoredBed extends TileEntity {
	private int colorCombo;
	private int plankColor;
	private String plankName;
	private int plankMeta;
	private boolean firstRun = true;

	public TileColoredBed() {
		super();
	}

	@Override
	public void writeToNBT(NBTTagCompound nbttagcompound) {
		super.writeToNBT(nbttagcompound);
		nbttagcompound.setInteger("colorCombo", colorCombo);
		nbttagcompound.setInteger("plankColor", plankColor);
		nbttagcompound.setInteger("plankMeta", plankMeta);
		nbttagcompound.setString("plankName", plankName);
	}

	@Override
	public void readFromNBT(NBTTagCompound nbttagcompound) {
		super.readFromNBT(nbttagcompound);
		this.colorCombo = nbttagcompound.getInteger("colorCombo");
		this.plankColor = nbttagcompound.getInteger("plankColor");
		this.plankMeta = nbttagcompound.getInteger("plankMeta");
		this.plankName = nbttagcompound.getString("plankName");
	}

	public int getPlankColor() {
		return plankColor;
	}

	public void setPlankColor(int plankColor) {
		this.plankColor = plankColor;
		updateClients();
	}

	public String getPlankName() {
		return plankName;
	}

	public void setPlankName(String plankName) {
		this.plankName = plankName;
		updateClients();
	}

	public int getPlankMeta() {
		return plankMeta;
	}

	public void setPlankMeta(int plankMeta) {
		this.plankMeta = plankMeta;
	}

	public void setColorCombo(int combo) {
		this.colorCombo = combo;
		updateClients();
	}

	public int getColorCombo() {
		return this.colorCombo;
	}

	@Override
	public void updateEntity() {
		if (!worldObj.isRemote
				&& (worldObj.getWorldTime() % 20 == 0 || firstRun)) {
			firstRun = false;
			updateClients();

		}
	}

	@Override
	public final Packet getDescriptionPacket() {
		NBTTagCompound nbt = new NBTTagCompound();
		this.writeToNBT(nbt);
		return new S35PacketUpdateTileEntity(this.xCoord, this.yCoord,
				this.zCoord, -5, nbt);
	}

	@Override
	public final void onDataPacket(NetworkManager net,
			S35PacketUpdateTileEntity packet) {
		NBTTagCompound nbt = packet.func_148857_g();
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
