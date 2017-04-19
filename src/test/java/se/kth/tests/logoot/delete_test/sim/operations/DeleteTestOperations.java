package se.kth.tests.logoot.delete_test.sim.operations;

import se.kth.sim.common.util.ScenarioSetup;
import se.kth.tests.logoot.delete_test.sim.components.DeleteTestHostMngrComp;
import se.kth.tests.logoot.delete_test.sim.components.DeleteTestObserver;
import se.sics.kompics.network.Address;
import se.sics.kompics.simulator.adaptor.Operation3;
import se.sics.kompics.simulator.events.system.StartNodeEvent;
import se.sics.ktoolbox.util.network.KAddress;

import java.util.HashMap;
import java.util.Map;

/**
 * @author Kim Hammar on 2017-04-11.
 */
public class DeleteTestOperations {

    public static Operation3<StartNodeEvent, Integer, Integer, Integer> startNodeOp = new Operation3<StartNodeEvent, Integer, Integer, Integer>() {

        @Override
        public StartNodeEvent generate(final Integer nodeId, final Integer numberOfInserts, final Integer numberOfDeletions) {
            return new StartNodeEvent() {
                KAddress selfAdr;

                {
                    String nodeIp = "193.0.0." + nodeId;
                    selfAdr = ScenarioSetup.getNodeAdr(nodeIp, nodeId);
                }

                @Override
                public Address getNodeAddress() {
                    return selfAdr;
                }

                @Override
                public Class getComponentDefinition() {
                    return DeleteTestHostMngrComp.class;
                }

                @Override
                public DeleteTestHostMngrComp.Init getComponentInit() {
                    return new DeleteTestHostMngrComp.Init(selfAdr, ScenarioSetup.bootstrapServer, ScenarioSetup.croupierOId, numberOfInserts, numberOfDeletions);
                }

                @Override
                public Map<String, Object> initConfigUpdate() {
                    Map<String, Object> nodeConfig = new HashMap<>();
                    nodeConfig.put("system.id", nodeId);
                    nodeConfig.put("system.seed", ScenarioSetup.getNodeSeed(nodeId));
                    nodeConfig.put("system.port", ScenarioSetup.appPort);
                    return nodeConfig;
                }
            };
        }
    };

    public static Operation3<StartNodeEvent, Integer, Integer, Integer> startObserverOp = new Operation3<StartNodeEvent, Integer, Integer, Integer>() {
        @Override
        public StartNodeEvent generate(final Integer numberOfNodes, final Integer numberOfInserts, final Integer numberOfDeletions) {
            return new StartNodeEvent() {
                KAddress selfAdr;

                {
                    String nodeIp = "193.193.0.1";
                    selfAdr = ScenarioSetup.getNodeAdr(nodeIp, numberOfNodes + 10);
                }

                @Override
                public Address getNodeAddress() {
                    return selfAdr;
                }

                @Override
                public Class getComponentDefinition() {
                    return DeleteTestObserver.class;
                }

                @Override
                public DeleteTestObserver.Init getComponentInit() {
                    return new DeleteTestObserver.Init(numberOfNodes, numberOfInserts, numberOfDeletions);
                }
            };
        }
    };

}
