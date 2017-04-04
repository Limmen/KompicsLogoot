package se.kth.crb.port;

import se.kth.crb.event.CRBBroadcast;
import se.kth.crb.event.CRBDeliver;
import se.sics.kompics.PortType;


public class CausalOrderReliableBroadcast extends PortType {

    {
        indication(CRBDeliver.class);
        request(CRBBroadcast.class);
    }
}
