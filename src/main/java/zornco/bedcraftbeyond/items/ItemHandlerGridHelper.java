package zornco.bedcraftbeyond.items;

import net.minecraft.item.ItemStack;
import net.minecraftforge.items.IItemHandler;
import net.minecraftforge.items.IItemHandlerModifiable;
import net.minecraftforge.items.ItemStackHandler;
import org.lwjgl.util.*;

import java.awt.*;
import java.awt.Dimension;
import java.awt.Point;
import java.awt.Rectangle;
import java.util.ArrayList;
import java.util.List;

public abstract class ItemHandlerGridHelper {

    public static int getPositionFromPoint(Point p, Dimension size){
        return p.x + (p.y * size.width);
    }

    private static void doChecks(IItemHandlerSized handler, Rectangle gridArea){
        if(handler.getSlots() < (gridArea.getWidth() * gridArea.getHeight()))
            throw new IndexOutOfBoundsException("Grid size too big to traverse.");

        Rectangle handlerSize = new Rectangle(new Point(0,0), handler.getSize());
        if(!handlerSize.contains(gridArea))
            throw new IndexOutOfBoundsException("Given grid area is not valid for handler grid.");
    }

    public static int[] getGridIndexes(IItemHandlerSized handler, Rectangle area){
        List<Integer> points = new ArrayList<>();
        doChecks(handler, area);

        Point startPosition = area.getLocation();
        Point currePosition = new Point(startPosition);
        while(points.size() < area.width * area.height){
            points.add(getPositionFromPoint(currePosition, handler.getSize()));

            currePosition.translate(1,0);
            if(currePosition.x == area.width + startPosition.x) {
                currePosition.x = startPosition.x;
                currePosition.y += 1;
            }
        }

        int[] result = points.stream().mapToInt(Integer::intValue).toArray();
        return result;
    }

    public static ItemStack[] getGridItems(IItemHandlerSized handler, Dimension gridSize, Point startPoint){
        Rectangle gridArea = new Rectangle(startPoint, gridSize);
        return getGridItems(handler, gridArea);
    }

    public static ItemStack[] getGridItems(IItemHandlerSized handler, Rectangle area){
        doChecks(handler, area);
        List<ItemStack> stacks = new ArrayList<>();

        int[] stackIndexes = getGridIndexes(handler, area);
        for(int slotID : stackIndexes) {
            ItemStack stack = handler.getStackInSlot(slotID);
            stacks.add(stack);
        }

        return stacks.toArray(new ItemStack[stacks.size()]);
    }

    public static IItemHandler getItemsWrapped(IItemHandlerSized handler, Rectangle area){
        ItemStack[] stacks = getGridItems(handler, area);
        return new ItemStackHandler(stacks);
    }
}
