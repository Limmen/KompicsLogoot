package se.kth.gossipbeb.port;

import se.kth.gossipbeb.event.GBEBBroadcast;
import se.kth.gossipbeb.event.GBEBDeliver;
import se.sics.kompics.PortType;

/**
 * Created by 62maxime on 03/04/2017.
 */
public class GossipingBestEffortBroadcast extends PortType {

    {
        indication(GBEBDeliver.class);
        request(GBEBBroadcast.class);
    }
}
