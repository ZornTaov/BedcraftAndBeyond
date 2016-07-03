package zornco.bedcraftbeyond.parts;

import net.minecraft.item.Item;

public abstract class Part {

    public Part(){ }

    public abstract Type getPartType();

    public abstract Item getPartItem();


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
