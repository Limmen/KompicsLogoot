package se.kth.tests.logoot.delete_test.sim.scenarios;

import se.kth.sim.common.operations.CommonOperations;
import se.kth.tests.logoot.delete_test.sim.operations.DeleteTestOperations;
import se.sics.kompics.simulator.SimulationScenario;
import se.sics.kompics.simulator.adaptor.distributions.ConstantDistribution;
import se.sics.kompics.simulator.adaptor.distributions.extra.BasicIntSequentialDistribution;

/**
 * @author Kim Hammar on 2017-04-11.
 */
public class DeleteTestScenarios {
    public static SimulationScenario deletionTest(final int numberOfNodes, final int numberOfInserts, final int numberOfDeletions) {
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
                        raise(1, DeleteTestOperations.startObserverOp, new ConstantDistribution<Integer>(Integer.class, numberOfNodes), new ConstantDistribution<Integer>(Integer.class, numberOfInserts), new ConstantDistribution<Integer>(Integer.class, numberOfDeletions));
                    }
                };
                StochasticProcess startPeers = new StochasticProcess() {
                    {
                        eventInterArrivalTime(uniform(1000, 1100));
                        raise(numberOfNodes, DeleteTestOperations.startNodeOp, new BasicIntSequentialDistribution(1), new ConstantDistribution<Integer>(Integer.class, numberOfInserts), new ConstantDistribution<Integer>(Integer.class, numberOfDeletions));
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
