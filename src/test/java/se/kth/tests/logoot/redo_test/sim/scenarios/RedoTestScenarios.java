package se.kth.tests.logoot.redo_test.sim.scenarios;

import se.kth.sim.common.operations.CommonOperations;
import se.kth.tests.logoot.redo_test.sim.operations.RedoTestOperations;
import se.sics.kompics.simulator.SimulationScenario;
import se.sics.kompics.simulator.adaptor.distributions.ConstantDistribution;
import se.sics.kompics.simulator.adaptor.distributions.extra.BasicIntSequentialDistribution;

/**
 * @author Maxime Dufour on 2017-04-19.
 */
public class RedoTestScenarios {
    public static SimulationScenario redoTest(final int numberOfNodes, final int numberOfInserts) {
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
                        raise(1, RedoTestOperations.startObserverOp, new ConstantDistribution<Integer>(Integer.class, numberOfNodes), new ConstantDistribution<Integer>(Integer.class, numberOfInserts));
                    }
                };
                StochasticProcess startPeers = new StochasticProcess() {
                    {
                        eventInterArrivalTime(uniform(1000, 1100));
                        raise(numberOfNodes, RedoTestOperations.startNodeOp, new BasicIntSequentialDistribution(1), new ConstantDistribution<Integer>(Integer.class, numberOfInserts));
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
