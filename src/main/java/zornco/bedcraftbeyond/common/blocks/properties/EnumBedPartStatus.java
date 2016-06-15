package zornco.bedcraftbeyond.common.blocks.properties;

import net.minecraft.util.IStringSerializable;

public enum EnumBedPartStatus implements IStringSerializable {
   HEAD,
   HEAD_LINENS,
   FOOT,
   FOOT_LINENS;

   @Override
   public String getName() {
      return name().toLowerCase();
   }
}
