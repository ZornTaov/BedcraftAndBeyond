package zornco.bedcraftbeyond.parts;

import net.minecraft.item.ItemStack;

public interface IPartHolder {
    void updatePart(Part.Type part, ItemStack item);
}
