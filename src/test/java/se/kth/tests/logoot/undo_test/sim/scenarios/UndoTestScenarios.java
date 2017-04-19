package se.kth.tests.logoot.undo_test.sim.scenarios;

import se.kth.sim.common.operations.CommonOperations;
import se.kth.tests.logoot.insert_test.sim.operations.InsertTestOperations;
import se.kth.tests.logoot.undo_test.sim.operations.UndoTestOperations;
import se.sics.kompics.simulator.SimulationScenario;
import se.sics.kompics.simulator.adaptor.distributions.ConstantDistribution;
import se.sics.kompics.simulator.adaptor.distributions.extra.BasicIntSequentialDistribution;

/**
 * @author Maxime Dufour on 2017-04-19.
 */
public class UndoTestScenarios {
    public static SimulationScenario undoTest(final int numberOfNodes, final int numberOfInserts) {
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
                        raise(1, UndoTestOperations.startObserverOp, new ConstantDistribution<Integer>(Integer.class, numberOfNodes), new ConstantDistribution<Integer>(Integer.class, numberOfInserts));
                    }
                };
                StochasticProcess startPeers = new StochasticProcess() {
                    {
                        eventInterArrivalTime(uniform(1000, 1100));
                        raise(numberOfNodes, UndoTestOperations.startNodeOp, new BasicIntSequentialDistribution(1), new ConstantDistribution<Integer>(Integer.class, numberOfInserts));
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
