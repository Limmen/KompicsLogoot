package se.kth.tests.rb_test.operations;

import se.kth.sim.common.util.ScenarioSetup;
import se.kth.tests.rb_test.components.RBTestHostMngrComp;
import se.kth.tests.rb_test.components.RBTestObserver;
import se.sics.kompics.network.Address;
import se.sics.kompics.simulator.adaptor.Operation3;
import se.sics.kompics.simulator.events.system.StartNodeEvent;
import se.sics.ktoolbox.util.network.KAddress;

import java.util.HashMap;
import java.util.Map;

/**
 * @author Maxime Dufour on 2017-04-06.
 */
public class RBTestOperations {

    public static Operation3<StartNodeEvent, Integer, Integer, Integer> startNodeOp = new Operation3<StartNodeEvent, Integer, Integer, Integer>() {

        @Override
        public StartNodeEvent generate(final Integer nodeId, final Integer broadcastCount, final Integer churn) {
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
                    return RBTestHostMngrComp.class;
                }

                @Override
                public RBTestHostMngrComp.Init getComponentInit() {
                    return new RBTestHostMngrComp.Init(selfAdr, ScenarioSetup.bootstrapServer, ScenarioSetup.croupierOId, broadcastCount);
                }

                @Override
                public Map<String, Object> initConfigUpdate() {
                    Map<String, Object> nodeConfig = new HashMap<>();
                    nodeConfig.put("system.id", nodeId);
                    nodeConfig.put("system.seed", ScenarioSetup.getNodeSeed(nodeId));
                    nodeConfig.put("system.port", ScenarioSetup.appPort);
                    nodeConfig.put("system.churn", (churn == 1));
                    return nodeConfig;
                }
            };
        }
    };

    public static Operation3<StartNodeEvent, Integer, Integer, Integer> startObserverOp = new Operation3<StartNodeEvent, Integer, Integer, Integer>() {
        @Override
        public StartNodeEvent generate(final Integer numberOfCoreNodes, final Integer numberOfExtensionNodes, final Integer broadcastCount) {
            return new StartNodeEvent() {
                KAddress selfAdr;

                {
                    String nodeIp = "193.193.0.1";
                    selfAdr = ScenarioSetup.getNodeAdr(nodeIp, numberOfCoreNodes + 10);
                }


                @Override
                public Address getNodeAddress() {
                    return selfAdr;
                }

                @Override
                public Class getComponentDefinition() {
                    return RBTestObserver.class;
                }

                @Override
                public RBTestObserver.Init getComponentInit() {
                    return new RBTestObserver.Init(numberOfCoreNodes, numberOfExtensionNodes, broadcastCount);
                }
            };
        }
    };

}
