package se.kth.rb.event;

import se.sics.kompics.KompicsEvent;

import java.io.Serializable;

/**
 * Created by 62maxime on 03/04/2017.
 */
public class RBBroadcast implements KompicsEvent, Serializable {

    private KompicsEvent msg;

    public RBBroadcast(KompicsEvent msg) {
        this.msg = msg;
    }

    public KompicsEvent getMsg() {
        return msg;
    }
}
