package se.kth.tests.beb_test;

import org.junit.Assert;
import org.junit.Test;
import se.kth.tests.beb_test.sim.scenarios.BEBTestScenarios;
import se.kth.sim.common.result.SimulationResultMap;
import se.kth.sim.common.result.SimulationResultSingleton;
import se.kth.sim.common.util.ScenarioSetup;
import se.sics.kompics.simulator.SimulationScenario;
import se.sics.kompics.simulator.run.LauncherComp;

import java.util.HashSet;
import java.util.Map;

/**
 *
 * SimulationScenario to events correctness of the Best-Effort broadcast abstraction
 *
 * @author Kim Hammar on 2017-04-04.
 */
public class BEBTest {

    private static final int NODES_NO = 10;
    private static final int BROADCASTS_NO = 10;
    private final static SimulationResultMap result = SimulationResultSingleton.getInstance();

    @Test
    public void bestEffortDeliveryTest(){
        SimulationScenario.setSeed(ScenarioSetup.scenarioSeed);

        /**
         * Scenario where NODES_NO are booted and send BROADCASTS_NO broadcasts to each other.
         * SimationObserver terminates the simulation when all nodes have received NODES_NO*BROADCAST_NO messages,
         * all nodes should deliver exactly this amount of messages, this follows from the No duplication and no creation properties
         * and that all nodes are correct.
         */
        SimulationScenario bebTestScenario = BEBTestScenarios.bebTest(NODES_NO, BROADCASTS_NO);
        bebTestScenario.simulate(LauncherComp.class);

        Map<Integer, String> reference = result.get(Integer.toString(0), Map.class);
        for(int i = 0; i < NODES_NO; i++){
            Map<Integer, String> nodestate = result.get(Integer.toString(i), Map.class);

            Assert.assertEquals(NODES_NO*BROADCASTS_NO, nodestate.size()); //Check that nodes got correct amount of delivered messages (no creation)
            Assert.assertEquals(reference.keySet(), nodestate.keySet());//Check that all nodes got the same set of delivered messages
            Assert.assertEquals(new HashSet<>(reference.values()), new HashSet<>(nodestate.values())); //Check that all nodes got the same set of delivered messages
            Assert.assertEquals(NODES_NO*BROADCASTS_NO, new HashSet<>(reference.values()).size()); //No duplicates
        }
    }



}
