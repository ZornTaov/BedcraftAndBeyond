package zornco.bedcraftbeyond.blocks.tiles;

import net.minecraft.entity.player.EntityPlayer;
import net.minecraft.init.Blocks;
import net.minecraft.item.ItemStack;
import net.minecraft.nbt.NBTTagCompound;
import net.minecraft.network.NetworkManager;
import net.minecraft.network.Packet;
import net.minecraft.network.play.server.SPacketUpdateTileEntity;
import net.minecraft.tileentity.TileEntity;
import net.minecraft.util.ResourceLocation;
import net.minecraftforge.fml.common.network.NetworkRegistry;
import zornco.bedcraftbeyond.BedCraftBeyond;
import zornco.bedcraftbeyond.blocks.BlockWoodenBed;
import zornco.bedcraftbeyond.client.colors.IColoredItem;
import zornco.bedcraftbeyond.client.colors.EnumBedFabricType;
import zornco.bedcraftbeyond.item.ItemBlanket;
import zornco.bedcraftbeyond.item.ItemSheets;
import zornco.bedcraftbeyond.network.ColoredBedUpdate;
import zornco.bedcraftbeyond.util.PlankHelper;

// This tile is only to be used ONCE on beds!
// Place it on the head of the bed. Use BlockWoodenBed.getTileEntity anywhere on a bed to fetch this instance.
public class TileColoredBed extends TileEntity {
	private ItemStack blankets;
	private ItemStack sheets;
	private int plankColor;
	public ResourceLocation plankType;

	public TileColoredBed() { }

	@Override
	public void writeToNBT(NBTTagCompound tags) {
		super.writeToNBT(tags);
		if(blankets != null) tags.setTag("blankets", blankets.writeToNBT(new NBTTagCompound()));
		if(sheets != null) tags.setTag("sheets", sheets.writeToNBT(new NBTTagCompound()));
		tags.setInteger("plankColor", plankColor);

		// TODO: Fix plank type
		PlankHelper.validatePlank(tags, getPlankType());
		if (plankType != null) tags.setString("plankType", plankType.toString());
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
		return new ItemStack(Blocks.planks, 1);
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

	public EnumBedFabricType getPartColor(BlockWoodenBed.EnumColoredPart part){
		ItemStack i = null;
		if(part == BlockWoodenBed.EnumColoredPart.BLANKETS) i = blankets;
		if(part == BlockWoodenBed.EnumColoredPart.SHEETS) i = sheets;
		if(i == null) return EnumBedFabricType.NONE;
		EnumBedFabricType type = ((IColoredItem) i.getItem()).getColor(i);
		return type != null ? type : EnumBedFabricType.NONE;
	}

	public EnumBedFabricType getSheetsColor(){ return getPartColor(BlockWoodenBed.EnumColoredPart.SHEETS); }

	public EnumBedFabricType getBlanketsColor(){ return getPartColor(BlockWoodenBed.EnumColoredPart.BLANKETS); }

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

	public final void updateClients(BlockWoodenBed.EnumColoredPart part) {
		if (worldObj.isRemote) return;
		markDirty();
		ColoredBedUpdate update = new ColoredBedUpdate(pos, part, getPartColor(part));
		BedCraftBeyond.network.sendToAllAround(update, new NetworkRegistry.TargetPoint(worldObj.provider.getDimension(), pos.getX(), pos.getY(), pos.getZ(), 25));
	}

}
