package se.kth.tests.cb_test.sim.sim.components;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import se.kth.app.test.Pang;
import se.kth.broadcast.crb.event.CRBDeliver;
import se.kth.sim.common.result.SimulationResultMap;
import se.kth.sim.common.result.SimulationResultSingleton;
import se.sics.kompics.ComponentDefinition;
import se.sics.kompics.Handler;
import se.sics.kompics.Positive;
import se.sics.kompics.Start;
import se.sics.kompics.network.Network;
import se.sics.kompics.simulator.util.GlobalView;
import se.sics.kompics.timer.SchedulePeriodicTimeout;
import se.sics.kompics.timer.Timeout;
import se.sics.kompics.timer.Timer;

import java.util.*;

/**
 * @author Maxime Dufour on 2017-04-06.
 */
public class CBTestObserver extends ComponentDefinition {

    private static final Logger LOG = LoggerFactory.getLogger(CBTestObserver.class);
    private Positive<Network> network = requires(Network.class);
    private Positive<Timer> timer = requires(Timer.class);

    private int delay = 4000;
    private UUID timerId;
    private int[] nodesState;
    private int numberOfCoreNodes;
    private int numberOfBroadcasts;
    private final static SimulationResultMap result = SimulationResultSingleton.getInstance();

    public CBTestObserver(Init init) {
        numberOfCoreNodes = init.numberOfCoreNodes;
        numberOfBroadcasts = init.numberOfBroadcasts;
        nodesState = new int[numberOfCoreNodes];
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
            for (int i = 0; i < numberOfCoreNodes ; i++) {
                List<Integer> delvered = gv.getValue(Integer.toString(i + 1), List.class);
                LOG.info("Delivered Size: " + delvered.size());
                nodesState[i] = delvered.size();
            }
            boolean done = true;
            for (int i = 0; i < numberOfCoreNodes; i++) {
                if (nodesState[i] < numberOfBroadcasts * numberOfCoreNodes) {
                    done = false;
                }
            }
            if (done) {
                LOG.info("Terminating simulation as every node have delivered the broadcasts");
                saveSimulationResult();
                gv.terminate();

            } else
                LOG.info("Waiting for nodes to deliver");
        }
    };

    private void saveSimulationResult() {
        GlobalView gv = config().getValue("simulation.globalview", GlobalView.class);
        for (int i = 0; i < numberOfCoreNodes; i++) {
            List<Integer> delivered = gv.getValue(Integer.toString(i + 1), List.class);
            result.put(Integer.toString(i), delivered);
        }
    }


    public static class Init extends se.sics.kompics.Init<CBTestObserver> {

        public final int numberOfCoreNodes;
        public final int numberOfBroadcasts;

        public Init(int numberOfCoreNodes, int numberOfBroadcasts) {
            this.numberOfCoreNodes = numberOfCoreNodes;
            this.numberOfBroadcasts = numberOfBroadcasts;
        }

    }

    public class ObserverTimeout extends Timeout {

        public ObserverTimeout(SchedulePeriodicTimeout request) {
            super(request);
        }
    }

}
