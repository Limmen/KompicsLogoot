package se.kth.tests.beb_test.sim.scenarios;

import se.kth.tests.beb_test.sim.operations.BEBTestOperations;
import se.kth.sim.common.operations.CommonOperations;
import se.sics.kompics.simulator.SimulationScenario;
import se.sics.kompics.simulator.adaptor.distributions.ConstantDistribution;
import se.sics.kompics.simulator.adaptor.distributions.extra.BasicIntSequentialDistribution;

/**
 * @author Kim Hammar on 2017-04-04.
 */
public class BEBTestScenarios {
    public static SimulationScenario bebTest(final int numberOfNodes, final int numberOfBroadcasts) {
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
                        raise(1, BEBTestOperations.startObserverOp, new ConstantDistribution<Integer>(Integer.class, numberOfNodes), new ConstantDistribution<Integer>(Integer.class, numberOfBroadcasts));
                    }
                };
                StochasticProcess startPeers = new StochasticProcess() {
                    {
                        eventInterArrivalTime(uniform(1000, 1100));
                        raise(numberOfNodes, BEBTestOperations.startNodeOp, new BasicIntSequentialDistribution(1), new ConstantDistribution<Integer>(Integer.class, numberOfBroadcasts));
                    }
                };

                systemSetup.start();
                startBootstrapServer.startAfterTerminationOf(1000, systemSetup);
                startPeers.startAfterTerminationOf(1000, startBootstrapServer);
                startObserver.startAfterTerminationOf(1000, startPeers);
                terminateAfterTerminationOf(100000*100000, startPeers);
            }
        };

        return scen;
    }
}
