package se.kth.tests.beb_test;

import org.junit.Assert;
import org.junit.Test;
import se.kth.tests.beb_test.sim.scenarios.BEBTestScenarios;
import se.kth.sim.common.result.SimulationResultMap;
import se.kth.sim.common.result.SimulationResultSingleton;
import se.kth.sim.common.util.ScenarioSetup;
import se.sics.kompics.simulator.SimulationScenario;
import se.sics.kompics.simulator.run.LauncherComp;

import java.util.Map;

/**
 * @author Kim Hammar on 2017-04-04.
 */
public class BEBTest {

    private static final int NODES_NO = 2;
    private static final int BROADCASTS_NO = 10;
    private final static SimulationResultMap result = SimulationResultSingleton.getInstance();

    @Test
    public void bestEffortDeliveryTest(){
        SimulationScenario.setSeed(ScenarioSetup.scenarioSeed);
        SimulationScenario simpleBootScenario = BEBTestScenarios.bebTest(NODES_NO, BROADCASTS_NO);
        simpleBootScenario.simulate(LauncherComp.class);

        for(int i = 0; i < NODES_NO; i++){
            Map<Integer, String> nodestate = result.get(Integer.toString(i), Map.class);
            Assert.assertEquals(NODES_NO*BROADCASTS_NO, nodestate.size());
        }
    }
}
