package se.kth.tests.logoot;

import com.google.common.collect.Lists;
import org.junit.Assert;
import org.junit.Before;
import org.junit.Test;
import se.kth.app.logoot.Document;
import se.kth.app.logoot.LineId;
import se.kth.app.logoot.Position;
import se.kth.sim.common.util.ScenarioSetup;
import se.sics.ktoolbox.util.network.KAddress;

import java.util.List;

/**
 * Unit test-suite for Document class
 *
 * @author Kim Hammar on 2017-04-08.
 */
public class DocumentTest {
    Document document;
    LineId p = new LineId(Lists.newArrayList(LineIdTest.generatePosition(2, 4, 7), LineIdTest.generatePosition(59, 9, 5)));
    LineId q = new LineId(Lists.newArrayList(LineIdTest.generatePosition(10, 5, 3),
            LineIdTest.generatePosition(20, 3, 6), LineIdTest.generatePosition(3, 3, 9)));


    @Before
    public void setup() {
        KAddress address = ScenarioSetup.getNodeAdr("193.0.0.0", 1);
        document = new Document(address.getId());
    }

    /**
     * Test prefix generation for lines p and q
     */
    @Test
    public void prefixTest() {
        Assert.assertEquals(2, document.prefix(p, 1));
        Assert.assertEquals(10, document.prefix(q, 1));
        Assert.assertEquals(259, document.prefix(p, 2));
        Assert.assertEquals(1020, document.prefix(q, 2));
        Assert.assertEquals(2590, document.prefix(p, 3));
        Assert.assertEquals(10203, document.prefix(q, 3));
    }

    /**
     * Test generation of N lineIds between p and q
     */
    @Test
    public void generateLineIdTest() {
        int n = 7;
        int boundary = 10;
        List<LineId> list = document.generateLineId(p, q, n, boundary);
        Assert.assertEquals(n, list.size());
        for (LineId lineId : list) {
            for (Position position : lineId.getPositions()) {
                Assert.assertTrue(position.getDigit() > 2);
                Assert.assertTrue(position.getDigit() < 10);
            }
        }

        n = 760;
        boundary = 10;
        list = document.generateLineId(p, q, n, boundary);
        Assert.assertEquals(n, list.size());
        for (LineId lineId : list) {
            Position position0 = lineId.getPositions().get(0);
            Position position1 = lineId.getPositions().get(1);
            if (position0.equals(p.getPositions().get(0))) {
                Assert.assertTrue(position1.getDigit() > 59);
                Assert.assertTrue(position1.getDigit() < 100);
            }
            if (position0.equals(q.getPositions().get(0))) {
                Assert.assertTrue(position1.getDigit() >= 0);
                Assert.assertTrue(position1.getDigit() < 20);
            }
            if (!position0.lessThan(q.getPositions().get(0)) && !position0.equals(p.getPositions().get(0))) {
                Assert.assertTrue(position0.getDigit() > 2);
                Assert.assertTrue(position0.getDigit() < 10);

                Assert.assertTrue(position1.getDigit() >= 0);
                Assert.assertTrue(position1.getDigit() < 100);
            }
        }
    }

}
