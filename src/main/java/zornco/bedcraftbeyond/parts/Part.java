package zornco.bedcraftbeyond.parts;

import net.minecraft.item.Item;
import net.minecraft.item.ItemStack;

public abstract class Part {

    public Part(){ }

    public abstract Type getPartType();

    public abstract Item getPartItem();

    public enum Type {
        BLANKETS,
        SHEETS,
        STORAGE,
        ADDON,
        UNKNOWN,
        INVALID;

        public boolean isLinenPart(){
            return this == BLANKETS || this == SHEETS;
        }

        public boolean isUnknownOrInvalid() {
            return this == UNKNOWN || this == INVALID;
        }

    }

    public static Part.Type getPartType(ItemStack item){
        if(!(item.getItem() instanceof IPart)) return Type.INVALID;
        return ((IPart) item.getItem()).getPartReference().getPartType();
    }

    public static Part getPartReference(ItemStack stack) {
        if(!(stack.getItem() instanceof IPart)) return null;
        return ((IPart) stack.getItem()).getPartReference();
    }
}
