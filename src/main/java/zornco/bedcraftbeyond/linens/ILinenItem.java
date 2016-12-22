package zornco.bedcraftbeyond.linens;

import zornco.bedcraftbeyond.dyes.IColoredItem;

/**
 * Signal that an item is a linen item.
 * Used by the linen handler to make sure an item is valid for storage quickly.
 */
public interface ILinenItem extends IColoredItem {

    LinenType getLinenType();
}
