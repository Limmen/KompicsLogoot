package se.kth.tests.rb_test.rb_delivery_test;

import org.junit.Assert;
import org.junit.Test;
import se.kth.broadcast.crb.event.CRBDeliver;
import se.kth.sim.common.result.SimulationResultMap;
import se.kth.sim.common.result.SimulationResultSingleton;
import se.kth.sim.common.util.ScenarioSetup;
import se.kth.tests.rb_test.rb_delivery_test.sim.scenarios.RBTestScenarios;
import se.sics.kompics.simulator.SimulationScenario;
import se.sics.kompics.simulator.run.LauncherComp;

import java.util.Set;

/**
 *
 * Test ReliableDelivery property of ReliabeBroadcast abstraction.
 *
 * @author Kim Hammar on 2017-04-07.
 */
public class RBTest {

    private static final int NODES_NO_CORE = 10;
    private static final int NODES_NO_EXTENSION = 10;
    private final static SimulationResultMap result = SimulationResultSingleton.getInstance();

        @Test
        public void reliableDeliveryTest() {
            SimulationScenario.setSeed(ScenarioSetup.scenarioSeed);

            /**
             * Start NODES_NO_CORE + NODES_NO_EXTENSION. During their lifetime extension-nodes will send broadcasts to
             * all other nodes. Extension nodes are stopped and restarted as new nodes during the scenario and finally all of the
             * extension nodes are stopped.
             * Nodes with id 1-NODES_NO_EXTENSION are the extension nodes.
             * The scenario is terminated a substantial time after all extension nodes have been killed off, since only
             * extension nodes send broadcasts this mean that all correct nodes should have delivered the same set of
             * messages when the scenario is terminated.
             */
            SimulationScenario reliableDeliveryScenario = RBTestScenarios.reliableDeliveryTest(NODES_NO_CORE, NODES_NO_EXTENSION);
            reliableDeliveryScenario.simulate(LauncherComp.class);

            Set<CRBDeliver> reference = result.get(Integer.toString(NODES_NO_EXTENSION+1), Set.class);
            for(int i = NODES_NO_EXTENSION; i < NODES_NO_CORE+NODES_NO_EXTENSION; i++){
                /**
                 * Verify Agreement of RB:
                 * If a message m is delivered by some correct process, then m is eventually delivered by every correct process.
                 */
                Assert.assertEquals(reference, result.get(Integer.toString(i+1), Set.class));
            }

            /**
             * Just verify that the scenario works and nodes actually are crashed. The set of delivered messages
             * that are delivered by the extension nodes crashed early should be fewer than what is delivered by
             * the core nodes that live throughout the whole scenario.
             */
            Assert.assertTrue(result.get("1", Set.class).size() < reference.size());
        }
}
