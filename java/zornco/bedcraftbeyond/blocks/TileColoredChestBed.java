package zornco.bedcraftbeyond.blocks;

import java.util.Iterator;
import java.util.List;

import net.minecraft.entity.Entity;
import net.minecraft.entity.player.EntityPlayer;
import net.minecraft.inventory.IInventory;
import net.minecraft.item.ItemStack;
import net.minecraft.nbt.NBTTagCompound;
import net.minecraft.nbt.NBTTagList;
import net.minecraft.util.AxisAlignedBB;
import net.minecraft.util.ChatComponentText;
import net.minecraft.util.ChatComponentTranslation;
import net.minecraft.util.IChatComponent;
import net.minecraft.util.StatCollector;
import zornco.bedcraftbeyond.BedCraftBeyond;
import zornco.bedcraftbeyond.gui.ContainerColoredChestBed;

public class TileColoredChestBed extends TileColoredBed implements IInventory {
	private int ticksSinceSync = -1;
	public ItemStack[] chestContents;
	public float prevLidAngle;
	public float lidAngle;
	private int numUsingPlayers;
	private boolean inventoryTouched;
	public String ownerName = "";

	public TileColoredChestBed() {
		super();
		this.chestContents = new ItemStack[getSizeInventory()];
	}
	@Override
	public void writeToNBT(NBTTagCompound nbttagcompound) {
		super.writeToNBT(nbttagcompound);
		nbttagcompound.setString("OwnerName", ownerName);
		NBTTagList nbttaglist = new NBTTagList();
		for (int i = 0; i < chestContents.length; i++)
		{
			if (chestContents[i] != null)
			{
				NBTTagCompound nbttagcompound1 = new NBTTagCompound();
				nbttagcompound1.setByte("Slot", (byte) i);
				chestContents[i].writeToNBT(nbttagcompound1);
				nbttaglist.appendTag(nbttagcompound1);
			}
		}
		nbttagcompound.setTag("Items", nbttaglist);
	}
	public String getOwnerName() {
		return ownerName;
	}
	public void setOwnerName(String ownerName) {
		this.ownerName = ownerName;
	}
	@Override
	public void readFromNBT(NBTTagCompound nbttagcompound) {
		super.readFromNBT(nbttagcompound);
		ownerName = nbttagcompound.getString("OwnerName");
		NBTTagList nbttaglist = nbttagcompound.getTagList("Items", 10);
		chestContents = new ItemStack[getSizeInventory()];
		for (int i = 0; i < nbttaglist.tagCount(); i++)
		{
			NBTTagCompound nbttagcompound1 = (NBTTagCompound) nbttaglist.getCompoundTagAt(i);
			int j = nbttagcompound1.getByte("Slot") & 0xff;
			if (j >= 0 && j < chestContents.length)
			{
				chestContents[j] = ItemStack.loadItemStackFromNBT(nbttagcompound1);
			}
		}
	}

	@SuppressWarnings("unchecked")
	@Override
	public void update() {
		super.update();
		if (worldObj != null && !this.worldObj.isRemote && this.numUsingPlayers != 0 && (this.ticksSinceSync + pos.hashCode()) % 200 == 0)
		{
			this.numUsingPlayers = 0;
			float var1 = 5.0F;
			List<EntityPlayer> var2 = this.worldObj.getEntitiesWithinAABB(EntityPlayer.class, AxisAlignedBB.fromBounds(this.pos.getX() - var1, this.pos.getY() - var1, this.pos.getZ() - var1, this.pos.getX() + 1 + var1, this.pos.getY() + 1 + var1, this.pos.getZ() + 1 + var1));
			Iterator<EntityPlayer> var3 = var2.iterator();

			while (var3.hasNext())
			{
				EntityPlayer var4 = (EntityPlayer)var3.next();

				if (var4.openContainer instanceof ContainerColoredChestBed)
				{
					++this.numUsingPlayers;
				}
			}
		}

		if (worldObj != null && !worldObj.isRemote && ticksSinceSync < 0)
		{
			worldObj.addBlockEvent(pos, BedCraftBeyond.bedBlock, 3, ((numUsingPlayers << 3) & 0xF8));
		}
		if (!worldObj.isRemote && inventoryTouched)
		{
			inventoryTouched = false;
		}

		this.ticksSinceSync++;
		prevLidAngle = lidAngle;
		float f = 0.1F;
		if (numUsingPlayers > 0 && lidAngle == 0.0F)
		{
			worldObj.playSoundEffect(this.pos.getX() + 0.5D, this.pos.getY() + 0.5D, this.pos.getZ() + 0.5D, "random.chestopen", 0.5F, worldObj.rand.nextFloat() * 0.1F + 0.9F);
		}
		if (numUsingPlayers == 0 && lidAngle > 0.0F || numUsingPlayers > 0 && lidAngle < 1.0F)
		{
			float f1 = lidAngle;
			if (numUsingPlayers > 0)
			{
				lidAngle += f;
			}
			else
			{
				lidAngle -= f;
			}
			if (lidAngle > 1.0F)
			{
				lidAngle = 1.0F;
			}
			float f2 = 0.5F;
			if (lidAngle < f2 && f1 >= f2)
			{
				worldObj.playSoundEffect(this.pos.getX() + 0.5D, this.pos.getY() + 0.5D, this.pos.getZ() + 0.5D, "random.chestclosed", 0.5F, worldObj.rand.nextFloat() * 0.1F + 0.9F);
			}
			if (lidAngle < 0.0F)
			{
				lidAngle = 0.0F;
			}
		}
	}
	@Override
	public boolean receiveClientEvent(int par1, int par2)
	{
		if (par1 == 1)
		{
			this.numUsingPlayers = par2;
			return true;
		}
		else
		{
			return super.receiveClientEvent(par1, par2);
		}
	}

