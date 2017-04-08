package se.kth.tests.cb_test.sim.scenarios;

import se.kth.sim.common.operations.CommonOperations;
import se.kth.tests.cb_test.sim.operations.CBTestOperations;
import se.sics.kompics.simulator.SimulationScenario;
import se.sics.kompics.simulator.adaptor.distributions.ConstantDistribution;
import se.sics.kompics.simulator.adaptor.distributions.extra.BasicIntSequentialDistribution;

/**
 * @author Maxime Dufour on 2017-04-06.
 */
public class CBTestScenarios {

    public static SimulationScenario causalOrderScenario(final int numberOfCoreNodes, final int numberOfBroadcasts) {
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
                        raise(1, CBTestOperations.startObserverOp, new ConstantDistribution<Integer>(Integer.class, numberOfCoreNodes), new ConstantDistribution<Integer>(Integer.class, numberOfBroadcasts));
                    }
                };
                StochasticProcess startPeers = new StochasticProcess() {
                    {
                        eventInterArrivalTime(uniform(1000, 1100));
                        raise(numberOfCoreNodes, CBTestOperations.startNodeOp, new BasicIntSequentialDistribution(1), new ConstantDistribution<Integer>(Integer.class, numberOfBroadcasts));
                    }
                };

                systemSetup.start();
                startBootstrapServer.startAfterTerminationOf(1000, systemSetup);
                startPeers.startAfterTerminationOf(1000, startBootstrapServer);
                startObserver.startAfterTerminationOf(1000, startPeers);
                terminateAfterTerminationOf(100000 * 100000, startPeers);
            }
        };
        return scen;
    }
}
