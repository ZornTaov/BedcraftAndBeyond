package zornco.bedcraftbeyond.core.gui;

import net.minecraft.client.gui.Gui;
import net.minecraft.entity.player.EntityPlayer;
import net.minecraft.inventory.Slot;
import net.minecraftforge.items.IItemHandler;
import net.minecraftforge.items.SlotItemHandler;
import net.minecraftforge.items.wrapper.InvWrapper;
import org.w3c.dom.css.Rect;
import zornco.bedcraftbeyond.core.BedCraftBeyond;

import javax.vecmath.Vector2f;
import java.awt.*;
import java.util.ArrayList;
import java.util.List;

public class GuiUtils {

    private static final int SLOT_SIZE = 18;
    private static final Dimension STANDARD_INVENTORY_SIZE = new Dimension(SLOT_SIZE * 9, SLOT_SIZE * 4 + 4);

    public static List<Slot> createSlotGrid(IItemHandler inventory, int start, Dimension gridSize, Point startPos){
        ArrayList<Slot> slots = new ArrayList<>();

        float totalSlots = gridSize.width * gridSize.height;

        Point slotPos = new Point(1, 1);
        try {
            for (int slot = start; slot < start + totalSlots; ++slot) {
                Point slotPosition = new Point();
                slotPosition.x = startPos.x + ((slotPos.x - 1) * SLOT_SIZE) + 1;
                slotPosition.y = startPos.y + ((slotPos.y - 1) * SLOT_SIZE) + 1;
                slots.add(new SlotItemHandler(inventory, slot, slotPosition.x, slotPosition.y));

                ++slotPos.x;
                if (slotPos.x == gridSize.width + 1) {
                    slotPos.x = 1;
                    ++slotPos.y;
                }
            }
        }

        catch(Exception e){
            BedCraftBeyond.LOGGER.error(e);
        }

        return slots;
    }

    /**
     * Generates an array of slots for wrapping the player's main inventory.
     *
     * @param owner The player.
     * @param position The position (x-y) to start creating slots at.
     * @return
     */
    public static List<Slot> createStandardInventory(EntityPlayer owner, Point position) {
        ArrayList<Slot> slots = new ArrayList<>();

        // Inventory - start at Y = 2 to exclude hotbar
        InvWrapper inv = new InvWrapper(owner.inventory);
        List<Slot> mainSlots = createSlotGrid(inv, 9, new Dimension(9, 3), position);
        slots.addAll(mainSlots);


        // Hotbar
        // Reposition to below main inventory - basically three rows of slots down, plus a little space
        position.y += 4 + (SLOT_SIZE * 3);

        List<Slot> hotbar = createSlotGrid(inv, 0, new Dimension(9, 1), position);
        slots.addAll(hotbar);

        return slots;
    }

    public static Rectangle getInventoryArea(Point position){
        return new Rectangle(position, STANDARD_INVENTORY_SIZE);
    }

    public static Rectangle getInventoryAreaCentered(Dimension guiSize, int startY) {
        Point start = new Point((guiSize.width / 2) - (STANDARD_INVENTORY_SIZE.width / 2), startY);
        return new Rectangle(start, STANDARD_INVENTORY_SIZE);
    }

    public static void drawRectangle(Rectangle r, Color c){
        Gui.drawRect(r.x, r.y, r.x + r.width, r.y + r.height, c.getRGB());
    }
}