	public ItemStack[] getContents() {
		return chestContents;
	}
	@Override
	public int getSizeInventory() {
		return 9;
	}
	@Override
	public ItemStack getStackInSlot(int i) {
		inventoryTouched = true;
		return chestContents[i];
	}
	@Override
	public ItemStack decrStackSize(int i, int j) {
		if (chestContents[i] != null)
		{
			if (chestContents[i].stackSize <= j)
			{
				ItemStack itemstack = chestContents[i];
				chestContents[i] = null;
				markDirty();
				return itemstack;
			}
			ItemStack itemstack1 = chestContents[i].splitStack(j);
			if (chestContents[i].stackSize == 0)
			{
				chestContents[i] = null;
			}
			markDirty();
			return itemstack1;
		}
		else
		{
			return null;
		}
	}
	@Override
	public ItemStack removeStackFromSlot(int i) {
		if (this.chestContents[i] != null)
		{
			ItemStack var2 = this.chestContents[i];
			this.chestContents[i] = null;
			return var2;
		}
		else
		{
			return null;
		}
	}
	@Override
	public void setInventorySlotContents(int i, ItemStack itemstack) {
		chestContents[i] = itemstack;
		if (itemstack != null && itemstack.stackSize > getInventoryStackLimit())
		{
			itemstack.stackSize = getInventoryStackLimit();
		}
		markDirty();
	}
	@Override
	public String getName() {
		if (ownerName != "")
			return ownerName + "'s " + StatCollector.translateToLocal("inventory.drawers");
		return StatCollector.translateToLocal("inventory.drawers");
	}
	@Override
	public boolean hasCustomName() {
		return this.ownerName != null && this.ownerName.length() > 0;
	}
	@Override
	public int getInventoryStackLimit() {
		return 64;
	}
	@Override
	public boolean isUseableByPlayer(EntityPlayer entityplayer) {
		if (worldObj == null)
		{
			return true;
		}
		if (worldObj.getTileEntity(pos) != this)
		{
			return false;
		}
		//if (entityplayer.username != ownerName && ownerName != "")
		//	return false;
		return entityplayer.getDistanceSq(this.pos.getX() + 0.5D, this.pos.getY() + 0.5D, this.pos.getZ() + 0.5D) <= 64D;

	}
	@Override
	public void openInventory(EntityPlayer player) {
		if (worldObj == null) return;
		numUsingPlayers++;
		worldObj.addBlockEvent(pos, BedCraftBeyond.bedBlock, 1, numUsingPlayers);
	}
	@Override
	public void closeInventory(EntityPlayer player) {
		if (worldObj == null) return;
		numUsingPlayers--;
		worldObj.addBlockEvent(pos, BedCraftBeyond.bedBlock, 1, numUsingPlayers);
	}
	@Override
	public boolean isItemValidForSlot(int i, ItemStack itemstack) {
		return true;
	}
	@Override
	public IChatComponent getDisplayName() {
        return (IChatComponent)(this.hasCustomName() ? new ChatComponentText(this.getName()) : new ChatComponentTranslation(this.getName(), new Object[0]));
	}
	@Override
	public int getField(int id) {
		return 0;
	}
	@Override
	public void setField(int id, int value) {
		
	}
	@Override
	public int getFieldCount() {
		return 0;
	}
	@Override
	public void clear() {

        for (int i = 0; i < this.chestContents.length; ++i)
        {
            this.chestContents[i] = null;
        }
	}

}
