package se.kth.broadcast.mngr;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import se.kth.broadcast.crb.component.NoWaitingCB;
import se.kth.broadcast.gossipbeb.component.GBEB;
import se.kth.broadcast.gossipbeb.port.GossipingBestEffortBroadcast;
import se.kth.broadcast.rb.component.EagerRB;
import se.kth.broadcast.rb.port.ReliableBroadcast;
import se.sics.kompics.*;
import se.sics.kompics.network.Network;
import se.sics.kompics.timer.Timer;
import se.sics.ktoolbox.croupier.CroupierPort;
import se.sics.ktoolbox.util.network.KAddress;

/**
 * @author Kim Hammar on 2017-04-04.
 */
public class BroadCastMngrComp extends ComponentDefinition {
    private static final Logger LOG = LoggerFactory.getLogger(BroadCastMngrComp.class);

    //*****************************CONNECTIONS**********************************
    Positive<Timer> timerPort = requires(Timer.class);
    Positive<Network> networkPort = requires(Network.class);
    //***************************EXTERNAL_STATE*********************************
    private KAddress selfAdr;
    private ExtPort extPorts;
    //***************************INTERNAL_STATE*********************************
    private Component cb;
    private Component rb;
    private Component gbeb;

    public BroadCastMngrComp(Init init) {
        selfAdr = init.selfAdr;
        extPorts = init.extPorts;

        subscribe(handleStart, control);
    }

    Handler handleStart = new Handler<Start>() {
        @Override
        public void handle(Start event) {
            connectBroadcast();
            trigger(Start.event, gbeb.control());
            trigger(Start.event, rb.control());
            trigger(Start.event, cb.control());
        }
    };

    private void connectBroadcast() {
        cb = create(NoWaitingCB.class, new NoWaitingCB.Init(selfAdr));
        rb = create(EagerRB.class, new EagerRB.Init(selfAdr));
        gbeb = create(GBEB.class, new GBEB.Init(selfAdr));
        connect(cb.getNegative(ReliableBroadcast.class), rb.getPositive(ReliableBroadcast.class), Channel.TWO_WAY);
        connect(rb.getNegative(GossipingBestEffortBroadcast.class), gbeb.getPositive(GossipingBestEffortBroadcast.class), Channel.TWO_WAY);
        connect(gbeb.getNegative(Network.class), networkPort, Channel.TWO_WAY);
        connect(gbeb.getNegative(CroupierPort.class), extPorts.croupierPort, Channel.TWO_WAY);
    }

    public static class Init extends se.sics.kompics.Init<BroadCastMngrComp> {

        public final ExtPort extPorts;
        public final KAddress selfAdr;

        public Init(ExtPort extPorts, KAddress selfAdr) {
            this.extPorts = extPorts;
            this.selfAdr = selfAdr;
        }
    }

    public static class ExtPort {
        public final Positive<Network> networkPort;
        public final Positive<CroupierPort> croupierPort;

        public ExtPort(Positive<Network> networkPort, Positive<CroupierPort> croupierPort) {
            this.networkPort = networkPort;
            this.croupierPort = croupierPort;
        }
    }
}
