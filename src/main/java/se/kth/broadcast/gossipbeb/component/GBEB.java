package se.kth.broadcast.gossipbeb.component;

import com.google.common.collect.ImmutableSet;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import se.kth.broadcast.gossipbeb.event.GBEBBroadcast;
import se.kth.broadcast.gossipbeb.event.GBEBDeliver;
import se.kth.broadcast.gossipbeb.event.HistoryRequest;
import se.kth.broadcast.gossipbeb.event.HistoryResponse;
import se.kth.broadcast.gossipbeb.port.GossipingBestEffortBroadcast;
import se.kth.croupier.util.CroupierHelper;
import se.sics.kompics.*;
import se.sics.kompics.network.Network;
import se.sics.kompics.network.Transport;
import se.sics.ktoolbox.croupier.CroupierPort;
import se.sics.ktoolbox.croupier.event.CroupierSample;
import se.sics.ktoolbox.util.network.KAddress;
import se.sics.ktoolbox.util.network.KContentMsg;
import se.sics.ktoolbox.util.network.KHeader;
import se.sics.ktoolbox.util.network.basic.BasicContentMsg;
import se.sics.ktoolbox.util.network.basic.BasicHeader;

import java.util.HashSet;
import java.util.List;

/**
 * Gossip-Best-Effort-Broadcast component.
 * The component is connected to a PSS that provides it with a new set of neighbors periodically.
 */
public class GBEB extends ComponentDefinition {


    private static final Logger LOG = LoggerFactory.getLogger(GBEB.class);

    //*******************************CONNECTIONS********************************
    private Positive<Network> networkPort = requires(Network.class);
    private Positive<CroupierPort> croupierPort = requires(CroupierPort.class);
    private Negative<GossipingBestEffortBroadcast> gbeb = provides(GossipingBestEffortBroadcast.class);
    //**************************************************************************
    private KAddress selfAdr;
    private HashSet<Pair<KAddress, KompicsEvent>> past;


    /**
     * Initialize component and subscribe to channels.
     *
     * @param init
     */
    public GBEB(Init init) {
        past = new HashSet<>();
        selfAdr = init.selfAdr;

        subscribe(broadcastHandler, gbeb);
        subscribe(sampleHandler, croupierPort);
        subscribe(historyRequestHandler, networkPort);
        subscribe(historyResponseHandler, networkPort);
    }

    protected final Handler<GBEBBroadcast> broadcastHandler = new Handler<GBEBBroadcast>() {
        @Override
        public void handle(GBEBBroadcast gbebBroadcast) {
            LOG.debug("GBEBBroadcast receives by {}", selfAdr);
            past.add(new Pair<>(selfAdr, gbebBroadcast.getEvent()));
            trigger(new GBEBDeliver(selfAdr, gbebBroadcast.getEvent()), gbeb);
        }
    };

    protected final Handler<CroupierSample> sampleHandler = new Handler<CroupierSample>() {
        @Override
        public void handle(CroupierSample croupierSample) {
            LOG.debug("Croupier Sample received by {}", selfAdr);
            List<KAddress> sample = CroupierHelper.getSample(croupierSample);
            for (KAddress peer : sample) {
                KHeader header = new BasicHeader(selfAdr, peer, Transport.UDP);
                KContentMsg msg = new BasicContentMsg(header, new HistoryRequest());
                LOG.debug("HistoryRequest sent to {} from {}", peer, selfAdr);
                trigger(msg, networkPort);
            }
        }
    };

    protected final ClassMatchedHandler historyRequestHandler = new ClassMatchedHandler<HistoryRequest, KContentMsg<?, ?, HistoryRequest>>() {
        @Override
        public void handle(HistoryRequest historyRequest, KContentMsg<?, ?, HistoryRequest> container) {
            LOG.debug("HistoryRequest received by {}", selfAdr);
            KAddress peer = container.getHeader().getSource();
            KHeader header = new BasicHeader(selfAdr, peer, Transport.TCP);
            KContentMsg msg = new BasicContentMsg(header, new HistoryResponse(ImmutableSet.copyOf(past)));
            LOG.debug("HistoryResponse sent to {} from {}", peer, selfAdr);
            trigger(msg, networkPort);
        }
    };

    protected final ClassMatchedHandler historyResponseHandler = new ClassMatchedHandler<HistoryResponse, KContentMsg<?, ?, HistoryResponse>>() {

        @Override
        public void handle(HistoryResponse historyResponse, KContentMsg<?, ?, HistoryResponse> container) {
            LOG.debug("HistoryResponse received by {} from {}", selfAdr, container.getHeader().getSource());
            HashSet<Pair<KAddress, KompicsEvent>> unseen = new HashSet<>(historyResponse.getHistory());
            unseen.removeAll(past);
            for (Pair<KAddress, KompicsEvent> pair : unseen) {
                LOG.debug("BEBDeliver sent to {}", selfAdr);
                trigger(new GBEBDeliver(pair.p1, pair.p2), gbeb);
            }
            past.addAll(unseen);
        }
    };


    /**
     * Init-event with the address of the node
     */
    public static class Init extends se.sics.kompics.Init<GBEB> {
        public final KAddress selfAdr;

        public Init(KAddress selfAdr) {
            this.selfAdr = selfAdr;
        }
    }

}
