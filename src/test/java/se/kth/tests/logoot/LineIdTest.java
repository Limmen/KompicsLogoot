package se.kth.tests.logoot;

import com.google.common.collect.Lists;
import junit.framework.Assert;
import org.junit.Test;
import se.kth.app.logoot.LineId;
import se.kth.app.logoot.Position;
import se.kth.sim.common.util.ScenarioSetup;
import se.sics.ktoolbox.util.network.KAddress;

/**
 * Unit test-suite for LineId class
 *
 * @author Kim Hammar on 2017-04-08.
 */
public class LineIdTest {

    /**
     * Test line comparisons
     */
    @Test
    public void comparableTest(){
        /**
         * Ordered Document:
         *
         * (0,NA,NA)
         * (131,1,4)
         * (131,1,4,(2471,5,23)
         * (131,3,2)
         * (MAX,NA,NA)
         */
        LineId line0 = new LineId(Lists.newArrayList(new Position(0, null, null)));
        LineId line1 = new LineId(Lists.newArrayList(generatePosition(131,1,4)));
        LineId line2 = new LineId(Lists.newArrayList(generatePosition(131,1,4), generatePosition(2471,5,23)));
        LineId line3 = new LineId(Lists.newArrayList(generatePosition(131, 3 ,2)));
        LineId line4 = new LineId(Lists.newArrayList(new Position(Integer.MAX_VALUE - 1, null, null)));

        Assert.assertEquals(0, line0.compareTo(line0));
        Assert.assertEquals(0, line1.compareTo(line1));
        Assert.assertEquals(0, line2.compareTo(line2));
        Assert.assertEquals(0, line3.compareTo(line3));
        Assert.assertEquals(0, line4.compareTo(line4));

        Assert.assertEquals(-1, line0.compareTo(line1));
        Assert.assertEquals(-1, line1.compareTo(line2));
        Assert.assertEquals(-1, line2.compareTo(line3));
        Assert.assertEquals(-1, line3.compareTo(line4));
    }

    /**
     * Helper function that generates a new Position
     *
     * @param digit
     * @param siteId
     * @param clock
     * @return
     */
    public static Position generatePosition(int digit, int siteId, int clock){
        KAddress address = ScenarioSetup.getNodeAdr("193.0.0.0", siteId);
        return new Position(digit, address.getId(), clock);
    }
}
