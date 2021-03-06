package se.kth.broadcast.gossipbeb.port;

import se.kth.broadcast.gossipbeb.event.GBEBBroadcast;
import se.kth.broadcast.gossipbeb.event.GBEBDeliver;
import se.sics.kompics.PortType;

/**
 * Port exposed by a component providing GossipingBestEffortBroadcast service.
 *
 * (+GBEBBroadcast, -GBEBDeliver)
 *
 * Created by 62maxime on 03/04/2017.
 */
public class GossipingBestEffortBroadcast extends PortType {

    {
        indication(GBEBDeliver.class);
        request(GBEBBroadcast.class);
    }
}
