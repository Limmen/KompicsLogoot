package se.kth.tests.rb_test.rb_delivery_test.sim.operations;

import se.kth.sim.common.util.ScenarioSetup;
import se.kth.tests.rb_test.rb_delivery_test.sim.components.RBTestHostMngrComp;
import se.sics.kompics.network.Address;
import se.sics.kompics.simulator.adaptor.Operation2;
import se.sics.kompics.simulator.events.system.StartNodeEvent;
import se.sics.ktoolbox.util.network.KAddress;

import java.util.HashMap;
import java.util.Map;

/**
 * @author Kim Hammar on 2017-04-07.
 */
public class RBTestOperations {
    public static Operation2<StartNodeEvent, Integer, Integer> startNodeOp = new Operation2<StartNodeEvent, Integer, Integer>() {

        @Override
        public StartNodeEvent generate(final Integer nodeId, final Integer extensionNodes) {
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
                    return new RBTestHostMngrComp.Init(selfAdr, ScenarioSetup.bootstrapServer, ScenarioSetup.croupierOId);
                }

                @Override
                public Map<String, Object> initConfigUpdate() {
                    Map<String, Object> nodeConfig = new HashMap<>();
                    nodeConfig.put("system.id", nodeId);
                    nodeConfig.put("system.seed", ScenarioSetup.getNodeSeed(nodeId));
                    nodeConfig.put("system.port", ScenarioSetup.appPort);
                    if(nodeId < extensionNodes)
                        nodeConfig.put("sim.coreNode", false);
                    else
                        nodeConfig.put("sim.coreNode", true);
                    return nodeConfig;
                }
            };
        }
    };

}
