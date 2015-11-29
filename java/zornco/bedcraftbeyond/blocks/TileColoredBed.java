package zornco.bedcraftbeyond.blocks;

import zornco.bedcraftbeyond.util.PlankHelper;
import net.minecraft.item.ItemStack;
import net.minecraft.nbt.NBTTagCompound;
import net.minecraft.network.NetworkManager;
import net.minecraft.network.Packet;
import net.minecraft.network.play.server.S35PacketUpdateTileEntity;
import net.minecraft.tileentity.TileEntity;

public class TileColoredBed extends TileEntity {
	private int colorCombo;
	private int plankColor;
	public ItemStack plankType;
	private boolean firstRun = true;

	public TileColoredBed() {
		super();
	}

	@Override
	public void writeToNBT(NBTTagCompound nbttagcompound) {
		super.writeToNBT(nbttagcompound);
		nbttagcompound.setInteger("colorCombo", colorCombo);
		nbttagcompound.setInteger("plankColor", plankColor);
		PlankHelper.validatePlank(nbttagcompound);
	}

	@Override
	public void readFromNBT(NBTTagCompound nbttagcompound) {
		super.readFromNBT(nbttagcompound);
		this.colorCombo = nbttagcompound.getInteger("colorCombo");
		this.plankColor = nbttagcompound.getInteger("plankColor");
		this.plankType = PlankHelper.validatePlank(nbttagcompound);
	}

	public ItemStack getPlankType() {
		return plankType;
	}

	public void setPlankType(ItemStack plankType) {
		this.plankType = plankType;
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
