package zornco.bedcraftbeyond.beds.tileentity;

import net.minecraft.item.DyeColor;
import net.minecraft.network.play.server.SUpdateTileEntityPacket;
import net.minecraft.tileentity.ITickableTileEntity;
import net.minecraft.tileentity.TileEntity;
import net.minecraft.tileentity.TileEntityType;
import net.minecraftforge.api.distmarker.*;
import zornco.bedcraftbeyond.beds.blocks.RainbowBedBlock;

public class RainbowBedTileEntity extends TileEntity implements ITickableTileEntity {
    private DyeColor color;

    public RainbowBedTileEntity() {
       super(TileEntityType.BED);
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
 
    public void setColor(DyeColor color) {
       this.color = color;
    }

    @Override
    public void tick() {
        // TODO Auto-generated method stub

    }
}
