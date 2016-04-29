package zornco.bedcraftbeyond.blocks.tiles;

import net.minecraft.init.Blocks;
import net.minecraft.item.EnumDyeColor;
import net.minecraft.item.ItemStack;
import net.minecraft.nbt.NBTTagCompound;
import net.minecraft.network.NetworkManager;
import net.minecraft.network.Packet;
import net.minecraft.network.play.server.SPacketUpdateTileEntity;
import net.minecraft.tileentity.TileEntity;
import net.minecraft.util.EnumFacing;
import net.minecraft.util.ResourceLocation;
import zornco.bedcraftbeyond.util.PlankHelper;

// This tile is only to be used ONCE on beds!
// Place it on the head of the bed. Use BlockColoredBed.getTileEntity anywhere on a bed to fetch this instance.
public class TileColoredBed extends TileEntity {
	private EnumDyeColor blankets;
	private EnumDyeColor sheets;
	private int plankColor;
	public ResourceLocation plankType;

	public TileColoredBed() {
		blankets = EnumDyeColor.WHITE;
		sheets = EnumDyeColor.WHITE;
	}

	@Override
	public void writeToNBT(NBTTagCompound tags) {
		super.writeToNBT(tags);
		tags.setInteger("blankets", blankets.getMetadata());
		tags.setInteger("sheets", sheets.getMetadata());
		tags.setInteger("plankColor", plankColor);

		// TODO: Fix plank type
		PlankHelper.validatePlank(tags, getPlankType());
		if (plankType != null) tags.setString("plankType", plankType.toString());
	}

	@Override
	public void readFromNBT(NBTTagCompound tags) {
		super.readFromNBT(tags);
		this.blankets = EnumDyeColor.byMetadata(tags.getInteger("blankets"));
		this.sheets = EnumDyeColor.byMetadata(tags.getInteger("sheets"));
		this.plankColor = tags.getInteger("plankColor");
		// this.plankType = PlankHelper.validatePlank(tags.getTag("plankType"));
		// TODO: Fix plank type here
		this.plankType = new ResourceLocation("minecraft", "planks@0");
	}

	public ItemStack getPlankType(){
		return new ItemStack(Blocks.planks, 1);
	}

	public void setPlankType(ItemStack plankType) {
		this.plankType = plankType.getItem().getRegistryName();
		updateClients();
	}

	public int getPlankColor() {
		return plankColor;
	}

	public void setPlankColor(int plankColor) {
		this.plankColor = plankColor;
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
