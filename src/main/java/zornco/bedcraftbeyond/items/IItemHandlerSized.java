package zornco.bedcraftbeyond.items;

import net.minecraftforge.items.IItemHandlerModifiable;
import java.awt.Dimension;

public interface IItemHandlerSized extends IItemHandlerModifiable {
    Dimension getSize();
}
