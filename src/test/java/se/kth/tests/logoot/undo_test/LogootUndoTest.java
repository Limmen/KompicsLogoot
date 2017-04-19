package se.kth.tests.logoot.undo_test;

import org.junit.Assert;
import org.junit.Test;
import se.kth.sim.common.result.SimulationResultMap;
import se.kth.sim.common.result.SimulationResultSingleton;
import se.kth.sim.common.util.ScenarioSetup;
import se.kth.tests.logoot.undo_test.sim.scenarios.UndoTestScenarios;
import se.sics.kompics.simulator.SimulationScenario;
import se.sics.kompics.simulator.run.LauncherComp;

import java.util.ArrayList;

/**
 * @author Maxime Dufour on 2017-04-19.
 */
public class LogootUndoTest {

    private static final int NODES_NO = 3;
    private static final int INSERTIONS_NO = 10;
    private final static SimulationResultMap result = SimulationResultSingleton.getInstance();

    /**
     * This test aims to test the undo operation, each nodes insert the same number of lines and after the INSERTIONS_NO
     * lines are insert by each node, they undo their own insertions. We expected at the end, the number of lines in the
     * document is equals to 0
     */
    @Test
    public void undoTest() {
        SimulationScenario.setSeed(ScenarioSetup.scenarioSeed);

        SimulationScenario undoTestScenario = UndoTestScenarios.undoTest(NODES_NO, INSERTIONS_NO);
        undoTestScenario.simulate(LauncherComp.class);

        for(int i = 0; i < NODES_NO; i++){
            ArrayList<String> document = result.get(Integer.toString(i), ArrayList.class);
            Assert.assertEquals(0,document.size());
        }

    }
}
