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

    public static ItemStack getStackFromPoint(Point p, IItemHandlerSized handler){
        int slot = getPositionFromPoint(p, handler.getSize());
        return handler.getStackInSlot(slot);
    }

    public static int getPositionFromPoint(Point p, Dimension size){
        return p.x + (p.y * size.width);
    }

    private static void doChecks(IItemHandler handler, Dimension handlerSize, Rectangle gridArea){
        if(handler.getSlots() < (gridArea.getWidth() * gridArea.getHeight()))
            throw new IndexOutOfBoundsException("Grid size too big to traverse.");

        Rectangle handlerSizedWrapped = new Rectangle(new Point(), handlerSize);
        if(!handlerSizedWrapped.contains(gridArea))
            throw new IndexOutOfBoundsException("Given grid area is not valid for handler grid.");
    }

    public static int[] getGridIndexes(IItemHandler handler, Dimension handlerSize, Rectangle area){
        List<Integer> points = new ArrayList<>();
        doChecks(handler, handlerSize, area);

        Point startPosition = area.getLocation();
        Point currePosition = new Point(startPosition);
        while(points.size() < area.width * area.height){
            points.add(getPositionFromPoint(currePosition, handlerSize));

            currePosition.translate(1,0);
            if(currePosition.x == area.width + startPosition.x) {
                currePosition.x = startPosition.x;
                currePosition.y += 1;
            }
        }

        int[] result = points.stream().mapToInt(Integer::intValue).toArray();
        return result;
    }

    public static ItemStack[] getGridItems(IItemHandler handler, Dimension handlerSize, Rectangle area){
        doChecks(handler, handlerSize, area);
        List<ItemStack> stacks = new ArrayList<>();

        int[] stackIndexes = getGridIndexes(handler, handlerSize, area);
        for(int slotID : stackIndexes) {
            ItemStack stack = handler.getStackInSlot(slotID);
            stacks.add(stack);
        }

        return stacks.toArray(new ItemStack[stacks.size()]);
    }

    public static ItemStack[] getGridItems(IItemHandlerSized handler, Dimension gridSize, Point startPoint){
        Rectangle gridArea = new Rectangle(startPoint, gridSize);
        return getGridItems(handler, gridArea);
    }

    public static ItemStack[] getGridItems(IItemHandlerSized handler, Rectangle area){
        doChecks(handler, handler.getSize(), area);
        List<ItemStack> stacks = new ArrayList<>();

        int[] stackIndexes = getGridIndexes(handler, handler.getSize(), area);
        for(int slotID : stackIndexes) {
            ItemStack stack = handler.getStackInSlot(slotID);
            stacks.add(stack);
        }

        return stacks.toArray(new ItemStack[stacks.size()]);
    }

    public static IItemHandler getItemsWrapped(IItemHandler handler, Dimension handlerSize, Rectangle area){
        ItemStack[] stacks = getGridItems(handler, handlerSize, area);
        return new ItemStackHandler(stacks);
    }

    public static IItemHandler getItemsWrapped(IItemHandlerSized handler, Rectangle area){
        ItemStack[] stacks = getGridItems(handler, area);
        return new ItemStackHandler(stacks);
    }
}
