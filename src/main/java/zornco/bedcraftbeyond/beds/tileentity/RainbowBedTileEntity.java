package zornco.bedcraftbeyond.beds.tileentity;

import net.minecraft.item.DyeColor;
import net.minecraft.nbt.CompoundNBT;
import net.minecraft.network.play.server.SUpdateTileEntityPacket;
import net.minecraft.tileentity.ITickableTileEntity;
import net.minecraft.tileentity.TileEntity;
import net.minecraft.tileentity.TileEntityType;
import net.minecraftforge.api.distmarker.*;
import zornco.bedcraftbeyond.beds.blocks.RainbowBedBlock;
import zornco.bedcraftbeyond.core.BedcraftBlocks;

public class RainbowBedTileEntity extends TileEntity implements ITickableTileEntity {
    private DyeColor color;
    private int rainbowColor1 = 0, rainbowColor2 = 0;

    public RainbowBedTileEntity() {
       super(BedcraftBlocks.Rainbowbedte.get());
    }
 
    public RainbowBedTileEntity(DyeColor colorIn) {
       this();
       this.setColor(colorIn);
    }
 
	public RainbowBedTileEntity(DyeColor color, DyeColor color2) {
        this();
	}

    /**
     * Retrieves packet to send to the client whenever this Tile Entity is resynced via World.notifyBlockUpdate. For
     * modded TE's, this packet comes back to you clientside in {@link #onDataPacket}
     */
    public SUpdateTileEntityPacket getUpdatePacket() {
       return new SUpdateTileEntityPacket(this.pos, 11, this.getUpdateTag());
    }
 
    @OnlyIn(Dist.CLIENT)
    public DyeColor getColor() {
       if (this.color == null) {
          this.color = ((RainbowBedBlock)this.getBlockState().getBlock()).getColor();
       }
 
       return this.color;
    }
    @Override
    public CompoundNBT write(CompoundNBT nbttagcompound) {
       super.write(nbttagcompound);
       nbttagcompound.putInt("rainbowOffset1", getRainbowColor1());
       nbttagcompound.putInt("rainbowOffset2", getRainbowColor2());
       return nbttagcompound;
    }
    @Override
    public void read(CompoundNBT nbttagcompound) {
       super.read(nbttagcompound);
       setRainbowColor1(nbttagcompound.getInt("rainbowOffset1"));
       setRainbowColor2(nbttagcompound.getInt("rainbowOffset2"));
    }
 
    public void setColor(DyeColor color) {
       this.color = color;
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
    public void tick() {
        
      incColor1();
      incColor2();
    }
}
