package zornco.bedcraftbeyond.common.blocks.properties;

import net.minecraft.util.IStringSerializable;

public enum EnumBedPartStatus implements IStringSerializable {
   HEAD_VALID,
   HEAD_INVALID,
   FOOT_VALID,
   FOOT_INVALID;

   @Override
   public String getName() {
      return name().toLowerCase();
   }
}
