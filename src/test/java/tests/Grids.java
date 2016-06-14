package tests;

import jline.internal.TestAccessible;
import net.minecraftforge.items.ItemStackHandler;
import org.junit.Assert;
import org.junit.Test;
import org.lwjgl.Sys;
import zornco.bedcraftbeyond.items.ItemHandlerGridHelper;
import zornco.bedcraftbeyond.items.SizedItemHandlerWrapper;

import java.awt.*;
import java.util.HashMap;
import java.util.Map;
import java.util.logging.*;

public class Grids {

    private static Logger createLogger(){
        Logger logger = Logger.getLogger("Testing");
        logger.setUseParentHandlers(false);
        if(logger.getHandlers().length == 1) return logger;

        Handler conHdlr = new ConsoleHandler();
        conHdlr.setFormatter(new Formatter() {
            public String format(LogRecord record) {
                return String.format("[%s] %s: %s\n", record.getLevel(), record.getSourceMethodName(), record.getMessage());
            }
        });

        logger.addHandler(conHdlr);
        return logger;
    }

    @Test
    public void TestSlotIndexes() throws Exception {

        Logger logger = createLogger();
        Map<Integer, Rectangle> tests = new HashMap<>();

        // 00 01 02 03 04 05 06 07 08
        // 09 10 11 12 13 14 15 16 17
        // 18 19 20 21 22 23 24 25 26
        tests.put(17, new Rectangle(new Point(8,1), new Dimension(9,3)));

        // 0 1 2
        // 3 4 5
        // 6 7 8
        tests.put(5, new Rectangle(new Point(2,1), new Dimension(3,3)));

        // 0 1
        // 2 3
        tests.put(3, new Rectangle(new Point(1,1), new Dimension(2,2)));

        for(Map.Entry<Integer, Rectangle> test : tests.entrySet()){
            int slot = ItemHandlerGridHelper.getPositionFromPoint(test.getValue().getLocation(), test.getValue().getSize());
            logger.info(String.format("Running test: %s slot from %s", slot, test.getValue()));
            try {
                Assert.assertEquals((int) test.getKey(), slot);
            }

            catch (AssertionError ae){
                logger.severe(ae.getMessage());
            }
        }
    }
}
