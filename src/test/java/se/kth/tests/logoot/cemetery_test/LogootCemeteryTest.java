package se.kth.tests.logoot.cemetery_test;

import com.google.common.collect.Lists;
import org.junit.Assert;
import org.junit.Test;
import se.kth.sim.common.result.SimulationResultMap;
import se.kth.sim.common.result.SimulationResultSingleton;
import se.kth.sim.common.util.ScenarioSetup;
import se.kth.tests.logoot.cemetery_test.sim.scenarios.CemeteryTestScenarios;
import se.sics.kompics.simulator.SimulationScenario;
import se.sics.kompics.simulator.run.LauncherComp;

import java.util.ArrayList;

/**
 * @author Maxime Dufour on 2017-04-23.
 */
public class LogootCemeteryTest {


    private static final int NODES_NO = 2;
    private static final int INSERTIONS_NO = 4;
    private final static SimulationResultMap result = SimulationResultSingleton.getInstance();


    /**
     * This test aims to test the cemetery in the algorithm. The cemetery is responsible to handle when the same
     * operations (insertion and deletion) is done concurrently by several peers. This test is based on the one
     * introduced in the logoot paper. There are 2 peers (P1 and P2) with the same inital document state (3 lines with
     * respectively A,B,C). Then, P1 and P2, concurrently, remove the third line and p1 also inserts the line with D.
     * After applying P2's patch, P1 undoes P2's patch but the line C is still removed
     */
    @Test
    public void cemeteryTest() {
        SimulationScenario.setSeed(ScenarioSetup.scenarioSeed);
        SimulationScenario cemeteryTestScenario = CemeteryTestScenarios.cemeteryTest(NODES_NO, INSERTIONS_NO);
        cemeteryTestScenario.simulate(LauncherComp.class);

        ArrayList<String> finalDocument = Lists.newArrayList("A\n", "B\n", "D\n");
        for (int i = 0; i < NODES_NO; i++) {
            ArrayList<String> document = result.get(Integer.toString(i), ArrayList.class);
            Assert.assertEquals(3, document.size());
            for (int j = 0; j < document.size(); j++) {
                Assert.assertEquals(finalDocument.get(j), document.get(j));
            }
        }
    }
}
