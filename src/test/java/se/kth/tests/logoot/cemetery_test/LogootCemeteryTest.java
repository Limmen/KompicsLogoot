package se.kth.tests.logoot.cemetery_test;

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

    @Test
    public void deletionsCommuteTest() {
        SimulationScenario.setSeed(ScenarioSetup.scenarioSeed);
        SimulationScenario cemeteryTestScenario = CemeteryTestScenarios.cemeteryTest(NODES_NO, INSERTIONS_NO);
        cemeteryTestScenario.simulate(LauncherComp.class);

        ArrayList<String> referenceDocument = result.get("0", ArrayList.class);
        System.out.println(referenceDocument);
        for (int i = 0; i < NODES_NO; i++) {
            ArrayList<String> document = result.get(Integer.toString(i), ArrayList.class);
            Assert.assertEquals(referenceDocument, document);
            Assert.assertEquals(3, document.size());
        }
    }
}
