package se.kth.tests.logoot.unit_tests;

import com.google.common.collect.Lists;
import org.junit.Assert;
import org.junit.Test;
import se.kth.app.events.Patch;
import se.kth.app.logoot.*;

/**
 * Unit tests-suite for Patch class
 *
 * @author Kim Hammar on 2017-04-19.
 */
public class PatchTest {

    @Test
    public void inverseTest() {
        LineId lineId1 = new LineId(Lists.newArrayList(LineIdTest.generatePosition(131, 1, 4)));
        LineId lineId2 = new LineId(Lists.newArrayList(LineIdTest.generatePosition(131, 1, 4), LineIdTest.generatePosition(2471, 5, 23)));
        LineId lineId3 = new LineId(Lists.newArrayList(LineIdTest.generatePosition(131, 3, 2)));

        /**
         * Test inverse patch of only insertions
         */
        Operation operation1 = new Insert(lineId1, "first line");
        Operation operation2 = new Insert(lineId2, "second line");
        Operation operation3 = new Insert(lineId3, "third line");

        Patch patch = new Patch(Lists.<Operation>newArrayList(operation1, operation2, operation3));
        Patch invertedPatch = patch.inverse();

        Assert.assertEquals(patch.getOps().size(), invertedPatch.getOps().size());
        Assert.assertTrue(invertedPatch.getOps().stream().allMatch(op -> op.getOperationType() == OperationType.DELETE));

        /**
         * Test inverse patch of only deletions
         */
        operation1 = new Delete(lineId1, "first line");
        operation2 = new Delete(lineId2, "second line");
        operation3 = new Delete(lineId3, "third line");

        patch = new Patch(Lists.<Operation>newArrayList(operation1, operation2, operation3));
        invertedPatch = patch.inverse();

        Assert.assertEquals(patch.getOps().size(), invertedPatch.getOps().size());
        Assert.assertTrue(invertedPatch.getOps().stream().allMatch(op -> op.getOperationType() == OperationType.INSERT));

        /**
         * Test inverse patch of mix of insertions/deletions
         */
        operation1 = new Insert(lineId1, "first line");
        operation2 = new Delete(lineId2, "second line");
        operation3 = new Insert(lineId3, "third line");

        patch = new Patch(Lists.<Operation>newArrayList(operation1, operation2, operation3));
        invertedPatch = patch.inverse();

        Assert.assertEquals(patch.getOps().size(), invertedPatch.getOps().size());
        Assert.assertEquals(OperationType.DELETE, invertedPatch.getOps().get(0).getOperationType());
        Assert.assertEquals(OperationType.INSERT, invertedPatch.getOps().get(1).getOperationType());
        Assert.assertEquals(OperationType.DELETE, invertedPatch.getOps().get(2).getOperationType());
    }
}
