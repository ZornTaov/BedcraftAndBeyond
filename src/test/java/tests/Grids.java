package tests;

import net.minecraftforge.items.ItemStackHandler;
import org.junit.Test;
import zornco.bedcraftbeyond.items.ItemHandlerGridHelper;
import zornco.bedcraftbeyond.items.SizedItemHandlerWrapper;

import org.junit.Assert;

import java.awt.*;

public class Grids {

    @Test
    public void TestSlotGetting() throws Exception {
        SizedItemHandlerWrapper wrapper = new SizedItemHandlerWrapper(new ItemStackHandler(9), new Dimension(3, 3));
        int[] slotsV = ItemHandlerGridHelper.getGridIndexes(wrapper, new Rectangle(2,0,1,3));
        int[] slotsH = ItemHandlerGridHelper.getGridIndexes(wrapper, new Rectangle(0,0,3,1));

        // 0 1 2
        // 3 4 5
        // 6 7 8
        Assert.assertArrayEquals(new int[]{ 2, 5, 8 }, slotsV);
        Assert.assertArrayEquals(new int[]{ 0, 1, 2 }, slotsH);
    }
}
