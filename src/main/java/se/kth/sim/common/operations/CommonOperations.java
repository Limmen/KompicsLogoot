package se.kth.sim.common.operations;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import se.kth.sim.common.util.ScenarioSetup;
import se.kth.sim.common.util.SimNodeIdExtractor;
import se.kth.system.HostMngrComp;
import se.sics.kompics.network.Address;
import se.sics.kompics.simulator.adaptor.Operation;
import se.sics.kompics.simulator.adaptor.Operation1;
import se.sics.kompics.simulator.events.system.KillNodeEvent;
import se.sics.kompics.simulator.events.system.SetupEvent;
import se.sics.kompics.simulator.events.system.StartNodeEvent;
import se.sics.kompics.simulator.network.identifier.IdentifierExtractor;
import se.sics.ktoolbox.omngr.bootstrap.BootstrapServerComp;
import se.sics.ktoolbox.util.network.KAddress;

import java.util.HashMap;
import java.util.Map;

/**
 * Common operations used in many simulation scenarios.
 *
 * @author Kim Hammar on 2017-04-04.
 */
public class CommonOperations {
    private static final Logger LOG = LoggerFactory.getLogger(CommonOperations.class);

    /**
     * Operation for doing system-setup tasks.
     */
    public static Operation<SetupEvent> systemSetupOp = new Operation<SetupEvent>() {
        @Override
        public SetupEvent generate() {
            return new SetupEvent() {
                @Override
                public IdentifierExtractor getIdentifierExtractor() {
                    return new SimNodeIdExtractor();
                }
            };
        }
    };

    /**
     * Operation for booting the bootstrap-server.
     */
    public static Operation<StartNodeEvent> startBootstrapServerOp = new Operation<StartNodeEvent>() {

        @Override
        public StartNodeEvent generate() {
            return new StartNodeEvent() {
                KAddress selfAdr;

                {
                    selfAdr = ScenarioSetup.bootstrapServer;
                }

                @Override
                public Address getNodeAddress() {
                    return selfAdr;
                }

                @Override
                public Class getComponentDefinition() {
                    return BootstrapServerComp.class;
                }

                @Override
                public BootstrapServerComp.Init getComponentInit() {
                    return new BootstrapServerComp.Init(selfAdr);
                }
            };
        }
    };

    /**
     * Starts a peer node from the HostMngrComp.class
     */
    public static Operation1<StartNodeEvent, Integer> startNodeOp = new Operation1<StartNodeEvent, Integer>() {

        @Override
        public StartNodeEvent generate(final Integer nodeId) {
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
                    return HostMngrComp.class;
                }

                @Override
                public HostMngrComp.Init getComponentInit() {
                    return new HostMngrComp.Init(selfAdr, ScenarioSetup.bootstrapServer, ScenarioSetup.croupierOId);
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

    /**
     * Operation for killing a node by generating a KillNodeEvent, takes pid as argument.
     */
    public static Operation1 killNodeOp = new Operation1<KillNodeEvent, Integer>() {
        @Override
        public KillNodeEvent generate(final Integer self) {
            return new KillNodeEvent() {
                KAddress selfAdr;

                {
                    String nodeIp = "193.0.0." + self;
                    selfAdr = ScenarioSetup.getNodeAdr(nodeIp, self);
                    LOG.info("Killing node: " + selfAdr);
                }

                @Override
                public Address getNodeAddress() {
                    return selfAdr;
                }

                @Override
                public String toString() {
                    return "KillNode<" + selfAdr.toString() + ">";
                }
            };
        }
    };

}
