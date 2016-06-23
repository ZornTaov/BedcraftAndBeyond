package zornco.bedcraftbeyond.beds.parts;

import zornco.bedcraftbeyond.beds.parts.linens.LinenType;

public enum EnumBedPart {
    FRAME,
    BLANKETS,
    SHEETS,
    STORAGE,
    UNKNOWN;

    public boolean isLinenPart(){
        return this == BLANKETS || this == SHEETS;
    }

    public LinenType toLinenPart(){
        if(!isLinenPart()) return LinenType.UNKNOWN;
        switch (this){
            case BLANKETS:
                return LinenType.BLANKETS;
            case SHEETS:
                return LinenType.SHEETS;
            default:
                return LinenType.UNKNOWN;
        }
    }
}
