package se.kth.broadcast.rb.component;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import se.kth.broadcast.gossipbeb.event.GBEBBroadcast;
import se.kth.broadcast.gossipbeb.event.GBEBDeliver;
import se.kth.broadcast.gossipbeb.port.GossipingBestEffortBroadcast;
import se.kth.broadcast.rb.event.RBBroadcast;
import se.kth.broadcast.rb.event.RBDeliver;
import se.kth.broadcast.rb.port.ReliableBroadcast;
import se.sics.kompics.*;
import se.sics.ktoolbox.util.network.KAddress;

import java.util.HashSet;

/**
 * Created by 62maxime on 03/04/2017.
 */
public class EagerRB extends ComponentDefinition {


    private static final Logger LOG = LoggerFactory.getLogger(EagerRB.class);

    //*******************************CONNECTIONS********************************
    private Positive<GossipingBestEffortBroadcast> gbeb = requires(GossipingBestEffortBroadcast.class);
    private Negative<ReliableBroadcast> rb = provides(ReliableBroadcast.class);
    //**************************************************************************

    private HashSet<KompicsEvent> delivered;
    private KAddress selfAdr;

    public EagerRB(Init init) {
        this.delivered = new HashSet<>();
        this.selfAdr = init.selfAdr;

        subscribe(broadcastHandler, rb);
        subscribe(deliverHandler, gbeb);

    }

    Handler broadcastHandler = new Handler<RBBroadcast>() {
        @Override
        public void handle(RBBroadcast rBroadcast) {
            LOG.debug("RBBroadcast received by {}", selfAdr);
            trigger(new GBEBBroadcast(new RBData(selfAdr, rBroadcast.getMsg())), gbeb);
        }
    };


    ClassMatchedHandler<RBData, GBEBDeliver> deliverHandler = new ClassMatchedHandler<RBData, GBEBDeliver>() {
        @Override
        public void handle(RBData rbData, GBEBDeliver gbebDeliver) {
            LOG.debug("GBEBDeliver received by {}", selfAdr);
            if (delivered.add(rbData.getMsg())) {
                LOG.debug("RBDeliver sent by {}", selfAdr);
                trigger(new RBDeliver(rbData.getSource(), rbData.getMsg()), rb);
                trigger(new GBEBBroadcast(gbebDeliver.getPayload()), gbeb);
            }
        }
    };


    public static class Init extends se.sics.kompics.Init<EagerRB> {

        public final KAddress selfAdr;


        public Init(KAddress selfAdr) {
            this.selfAdr = selfAdr;
        }
    }
}
