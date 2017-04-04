package se.kth.broadcast.rb.port;

import se.kth.broadcast.rb.event.RBBroadcast;
import se.kth.broadcast.rb.event.RBDeliver;
import se.sics.kompics.PortType;

/**
 * Created by 62maxime on 03/04/2017.
 */
public class ReliableBroadcast extends PortType {

    {
        indication(RBDeliver.class);
        request(RBBroadcast.class);
    }
}
