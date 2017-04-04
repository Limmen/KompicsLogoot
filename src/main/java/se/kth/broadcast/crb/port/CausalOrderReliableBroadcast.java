package se.kth.broadcast.crb.port;

import se.kth.broadcast.crb.event.CRBBroadcast;
import se.kth.broadcast.crb.event.CRBDeliver;
import se.sics.kompics.PortType;


public class CausalOrderReliableBroadcast extends PortType {

    {
        indication(CRBDeliver.class);
        request(CRBBroadcast.class);
    }
}
