package se.kth.tests.rb_test.scenarios;

import se.kth.sim.common.operations.CommonOperations;
import se.kth.tests.rb_test.operations.RBTestOperations;
import se.sics.kompics.simulator.SimulationScenario;
import se.sics.kompics.simulator.adaptor.distributions.ConstantDistribution;
import se.sics.kompics.simulator.adaptor.distributions.extra.BasicIntSequentialDistribution;

/**
 * @author Maxime Dufour on 2017-04-06.
 */
public class RBTestScenarios {
    public static SimulationScenario bebTest(final int numberOfCoreNodes, final int numberOfExtensionNodes, final int numberOfBroadcasts) {
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
                StochasticProcess startObserver = new StochasticProcess() {
                    {
                        eventInterArrivalTime(constant(1000));
                        raise(1, RBTestOperations.startObserverOp, new ConstantDistribution<Integer>(Integer.class, numberOfCoreNodes), new ConstantDistribution<Integer>(Integer.class, numberOfExtensionNodes), new ConstantDistribution<Integer>(Integer.class, numberOfBroadcasts));
                    }
                };
                StochasticProcess startPeers = new StochasticProcess() {
                    {
                        eventInterArrivalTime(uniform(1000, 1100));
                        raise(numberOfCoreNodes, RBTestOperations.startNodeOp, new BasicIntSequentialDistribution(1), new ConstantDistribution<Integer>(Integer.class, numberOfBroadcasts), new ConstantDistribution<Integer>(Integer.class, 0));
                    }
                };
                StochasticProcess startChurnPeers = new StochasticProcess() {
                    {
                        eventInterArrivalTime(uniform(1000, 1100));
                        raise(numberOfExtensionNodes, RBTestOperations.startNodeOp, new BasicIntSequentialDistribution(numberOfCoreNodes + 1), new ConstantDistribution<Integer>(Integer.class, 0), new ConstantDistribution<Integer>(Integer.class, 1));
                    }
                };

                systemSetup.start();
                startBootstrapServer.startAfterTerminationOf(1000, systemSetup);
                startPeers.startAfterTerminationOf(1000, startBootstrapServer);
                startChurnPeers.startAfterTerminationOf(1000, startPeers);
                startObserver.startAfterTerminationOf(1000, startPeers);
                terminateAfterTerminationOf(100000 * 100000, startPeers);
            }
        };

        return scen;
    }
}
