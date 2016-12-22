package zornco.bedcraftbeyond.linens;

import net.minecraft.util.IStringSerializable;

public class LinenFabricTypes {

    public enum BlanketTypes implements IStringSerializable {
        NONE("none"),
        SOLID_COLOR("solid_color"),
        RAINBOW("rainbow");

        private String name;
        BlanketTypes(String name) { this.name = name; }

        @Override
        public String getName() {
            return name;
        }
    }

    public enum SheetTypes implements IStringSerializable {
        NONE("none"),
        SOLID_COLOR("solid_color"),
        RAINBOW("rainbow");

        private String name;
        SheetTypes(String name) { this.name = name; }

        @Override
        public String getName() {
            return name;
        }
    }
}
