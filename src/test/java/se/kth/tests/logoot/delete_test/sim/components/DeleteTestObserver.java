package se.kth.tests.logoot.delete_test.sim.components;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import se.kth.app.logoot.Document;
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

import java.util.ArrayList;
import java.util.UUID;

/**
 * @author Kim Hammar on 2017-04-11.
 */
public class DeleteTestObserver extends ComponentDefinition {

    private static final Logger LOG = LoggerFactory.getLogger(DeleteTestObserver.class);
    private Positive<Network> network = requires(Network.class);
    private Positive<Timer> timer = requires(Timer.class);

    private int delay = 4000;
    private UUID timerId;
    private int[] nodesState;
    private int numberOfNodes;
    private int numberOfInsertionsPerNode;
    private int numberOfDeletionsPerNode;
    private final static SimulationResultMap result = SimulationResultSingleton.getInstance();

    public DeleteTestObserver(Init init) {
        numberOfNodes = init.numberOfNodes;
        numberOfInsertionsPerNode = init.numberOfInsertionsPerNode;
        numberOfDeletionsPerNode = init.numberOfDeletionsPerNode;
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
                ArrayList<CRBDeliver> delivered = gv.getValue(Integer.toString(i + 1)+"-delivered", ArrayList.class);
                LOG.info("Delivered Size: " + delivered.size());
                nodesState[i] = delivered.size();
            }
            boolean done = true;
            for (int i = 0; i < numberOfNodes; i++) {
                if (nodesState[i] < (numberOfInsertionsPerNode + numberOfDeletionsPerNode) * numberOfNodes)
                    done = false;
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

        for (int i = 0; i < numberOfNodes; i++) {
            Document document = gv.getValue(Integer.toString(i + 1)+"-document", Document.class);
            result.put(Integer.toString(i), document.getDocumentLines());
        }
    }


    public static class Init extends se.sics.kompics.Init<DeleteTestObserver> {

        public final int numberOfNodes;
        public final int numberOfInsertionsPerNode;
        public final int numberOfDeletionsPerNode;

        public Init(int numberOfNodes, int numberOfInsertionsPerNode, int numberOfDeletionsPerNode) {
            this.numberOfNodes = numberOfNodes;
            this.numberOfInsertionsPerNode = numberOfInsertionsPerNode;
            this.numberOfDeletionsPerNode = numberOfDeletionsPerNode;
        }
    }

    public class ObserverTimeout extends Timeout {

        public ObserverTimeout(SchedulePeriodicTimeout request) {
            super(request);
        }
    }

}
