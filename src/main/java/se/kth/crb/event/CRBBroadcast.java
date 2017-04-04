package se.kth.crb.event;

import se.kth.gossipbeb.component.Pair;
import se.sics.kompics.KompicsEvent;

import java.io.Serializable;
import java.util.List;

/**
 * Created by 62maxime on 04/04/2017.
 */
public class CRBBroadcast implements KompicsEvent, Serializable {

    private KompicsEvent payload;
    private List<Pair> past;

    public CRBBroadcast(KompicsEvent payload, List<Pair> past) {
        this.payload = payload;
        this.past = past;
    }

    public CRBBroadcast(KompicsEvent payload) {
        this.payload = payload;
    }

    public KompicsEvent getPayload() {
        return payload;
    }
}
