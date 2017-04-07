package se.kth.tests.rb_test.rb_delivery_test.sim.scenarios;

import se.kth.sim.common.operations.CommonOperations;
import se.kth.tests.rb_test.rb_delivery_test.sim.operations.RBTestOperations;
import se.sics.kompics.simulator.SimulationScenario;
import se.sics.kompics.simulator.adaptor.distributions.ConstantDistribution;
import se.sics.kompics.simulator.adaptor.distributions.extra.BasicIntSequentialDistribution;

/**
 * @author Kim Hammar on 2017-04-07.
 */
public class RBTestScenarios {

    public static SimulationScenario reliableDeliveryTest(final int coreNodes, final int extensionNodes) {
        SimulationScenario scen = new SimulationScenario() {
            {
                StochasticProcess systemSetup = new StochasticProcess() {
                    {
                        eventInterArrivalTime(constant(1000));
                        raise(1, CommonOperations.systemSetupOp);
                    }
                };
                StochasticProcess startBootstrapServer = new StochasticProcess() {
                    {
                        eventInterArrivalTime(constant(1000));
                        raise(1, CommonOperations.startBootstrapServerOp);
                    }
                };
                StochasticProcess startAllPeers = new StochasticProcess() {
                    {
                        eventInterArrivalTime(uniform(1000, 1100));
                        raise(coreNodes+extensionNodes, RBTestOperations.startNodeOp, new BasicIntSequentialDistribution(1), new ConstantDistribution<Integer>(Integer.class, extensionNodes));
                    }
                };

                StochasticProcess killFirstHalfExtensionNodes = new StochasticProcess() {
                    {
                        eventInterArrivalTime(uniform(500, 700));
                        raise(extensionNodes/2, CommonOperations.killNodeOp, new BasicIntSequentialDistribution((1)));
                    }
                };

                StochasticProcess restartFirstHalfExtensionNodes = new StochasticProcess() {
                    {
                        eventInterArrivalTime(uniform(1000, 1100));
                        raise(extensionNodes/2, RBTestOperations.startNodeOp, new BasicIntSequentialDistribution(1), new ConstantDistribution<Integer>(Integer.class, extensionNodes));
                    }
                };
                StochasticProcess killAllExtensionNodes = new StochasticProcess() {
                    {
                        eventInterArrivalTime(uniform(500, 700));
                        raise(extensionNodes, CommonOperations.killNodeOp, new BasicIntSequentialDistribution((1)));
                    }
                };

                systemSetup.start();
                startBootstrapServer.startAfterTerminationOf(1000, systemSetup);
                startAllPeers.startAfterTerminationOf(1000, startBootstrapServer);
                /**
                 * Simulate some churn, crashes + restarts
                 */
                killFirstHalfExtensionNodes.startAfterTerminationOf(1000, startAllPeers);
                restartFirstHalfExtensionNodes.startAfterTerminationOf(1000, killFirstHalfExtensionNodes);
                killAllExtensionNodes.startAfterTerminationOf(10000, restartFirstHalfExtensionNodes);
                terminateAfterTerminationOf(10000, killAllExtensionNodes); //This should be enough time for all correct nodes to deliver broadcasts from the crashed nodes
            }
        };

        return scen;
    }
}
