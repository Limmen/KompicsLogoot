package se.kth.gossipbeb.event;

import se.sics.kompics.KompicsEvent;

import java.io.Serializable;

/**
 * Created by 62maxime on 03/04/2017.
 */
public class GBEBBroadcast implements KompicsEvent, Serializable {

    private KompicsEvent msg;

    public GBEBBroadcast(KompicsEvent msg) {
        this.msg = msg;
    }

    public KompicsEvent getEvent() {
        return msg;
    }
}
