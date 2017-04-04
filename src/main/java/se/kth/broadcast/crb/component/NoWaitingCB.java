package se.kth.broadcast.crb.component;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import se.kth.broadcast.crb.event.CRBBroadcast;
import se.kth.broadcast.crb.event.CRBDeliver;
import se.kth.broadcast.crb.port.CausalOrderReliableBroadcast;
import se.kth.broadcast.gossipbeb.component.Pair;
import se.kth.broadcast.rb.event.RBBroadcast;
import se.kth.broadcast.rb.event.RBDeliver;
import se.kth.broadcast.rb.port.ReliableBroadcast;
import se.sics.kompics.*;
import se.sics.ktoolbox.util.network.KAddress;

import java.util.HashSet;
import java.util.LinkedList;

public class NoWaitingCB extends ComponentDefinition {

    private static final Logger LOG = LoggerFactory.getLogger(NoWaitingCB.class);

    //*******************************CONNECTIONS********************************
    private Positive<ReliableBroadcast> rb = requires(ReliableBroadcast.class);
    private Negative<CausalOrderReliableBroadcast> crb = provides(CausalOrderReliableBroadcast.class);
    //**************************************************************************


    private KAddress selfAdr;
    private HashSet<KompicsEvent> delivered;
    private LinkedList<Pair<KAddress, KompicsEvent>> past;


    public NoWaitingCB(Init init) {
        this.selfAdr = init.selfAdr;
        this.delivered = new HashSet<>();
        this.past = new LinkedList<>();

        subscribe(broadcastHandler, crb);
        subscribe(deliverHandler, rb);
    }

    Handler<CRBBroadcast> broadcastHandler = new Handler<CRBBroadcast>() {
        @Override
        public void handle(CRBBroadcast crbBroadcast) {
            LOG.debug("CRBBroadcast received by {}", selfAdr);
            KompicsEvent msg = crbBroadcast.getPayload();
            trigger(new RBBroadcast(new CRBData(past, msg)), rb);
            past.addLast(new Pair<>(selfAdr, msg));
        }
    };

    ClassMatchedHandler<CRBData, RBDeliver> deliverHandler = new ClassMatchedHandler<CRBData, RBDeliver>() {
        @Override
        public void handle(CRBData crbData, RBDeliver rbDeliver) {
            LOG.debug("CRBData received by {} from {}", selfAdr, rbDeliver.getSource());
            if (!delivered.contains(crbData.getMsg())) {
                for (Pair<KAddress, KompicsEvent> pair : past) {
                    if (!delivered.contains(pair.p2)) {
                        trigger(new CRBDeliver(pair.p1, pair.p2), crb);
                        delivered.add(pair.p2);
                        if (!past.contains(pair)) {
                            past.addLast(pair);
                        }
                    }
                }
                if (!delivered.contains(crbData.getMsg()))
                    trigger(new CRBDeliver(rbDeliver.getSource(), crbData.getMsg()), crb);
                delivered.add(crbData.getMsg());
                Pair<KAddress, KompicsEvent> pair = new Pair<>(rbDeliver.getSource(), crbData.getMsg());
                if (!past.contains(pair)) {
                    past.addLast(pair);
                }
            }
        }
    };

    public static class Init extends se.sics.kompics.Init<NoWaitingCB> {
        public final KAddress selfAdr;

        public Init(KAddress selfAdr) {
            this.selfAdr = selfAdr;
        }
    }
}
