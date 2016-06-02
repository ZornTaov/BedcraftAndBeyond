package zornco.bedcraftbeyond.common.gui;

import com.google.common.collect.ImmutableList;
import net.minecraft.entity.player.EntityPlayer;
import net.minecraft.inventory.Slot;
import net.minecraftforge.items.IItemHandler;
import net.minecraftforge.items.SlotItemHandler;
import net.minecraftforge.items.wrapper.InvWrapper;
import zornco.bedcraftbeyond.BedCraftBeyond;

import javax.vecmath.Vector2f;
import java.util.ArrayList;
import java.util.List;

public class GuiUtils {

    private static final int SLOT_SIZE = 18;

    public static List<Slot> createSlotGrid(IItemHandler inventory, int start, Vector2f gridSize, Vector2f startPos){
        ArrayList<Slot> slots = new ArrayList<>();

        float totalSlots = gridSize.x * gridSize.y;

        Vector2f slotPos = new Vector2f(1, 1);
        try {
            for (int slot = start; slot < start + totalSlots; ++slot) {
                Vector2f slotPosition = new Vector2f();
                slotPosition.x = (int) startPos.x + ((slotPos.x - 1) * SLOT_SIZE) + 1;
                slotPosition.y = (int) startPos.y + ((slotPos.y - 1) * SLOT_SIZE) + 1;
                slots.add(new SlotItemHandler(inventory, slot, (int) slotPosition.x, (int) slotPosition.y));

                ++slotPos.x;
                if (slotPos.x == gridSize.x + 1) {
                    slotPos.x = 1;
                    ++slotPos.y;
                }
            }
        }

        catch(Exception e){
            BedCraftBeyond.logger.error(e);
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
    public static List<Slot> createStandardInventory(EntityPlayer owner, Vector2f position) {
        ArrayList<Slot> slots = new ArrayList<Slot>();

        // Inventory - start at Y = 2 to exclude hotbar
        InvWrapper inv = new InvWrapper(owner.inventory);
        List<Slot> mainSlots = createSlotGrid(inv, 9, new Vector2f(9, 3), position);
        slots.addAll(mainSlots);


        // Hotbar
        // Reposition to below main inventory - basically three rows of slots down, plus a little space
        position.y += 4 + (SLOT_SIZE * 3);

        List<Slot> hotbar = createSlotGrid(inv, 0, new Vector2f(9, 1), position);
        slots.addAll(hotbar);

        return slots;
    }
}
