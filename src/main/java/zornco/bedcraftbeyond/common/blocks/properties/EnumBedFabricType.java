package zornco.bedcraftbeyond.common.blocks.properties;

import net.minecraft.item.EnumDyeColor;
import net.minecraft.item.ItemDye;
import net.minecraft.util.IStringSerializable;

public enum EnumBedFabricType implements IStringSerializable {
  SOLID_COLOR("colored"),
  TEXTURED("textured"),
  RAINBOW("rainbow"),
  NONE("none");

  private String name;
  EnumBedFabricType(String name){ this.name = name; }

  public String getName(){ return name; }
}
