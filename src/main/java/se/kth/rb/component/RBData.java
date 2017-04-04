package se.kth.rb.component;

import se.sics.kompics.KompicsEvent;
import se.sics.ktoolbox.util.network.KAddress;

/**
 * Created by 62maxime on 04/04/2017.
 */
public class RBData implements KompicsEvent {

    private final KAddress source;
    private final KompicsEvent msg;

    public RBData(KAddress source, KompicsEvent msg) {
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
