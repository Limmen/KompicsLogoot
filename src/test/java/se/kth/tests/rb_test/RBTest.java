package se.kth.tests.rb_test;

import org.junit.Assert;
import org.junit.Test;
import se.kth.sim.common.result.SimulationResultMap;
import se.kth.sim.common.result.SimulationResultSingleton;
import se.kth.sim.common.util.ScenarioSetup;
import se.kth.tests.rb_test.scenarios.RBTestScenarios;
import se.sics.kompics.simulator.SimulationScenario;
import se.sics.kompics.simulator.run.LauncherComp;

import java.util.HashSet;

/**
 *
 * SimulationScenario to test correctness of the Reliable broadcast abstraction
 *
 * @author Maxime Dufour on 2017-04-06.
 */
public class RBTest {

    private static final int NODES_NO_CORE = 10;
    private static final int NODES_NO_EXTENSION = 3;
    private static final int BROADCASTS_NO = 10;
    private final static SimulationResultMap result = SimulationResultSingleton.getInstance();

    /**
     * This test aims to check if in churn systems all nodes eventually delivered all messages. This test relies
     * on creation and duplication property and will only test ("if a correct node delivers a message, every correct
     * nodes deliver the message")
     */
    @Test
    public void ReliableDeliveryTest(){
        SimulationScenario.setSeed(ScenarioSetup.scenarioSeed);

        SimulationScenario simpleBootScenario = RBTestScenarios.bebTest(NODES_NO_CORE, NODES_NO_EXTENSION, BROADCASTS_NO);
        simpleBootScenario.simulate(LauncherComp.class);

        HashSet reference = result.get(Integer.toString(0), HashSet.class);
        for(int i = 0; i < NODES_NO_CORE + NODES_NO_EXTENSION; i++){
            HashSet nodestate = result.get(Integer.toString(i), HashSet.class);
            Assert.assertEquals(reference, nodestate); //Check that all nodes got the same set of delivered messages
        }
    }



}
