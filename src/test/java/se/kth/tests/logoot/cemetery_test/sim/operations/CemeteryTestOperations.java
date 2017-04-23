package se.kth.tests.logoot.cemetery_test.sim.operations;

import se.kth.sim.common.util.ScenarioSetup;
import se.kth.tests.logoot.cemetery_test.sim.components.CemeteryTestHostMngrComp;
import se.kth.tests.logoot.cemetery_test.sim.components.CemeteryTestObserver;
import se.sics.kompics.network.Address;
import se.sics.kompics.simulator.adaptor.Operation2;
import se.sics.kompics.simulator.events.system.StartNodeEvent;
import se.sics.ktoolbox.util.network.KAddress;

import java.util.HashMap;
import java.util.Map;

/**
 * @author Maxime Dufour on 2017-04-19.
 */
public class CemeteryTestOperations {

    public static Operation2<StartNodeEvent, Integer, Integer> startNodeOp = new Operation2<StartNodeEvent, Integer, Integer>() {

        @Override
        public StartNodeEvent generate(final Integer nodeId, final Integer numberOfInserts) {
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
                    return CemeteryTestHostMngrComp.class;
                }

                @Override
                public CemeteryTestHostMngrComp.Init getComponentInit() {
                    return new CemeteryTestHostMngrComp.Init(selfAdr, ScenarioSetup.bootstrapServer, ScenarioSetup.croupierOId, numberOfInserts);
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
        public StartNodeEvent generate(final Integer numberOfNodes, final Integer numberOfInserts) {
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
                    return CemeteryTestObserver.class;
                }

                @Override
                public CemeteryTestObserver.Init getComponentInit() {
                    return new CemeteryTestObserver.Init(numberOfNodes, numberOfInserts);
                }
            };
        }
    };

}
