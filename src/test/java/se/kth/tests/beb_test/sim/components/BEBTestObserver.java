package se.kth.tests.beb_test.sim.components;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import se.kth.broadcast.crb.event.CRBDeliver;
import se.kth.tests.common.result.SimulationResultMap;
import se.kth.tests.common.result.SimulationResultSingleton;
import se.sics.kompics.ComponentDefinition;
import se.sics.kompics.Handler;
import se.sics.kompics.Positive;
import se.sics.kompics.Start;
import se.sics.kompics.network.Network;
import se.sics.kompics.simulator.util.GlobalView;
import se.sics.kompics.timer.SchedulePeriodicTimeout;
import se.sics.kompics.timer.Timeout;
import se.sics.kompics.timer.Timer;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.Map;
import java.util.UUID;

/**
 * @author Kim Hammar on 2017-04-04.
 */
public class BEBTestObserver extends ComponentDefinition {

    private static final Logger LOG = LoggerFactory.getLogger(BEBTestObserver.class);
    private Positive<Network> network = requires(Network.class);
    private Positive<Timer> timer = requires(Timer.class);

    private int delay = 4000;
    private UUID timerId;
    private int[] nodesState;
    private int numberOfNodes;
    private int numberOfBroadcasts;
    private final static SimulationResultMap result = SimulationResultSingleton.getInstance();

    public BEBTestObserver(Init init) {
        numberOfNodes = init.numberOfNodes;
        numberOfBroadcasts = init.numberOfBroadcasts;
        nodesState = new int[numberOfNodes];
        subscribe(handleStart, control);
        subscribe(handleTimeout, timer);
    }

    Handler<Start> handleStart = new Handler<Start>() {
        @Override
        public void handle(Start event) {
            SchedulePeriodicTimeout spt = new SchedulePeriodicTimeout(delay, delay);
            spt.setTimeoutEvent(new ObserverTimeout(spt));
            trigger(spt, timer);
            timerId = spt.getTimeoutEvent().getTimeoutId();
        }
    };

    protected final Handler<ObserverTimeout> handleTimeout = new Handler<ObserverTimeout>() {
        @Override
        public void handle(ObserverTimeout event) {
            GlobalView gv = config().getValue("simulation.globalview", GlobalView.class);
            for (int i = 0; i < numberOfNodes; i++) {
                ArrayList<CRBDeliver> delvered = gv.getValue(Integer.toString(i + 1), ArrayList.class);
                LOG.info("Delivered Size: " + delvered.size());
                nodesState[i] = delvered.size();
            }
            boolean done = true;
            for (int i = 0; i < numberOfNodes; i++) {
                if (nodesState[i] < numberOfBroadcasts * numberOfNodes)
                    done = false;
            }
            if (done) {
                LOG.info("Terminating simulation as every node have delivered the broadcast");
                saveSimulationResult();
                gv.terminate();

            } else
                LOG.info("Waiting for nodes to deliver");
        }
    };

    private void saveSimulationResult() {
        GlobalView gv = config().getValue("simulation.globalview", GlobalView.class);
        for(int i = 0; i < numberOfNodes; i++){
            Map<Integer, String> nodeState = new HashMap<>();
            ArrayList<CRBDeliver> delivered = gv.getValue(Integer.toString(i+1), ArrayList.class);
            for (int j = 0; j < delivered.size(); j++) {
                nodeState.put(j, delivered.get(j).toString());
            }
            result.put(Integer.toString(i), nodeState);
        }
    }


    public static class Init extends se.sics.kompics.Init<BEBTestObserver> {

        public final int numberOfNodes;
        public final int numberOfBroadcasts;

        public Init(int numberOfNodes, int numberOfBroadcasts) {
            this.numberOfNodes = numberOfNodes;
            this.numberOfBroadcasts = numberOfBroadcasts;
        }
    }

    public class ObserverTimeout extends Timeout {

        public ObserverTimeout(SchedulePeriodicTimeout request) {
            super(request);
        }
    }

}
