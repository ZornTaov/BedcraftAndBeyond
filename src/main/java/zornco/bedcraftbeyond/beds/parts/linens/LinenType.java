package zornco.bedcraftbeyond.beds.parts.linens;

import zornco.bedcraftbeyond.beds.parts.EnumBedPart;

public enum LinenType {
    BLANKETS,
    SHEETS,
    UNKNOWN;

    public EnumBedPart toBedPart(){
        switch (this){
            case BLANKETS:
                return EnumBedPart.BLANKETS;
            case SHEETS:
                return EnumBedPart.SHEETS;
            default:
                return EnumBedPart.UNKNOWN;
        }
    }
}
