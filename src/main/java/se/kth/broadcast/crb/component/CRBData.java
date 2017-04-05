package se.kth.broadcast.crb.component;

import se.kth.broadcast.gossipbeb.component.Pair;
import se.sics.kompics.KompicsEvent;
import se.sics.ktoolbox.util.network.KAddress;

import java.util.List;

/**
 * Created by 62maxime on 04/04/2017.
 */
public class CRBData implements KompicsEvent {

    private List<Pair<KAddress, KompicsEvent>> past;
    private KompicsEvent msg;

    public CRBData(List<Pair<KAddress, KompicsEvent>> past, KompicsEvent msg) {
        this.past = past;
        this.msg = msg;
    }

    @Override
    public String toString() {
        return "CRBData{" +
                "past=" + past +
                ", msg=" + msg +
                '}';
    }

    public List<Pair<KAddress, KompicsEvent>> getPast() {
        return past;
    }

    public KompicsEvent getMsg() {
        return msg;
    }
}
