package se.kth.tests.logoot.unit_tests;

import junit.framework.Assert;
import org.junit.Test;
import se.kth.app.logoot.Position;
import se.kth.sim.common.util.ScenarioSetup;
import se.sics.ktoolbox.util.network.KAddress;

/**
 * Unit test-suite for Position class
 *
 * @author Kim Hammar on 2017-04-08.
 */
public class PositionTest {

    /**
     * Test comparisons
     */
    @Test
    public void comparableTest() {
        String ip = "193.0.0.0";

        KAddress address1 = ScenarioSetup.getNodeAdr(ip, 1);
        KAddress address2 = ScenarioSetup.getNodeAdr(ip, 2);

        Position position1 = new Position(1, address1.getId(), 0);
        Position position2 = new Position(2, address1.getId(), 0);

        Assert.assertEquals(-1, position1.compareTo(position2));

        position1 = new Position(1, address1.getId(), 0);
        position2 = new Position(1, address2.getId(), 0);

        Assert.assertEquals(-1, position1.compareTo(position2));

        position1 = new Position(1, address1.getId(), 0);
        position2 = new Position(1, address1.getId(), 1);

        Assert.assertEquals(-1, position1.compareTo(position2));

        position1 = new Position(1, address1.getId(), 0);
        position2 = new Position(1, address1.getId(), 0);

        Assert.assertEquals(0, position1.compareTo(position2));

        Position first = new Position(0, null, null);
        Position last = new Position(Integer.MAX_VALUE - 1, null, null);

        Assert.assertEquals(1, position1.compareTo(first));
        Assert.assertEquals(1, last.compareTo(first));

    }
}
