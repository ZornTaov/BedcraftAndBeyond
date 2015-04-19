package zornco.bedcraftbeyond.blocks;

import net.minecraft.nbt.NBTTagCompound;
import net.minecraft.network.NetworkManager;
import net.minecraft.network.Packet;
import net.minecraft.network.play.server.S35PacketUpdateTileEntity;
import net.minecraft.tileentity.TileEntity;

public class TileColoredBed extends TileEntity {
	public int colorCombo;
	private boolean firstRun = true;
	public TileColoredBed() {
		super();
	}
	@Override
	public void writeToNBT(NBTTagCompound nbttagcompound)
	{
		super.writeToNBT(nbttagcompound);
		nbttagcompound.setInteger("colorCombo", colorCombo);
	}

	@Override
	public void readFromNBT(NBTTagCompound nbttagcompound)
	{
		super.readFromNBT(nbttagcompound);
		this.colorCombo = nbttagcompound.getInteger("colorCombo");
	}
	public void setColorCombo(int combo)
	{
		this.colorCombo = combo;
	}

	@Override
	public void updateEntity() {
		if (!worldObj.isRemote && (worldObj.getWorldTime() % 20 == 0 || firstRun )) {
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
		/*S35PacketUpdateTileEntity packet = this.getDescriptionPacket();
		PacketDispatcher.sendPacketToAllInDimension(packet,
				this.worldObj.provider.dimensionId);*/
	}



}
