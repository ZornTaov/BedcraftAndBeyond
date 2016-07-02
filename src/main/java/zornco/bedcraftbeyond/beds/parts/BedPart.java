package zornco.bedcraftbeyond.beds.parts;

import net.minecraft.block.Block;
import net.minecraft.item.Item;

import javax.annotation.Nullable;

public abstract class BedPart {

    public BedPart(){ }

    public abstract Type getPartType();

    public abstract boolean hasBlock();

    public abstract Item getPartItem();

    @Nullable
    public abstract Block getPartBlock();


    public enum Type {
        FRAME,
        BLANKETS,
        SHEETS,
        STORAGE,
        ADDON,
        UNKNOWN;

        public boolean isLinenPart(){
            return this == BLANKETS || this == SHEETS;
        }
    }
}
