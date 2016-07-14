package zornco.bedcraftbeyond.linens;

import net.minecraft.util.IStringSerializable;

public enum PropertyFabricType implements IStringSerializable {
  SOLID_COLOR("colored"),
  TEXTURED("textured"),
  RAINBOW("rainbow"),
  NONE("none");

  private String name;
  PropertyFabricType(String name){ this.name = name; }

  public String getName(){ return name; }
}
