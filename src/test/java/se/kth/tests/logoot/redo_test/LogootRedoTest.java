package se.kth.tests.logoot.redo_test;

import org.junit.Assert;
import org.junit.Test;
import se.kth.sim.common.result.SimulationResultMap;
import se.kth.sim.common.result.SimulationResultSingleton;
import se.kth.sim.common.util.ScenarioSetup;
import se.kth.tests.logoot.redo_test.sim.scenarios.RedoTestScenarios;
import se.sics.kompics.simulator.SimulationScenario;
import se.sics.kompics.simulator.run.LauncherComp;

import java.util.ArrayList;

/**
 * @author Maxime Dufour on 2017-04-19.
 */
public class LogootRedoTest {

    private static final int NODES_NO = 3;
    private static final int INSERTIONS_NO = 10;
    private final static SimulationResultMap result = SimulationResultSingleton.getInstance();

    /**
     * This test aims to test the redo operation, each nodes insert the same number of lines and after the INSERTIONS_NO
     * lines are insert by each node, they undo their own insertions and finally they restore the lines. We expected at the end, the number of lines in the
     * document is equals to NODES_NO*INSERTIONS_NO and that all nodes contain the same state (eventual consistency).
     */
    @Test
    public void redoTest() {
        SimulationScenario.setSeed(ScenarioSetup.scenarioSeed);

        SimulationScenario redoTestScenario = RedoTestScenarios.redoTest(NODES_NO, INSERTIONS_NO);
        redoTestScenario.simulate(LauncherComp.class);

        ArrayList<String> referenceDocument = result.get("0", ArrayList.class);
        for(int i = 0; i < NODES_NO; i++){
            ArrayList<String> document = result.get(Integer.toString(i), ArrayList.class);
            Assert.assertEquals(NODES_NO*INSERTIONS_NO, document.size());
            Assert.assertEquals(referenceDocument, document);
        }

    }
}
