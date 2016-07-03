package zornco.bedcraftbeyond.beds.wooden;

import net.minecraft.util.IStringSerializable;

public enum EnumWoodenFabricStatus implements IStringSerializable {
   HEAD,
   HEAD_LINENS,
   FOOT,
   FOOT_LINENS;

   @Override
   public String getName() {
      return name().toLowerCase();
   }
}
