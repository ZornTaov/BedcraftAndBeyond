
package zornco.bedcraftbeyond.blocks.properties;

import com.google.common.base.Optional;
import net.minecraft.block.properties.IProperty;
import net.minecraftforge.common.property.IUnlistedProperty;

import java.util.Collection;

public class PropertyString implements IUnlistedProperty<String> {
  private final String name;

  public PropertyString(String name) {
    this.name = name;
  }

  public String getName() {
    return this.name;
  }

  public boolean isValid(String value) {
    return !value.isEmpty();
  }

  public Class<String> getType() {
    return String.class;
  }

  public String valueToString(String value) {
    return value;
  }
}
