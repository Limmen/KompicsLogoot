package se.kth.crb.event;

import se.sics.kompics.KompicsEvent;
import se.sics.ktoolbox.util.network.KAddress;

import java.io.Serializable;

/**
 * Created by 62maxime on 04/04/2017.
 */
public class CRBDeliver implements KompicsEvent, Serializable {

    private KAddress source;
    private KompicsEvent msg;

    public CRBDeliver(KAddress source, KompicsEvent msg) {
        this.source = source;
        this.msg = msg;
    }

    public KAddress getSource() {
        return source;
    }

    public KompicsEvent getMsg() {
        return msg;
    }
}
