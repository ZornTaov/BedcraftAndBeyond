package zornco.bedcraftbeyond.client.colors;

import net.minecraft.item.EnumDyeColor;
import net.minecraft.item.ItemDye;
import net.minecraft.util.IStringSerializable;

public enum EnumBedFabricType implements IStringSerializable {
  WHITE("white", 0),
  ORANGE("orange", 1),
  MAGENTA("magenta", 2),
  LIGHT_BLUE("light_blue", 3),
  YELLOW("yellow", 4),
  LIME("lime", 5),
  PINK("pink", 6),
  GRAY("gray", 7),
  SILVER("silver", 8),
  CYAN("cyan", 9),
  PURPLE("purple", 10),
  BLUE("blue", 11),
  BROWN("brown", 12),
  GREEN("green", 13),
  RED("red", 14),
  BLACK("black", 15),
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
