package se.kth.tests.beb_test.sim.operations;

import se.kth.tests.beb_test.sim.components.BEBTestHostMngrComp;
import se.kth.tests.beb_test.sim.components.BEBTestObserver;
import se.kth.sim.common.util.ScenarioSetup;
import se.sics.kompics.network.Address;
import se.sics.kompics.simulator.adaptor.Operation2;
import se.sics.kompics.simulator.events.system.StartNodeEvent;
import se.sics.ktoolbox.util.network.KAddress;

import java.util.HashMap;
import java.util.Map;

/**
 * @author Kim Hammar on 2017-04-04.
 */
public class BEBTestOperations {

    public static Operation2<StartNodeEvent, Integer, Integer> startNodeOp = new Operation2<StartNodeEvent, Integer, Integer>() {

        @Override
        public StartNodeEvent generate(final Integer nodeId, final Integer broadcastCount) {
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
                    return BEBTestHostMngrComp.class;
                }

                @Override
                public BEBTestHostMngrComp.Init getComponentInit() {
                    return new BEBTestHostMngrComp.Init(selfAdr, ScenarioSetup.bootstrapServer, ScenarioSetup.croupierOId, broadcastCount);
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

    public static Operation2<StartNodeEvent, Integer, Integer> startObserverOp = new Operation2<StartNodeEvent, Integer, Integer>() {
        @Override
        public StartNodeEvent generate(final Integer numberOfNodes, final Integer broadcastCount) {
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
                    return BEBTestObserver.class;
                }

                @Override
                public BEBTestObserver.Init getComponentInit() {
                    return new BEBTestObserver.Init(numberOfNodes, broadcastCount);
                }
            };
        }
    };

}
