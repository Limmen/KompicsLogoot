package se.kth.tests.cb_test;

import org.junit.Assert;
import org.junit.Test;
import se.kth.sim.common.result.SimulationResultMap;
import se.kth.sim.common.result.SimulationResultSingleton;
import se.kth.sim.common.util.ScenarioSetup;
import se.sics.kompics.simulator.SimulationScenario;
import se.sics.kompics.simulator.run.LauncherComp;

import java.util.List;

import static se.kth.tests.cb_test.sim.scenarios.CBTestScenarios.causalOrderScenario;

/**
 * @author Maxime Dufour on 2017-04-08.
 */
public class CBTest {

    private static final int NODES_NO_CORE = 3;
    private static final int BROADCASTS_NO = 3;
    private final static SimulationResultMap result = SimulationResultSingleton.getInstance();

    /**
     * This test aims to check if the causal order delivery property holds in th system. In this test, there are only
     * correct nodes. To prove this property, a Lamport timestamp is added in Pang object and are incremented as
     * specified in the Lamport principle
     */
    @Test
    public void causalOrderDeliveryTest() {
        SimulationScenario.setSeed(ScenarioSetup.scenarioSeed);
        SimulationScenario causalOrderScenario = causalOrderScenario(NODES_NO_CORE, BROADCASTS_NO);
        causalOrderScenario.simulate(LauncherComp.class);

        for (int i = 0; i < NODES_NO_CORE; i++) {
            List<Integer> nodeState = result.get(Integer.toString(i), List.class);
            int previousTs = 0;
            int ts = 0;
            /**
             * Check if the Lamport timestamp are order in the delivery set
             */
            for (int j = 0; j < nodeState.size(); j++) {
                previousTs = ts;
                ts = nodeState.get(j);
                Assert.assertTrue(ts >= previousTs);
            }
        }
    }
}
