package zornco.bedcraftbeyond.client.colors;

import net.minecraft.item.EnumDyeColor;
import net.minecraft.item.ItemDye;
import net.minecraft.util.IStringSerializable;

public enum EnumBedFabricType implements IStringSerializable {
  SOLID_COLOR("colored"),
  TEXTURED("textured"),
  RAINBOW("rainbow"),
  NONE("none");

  private String name;
  private int meta;
  EnumBedFabricType(String name){ this.name = name; meta = -1; }
  EnumBedFabricType(String name, int meta){ this.name = name; this.meta = meta; }

  public EnumDyeColor toDye(){ return (this.meta != -1 ? EnumDyeColor.byMetadata(meta) : EnumDyeColor.BLACK); }
  public int getMetadata(){ return this.meta; }
  public String getName(){ return name; }
  public Boolean isDyeType(){ return this.meta != -1; }
  public int getDyeColor(){ if(this.isDyeType()) return ItemDye.dyeColors[meta]; return 0; }
}
