package se.kth.tests.logoot.insert_test;

import org.junit.Assert;
import org.junit.Test;
import se.kth.sim.common.result.SimulationResultMap;
import se.kth.sim.common.result.SimulationResultSingleton;
import se.kth.sim.common.util.ScenarioSetup;
import se.kth.tests.logoot.insert_test.sim.scenarios.InsertTestScenarios;
import se.sics.kompics.simulator.SimulationScenario;
import se.sics.kompics.simulator.run.LauncherComp;

import java.util.ArrayList;

/**
 *
 * Test that verifies that concurrent insertions in a logoot document commutes.
 *
 * @author Kim Hammar on 2017-04-10.
 */
public class LogootInsertTest {

    private static final int NODES_NO = 10;
    private static final int INSERTIONS_NO = 10;
    private final static SimulationResultMap result = SimulationResultSingleton.getInstance();

    @Test
    public void insertionsCommuteTest(){
        SimulationScenario.setSeed(ScenarioSetup.scenarioSeed);

        /**
         * Start a simulation scenario where NODES_NO nodes are started and make INSERTIONS_NO number of concurrent insertions
         * to a logoot document, after doing a insertion the operation is broadcasted to the rest of the nodes in the network.
         * In a logoot-document concurrent insertions commute due to unique totally ordered Identifiers. I.e when a set of nodes
         * perform a finite set of insertions concurrently and broadcast operations to each other, eventual consistency should be
         * achieved, i.e when the system is idle all nodes should reach the same state.
         * The scenario is terminated when the InsertTestObserver notices that all nodes have delivered all operations.
         */
        SimulationScenario insertionTestScenario = InsertTestScenarios.insertionTest(NODES_NO, INSERTIONS_NO);
        insertionTestScenario.simulate(LauncherComp.class);

        ArrayList<String> referenceDocument = result.get("0", ArrayList.class);
        /**
         * No crashes and Point-to-point perfect links + RB means all nodes should get all operations.
         * Also no concurrent insertion should overwrite another so every node should have a state with NODES_NO*INSERTIONS_NO inserted lines.
         */
        Assert.assertEquals(NODES_NO*INSERTIONS_NO, referenceDocument.size());
        for(int i = 0; i < NODES_NO; i++){
            ArrayList<String> document = result.get(Integer.toString(i), ArrayList.class); //Eventual Consistency, everyone should have the same state
            Assert.assertEquals(referenceDocument, document);
        }

    }
}
