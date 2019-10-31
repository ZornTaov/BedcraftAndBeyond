
package zornco.bedcraftbeyond.tests;

import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;

import zornco.bedcraftbeyond.core.util.ColorHelper;

public class ColorHelperTests {

    @Test
    @DisplayName("Can determine closest match")
    void closestMatch() {
    }

    void convertRBG() {
        int red = 0xEE;
        int green = 0x82;
        int blue = 0xEE;

        Color violet = new Color(red, green, blue);

        ColorHelper c = new ColorHelper();
        
    }
}