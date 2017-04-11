package se.kth.tests.logoot.unit_tests;

import com.google.common.collect.Lists;
import org.junit.Assert;
import org.junit.Before;
import org.junit.Test;
import se.kth.app.events.Patch;
import se.kth.app.logoot.*;
import se.kth.sim.common.util.ScenarioSetup;
import se.sics.ktoolbox.util.network.KAddress;

import java.util.List;

/**
 * Unit events-suite for Document class
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
        int clock = 0;
        List<LineId> list = document.generateLineId(p, q, n, boundary, clock);
        Assert.assertEquals(n, list.size());
        for (LineId lineId : list) {
            for (Position position : lineId.getPositions()) {
                Assert.assertTrue(position.getDigit() > 2);
                Assert.assertTrue(position.getDigit() < 10);
            }
        }
        n = 760;
        boundary = 10;
        list = document.generateLineId(p, q, n, boundary, clock);
        Assert.assertEquals(n, list.size());
        for (LineId lineId : list) {
            Position position0 = lineId.getPositions().get(0);
            Position position1 = lineId.getPositions().get(1);
            if (position0.equals(p.getPositions().get(0))) {
                Assert.assertTrue(position1.getDigit() > 59);
                Assert.assertTrue(position1.getDigit() < Integer.MAX_VALUE);
            }
            if (position0.equals(q.getPositions().get(0))) {
                Assert.assertTrue(position1.getDigit() >= 0);
                Assert.assertTrue(position1.getDigit() < 20);
            }
            if (!position0.equals(q.getPositions().get(0)) && !position0.equals(p.getPositions().get(0))) {
                Assert.assertTrue(position0.getDigit() > 2);
                Assert.assertTrue(position0.getDigit() < 10);
                Assert.assertTrue(position1.getDigit() >= 0);
                Assert.assertTrue(position1.getDigit() < Integer.MAX_VALUE);
            }
        }
        n = 76102;
        boundary = 10;
        list = document.generateLineId(p, q, n, boundary, clock);
        Assert.assertEquals(n, list.size());
        for (LineId lineId : list) {
            Position position0 = lineId.getPositions().get(0);
            Position position1 = lineId.getPositions().get(1);
            if (position0.equals(p.getPositions().get(0))) {
                Assert.assertTrue(position1.getDigit() > 59);
                Assert.assertTrue(position1.getDigit() < Integer.MAX_VALUE);
            }
            if (position0.equals(q.getPositions().get(0))) {
                Assert.assertTrue(position1.getDigit() >= 0);
                Assert.assertTrue(position1.getDigit() <= 20);
            }
            if (!position0.equals(q.getPositions().get(0)) && !position0.equals(p.getPositions().get(0))) {
                Assert.assertTrue(position0.getDigit() > 2);
                Assert.assertTrue(position0.getDigit() < 10);
                Assert.assertTrue(position1.getDigit() >= 0);
                Assert.assertTrue(position1.getDigit() < Integer.MAX_VALUE);
            }
        }

    }

    /**
     * Test exeuction of insertion and deletion operations
     */
    @Test
    public void executeTest() {
        LineId firstId = new LineId(Lists.newArrayList(new Position(0, null, null)));
        LineId lineId1 = new LineId(Lists.newArrayList(LineIdTest.generatePosition(131, 1, 4)));
        LineId lineId2 = new LineId(Lists.newArrayList(LineIdTest.generatePosition(131, 1, 4), LineIdTest.generatePosition(2471, 5, 23)));
        LineId lineId3 = new LineId(Lists.newArrayList(LineIdTest.generatePosition(131, 3, 2)));
        LineId endId = new LineId(Lists.newArrayList(new Position(Integer.MAX_VALUE - 1, null, null)));


        /**
         * Insertion test
         */
        Operation operation1 = new Insert(lineId1, "first line");
        Operation operation2 = new Insert(lineId2, "second line");
        Operation operation3 = new Insert(lineId3, "third line");

        Patch insertPatch = new Patch(Lists.<Operation>newArrayList(operation3, operation1, operation2));
        document.execute(insertPatch);

        Assert.assertEquals(3, document.getDocumentLines().size());
        Assert.assertEquals(5, document.getIdTable().size());

        Assert.assertEquals(firstId, document.getIdTable().get(0));
        Assert.assertEquals(lineId1, document.getIdTable().get(1));
        Assert.assertEquals(lineId2, document.getIdTable().get(2));
        Assert.assertEquals(lineId3, document.getIdTable().get(3));
        Assert.assertEquals(endId, document.getIdTable().get(4));

        Assert.assertEquals("first line", document.getDocumentLines().get(0));
        Assert.assertEquals("second line", document.getDocumentLines().get(1));
        Assert.assertEquals("third line", document.getDocumentLines().get(2));

        /**
         * Deletion test
         */
        operation1 = new Delete(lineId1, operation1.getContent());
        operation2 = new Delete(lineId2, operation2.getContent());
        operation3 = new Delete(lineId3, operation3.getContent());

        Patch deletePatch = new Patch(Lists.<Operation>newArrayList(operation3, operation1, operation2));
        document.execute(deletePatch);

        Assert.assertEquals(0, document.getDocumentLines().size());
        Assert.assertEquals(2, document.getIdTable().size());
        Assert.assertEquals(firstId, document.getIdTable().get(0));
        Assert.assertEquals(endId, document.getIdTable().get(1));
    }

}
