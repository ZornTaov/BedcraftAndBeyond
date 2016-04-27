package zornco.bedcraftbeyond.blocks;

import net.minecraft.nbt.NBTTagCompound;
import zornco.bedcraftbeyond.BedCraftBeyond;

public class TileRainbowBed extends TileColoredBed {

	private int rainbowColor1 = 0, rainbowColor2 = 0;
	public TileRainbowBed() {
		super();
	}
	public int incColor1() {
		return this.setRainbowColor1(getRainbowColor1()+1);
	}
	public int incColor2() {
		return this.setRainbowColor2(getRainbowColor2()+1);
	}
	public int getRainbowColor1() {
		return rainbowColor1;
	}
	public int getRainbowColor2() {
		return rainbowColor2;
	}
	public int setRainbowColor1(int color1) {
		this.rainbowColor1 = color1 < 600?color1:0;
		return this.rainbowColor1;
	}
	public int setRainbowColor2(int color1) {
		this.rainbowColor2 = color1 < 600?color1:0;
		return this.rainbowColor2;
	}
	@Override
	public void writeToNBT(NBTTagCompound nbttagcompound) {
		super.writeToNBT(nbttagcompound);
		nbttagcompound.setInteger("rainbowOffset1", getRainbowColor1());
		nbttagcompound.setInteger("rainbowOffset2", getRainbowColor2());
	}
	@Override
	public void readFromNBT(NBTTagCompound nbttagcompound) {
		super.readFromNBT(nbttagcompound);
		setRainbowColor1(nbttagcompound.getInteger("rainbowOffset1"));
		setRainbowColor2(nbttagcompound.getInteger("rainbowOffset2"));
	}
}
