package se.kth.tests.logoot.delete_test;

import org.junit.Assert;
import org.junit.Test;
import se.kth.sim.common.result.SimulationResultMap;
import se.kth.sim.common.result.SimulationResultSingleton;
import se.kth.sim.common.util.ScenarioSetup;
import se.kth.tests.logoot.delete_test.sim.scenarios.DeleteTestScenarios;
import se.sics.kompics.simulator.SimulationScenario;
import se.sics.kompics.simulator.run.LauncherComp;

import java.util.ArrayList;

/**
 *
 * Test that verifies that concurrent deletions + insertions in a logoot document commutes.
 *
 * @author Kim Hammar on 2017-04-19.
 */
public class LogootDeleteTest {

    private static final int NODES_NO = 10;
    private static final int INSERTIONS_NO = 10;
    private static final int DELETIONS_NO = 5;
    private final static SimulationResultMap result = SimulationResultSingleton.getInstance();

    @Test
    public void deletionsCommuteTest(){
        SimulationScenario.setSeed(ScenarioSetup.scenarioSeed);

        /**
         * Starts a simulation scenario where NODES_NO are doing INSERTIONS_NO insertions and DELETIONS_NO deletions to
         * the logoot document in a randomized order (deletions delete random line from the document).
         * The scenario is terminated by the observer when all nodes have received all the operations.
         * Upon termination the test verifies that all the operation commuted and that eventual consistency was reached.
         */
        SimulationScenario deleteTestScenario = DeleteTestScenarios.deletionTest(NODES_NO, INSERTIONS_NO, DELETIONS_NO);
        deleteTestScenario.simulate(LauncherComp.class);

        ArrayList<String> referenceDocument = result.get("0", ArrayList.class);
        for (int i = 0; i < NODES_NO; i++) {
            ArrayList<String> document = result.get(Integer.toString(i), ArrayList.class);
            Assert.assertEquals(referenceDocument, document);
        }
    }
}
